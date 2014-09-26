package chao;

import java.util.ArrayList;
import java.util.List;

public class MovesetWithState extends Moveset {
	
	private MemoryState state;

	public MovesetWithState(int score, List<Move> moves, int numChaoSwaps, int numMemoryCardSwaps, MemoryState state) {
		super(score, moves, numChaoSwaps, numMemoryCardSwaps);
		this.state = state;
	}

	public MovesetWithState(MemoryState state) {
		super();
		this.state = state;
	}

	public MemoryState getState() {
		return state;
	}

	public void setState(MemoryState state) {
		this.state = state;
	}

	@Override
	public MovesetWithState createCopy() {
		return new MovesetWithState(getScore(), new ArrayList<Move>(getMoves()), getNumChaoSwaps(), getNumMemoryCardSwaps(), getState().createCopy());
	}
	
}
