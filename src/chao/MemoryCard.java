package chao;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;



public class MemoryCard {

	private String name;
	private ChaoGarden hero; 
	private ChaoGarden neutral;
	private ChaoGarden dark;
	private boolean inserted;
	
	public MemoryCard(String name, ChaoGarden hero, ChaoGarden neutral,
			ChaoGarden dark, boolean inserted) {
		super();
		this.name = name;
		this.hero = hero;
		this.neutral = neutral;
		this.dark = dark;
		this.inserted = inserted;
	}

	public String getName() {
		return name;
	}

	public ChaoGarden getHeroGarden() {
		return hero;
	}

	public ChaoGarden getNeutralGarden() {
		return neutral;
	}

	public ChaoGarden getDarkGarden() {
		return dark;
	}
	
	public boolean isInserted() {
		return inserted;
	}
	
	public void insert() {
		inserted = true;
	}
	
	public void pullOut() {
		inserted = false;
	}
	
	public boolean equals(MemoryCard mc) {
		if (!mc.getName().equals(getName())) {
			return false;
		}
		for (ChaoGarden garden1 : getChaoGardenSet()) {
			for (ChaoGarden garden2 : mc.getChaoGardenSet()) {
				if (garden1.getName().equals(garden2.getName())) {
					if (!garden1.equals(garden2)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public Set<ChaoGarden> getChaoGardenSet() {
		Set<ChaoGarden> cgSet = new HashSet<ChaoGarden>();
		cgSet.add(hero);
		cgSet.add(neutral);
		cgSet.add(dark);
		return cgSet;
	}

	public MemoryCard makeCopy(String mcName) {
		MemoryCard copy = new MemoryCard(name.toString(), hero.makeCopy(mcName), neutral.makeCopy(mcName), dark.makeCopy(mcName), inserted);
		return copy;
	}

	public boolean swapChao(MoveChao move) {
		if (move.getMemoryCardOne().getName().equals(getName())) {
			return getGardenMap().get(move.getChaoGardenOne().getName()).swapChao(move.getChaoOne(), move.getChaoTwo());
		} else if (move.getMemoryCardTwo().getName().equals(getName())) {
			return getGardenMap().get(move.getChaoGardenTwo().getName()).swapChao(move.getChaoTwo(), move.getChaoOne());
		} else {
			System.out.println("Error: neither memory card from swap is current memory card");
		}
		return false;
	}

	private Map<String, ChaoGarden> getGardenMap() {
		Map<String, ChaoGarden> result = new HashMap<String, ChaoGarden>();
		result.put(Constants.HERO_GARDEN_NAME, hero);
		result.put(Constants.NEUTRAL_GARDEN_NAME, neutral);
		result.put(Constants.DARK_GARDEN_NAME, dark);
		return result;
	}

	public int getScore(MemoryCard memoryCard) {
		int score = 0;
		score += hero.getScore(memoryCard.getHeroGarden());
		score += neutral.getScore(memoryCard.getNeutralGarden());
		score += dark.getScore(memoryCard.getDarkGarden());
		return score;
	}
	
	public String toString() {
		String result = name;
		result += ": [Hero - "+hero.toString()+"], ";
		result += ": [Neutral - "+neutral.toString()+"], ";
		result += ": [Dark - "+dark.toString()+"]";
		return result;
	}
	
}
