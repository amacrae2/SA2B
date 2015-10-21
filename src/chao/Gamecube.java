package chao;

public class Gamecube {
	
	private MemoryCardSlot leftSlot;
	private MemoryCardSlot rightSlot;
	
	public Gamecube(MemoryCardSlot leftSlot, MemoryCardSlot rightSlot) {
		super();
		this.leftSlot = leftSlot;
		this.rightSlot = rightSlot;
	}

	public MemoryCardSlot getLeftSlot() {
		return leftSlot;
	}

	public MemoryCardSlot getRightSlot() {
		return rightSlot;
	}
	
	public boolean equals(Gamecube gc) {
		if ((same(leftSlot,gc.getLeftSlot()) && same(rightSlot,gc.getRightSlot())) || 
				(same(leftSlot,gc.getRightSlot()) && same(rightSlot,gc.getLeftSlot()))) {
			return true;
		}
		return false;
	}

	private boolean same(MemoryCardSlot slotOne, MemoryCardSlot slotTwo) {
		if (slotOne.equals(slotTwo)) {
			return true;
		}
		return false;
	}
	
	public String toString() {
		String result = "";
		result += "[left mc - "+leftSlot.toString()+"], \n";
		result += "[right mc - "+rightSlot.toString()+"]\n";
		return result;
	}
	
	public Gamecube createCopy() {
		return new Gamecube(leftSlot.makeCopy(), rightSlot.makeCopy());
	}

}
