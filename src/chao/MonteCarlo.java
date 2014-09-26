package chao;

public class MonteCarlo {
	
	static MonteCarloTreeSearchTree MCTSTree = new MonteCarloTreeSearchTree();

	public static Moveset findBestMoveset(MemoryState currState, MemoryState finalState, Moveset ms, Move prevMove, int moveNumber, double startTime) throws ChaoSwapException {
		// while goal is not reached and max iterations have not been met, spend x amount of time
		// and iterate through the current possible moves to find the one move that will most likely
		// lead to the highest score. 
		if (currState.equals(finalState)) {
			ms.updateScore(currState.getScore(finalState));
			ms.updateScore(Constants.FINDS_GOAL_STATE);
			return ms;
		} else if (ms.getMoves().size() >= Constants.MAX_ITERATIONS) {
			ms.updateScore(currState.getScore(finalState));
			return ms;
		} else {
//			Move bestMove = MonteCarloSearch.performDepthCharges(currState, finalState, ms);
			Move bestMove = MCTSTree.selectMove(currState, finalState, ms);
			moveNumber++;
			System.out.println("Moves so far: "+moveNumber);
			System.out.println("Minutes elapsed so far: "+((System.currentTimeMillis()-startTime)/1000/60));
			MemoryState nextState = currState.getNextState(bestMove);
			Moveset newMoveset = ms.updateMoveset(bestMove, prevMove);
			Moveset bestMoveset = findBestMoveset(nextState, finalState, newMoveset, bestMove, moveNumber, startTime);
			return bestMoveset;
		}

	}
	
}
