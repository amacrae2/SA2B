package chao;

import java.util.ArrayList;
import java.util.List;

public class Moveset implements Comparable<Moveset>{

	private int score = 0;
	private List<Move> moves = new ArrayList<Move>();
	private int numChaoSwaps = 0;
	private int numMemoryCardSwaps = 0;
	
	public Moveset(int score, List<Move> moves, int numChaoSwaps,int numMemoryCardSwaps) {
		this.score = score;
		this.moves = moves;
		this.numChaoSwaps = numChaoSwaps;
		this.numMemoryCardSwaps = numMemoryCardSwaps;
	}

	public int getScore() {
		return score;
	}

	public void updateScore(int score) {
		this.score += score;
	}

	public List<Move> getMoves() {
		return moves;
	}

	public void addMove(Move move) {
		moves.add(move);
	}

	public int getNumChaoSwaps() {
		return numChaoSwaps;
	}

	public void incrementNumChaoSwaps() {
		numChaoSwaps ++;
	}

	public int getNumMemoryCardSwaps() {
		return numMemoryCardSwaps;
	}

	public void incrementNumMemoryCardSwaps() {
		numMemoryCardSwaps ++;
	}

	public Moveset() {
		super();
	}

	public Moveset createCopy() {
		return new Moveset(score,new ArrayList<Move>(moves),numChaoSwaps,numMemoryCardSwaps);
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void setMoves(List<Move> moves) {
		this.moves = moves;
	}

	public void setNumChaoSwaps(int numChaoSwaps) {
		this.numChaoSwaps = numChaoSwaps;
	}

	public void setNumMemoryCardSwaps(int numMemoryCardSwaps) {
		this.numMemoryCardSwaps = numMemoryCardSwaps;
	}

	public int getTotalMoves() {
		return numChaoSwaps + numMemoryCardSwaps;
	}

	@Override
	public int compareTo(Moveset ms) {
		int scoreDiff = ms.getScore() - getScore();
		if (scoreDiff == 0) {
			return ms.getTotalMoves() - getTotalMoves();
		} else {
			return scoreDiff;
		}
	}

	public Moveset updateMoveset(Move move, Move prevMove) {
		Moveset newMoveset = createCopy();
		newMoveset.addMove(move);
		if (move instanceof MoveChao) {
			newMoveset.updateScore(Constants.CHAO_SWAP_PENALTY);
			newMoveset.incrementNumChaoSwaps();
		} else if (move instanceof MoveMemoryCard) {
			if (prevMove != null && !(prevMove instanceof MoveMemoryCard)) {
				newMoveset.updateScore(Constants.MEMORY_CARD_SWAP_PENALTY);
			}
			newMoveset.incrementNumMemoryCardSwaps();
		} else {
			System.out.println("Error: move is not an instance of MoveChao or MoveMemoryCard");
		}
		return newMoveset;
	}
	
}
