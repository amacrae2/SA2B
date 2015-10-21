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
	
	public boolean putsChaoInRightMcButWrongGarden(MemoryState finalState) throws ChaoSwapException {
		ChaoGarden finalGardenOne = finalState.findGardenChaoIsIn(chaoOne);
		MemoryCard finalMcOne = finalState.findMcChaoIsIn(chaoOne);
		ChaoGarden finalGardenTwo = finalState.findGardenChaoIsIn(chaoTwo);
		MemoryCard finalMcTwo = finalState.findMcChaoIsIn(chaoTwo);
		if ((finalMcOne.getName().equals(memoryCardTwo.getName()) && 
				!finalGardenOne.getName().equals(chaoGardenTwo.getName()) && 
				!chaoOne.equals("")) 
				|| 
				(finalMcTwo.getName().equals(memoryCardOne.getName()) && 
				!finalGardenTwo.getName().equals(chaoGardenOne.getName()) && 
				!chaoTwo.equals(""))) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean movesChaoOutOfRightMcButWrongGarden(MemoryState finalState) throws ChaoSwapException {
		ChaoGarden finalGardenOne = finalState.findGardenChaoIsIn(chaoOne);
		MemoryCard finalMcOne = finalState.findMcChaoIsIn(chaoOne);
		ChaoGarden finalGardenTwo = finalState.findGardenChaoIsIn(chaoTwo);
		MemoryCard finalMcTwo = finalState.findMcChaoIsIn(chaoTwo);
		if ((finalMcOne.getName().equals(memoryCardOne.getName()) && 
				!finalGardenOne.getName().equals(chaoGardenOne.getName()) && 
				!chaoOne.equals("")) 
				|| 
				(finalMcTwo.getName().equals(memoryCardTwo.getName()) && 
				!finalGardenTwo.getName().equals(chaoGardenTwo.getName()) && 
				!chaoTwo.equals(""))) {
			return true;
		} else {
			return false;
		}
	}
	
	public String toString() {
		return "Swap "+chaoOne+" from the "+chaoGardenOne.getName()+" garden on "+memoryCardOne.getName()+" with "+chaoTwo+" from the "+chaoGardenTwo.getName()+" garden on "+memoryCardTwo.getName();
	}

}
