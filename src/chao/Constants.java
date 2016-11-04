package chao;

import java.util.ArrayList;
import java.util.List;

public class Constants {
	
	public static final String MC_ONE_NAME = "White";
	public static final String MC_TWO_NAME = "Black1";
	public static final String MC_THREE_NAME = "Black2";
	public static final String MC_FOUR_NAME = "AM";
	public static final String MC_FIVE_NAME = "Gray3";
	public static final String MC_SIX_NAME = "Gray4";
	public static final String MC_SEVEN_NAME = "Black3";
	public static final String MC_EIGHT_NAME = "32mb";
	public static final String HERO_GARDEN_NAME = "Hero";
	public static final String NEUTRAL_GARDEN_NAME = "Neutral";
	public static final String DARK_GARDEN_NAME = "Dark";
	
	public static final String ROUND = "round4";
	public static final int GENERATION = 3;
	public static final int GARDEN_SIZE = 8;
	public static final int FINDS_GOAL_STATE = 10000;
	public static final int CHAO_SWAP_PENALTY = -10;
	public static final int MEMORY_CARD_SWAP_PENALTY = -5;
	public static final int MEMORY_CARD_DOUBLE_SWAP_PENALTY = -1;
	public static final int MEMORY_CARD_TRIPLE_SWAP_PENALTY = -100;
	public static final int CHAO_MATCH_SCORE = 15;
	public static final int CHAO_NON_MATCH_PENALTY = -1;
	public static final int CHAO_RIGHT_MC_WRONG_GARDEN_PENALTY = -5; // -17
	public static final int CHAO_RIGHT_MC_WRONG_GARDEN_MOVE_OUT_SCORE = 10;
	public static final int MAX_ITERATIONS = 100;
	public static final long TIME_ALLOWED_PER_MOVE = 1000*60*1;
	public static final int NUM_GARDENS = 3;
	public static final int NUM_MEMORY_CARDS = 6;
	public static final double PROJECTED_NUM_SWAPS_PER_CHAO = 1;
	public static final double MILLI = 1000;
	public static final int SEC_PER_MIN = 60;
	public static final int FIRST_PLACE_SCORE = 90;
	public static final int SECOND_PLACE_SCORE = 60;
	public static final int THIRD_PLACE_SCORE = 30;
	public static final int FOURTH_PLACE_SCORE = 0;
	public static final int TOP_PLACE_SCORE = 110;
	public static final int TOTAL_NUM_CHAOS_GEN_ONE = 48;
	public static final int TOTAL_NUM_CHAOS_GEN_TWO = 96;
	
	public static final String OUTPUT_FILE_NAME = "chao_swaps.txt";
	public static final String OUTPUT_FILE_NAME2 = "chao_stats.txt";
	public static final String THETA_FILE_NAME_FILTERED_STOCHASTIC = "chao_thetas_filtered_stochastic.csv";
	public static final String THETA_FILE_NAME_UNFILTERED_STOCHASTIC = "chao_thetas_unfiltered_stochastic.csv";
	public static final String THETA_FILE_NAME_FILTERED_BATCH = "chao_thetas_filtered_batch.csv";
	public static final String THETA_FILE_NAME_UNFILTERED_BATCH = "chao_thetas_unfiltered_batch.csv";
	public static final String THETA_FILE_NAME_FILTERED_FINAL_STOCHASTIC = "chao_thetas_filtered_final_stochastic.csv";
	public static final String THETA_FILE_NAME_UNFILTERED_FINAL_STOCHASTIC = "chao_thetas_unfiltered_final_stochastic.csv";
	public static final String THETA_FILE_NAME_FILTERED_FINAL_BATCH = "chao_thetas_filtered_final_batch.csv";
	public static final String THETA_FILE_NAME_UNFILTERED_FINAL_BATCH = "chao_thetas_unfiltered_final_batch.csv";

	
	public static final String DB_NAME = "Chao";
	
	public static final List<String> COURSE_LIST = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
	{
	    add("crab_pool");
	    add("stump_valley");
	    add("mushroom_forest");
	    add("block_canyon");
	    add("aquamarine");
	    add("topaz");
	    add("peridot");
	    add("garnet");
	    add("onix");
	    add("diamond");
	    add("overall");
	    add("karate");
	}};
	
	
	public static List<String> getListOfMCNames() {
		List<String> mcNames = new ArrayList<String>();
		mcNames.add(MC_ONE_NAME);
		mcNames.add(MC_TWO_NAME);
		mcNames.add(MC_THREE_NAME);
		mcNames.add(MC_FOUR_NAME);
		mcNames.add(MC_FIVE_NAME);
		mcNames.add(MC_SIX_NAME);
		mcNames.add(MC_SEVEN_NAME);
		mcNames.add(MC_EIGHT_NAME);
		return mcNames;
	}
	
	public static List<String> getListOfGardenNames() {
		List<String> gardenNames = new ArrayList<String>();
		gardenNames.add(HERO_GARDEN_NAME);
		gardenNames.add(NEUTRAL_GARDEN_NAME);
		gardenNames.add(DARK_GARDEN_NAME);
		return gardenNames;
	}
	
	public static List<String> getFeatureNames() {
		List<String> featureNames = new ArrayList<String>();
		featureNames.add("swim_stat");
		featureNames.add("fly_stat");
		featureNames.add("run_stat");
		featureNames.add("power_stat");
		featureNames.add("stamina_stat");
		featureNames.add("hat");
		featureNames.add("intellegence");
		featureNames.add("trips");
		featureNames.add("luck");
		return featureNames;
	}

	public static List<Double> getListOfPowers() {
		List<Double> powers = new ArrayList<Double>();
		powers.add(1.0/4.0); // swim stat
		powers.add(1.0/2.0); // fly stat ...
		powers.add(1.0/4.0);
		powers.add(1.0/2.0);
		powers.add(1.0/1.0);
		powers.add(1.0/1.0);
		powers.add(1.0/1.0);
		powers.add(1.0/1.0);
		powers.add(1.0/2.0);
		return powers;
	}
	
	
}
