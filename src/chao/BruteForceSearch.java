package chao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BruteForceSearch {

	private static int endNodesReached;
	private static int goalNodesFound;
	private static List<Moveset> topTenMovesets = new ArrayList<Moveset>();


	public static Moveset findBestMoveset(MemoryState currState, MemoryState finalState, Moveset ms, Move prevMove, boolean isTop) throws ChaoSwapException {
		// have possible options: switch chao -1, switch memory card -5, goal +100
		// try each combination of options until 100 steps or until terminal state is reached (comparison matches)
		if (currState.equals(finalState)) {
			ms.updateScore(Constants.FINDS_GOAL_STATE);
			endNodesReached ++;
			goalNodesFound ++;
			return ms;
		} else if (ms.getMoves().size() > Constants.MAX_ITERATIONS) {
			endNodesReached ++;
			return ms;
		} else {
			Moveset bestMoveset = new Moveset();
			bestMoveset.setScore(Integer.MIN_VALUE);
			List<Move> possibleMoves = currState.getPossibleMovesList();
			int iter = 0;
			for (Move move : possibleMoves) {
				Moveset newMoveset = ms.updateMoveset(move, prevMove);
				MemoryState nextState = currState.getNextState(move);
				newMoveset = findBestMoveset(nextState, finalState, newMoveset, move, false);
				if (newMoveset.getScore() > bestMoveset.getScore() || hasLessMoves(newMoveset,bestMoveset)) {
					bestMoveset = newMoveset;
					printTopTenScoresIfChange(newMoveset);
				}
				if (isTop) {
					iter ++;
					System.out.println(100*iter/possibleMoves.size()+"% complete.");
				}
				if (endNodesReached % 100000 == 0) {
					System.out.println(goalNodesFound+" Goal Nodes Found, "+endNodesReached+" End Nodes Reached");
				}
			}
			return bestMoveset;
		}

	}
	
	private static void printTopTenScoresIfChange(Moveset newMoveset) {
		if (topTenMovesets.size() < 10 || newMoveset.getScore() > topTenMovesets.get(topTenMovesets.size()-1).getScore() || hasLessMoves(newMoveset, topTenMovesets.get(topTenMovesets.size()-1))) {
			topTenMovesets.add(newMoveset);
			Collections.sort(topTenMovesets);
			if (topTenMovesets.size() > 10) {
				topTenMovesets.remove(10);
			}
			System.out.print("Top ten Moveset scores: ");
			for (int i = 0; i < topTenMovesets.size(); i++) {
				System.out.print(topTenMovesets.get(i).getScore()+", ");
			}
			System.out.println("");
		}
	}

	private static boolean hasLessMoves(Moveset newMoveset, Moveset bestMoveset) {
		if (newMoveset.getScore() == bestMoveset.getScore() && newMoveset.getTotalMoves() < bestMoveset.getTotalMoves()) {
			return true;
		}
		return false;
	}

	
}
