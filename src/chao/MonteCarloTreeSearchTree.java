package chao;

import java.util.List;
import java.util.Random;

public class MonteCarloTreeSearchTree {
	
	private StateNode topNode;
	private int c = 60; //constant used to determine which node depthCharge next
	private int depthCharges = 0;
	private int relevantDepthCharges = 0;
	private int depth = 0;
	private int goalNodesFound = 0;


	public Move selectMove(MemoryState currState, MemoryState finalState, Moveset ms) throws ChaoSwapException {
		relevantDepthCharges = 0;
		long start = System.currentTimeMillis();
		long finishBy = start+Constants.TIME_ALLOWED_PER_MOVE;
		uptateTopNode(currState, ms);
		
//		topNode = new StateNode(currState);
//	    createMoveNodesForParentStateNode(topNode);
		
		Move selection = monteCarloTreeSearch(finishBy, currState, finalState, ms);
		return selection;
	}

	private Move monteCarloTreeSearch(long finishBy, MemoryState currState, MemoryState finalState, Moveset ms) throws ChaoSwapException {
		List<Move> moves = currState.getPossibleMovesList();
		Move selection = moves.get(0);

		// Perform depth charges for each candidate move, and keep track
		// of the total score and total attempts accumulated for each move.
		int iter = 0;
		int numMoves = 0;
		while (true) {
		    if (System.currentTimeMillis() > finishBy)
		        break;

		    depth = 0;
		    MoveNode bestChild = findMoveToExplore(topNode, numMoves);
    		StateNode stateToExplore = findBestStateToExplore(bestChild, numMoves+1, currState, finalState, null, 0);
    		if (stateToExplore != null) {
        	    depthCharge(stateToExplore, depth, finalState); // perform depth charge and update child and parents
    		}
		    iter ++;
		    depthCharges++;
		}
		selection = computeBestMove(topNode);
		System.out.println("[Advanced MCTS Meta] Total depth charges this round: "+iter);
		System.out.println("[Advanced MCTS Meta] Total depth charges in total: "+depthCharges);
		System.out.println("[Advanced MCTS Meta] Total relevant depth charges: "+relevantDepthCharges);
		System.out.println("[Advanced MCTS Meta] Percent from earlier depth charges: "+((1.0-((double)iter/(double)(relevantDepthCharges+1)))*100.0)+"%");
		System.out.println("[Advanced MCTS Meta] Total times goal states found: "+goalNodesFound);
		return selection;
	}

	private void uptateTopNode(MemoryState currState, Moveset ms) {
		// find if this current state has been visited before
		List<Move> moves = ms.getMoves();
		if (ms.getMoves().size() > 0 && topNode.getNextState(moves.get(moves.size()-1)).getState().equals(currState)) {
			Move lastMove = moves.get(moves.size()-1);
			topNode = topNode.updateParent(lastMove);
			if (topNode == null) {
				System.out.println("error: could not find start node for this iteration");;
				topNode = new StateNode(currState);
			    createMoveNodesForParentStateNode(topNode);
			}
		} else { 
			topNode = new StateNode(currState);
		    createMoveNodesForParentStateNode(topNode);
		}
	}

	private Move computeBestMove(StateNode parentNode) {
		// Compute the expected score for each move and Find the move with the best expected score
		Move bestMove = null;
		double bestMoveScore = Double.NEGATIVE_INFINITY;
		MoveNode bestChild = null;
		List<MoveNode> children = parentNode.getChildren();
		for (int i = 0; i < children.size(); i++) {
			MoveNode child = children.get(i);
		    double currMoveScore = child.getExpectedScore();
		    System.out.println("[Advanced MCTS Meta] move "+child.getMove()+" = "+currMoveScore+" | depth charges = "+child.getTimesVisited());
		    relevantDepthCharges += child.getTimesVisited();
		    if (currMoveScore > bestMoveScore) {
		    	bestMoveScore = currMoveScore;
		    	bestMove = child.getMove();
		    	bestChild = child;
		    }
		}
		System.out.println("[Advanced MCTS Meta] expected score = "+bestChild.getExpectedScore());
		System.out.println("[Advanced MCTS Meta] Best Move = "+bestChild.getMove().toString());
		return bestMove;
	}

