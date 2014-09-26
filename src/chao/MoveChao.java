package chao;

public class MoveChao extends Move {
	
	private MemoryCard memoryCardOne;
	private ChaoGarden chaoGardenOne;
	private String chaoOne;
	private MemoryCard memoryCardTwo;
	private ChaoGarden chaoGardenTwo;
	private String chaoTwo;

	public MoveChao(MemoryCard memoryCardOne, ChaoGarden chaoGardenOne,
			String chaoOne, MemoryCard memoryCardTwo, ChaoGarden chaoGardenTwo,
			String chaoTwo) {
		this.memoryCardOne = memoryCardOne;
		this.chaoGardenOne = chaoGardenOne;
		this.chaoOne = chaoOne;
		this.memoryCardTwo = memoryCardTwo;
		this.chaoGardenTwo = chaoGardenTwo;
		this.chaoTwo = chaoTwo;
	}

	public MemoryCard getMemoryCardOne() {
		return memoryCardOne;
	}

	public MemoryCard getMemoryCardTwo() {
		return memoryCardTwo;
	}

	public ChaoGarden getChaoGardenOne() {
		return chaoGardenOne;
	}

	public String getChaoOne() {
		return chaoOne;
	}

	public ChaoGarden getChaoGardenTwo() {
		return chaoGardenTwo;
	}

	public String getChaoTwo() {
		return chaoTwo;
	}
	
	public String toString() {
		return "Swap "+chaoOne+" from the "+chaoGardenOne.getName()+" garden on "+memoryCardOne.getName()+" with "+chaoTwo+" from the "+chaoGardenTwo.getName()+" garden on "+memoryCardTwo.getName();
	}

}
