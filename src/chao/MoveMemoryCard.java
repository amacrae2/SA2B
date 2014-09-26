package chao;

public class MoveMemoryCard extends Move {
	
	private MemoryCard newMemoryCard;
	private MemoryCard oldMemoryCard;

	
	public MoveMemoryCard(MemoryCard newMemoryCard, MemoryCard oldMemoryCard) {
		super();
		this.newMemoryCard = newMemoryCard;
		this.oldMemoryCard = oldMemoryCard;
	}

	public MemoryCard getNewMemoryCard() {
		return newMemoryCard;
	}

	public MemoryCard getOldMemoryCard() {
		return oldMemoryCard;
	}

	public String toString() {
		return "remove "+oldMemoryCard.getName()+" and insert "+newMemoryCard.getName();
	}
	
}
