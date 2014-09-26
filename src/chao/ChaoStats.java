package chao;

import java.util.ArrayList;
import java.util.List;

public class ChaoStats {
	
	private int swim;
	private int fly;
	private int run;
	private int power;
	private int total;
	private int swimStat;
	private int flyStat;
	private int runStat;
	private int powerStat; 
	private int totalStat;
	private int stamina;
	private int staminaStat;
	private int hat;
	private int intellegence;
	private int trips;
	private int luck;
	private List<Integer> stats = new ArrayList<>();

	
	public ChaoStats(int swim, int fly, int run, int power, int total,
			int swimStat, int flyStat, int runStat, int powerStat, int totalStat,
			int stamina, int staminaStat, int hat, int intellegence, int trips, int luck) {
		super();
		this.swim = swim;
		this.fly = fly;
		this.run = run;
		this.power = power;
		this.total = total;
		this.swimStat = swimStat;
		this.flyStat = flyStat;
		this.runStat = runStat;
		this.powerStat = powerStat;
		this.totalStat = totalStat;
		this.stamina = stamina;
		this.staminaStat = staminaStat;
		this.hat = hat;
		this.intellegence = intellegence;
		this.trips = trips;
		this.luck = luck;
		addStatsToList();
	}
	
	public int getStamina() {
		return stamina;
	}

	public int getStaminaStat() {
		return staminaStat;
	}

	private void addStatsToList() {
		stats.add(swimStat);
		stats.add(flyStat);
		stats.add(runStat);
		stats.add(powerStat);
		stats.add(staminaStat);
		stats.add(hat);
		stats.add(intellegence);
		stats.add(trips);
		stats.add(luck);		
		stats.add(totalStat);
		stats.add(swim);
		stats.add(fly);
		stats.add(run);
		stats.add(power);
		stats.add(total);	
		stats.add(stamina);
	}

	public int getSwim() {
		return swim;
	}

	public int getFly() {
		return fly;
	}

	public int getRun() {
		return run;
	}

	public int getPower() {
		return power;
	}

	public int getTotal() {
		return total;
	}

	public int getSwimStat() {
		return swimStat;
	}

	public int getFlyStat() {
		return flyStat;
	}

	public int getRunStat() {
		return runStat;
	}

	public int getPowerStat() {
		return powerStat;
	}

	public int getTotalStat() {
		return totalStat;
	}

	// indexing goes 1-4 for stats, 5-7 for extras
	public int get(int index) {
		return stats.get(index);
	}

	public String toString() {
		return "{[swim stat:"+getSwimStat()+"][fly stat:"+getFlyStat()+
				"][run stat:"+getRunStat()+"][power stat:"+getPowerStat()+
				"][total stat:"+getTotalStat()+"[swim:"+getSwim()+
				"][fly:"+getFly()+"][run:"+getRun()+"][power:"+getPower()+
				"][total:"+getTotal();
	}
	
}