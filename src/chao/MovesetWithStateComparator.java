package chao;

import java.util.Comparator;

public class MovesetWithStateComparator implements Comparator<MovesetWithState> {
	
	MemoryState finalState;

	public MovesetWithStateComparator(MemoryState finalState) {
		this.finalState = finalState;
	}

	@Override
	public int compare(MovesetWithState mss1, MovesetWithState mss2) {
		int mss1Score = mss1.getScore()+mss1.getState().getScore(finalState);
		int mss2Score = mss2.getScore()+mss2.getState().getScore(finalState);
		return mss2Score - mss1Score;
	}
	
}
