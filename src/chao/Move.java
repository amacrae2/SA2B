package chao;

public abstract class Move {
	
	public abstract String toString();
	
	public boolean equals(Move m) {
		if (this.toString().equals(m.toString())) {
			return true;
		}
		return false;
	}
	
}
