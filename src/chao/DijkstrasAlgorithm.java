package chao;

import java.util.List;

import com.google.common.collect.MinMaxPriorityQueue;

// actually A* ... not Dijkstra's
public class DijkstrasAlgorithm {
	
	private static final int MAX_PQ_SIZE = 10000;

	public static MovesetWithState findBestMoveset(MovesetWithState currMovesetWithState, MemoryState finalState) throws ChaoSwapException {
		// create priority queue
//		PriorityQueue<MovesetWithState> pq = new PriorityQueue<>(1174279, new MovesetWithStateComparator(finalState));
		MinMaxPriorityQueue<MovesetWithState> pq = MinMaxPriorityQueue.orderedBy(new MovesetWithStateComparator(finalState))
                									.maximumSize(MAX_PQ_SIZE)
                									.create();
		
		// while we have not found the final state
		int iter = 0;
		int accumulated;
		int projected;
		long start = System.currentTimeMillis();
		long totalms = 0;
		while (pq.isEmpty() || !pq.peek().getState().equals(finalState)) {
			if (!pq.isEmpty()) {
				// pick the best movesets so far
				currMovesetWithState = pq.poll();
				iter++;
				accumulated = currMovesetWithState.getScore();
				projected = currMovesetWithState.getState().getScore(finalState);
//				System.out.println("Explored Moveset:  "+currMovesetWithState.getMoves().toString());
				if (currMovesetWithState.getMoves().size()>=3) {
					System.out.println("Third to Last Move: "+currMovesetWithState.getMoves().get(currMovesetWithState.getMoves().size()-3).toString());
					System.out.println("Second to Last Move: "+currMovesetWithState.getMoves().get(currMovesetWithState.getMoves().size()-2).toString());
				}
				System.out.println("Last Move: "+currMovesetWithState.getMoves().get(currMovesetWithState.getMoves().size()-1).toString());
				System.out.println("Accumulated Score: "+accumulated);
				System.out.println("Exprected Score: "+projected);
				System.out.println("Total Score: "+(accumulated+projected));
				System.out.println("moves: "+currMovesetWithState.getTotalMoves()+", chao swaps: "+currMovesetWithState.getNumChaoSwaps()+", mem-card swaps: "+currMovesetWithState.getNumMemoryCardSwaps());
				System.out.println("iterations: "+iter);
				long diff = System.currentTimeMillis()-start;
				totalms+=diff;
//				long aveTimePerIter = totalms/iter;
				long aveTimePerMove = totalms/(currMovesetWithState.getTotalMoves()+1);
//				System.out.println("time: "+ (diff));
//				System.out.println("ave time per iter: "+ (aveTimePerIter));
//				System.out.println("ave time per move: "+ (aveTimePerMove));	
				System.out.println("projected time remaining: "+((aveTimePerMove*(Constants.GARDEN_SIZE*Constants.NUM_GARDENS*Constants.NUM_MEMORY_CARDS*Constants.PROJECTED_NUM_SWAPS_PER_CHAO-currMovesetWithState.getTotalMoves()))/Constants.MILLI/Constants.SEC_PER_MIN)+" Min");
				System.out.println(currMovesetWithState.getState().toString());
				start = System.currentTimeMillis();
			}
			// get next movesetWithStates and add them to priority queue
			List<Move> possibleMoves = currMovesetWithState.getState().getPossibleMovesList();
			for (Move move : possibleMoves) {
				MovesetWithState mswsTemp = currMovesetWithState.createCopy();
				mswsTemp.addMove(move);
				mswsTemp.setState(currMovesetWithState.getState().getNextState(move));
				if (move instanceof MoveChao) {
					mswsTemp.incrementNumChaoSwaps();
					mswsTemp.updateScore(Constants.CHAO_SWAP_PENALTY);
					if (((MoveChao) move).putsChaoInRightMcButWrongGarden(finalState)) {
						mswsTemp.updateScore(Constants.CHAO_RIGHT_MC_WRONG_GARDEN_PENALTY);
					} else if (((MoveChao) move).movesChaoOutOfRightMcButWrongGarden(finalState)) {
						mswsTemp.updateScore(Constants.CHAO_RIGHT_MC_WRONG_GARDEN_MOVE_OUT_SCORE);
					}
					
				} else if (move instanceof MoveMemoryCard) {
					mswsTemp.incrementNumMemoryCardSwaps();
					if (currMovesetWithState.getMoves().size()!=0 /* && (!prevMoveIsMoveMemCard(currMovesetWithState,1) || prevTwoMovesAreMoveMemCard(currMovesetWithState)) */) {
						if (prevTwoMovesAreMoveMemCard(currMovesetWithState)) {
							mswsTemp.updateScore(Constants.MEMORY_CARD_TRIPLE_SWAP_PENALTY);
						} else if (prevMoveIsMoveMemCard(currMovesetWithState, 1)) {
							mswsTemp.updateScore(Constants.MEMORY_CARD_DOUBLE_SWAP_PENALTY);
						} else {
							mswsTemp.updateScore(Constants.MEMORY_CARD_SWAP_PENALTY);
						}
					}
				}
				pq.add(mswsTemp);
			}
		}
		return pq.peek();
		
	}

	private static boolean prevTwoMovesAreMoveMemCard(MovesetWithState currMovesetWithState) {
		if (currMovesetWithState.getMoves().size() < 2) {
			return false;
		}
		return (prevMoveIsMoveMemCard(currMovesetWithState, 1) && prevMoveIsMoveMemCard(currMovesetWithState, 2));
	}

	private static boolean prevMoveIsMoveMemCard(MovesetWithState currMovesetWithState, int prevIndex) {
		int prevMoveIndex = currMovesetWithState.getMoves().size()-prevIndex;
		if (prevMoveIndex < 0) {
			prevMoveIndex = 0;
		}
		return currMovesetWithState.getMoves().get(prevMoveIndex) instanceof MoveMemoryCard;
	} 

}
