package chao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MonteCarloSearch {
	
	private static int goalNodesFound = 0;

	public static Move performDepthCharges(MemoryState currState, MemoryState finalState, Moveset ms) throws ChaoSwapException {
		// perform depth charges to find the best move at this level
		List<Move> possibleMoves = currState.getPossibleMovesList();
		long start = System.currentTimeMillis();
		int depthChargesPerMove = 0;
		Map<Move, Integer> moveScores = new HashMap<Move, Integer>();
		goalNodesFound = 0;
		while (System.currentTimeMillis() < start + Constants.TIME_ALLOWED_PER_MOVE) {
			for (int i = 0; i < possibleMoves.size(); i ++) {
				Move move = possibleMoves.get(i);
				int score = depthCharge(currState, finalState, move, ms.createCopy());
				if (moveScores.containsKey(move)) {
					score = moveScores.get(move)+score;
				} 
				moveScores.put(move, score);
			}
			depthChargesPerMove++;
		}
		Move bestMove = determineBestMoveFromAveScores(depthChargesPerMove, moveScores);
		System.out.println("Number of Moves: "+possibleMoves.size());
		System.out.println("Depth Charges per move: "+depthChargesPerMove);
		System.out.println("Goal nodes found: "+goalNodesFound);
		return bestMove;
	}

	private static int depthCharge(MemoryState currState, MemoryState finalState, Move move, Moveset ms) throws ChaoSwapException {
		MemoryState nextState = currState.getNextState(move);
		Move prevMove = null;
		while(ms.getTotalMoves() <= Constants.MAX_ITERATIONS && !nextState.equals(finalState)) {
			List<Move> possibleMoves = nextState.getPossibleMovesList();
			Move randomMove = selectRandomMove(possibleMoves);
			nextState = nextState.getNextState(randomMove);
			ms = ms.updateMoveset(randomMove, prevMove);
			prevMove = randomMove;
		}
		ms.updateScore(nextState.getScore(finalState));
		if (nextState.equals(finalState)) {
			goalNodesFound++;
		}
		return ms.getScore();
	}

	private static Move selectRandomMove(List<Move> possibleMoves) {
		Random randomizer = new Random();
		Move random = possibleMoves.get(randomizer.nextInt(possibleMoves.size()));
		return random;
	}

	private static Move determineBestMoveFromAveScores(int depthChargesPerMove, Map<Move, Integer> moveScores) {
		// find best move after performing depth charges based on results of the depth charges
		double bestAveScore = Double.NEGATIVE_INFINITY;
		Move bestMove = null;
		for (Move key : moveScores.keySet()) {
			double aveScore = moveScores.get(key)/((double)depthChargesPerMove);
			if (aveScore > bestAveScore) {
				bestAveScore = aveScore;
				bestMove = key;
			}
			System.out.println(key+" = "+aveScore);
		}
		System.out.println("Best Move: "+bestMove+" = "+bestAveScore);
		return bestMove;
	}
}