	private StateNode findBestStateToExplore(MoveNode child, int numMoves, MemoryState currState, MemoryState finalState, Move prevMove, double score) throws ChaoSwapException {
		depth ++;
		Move currMove = child.getMove();
		score = updateScoreFromMove(prevMove, score, currMove);
		prevMove = currMove;
		for (StateNode sNode : child.getChildren()) {
			// if we reached a terminal state
			if(currState.getNextState(child.getMove()).equals(finalState) || numMoves >= Constants.MAX_ITERATIONS) {
				score += currState.getScore(finalState);
				sNode.addScore(score); // this will back propagate depth charge score all the way to top of tree
				if (currState.getNextState(child.getMove()).equals(finalState)) {
					goalNodesFound++;
				}
				return null;
			}

			// if not visited before
			if (sNode.getTimesVisited()==0) {
			    createMoveNodesForParentStateNode(sNode);
			    sNode.setCurrScore(score);
				return sNode;
			}
		}

		// if all nodes have been visited before
	    StateNode bestSNode = calculateBestStateToExplore(child);
	    MoveNode bestChild = findMoveToExplore(bestSNode, numMoves);
	    return findBestStateToExplore(bestChild, numMoves+1, bestSNode.getState(), finalState, prevMove, score);
	}

	private double updateScoreFromMove(Move prevMove, double score, Move currMove) {
		if (currMove instanceof MoveChao) {
			score += Constants.CHAO_SWAP_PENALTY;
		} else if (currMove instanceof MoveMemoryCard && !(prevMove instanceof MoveMemoryCard)) {
			score += Constants.MEMORY_CARD_SWAP_PENALTY;
		}
		return score;
	}

	private MoveNode findMoveToExplore(StateNode parentNode, int numMoves) throws ChaoSwapException {
		MoveNode bestChild = null;
		MemoryState currState = parentNode.getState();
	    for (MoveNode child : parentNode.getChildren()) {
	    	// if child has not yet been visited
	    	if (child.getTimesVisited() == 0) {
	    		createStateNodesForParentMoveNode(currState, child);
	    		return child;
	    	}
	    }
	    // if child was visited before
	    bestChild = calculateBestMoveToExplore(parentNode, numMoves+1);


	    return bestChild;
	}

	private MoveNode calculateBestMoveToExplore(StateNode parentNode, int numMoves) {
		double bestMoveScore = Double.NEGATIVE_INFINITY;
	    MoveNode bestChild = null;
		for (MoveNode child : parentNode.getChildren()) {
			double vi = (double)child.getTotalScore() / child.getTimesVisited();
			int np = parentNode.getTimesVisited();
			int ni = child.getTimesVisited();
			double moveScore = vi + c*Math.sqrt(Math.log(np) / ni);
			if (bestMoveScore < moveScore) {
				bestMoveScore = moveScore;
				bestChild = child;
			}
		}
//		System.out.println("best score of "+bestChild.getExpectedScore()+" after move "+numMoves);
		return bestChild;
	}

	private StateNode calculateBestStateToExplore(MoveNode bestChild) {
		double bestMoveScore = Double.NEGATIVE_INFINITY;
		StateNode bestSNode = null;
		for (StateNode sNode : bestChild.getChildren()) {
			double vi = (double)sNode.getTotalScore() / sNode.getTimesVisited();
			int np = bestChild.getTimesVisited();
			int ni = sNode.getTimesVisited();
			double moveScore = vi + c*Math.sqrt(Math.log(np) / ni);
			if (bestMoveScore < moveScore) {
				bestMoveScore = moveScore;
				bestSNode = sNode;
			}
		}
		return bestSNode;
	}

	private void createMoveNodesForParentStateNode(StateNode sNode) {
		// find moves from this state
		List<Move> nextMoves = sNode.getState().getPossibleMovesList();

		//create a moveNode for each move
		for (Move move : nextMoves) {
			MoveNode mNode = new MoveNode(sNode, move);
			sNode.addChild(mNode);
		}
		if (sNode.getChildren() == null) {
			System.out.println("Error: state node has no children");
		}
	}

	private void createStateNodesForParentMoveNode(MemoryState currState, MoveNode child) throws ChaoSwapException {
    	Move currMove = child.getMove();
		MemoryState nextState = currState.getNextState(currMove);
		StateNode sNode = new StateNode(child, nextState, currMove);
		child.addChild(sNode);
	}

	private void depthCharge(StateNode sNode, int currDepth, MemoryState finalState) throws ChaoSwapException {
		
		MemoryState currState = sNode.getState();
		double score = sNode.getCurrScore();
		Move prevMove = sNode.getMoveToState();
		while(currDepth <= Constants.MAX_ITERATIONS && !currState.equals(finalState)) {
			List<Move> possibleMoves = currState.getPossibleMovesList();
			Move randomMove = selectRandomMove(possibleMoves);
			currState = currState.getNextState(randomMove);
			score = updateScoreFromMove(prevMove, score, randomMove);
			prevMove = randomMove;
			currDepth ++;
		}
		score += currState.getScore(finalState);
		if (currState.equals(finalState)) {
			goalNodesFound++;
		}
		sNode.addScore(score);
	}
	
	private Move selectRandomMove(List<Move> possibleMoves) {
		Random randomizer = new Random();
		Move random = possibleMoves.get(randomizer.nextInt(possibleMoves.size()));
		return random;
	}
}
