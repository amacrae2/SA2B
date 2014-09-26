package chao;

public class Chao {

	private String name;
	private String memoryCard;
	private String garden;
	private ChaoStats stats;
	
	public Chao(String name, String memoryCard, String garden, String swim,
			String fly, String run, String power, String total,
			String swimStat, String flyStat, String runStat, String powerStat,
			String totalStat, String stamina, String staminaStat, String hat, String intellegence, String trips, String luck) {
		this.name = name;
		this.memoryCard = memoryCard;
		this.garden = garden;
		stats = new ChaoStats(Integer.parseInt(swim), Integer.parseInt(fly), Integer.parseInt(run), 
				Integer.parseInt(power), Integer.parseInt(total), Integer.parseInt(swimStat), 
				Integer.parseInt(flyStat), Integer.parseInt(runStat), Integer.parseInt(powerStat), 
				Integer.parseInt(totalStat), Integer.parseInt(stamina), Integer.parseInt(staminaStat),
				Integer.parseInt(hat), Integer.parseInt(intellegence), Integer.parseInt(trips), Integer.parseInt(luck));
	}

	public Chao(String name, String memoryCard, String garden, int swim,
			int fly, int run, int power, int total, int swimStat, int flyStat,
			int runStat, int powerStat, int totalStat, int stamina, int staminaStat,
			boolean hat, int intellegence, int trips, int luck) {
		this.name = name;
		this.memoryCard = memoryCard;
		this.garden = garden;
		stats = new ChaoStats(swim, fly, run, power, total, swimStat, flyStat, runStat, powerStat, 
								totalStat, stamina, staminaStat, hat? 1 : 0, intellegence, trips, luck);
	}

	public String getName() {
		return name;
	}

	public String getMemoryCard() {
		return memoryCard;
	}

	public String getGarden() {
		return garden;
	}

	public ChaoStats getStats() {
		return stats;
	}
	
	public String toString() {
		return name+stats.toString();
	}
	
}
