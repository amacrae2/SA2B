package chao;

public class OutputScore implements Comparable<OutputScore> {

	private String output;
	private double filteredScore;
	private double unfilteresScore;
	private boolean unfiltered;
	
	public OutputScore(String output, double filteredScore, double unfilteresScore, boolean unfiltered) {
		this.output = output;
		this.filteredScore = filteredScore;
		this.unfilteresScore = unfilteresScore;
		this.unfiltered = unfiltered;
	}
	
	public OutputScore(String output, double filteredScore) {
		this.output = output;
		this.filteredScore = filteredScore;
	}
	
	@Override
	public int compareTo(OutputScore o) {
		double diff = 0.0;
		if (unfiltered) {
			diff =  o.getUnfilteresScore() - this.unfilteresScore;
		} else {
			diff =  o.getFilteredScore() - this.filteredScore;
		}
		if (diff > 0) {
			return 1;
		} else if (diff < 0) {
			return -1;
		} else {
			return 0;
		}
	}

	public String getOutput() {
		return output;
	}

	public double getFilteredScore() {
		return filteredScore;
	}

	public double getUnfilteresScore() {
		return unfilteresScore;
	}


	
	
	
}
