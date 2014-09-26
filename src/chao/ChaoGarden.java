package chao;

import java.util.HashSet;
import java.util.Set;

public class ChaoGarden {
	
	private String name;
	private String memoryCard;
	private Set<String> chaos;
	
	public ChaoGarden(String name, String memoryCard) {
		this.name = name;
		this.memoryCard = memoryCard;
		chaos = new HashSet<String>();
	}
	
	public ChaoGarden(String name, String memoryCard, Set<String> chaos) {
		this.name = name;
		this.memoryCard = memoryCard;
		this.chaos = chaos;
	}
	
	public boolean isFull() {
		if (chaos.size() == Constants.GARDEN_SIZE) {
			return true;
		} else {
			return false;
		}
	}
	
	public void addChao(String chao) {
		if (isFull()) {
			System.out.println("Error: tried to add chao to full garden. "
					+ "Garden = "+name+", memory card = "+memoryCard);
		} else {
			chaos.add(chao);
		}
	}
	
	public boolean containsChao(String chao) {
		if (chaos.contains(chao)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Swaps a new chao and returns the old chao. 
	 * If old chao is not found in memory card, 
	 * new chao is not added and returned instead
	 * @param newChao
	 * @param chaoToBeRemoved
	 * @return boolean is swap was successful
	 */
	public boolean swapChao(String chaoToBeRemoved, String newChao) {
		if (chaoToBeRemoved.equals("") && !isFull()) { 
			if (!newChao.equals("")) {
				chaos.add(newChao);
			}
			return true;
		} else if (containsChao(chaoToBeRemoved)) {
			chaos.remove(chaoToBeRemoved);
			if (!newChao.equals("")) {
				chaos.add(newChao);
			}
			return true;
		} else {
			System.out.println("No Chao by the name "+chaoToBeRemoved+" exists to be swapped out in "
								+"Garden = "+name+", memory card = "+memoryCard);
		}
		return false;
	}

	public Set<String> getChaos() {
		Set<String> result = new HashSet<>(chaos);
		if (!isFull()) {
			result.add("");
		}
		return result;
	}

	public void setChaos(Set<String> chaos) {
		this.chaos = chaos;
	}

	public String getName() {
		return name;
	}

	public String getMemoryCard() {
		return memoryCard;
	}
	
	public boolean equals(ChaoGarden garden) {
		for (String chao : chaos) {
			if (!garden.getChaos().contains(chao)) {
				return false;
			}
		}
		return true;
	}

	public ChaoGarden makeCopy(String mcName) {
		Set<String> newChaoSet = new HashSet<String>();
		for (String chao : chaos) {
			newChaoSet.add(chao.toString());
		}
		ChaoGarden copy = new ChaoGarden(name.toString(), mcName, newChaoSet);
		return copy;
	}

	public int getScore(ChaoGarden garden) {
		int score = 0;
		for (String chao : chaos) {
			if (garden.getChaos().contains(chao)) {
				score += Constants.CHAO_MATCH_SCORE;
			} else {
				score += Constants.CHAO_NON_MATCH_PENALTY;
			}
		}
		return score;
	}
	
	public String toString() {
		String result = "[";
		for (String chao : chaos) {
			result += chao+", ";
		}
		if (chaos.size() > 0) {
			result = result.substring(0, result.length()-2);
		}
		result += "]";
		return result;
	}
	
}
