package chao;

public class MemoryCardSlot {

	private boolean empty;
	private MemoryCard mc;
	
	public MemoryCardSlot(MemoryCard mc) {
		if (mc == null) {
			empty = true;
		} else {
			empty = false;
			this.mc = mc;
		}
	}
	
	public boolean isEmpty() {
		return empty;
	}

	public MemoryCard getMc() {
		return mc;
	}

	public void setMc(MemoryCard mc) {
		this.mc = mc;
	}
	
	public boolean equals(MemoryCardSlot mcSlot) {
		if (isEmpty() == mcSlot.isEmpty()) {
			if (isEmpty()) {
				return true;
			} else if (mc.equals(mcSlot.getMc())) {
				return true;
			}
		}
		return false;
	}

	public MemoryCardSlot makeCopy() {
		return new MemoryCardSlot(mc.makeCopy(mc.getName()));
	}
	
	public String toString() {
		if (empty) {
			return "empty";
		} else {
			return mc.toString();
		}
	}
	
}
