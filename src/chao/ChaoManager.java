package chao;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ChaoManager {	
	
	public static void main(String[] args) throws ChaoSwapException, ArgumentNumberException, IOException, ImpossibleRaceException {
		Connection conn = SQLManager.getConnection();
		
		// if no parameters given
		if (args.length < 1) {
			promptUserForCorrectInput();

		}
		
		String command = args[0];
		if (command.equals("add_chao")) {
			// add chao to db
			addChaoWrapper(args, conn);
			System.out.println("Success!");
		} else if (command.equals("update_chao")) {
			//update chao in db
			updateChaoWrapper(args, conn);
			System.out.println("Success!");
		} else if (command.equals("update_garden")) {
			// update garden
			updateGarden(args, conn);
			System.out.println("Success!");
		} else if (command.equals("write_stats")) {
			// write stats in tab delimited format
			writeStats(args, conn);
			System.out.println("Success!");
		} else if (command.equals("find_swaps")) {
			// write file of swaps to perform in order
			findChaoSwaps(args, conn);
			System.out.println("Success!");
		} else if (command.equals("add_result")) {
			// add result
			addResult(args, conn);
			System.out.println("Success!");
		} else if (command.equals("predict_result")) {
			// predict result
			predictResult(args, conn, false);
			System.out.println("Success!");
		} else if (command.equals("predict_result_unfiltered")) {
			// predict result
			predictResult(args, conn, true);
			System.out.println("Success!");
		} else if (command.equals("make_swap")) {
			// make swaps based on file or input chaos to swap
			makeChaoSwap(args, conn);
			System.out.println("Success!");
		} else if (command.equals("commands")) {
			displayCommands(args);
		} else {
			// message to input correct syntax
			promptUserForCorrectInput();
		}
		
		
	}


	private static void writeStats(String[] args, Connection conn)
			throws ArgumentNumberException {
		// should be write_stats stat_to_order_by 
		if (args.length != 2 && args.length != 1) {
			handleWrongSizeException();
		}
		String statToOrderBy = getStatToOrderBy(args);
		ResultSet rs = generateResultSetForAllChaos(conn, statToOrderBy);
		writeStatsToFile(rs);
	}


	private static String getStatToOrderBy(String[] args) {
		String statToOrderBy;
		if (args.length == 2) {
			statToOrderBy = args[1];
		} else {
			statToOrderBy = "name";
		}
		return statToOrderBy;
	}


	private static ResultSet generateResultSetForAllChaos(Connection conn, String statToOrderBy) {
		String sqlQuery = "SELECT * FROM Chao ORDER BY "+statToOrderBy;
		if (statToOrderBy != "name" && statToOrderBy != "memory_card" && statToOrderBy != "garden") {
			sqlQuery += " DESC";
		}
		sqlQuery += ";";
		ResultSet rs = SQLManager.queryFromDB(conn, sqlQuery);
		return rs;
	}


	private static void writeStatsToFile(ResultSet rs) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(Constants.OUTPUT_FILE_NAME2, "UTF-8");
			writer.println("name\tmemory_card\tgarden\tswim\tfly\trun\tpower\ttotal\t"
					+ "swim_stat\tfly_stat\trun_stat\tpower_stat\ttotal_stat\tstamina\tstamina_stat"
					+ "\that\tintellegence\ttrips\tluck\tgeneration");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		writeLineOfStats(rs, writer);
		writer.close();
	}


	private static void writeLineOfStats(ResultSet rs, PrintWriter writer) {
		try {
			while (rs.next()) {
				writer.println(rs.getString(2)+"\t"+rs.getString(3)+"\t"+rs.getString(4)+"\t"+rs.getString(5)+"\t"+
						   rs.getString(6)+"\t"+rs.getString(7)+"\t"+ rs.getString(8)+"\t"+
						   rs.getString(9)+"\t"+ rs.getString(10)+"\t"+ rs.getString(11)+"\t"+
						   rs.getString(12)+"\t"+ rs.getString(13)+"\t"+ rs.getString(14)+"\t"+
						   rs.getString(15)+"\t"+ rs.getString(16)+"\t"+ rs.getString(17)+"\t"+
						   rs.getString(18)+"\t"+ rs.getString(19)+"\t"+ rs.getString(20)+"\t"+ rs.getString(21));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	private static void displayCommands(String[] args)
			throws ArgumentNumberException {
		if (args.length != 1) {
			handleWrongSizeException();
		}
		promptUser();
	}


	private static void makeChaoSwap(String[] args, Connection conn)
			throws ArgumentNumberException, FileNotFoundException, IOException {
		if (args.length != 2 && args.length != 7) { 
			handleWrongSizeException();
		}
		if (args.length == 7) { // 7 is chao_name1 garden1 mc1 name2 garden2 mc2
			makeChaoSwapsFromArgs(args, conn);
		} else {
			makeChaoSwapsFromFile(args, conn);
		}
	}


	private static void makeChaoSwapsFromFile(String[] args, Connection conn) throws FileNotFoundException, IOException {
		BufferedReader reader = new BufferedReader(new FileReader(args[1]));
		String line = null;
		while ((line = reader.readLine()) != null) {
			String[] words = line.split(" ");
			if (words.length == 16) {
				String chao1 = words[1];
				String garden1 = words[4];
				String mc1 = words[7];
				String chao2 = words[9];
				String garden2 = words[12];
				String mc2 = words[15];
				makeChaoSwap(conn, chao1, garden1, mc1, chao2, garden2, mc2);
			}
		}
		reader.close();
	}


	private static void makeChaoSwapsFromArgs(String[] args, Connection conn) {
		String chao1 = args[1];
		String garden1 = args[2];
		String mc1 = args[3];
		String chao2 = args[4];
		String garden2 = args[5];
		String mc2 = args[6];
		makeChaoSwap(conn, chao1, garden1, mc1, chao2, garden2, mc2);
	}


	private static void makeChaoSwap(Connection conn, String chao1,
			String garden1, String mc1, String chao2, String garden2, String mc2) {
		makeMcUpdate(conn, chao1, garden1, mc1, chao2);
		makeMcUpdate(conn, chao2, garden2, mc2, chao1);
		updateChao(conn, chao2, mc1, garden1);
		updateChao(conn, chao1, mc2, garden2);
	}


	private static void makeMcUpdate(Connection conn, String chao1, String garden, String mc, String chao2) {
		String whereClause = "WHERE mc_name = '"+mc+"' AND garden = '"+garden+"';";
		String sqlQueryGet = "SELECT chao FROM Memory_Card "+whereClause;
		ResultSet rs = SQLManager.queryFromDB(conn, sqlQueryGet);
		try {
			rs.next();
			String chaos = rs.getString(1);
			chaos = chaos.replaceFirst(chao1, chao2);
			String sqlQueryUpdate = "UPDATE Memory_Card SET chao = '"+chaos+"' "+whereClause;
			SQLManager.updateDB(conn, sqlQueryUpdate);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void updateChao(Connection conn, String name, String memoryCard, String garden) {
		String sqlQuery = "UPDATE Chao SET memory_card='"+memoryCard+"',garden='"+garden+"'WHERE name='"+name+"';";
		SQLManager.updateDB(conn, sqlQuery);
	}


	private static void predictResult(String[] args, Connection conn, boolean unfiltered) throws ArgumentNumberException, ImpossibleRaceException {
		if (args.length < 2) {
			handleWrongSizeException();
		}
		String course = args[1];
		List<Chao> currChaos = getChaosInRace(args, conn, course);

		// get results (mapping from chao name to place[100,75,50,25]) for given course
		Map<String, List<Integer>> raceResults = getRaceResultsFromRecords(conn, course, false);
		Map<String, List<Integer>> raceResultsFinal = getRaceResultsFromRecords(conn, course, true); 
		
		// get stats for chao and create mapping Chao : place[100,75,50,25]
		Map<Chao, Integer> chaoRecords = createChaoRecords(conn, raceResults);
		Map<Chao, Integer> chaoRecordsFinal = createChaoRecords(conn, raceResultsFinal);
		
		// find max and min values for each feature
		List<Map<String, Integer>> minMaxes = getFeatureMinMaxes(conn);
				
		performGradientDescentToFindThetasAndPrintResults(course, currChaos, chaoRecords, chaoRecordsFinal, minMaxes, false, unfiltered);
		performGradientDescentToFindThetasAndPrintResults(course, currChaos, chaoRecords, chaoRecordsFinal, minMaxes, true, unfiltered);
	}


	private static void performGradientDescentToFindThetasAndPrintResults(
			String course, List<Chao> currChaos, Map<Chao, Integer> chaoRecords,
			Map<Chao, Integer> chaoRecordsFinal, List<Map<String, Integer>> minMaxes, boolean power, boolean unfiltered) {
		// find thetas 
		if (power) {
			System.out.println("Finding power results...");
		}
		List<Double> thetasFilteredStochastic = GradientDescent.getThetasFiltered(course, chaoRecords, minMaxes, false, false, Constants.THETA_FILE_NAME_FILTERED_STOCHASTIC, power);
		List<Double> thetasUnfilteredStochastic = GradientDescent.getThetasUnfiltered(course, chaoRecords, minMaxes, false, false, Constants.THETA_FILE_NAME_UNFILTERED_STOCHASTIC, power);
		List<Double> thetasFilteredBatch = GradientDescent.getThetasFiltered(course, chaoRecords, minMaxes, false, true, Constants.THETA_FILE_NAME_FILTERED_BATCH, power);
		List<Double> thetasUnfilteredBatch = GradientDescent.getThetasUnfiltered(course, chaoRecords, minMaxes, false, true, Constants.THETA_FILE_NAME_UNFILTERED_BATCH, power);
		List<Double> thetasFilteredStochasticFinal = GradientDescent.getThetasFiltered(course, chaoRecordsFinal, minMaxes, true, false, Constants.THETA_FILE_NAME_FILTERED_FINAL_STOCHASTIC, power);
		List<Double> thetasUnfilteredStochasticFinal = GradientDescent.getThetasUnfiltered(course, chaoRecordsFinal, minMaxes, true, false, Constants.THETA_FILE_NAME_UNFILTERED_FINAL_STOCHASTIC, power);
		List<Double> thetasFilteredBatchFinal = GradientDescent.getThetasFiltered(course, chaoRecordsFinal, minMaxes, true, true, Constants.THETA_FILE_NAME_FILTERED_FINAL_BATCH, power);
		List<Double> thetasUnfilteredBatchFinal = GradientDescent.getThetasUnfiltered(course, chaoRecordsFinal, minMaxes, true, true, Constants.THETA_FILE_NAME_UNFILTERED_FINAL_BATCH, power);
		System.out.println("");
		System.out.println("Filtered thetas stochastic: "+thetasFilteredStochastic);
		System.out.println("Unfiltered thetas stochastic: "+thetasUnfilteredStochastic);
		System.out.println("");
		System.out.println("Filtered thetas batch: "+thetasFilteredBatch);
		System.out.println("Unfiltered thetas batch: "+thetasUnfilteredBatch);
		System.out.println("");
		System.out.println("Filtered thetas stochastic final: "+thetasFilteredStochasticFinal);
		System.out.println("Unfiltered thetas stochastic final: "+thetasUnfilteredStochasticFinal);
		System.out.println("");
		System.out.println("Filtered thetas batch final: "+thetasFilteredBatchFinal);
		System.out.println("Unfiltered thetas batch final: "+thetasUnfilteredBatchFinal);
		System.out.println("");
		
		// calculate a score for each chao in current race
		System.out.println("Stochastic: ");
		printChaoRanks(currChaos, minMaxes, thetasFilteredStochastic, thetasUnfilteredStochastic, power, unfiltered);
		System.out.println("Batch: ");
		printChaoRanks(currChaos, minMaxes, thetasFilteredBatch, thetasUnfilteredBatch, power, unfiltered);
		System.out.println("Final Stochastic: ");
		printChaoRanks(currChaos, minMaxes, thetasFilteredStochasticFinal, thetasUnfilteredStochasticFinal, power, unfiltered);
		System.out.println("Final Batch: ");
		printChaoRanks(currChaos, minMaxes, thetasFilteredBatchFinal, thetasUnfilteredBatchFinal, power, unfiltered);
	}


	private static List<Map<String, Integer>> getFeatureMinMaxes(Connection conn) {
		List<Map<String, Integer>> minMaxes = new ArrayList<Map<String, Integer>>();
		for (String featureName : Constants.getFeatureNames()) {
			Map<String, Integer> currMinMax = new HashMap<String, Integer>();
			String sqlQuery = "SELECT min(abs("+featureName+")),max(abs("+featureName+")) FROM Chao;";
					ResultSet rs = SQLManager.queryFromDB(conn, sqlQuery);
					try {
						rs.next();
						currMinMax.put("min", rs.getInt(1));
						currMinMax.put("max", rs.getInt(2));
						minMaxes.add(currMinMax);
					} catch (SQLException e) {
						e.printStackTrace();
					}
		}
		return minMaxes;
	}


	private static void printChaoRanks(List<Chao> currChaos, List<Map<String, Integer>> minMaxes, List<Double> thetasFiltered, List<Double> thetasUnfiltered, boolean power, boolean unfiltered) {
		List<OutputScore> outputs = new ArrayList<OutputScore>();
		for (Chao chao : currChaos) {
			String output = "";
			// get score for filetered results
			double scoreFiltered = GradientDescent.findScore(chao, thetasFiltered, minMaxes, power);
			// get score for unfiltered results
			double scoreUnfiltered = GradientDescent.findScore(chao, thetasUnfiltered, minMaxes, power);
			output += chao.getName()+": [Filtered: "+scoreFiltered+"], [Unfiltered: "+scoreUnfiltered+"]";
			OutputScore outputScore = new OutputScore(output, scoreFiltered, scoreUnfiltered, unfiltered);
			outputs.add(outputScore);
		}
		Collections.sort(outputs);
		for (OutputScore output : outputs) {
			System.out.println(output.getOutput());
		}
	}


	private static List<Chao> getChaosInRace(String[] args, Connection conn, String course) throws ImpossibleRaceException {
		List<Chao> currChaos = null;
		if (args.length != 2) {
			List<String> currChaoNames = getCurrChaoNames(args, 2);
			if (!raceIsPossible(conn, course, currChaoNames)) {
				throw new ImpossibleRaceException();
			}
			currChaos = getRacingChaosStats(conn, currChaoNames);
		} else {
			currChaos = getAllChaosStats(conn);
		}
		return currChaos;
	}


	private static List<String> getCurrChaoNames(String[] args, int startIndex) {
		List<String> currChaoNames = new ArrayList<String>();
		for (int i = startIndex; i < args.length; i++) {
			currChaoNames.add(args[i]);
		}
		return currChaoNames;
	}

	private static List<Chao> getAllChaosStats(Connection conn) {
		List<Chao> currChaos = new ArrayList<Chao>();
		String sqlQuery = "SELECT * FROM Chao;";
		createChaoFromResultSet(conn, currChaos, sqlQuery);
		return currChaos;
	}
	
	private static List<Chao> getRacingChaosStats(Connection conn, List<String> currChaoNames) {
		List<Chao> currChaos = new ArrayList<Chao>();
		for (String name : currChaoNames) {
			String sqlQuery = "SELECT * FROM Chao WHERE name = '"+name+"';";
			createChaoFromResultSet(conn, currChaos, sqlQuery);
		}
		return currChaos;
	}


	private static void createChaoFromResultSet(Connection conn, List<Chao> currChaos, String sqlQuery) {
		ResultSet rs = SQLManager.queryFromDB(conn, sqlQuery);
		try {
			while (rs.next()) {
				// make chao from rs
				Chao chao = new Chao(rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						   rs.getString(6), rs.getString(7), rs.getString(8),
						   rs.getString(9), rs.getString(10), rs.getString(11),
						   rs.getString(12), rs.getString(13), rs.getString(14),
						   rs.getString(15), rs.getString(16), rs.getString(17), 
						   rs.getString(18), rs.getString(19), rs.getString(20));
				currChaos.add(chao);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	private static Map<Chao, Integer> createChaoRecords(Connection conn, Map<String, List<Integer>> raceResults) {
		Map<Chao, Integer> chaoRecords = new HashMap<Chao, Integer>();
		for (String name : raceResults.keySet()) {
			String sqlQuery = "SELECT * FROM Chao WHERE name = '"+name+"';";
			ResultSet rs = SQLManager.queryFromDB(conn, sqlQuery);
			try {
				while (rs.next()) {
					// make chao from rs and add it to chaoRecords
					for (int result : raceResults.get(name)) {
						Chao chao = new Chao(name, rs.getString(3), rs.getString(4), rs.getString(5),
								   rs.getString(6), rs.getString(7), rs.getString(8),
								   rs.getString(9), rs.getString(10), rs.getString(11),
								   rs.getString(12), rs.getString(13), rs.getString(14),
								   rs.getString(15), rs.getString(16), rs.getString(17), 
								   rs.getString(18), rs.getString(19), rs.getString(20));
						chaoRecords.put(chao, result);
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return chaoRecords;
	}


	private static Map<String, List<Integer>> getRaceResultsFromRecords(Connection conn, String course, boolean onlyFinal) {
		Map<String, List<Integer>> raceResults = new HashMap<String, List<Integer>>();
		if (!onlyFinal) {
			addFourLaneRaceResults(conn, course, raceResults);
		}
		addFinalRaceResults(conn, course, raceResults);
		return raceResults;
	}


	private static void addFinalRaceResults(Connection conn, String course, Map<String, List<Integer>> raceResults) {
		ResultSet rs;
		String sqlQuery = "SELECT * FROM results ";
		if (course != null) {
			sqlQuery +=  "WHERE course = '"+course+"'";
		}
		sqlQuery += ";";
		rs = SQLManager.queryFromDB(conn, sqlQuery); 
		try {
			while (rs.next()) {
				populateRaceResults(rs, raceResults, 3, rs.getInt(4));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	private static void addFourLaneRaceResults(Connection conn, String course, Map<String, List<Integer>> raceResults) {
		ResultSet rs;
		String sqlQuery = "SELECT * FROM records ";
		if (course != null) {
			sqlQuery +=  "WHERE course = '"+course+"'";
		}
		sqlQuery += ";";
		rs = SQLManager.queryFromDB(conn, sqlQuery); 
		try {
			while (rs.next()) {
				populateRaceResults(rs, raceResults, 3, Constants.FIRST_PLACE_SCORE);
				populateRaceResults(rs, raceResults, 4, Constants.SECOND_PLACE_SCORE);
				populateRaceResults(rs, raceResults, 5, Constants.THIRD_PLACE_SCORE);
				populateRaceResults(rs, raceResults, 6, Constants.FOURTH_PLACE_SCORE);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	private static void populateRaceResults(ResultSet rs, Map<String, List<Integer>> raceResults, int i, int score) throws SQLException {
		String name = rs.getString(i);
		if (!raceResults.containsKey(name)) {
			List<Integer> scores = new ArrayList<Integer>();
			scores.add(score);
			raceResults.put(name, scores);
		} else {
			List<Integer> scores = raceResults.get(name);
			scores.add(score);
			raceResults.put(name, scores);
		}
	}


	private static void addResult(String[] args, Connection conn) throws ArgumentNumberException, ImpossibleRaceException {
		if (args.length != 6 && args.length != 3) {
			handleWrongSizeException();
		}
		if (args.length == 6) {
			addResultFromInput(args, conn);
		} else {
			List<String> chaos = extractChaoNames(args);
			addResultsFromFile(args, conn, chaos);
			
			// calculate and update luck
			Map<String, List<List<String>>> courseResults = getActualResults(conn);
			List<String> genOneNames = getGenOneChaos(conn);
			findLucksAndUpdateDB(conn, chaos, courseResults, genOneNames);
		}
	}


	private static Map<String, List<List<String>>> getActualResults(Connection conn) {
		// get list of actual results by looking at groups of results (check when score goes up)
		Map<String, List<List<String>>> courseResults = new HashMap<String, List<List<String>>>();
		String sqlQuery = "SELECT name,score,course FROM results;";
		ResultSet rs = SQLManager.queryFromDB(conn, sqlQuery); 
		try {
			String course = "";
			while (!rs.isAfterLast()) {
				List<String> resultRound = new ArrayList<String>();
				if (rs.isBeforeFirst()) {
					rs.next();
				}
				int prevScore = Integer.MAX_VALUE;
				course = rs.getString(3);
				while (!rs.isAfterLast() && rs.getInt(2) < prevScore) {
					resultRound.add(rs.getString(1));
					prevScore = rs.getInt(2);
					rs.next();
				}
				updateCourseResults(courseResults, course, resultRound);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return courseResults;
	}


	private static void updateCourseResults(Map<String, List<List<String>>> courseResults, String course, List<String> resultRound) {
		List<List<String>> results = null;
		if (courseResults.containsKey(course)) {
			results = courseResults.get(course);
		} else {
			results = new ArrayList<List<String>>();
		}
		results.add(resultRound);
		courseResults.put(course, results);
	}


	private static List<String> getGenOneChaos(Connection conn) {
		// get list of gen 1 chaos through query to MySQL
		List<String> genOneNames = new ArrayList<String>();
		String sqlQuery = "SELECT name FROM Chao WHERE generation = 1;";
		ResultSet rs = SQLManager.queryFromDB(conn, sqlQuery); 
		try {
			while (rs.next()) {
				genOneNames.add(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return genOneNames;
	}


	private static void findLucksAndUpdateDB(Connection conn, List<String> chaos, Map<String, List<List<String>>> courseResults, List<String> genOneNames) {
		// get expected order of chaos depending on how long the list of chaos is
		for (String course : courseResults.keySet()) {
			// get results (mapping from chao name to place[100,75,50,25]) for given course
			Map<String, List<Integer>> raceResults = new HashMap<String, List<Integer>>();
			addFinalRaceResults(conn, course, raceResults);
			// get stats for chao and create mapping Chao : place[100,75,50,25]
			Map<Chao, Integer> chaoRecords = createChaoRecords(conn, raceResults);
			// get feature mins and maxes to normalize
			List<Map<String, Integer>> minMaxes = getFeatureMinMaxes(conn);
			// find thetas
			List<Double> thetasFilteredStochastic = GradientDescent.getThetasFiltered(course, chaoRecords, minMaxes, false, false, Constants.THETA_FILE_NAME_FILTERED_STOCHASTIC, false);
			for (List<String> resultRound : courseResults.get(course)) {
				List<Chao> currGenChaos = null;
				if (resultRound.size() == Constants.TOTAL_NUM_CHAOS_GEN_ONE) {
					currGenChaos = getRacingChaosStats(conn, genOneNames);
				} else {
					currGenChaos = getAllChaosStats(conn);
				}
				updateLuckInDB(conn, resultRound, currGenChaos, thetasFilteredStochastic);
			}
		}
	}


	private static void updateLuckInDB(Connection conn, List<String> resultRound, List<Chao> currGenChaos, List<Double> thetasFilteredStochastic) {

		// find the expected place of each chao
		Map<String, Integer> expectedPlaces = findExpectedFinishOrder(conn, currGenChaos, thetasFilteredStochastic);
		
		// find the actual place of each chao (stored in resultRound in order
		
		// calculate the luck for each chao
		for (int i = 0; i < resultRound.size(); i++) {
			String currChao = resultRound.get(i);
			int luck = expectedPlaces.get(currChao) - i+1;
			
			String sqlQuery = "UPDATE Chao SET luck = "+luck+" WHERE name = '"+currChao+"';";
			SQLManager.updateDB(conn, sqlQuery);
		}
	}


	private static List<String> extractChaoNames(String[] args) {
		List<String> chaos = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(args[2]));
			String line = null;
			while ((line = reader.readLine()) != null) {
				chaos.add(line);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return chaos;
	}


	private static Map<String, Integer> findExpectedFinishOrder(Connection conn, List<Chao> genOneChaos, List<Double> thetasFiltered) {
		Map<String, Integer> expectedPlaces = new HashMap<String, Integer>();
		List<OutputScore> outputs = new ArrayList<OutputScore>();
		List<Map<String, Integer>> minMaxes = getFeatureMinMaxes(conn);
		for (Chao chao : genOneChaos) {
			// get score for filetered results
			double scoreFiltered = GradientDescent.findScore(chao, thetasFiltered, minMaxes, false);
			OutputScore outputScore = new OutputScore(chao.getName(), scoreFiltered);
			outputs.add(outputScore);
		}
		Collections.sort(outputs);
		for (int i = 0; i < outputs.size(); i++) {
			OutputScore output = outputs.get(i);
			expectedPlaces.put(output.getOutput(), i+1);
		}
		return expectedPlaces;
	}


	private static void addResultsFromFile(String[] args, Connection conn, List<String> chaos) {
		double score = Constants.TOP_PLACE_SCORE;
		int iter = 1;
		int numChao = chaos.size();
		for (String chao : chaos) {
			String sqlQuery = "INSERT INTO results "
					+ "(course,name,score)"
					+ " VALUES ('"+args[1]+"','"+chao+"','"+score+"');";
			SQLManager.updateDB(conn, sqlQuery);
			score = Constants.TOP_PLACE_SCORE*(numChao-iter)/numChao; // linear method
//			score = (double) Math.round(Math.pow(Math.sqrt(Constants.TOP_PLACE_SCORE)*(numChao-iter)/numChao,2) * 100000) / 100000; // power method
			iter ++;
		}
	}


	private static void addResultFromInput(String[] args, Connection conn) throws ImpossibleRaceException {
		String course = args[1];
		String firstPlace = args[2];
		String secondPlace = args[3];
		String thirdPlace = args[4];
		String fourthPlace = args[5];
		List<String> currChaoNames = getCurrChaoNames(args, 2);
		if (!raceIsPossible(conn, course, currChaoNames)) {
			throw new ImpossibleRaceException();
		}
		String sqlQuery = "INSERT INTO records "
				+ "(course,first_place,second_place,third_place,fourth_place)"
				+ " VALUES ('"+course+"','"+firstPlace+"','"+secondPlace+"',"
				+ "'"+thirdPlace+"','"+fourthPlace+"');";
		SQLManager.updateDB(conn, sqlQuery);
	}


	private static boolean raceIsPossible(Connection conn, String course, List<String> chaoNames) {
		if (!Constants.COURSE_LIST.contains(course)) {
			System.out.println("Error: course does not exist");
			return false;
		}
		for (String chaoName : chaoNames) {
			if (!chaoExists(conn, chaoName)) {
				System.out.println("Error: Chao "+chaoName+" does not exist");
				return false;
			}
		}
		return true;
	}


	private static void findChaoSwaps(String[] args, Connection conn) throws ArgumentNumberException, IOException, ChaoSwapException {
		MemoryState currState = getCurrMemoryState(conn);
		MemoryState finalState = getFinalMemoryState(args);
		double startTime = System.currentTimeMillis();
		Moveset bestMoveSet = AStarSearch.findBestMoveset(new MovesetWithState(currState), finalState);
//		Moveset bestMoveSet = MonteCarlo.findBestMoveset(currState, finalState, new Moveset(), null, 0, startTime);
		double timeTaken = System.currentTimeMillis() - startTime;
		printChaoSwapOutput(bestMoveSet, timeTaken);
	}


	private static void printChaoSwapOutput(Moveset bestMoveSet, double timeTaken) {
		System.out.println("time taken = "+timeTaken/Constants.MILLI/Constants.SEC_PER_MIN+" min");
		writeToFile(bestMoveSet);
		System.out.println("Done.");
		System.out.println("Total moves: "+bestMoveSet.getTotalMoves());
		System.out.println("Chao swaps: "+bestMoveSet.getNumChaoSwaps());
		System.out.println("Memory card swaps: "+bestMoveSet.getNumMemoryCardSwaps());
		for (int i = 0; i < bestMoveSet.getTotalMoves(); i++) {
			System.out.println(bestMoveSet.getMoves().get(i).toString());
		}
		System.out.println("Total moves: "+bestMoveSet.getTotalMoves());
	}
	

	private static void writeToFile(Moveset bestMoveSet) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(Constants.OUTPUT_FILE_NAME, "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		writer.println("Total moves: "+bestMoveSet.getTotalMoves());
		writer.println("Chao swaps: "+bestMoveSet.getNumChaoSwaps());
		writer.println("Memory card swaps: "+bestMoveSet.getNumMemoryCardSwaps());		
		for (int i = 0; i < bestMoveSet.getTotalMoves(); i++) {
			writer.println(bestMoveSet.getMoves().get(i).toString());
		}
		writer.println("Total moves: "+bestMoveSet.getTotalMoves());
		writer.close();
	}

	private static MemoryState getFinalMemoryState(String[] args) throws ArgumentNumberException, IOException {
		if (args.length != 2) {
			handleWrongSizeException();
		}
		Map<String, MemoryCard> memoryCards = new HashMap<String, MemoryCard>();
		Gamecube gc = null;
		MemoryCardSlot leftSlot = null;
		MemoryCardSlot rightSlot = null;
		
		BufferedReader reader = new BufferedReader(new FileReader(args[1]));
		String line = null;
		for (String mcName : Constants.getListOfMCNames()) {
			Map<String, ChaoGarden> chaoGardens = new HashMap<String, ChaoGarden>();
			for (String gardenName : Constants.getListOfGardenNames()) {
				ChaoGarden garden = new ChaoGarden(gardenName, mcName);
				int i = 1;
				while ((line = reader.readLine()) != null) {
					garden.addChao(line);
					i++;
				    if (i > Constants.GARDEN_SIZE) {
				    	break;
				    }
				}
				chaoGardens.put(gardenName, garden);
			}
			
			// create mc info and insert two first mcs into gamecube just to begin.
			if (leftSlot == null) {
				leftSlot = fillMCInfo(memoryCards, mcName, chaoGardens, true);
			} else if (rightSlot == null) {
				rightSlot = fillMCInfo(memoryCards, mcName, chaoGardens, true);
			} else {
				fillMCInfo(memoryCards, mcName, chaoGardens, false);				
			}
			
		}
		
		reader.close();
		gc = new Gamecube(leftSlot, rightSlot);
		MemoryState mState = new MemoryState(memoryCards, gc);
		return mState;
	}

	private static MemoryState getCurrMemoryState(Connection conn) {
		Map<String, MemoryCard> memoryCards = new HashMap<String, MemoryCard>();
		Gamecube gc = null;
		MemoryCardSlot leftSlot = null;
		MemoryCardSlot rightSlot = null;
		for (String mcName : Constants.getListOfMCNames()) {
			Map<String, ChaoGarden> chaoGardens = createInitialChaoGardens(conn, mcName);
			
			// create mc info and insert two first mcs into gamecube just to begin.
			if (leftSlot == null) {
				leftSlot = fillMCInfo(memoryCards, mcName, chaoGardens, true);
			} else if (rightSlot == null) {
				rightSlot = fillMCInfo(memoryCards, mcName, chaoGardens, true);
			} else {
				fillMCInfo(memoryCards, mcName, chaoGardens, false);				
			}
		}
		gc = new Gamecube(leftSlot, rightSlot);
		MemoryState ms = new MemoryState(memoryCards, gc);
		return ms;
	}

	private static Map<String, ChaoGarden> createInitialChaoGardens(Connection conn, String mcName) {
		ResultSet rs;
		Map<String, ChaoGarden> chaoGardens = new HashMap<String, ChaoGarden>();
		for (String gardenName : Constants.getListOfGardenNames()) {
			String sqlQuery = "SELECT chao FROM Memory_Card WHERE mc_name = '"+mcName+"' AND garden = '"+gardenName+"';";
			rs = SQLManager.queryFromDB(conn, sqlQuery); 
			try {
				Set<String> chaos = parseChaosResultSet(rs);
				ChaoGarden garden = new ChaoGarden(gardenName, mcName, chaos);
				chaoGardens.put(gardenName, garden);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		return chaoGardens;
	}

	private static Set<String> parseChaosResultSet(ResultSet rs) throws SQLException {
		Set<String> chaos = new HashSet<String>();
		rs.next();
		String chaoString = rs.getString(1);
		String[] chaoArray = chaoString.split("\\s+");
		for (int i = 0; i < chaoArray.length; i++) { // for each chao
			chaos.add(chaoArray[i]);
		}
		return chaos;
	}

	private static MemoryCardSlot fillMCInfo(Map<String, MemoryCard> memoryCards, String mcName, Map<String, ChaoGarden> chaoGardens, boolean inserted) {
		MemoryCardSlot slot;
		MemoryCard mc = new MemoryCard(mcName, chaoGardens.get(Constants.HERO_GARDEN_NAME), chaoGardens.get(Constants.NEUTRAL_GARDEN_NAME), chaoGardens.get(Constants.DARK_GARDEN_NAME), inserted);
		memoryCards.put(mcName, mc);
		slot = new MemoryCardSlot(mc);
		return slot;
	}

	private static void updateGarden(String[] args, Connection conn) throws ArgumentNumberException {
		if (args.length != 11) {
			handleWrongSizeException();
		} else {
			String memoryCard = args[1];
			String garden = args[2];
			if (!memoryCardExists(conn,memoryCard) || !gardenExists(conn,garden)) {
				System.out.println("Error: wrong garden or memory card name");
				return;
			}
			String chaoNames = "";
			for (int i = 3; i < 11; i++) {
				chaoNames = chaoNames+args[i]+" ";
			}
			chaoNames = chaoNames.substring(0, chaoNames.length()-1);
			
			String sqlQuery = "UPDATE Memory_Card "
					+ "SET chao='"+chaoNames+"'"
					+ "WHERE mc_name='"+memoryCard+"' AND garden='"+garden+"';";
			SQLManager.updateDB(conn, sqlQuery);
		}
	}

	private static boolean chaoExists(Connection conn, String chao){
		String sqlQuery = "SELECT name FROM chao WHERE name = '"+chao+"';";
		return checkIfResultSetMatches(conn, chao, sqlQuery);
	}
	
	private static boolean gardenExists(Connection conn, String garden){
		String sqlQuery = "SELECT garden FROM Memory_Card WHERE garden = '"+garden+"';";
		return checkIfResultSetMatches(conn, garden, sqlQuery);
	}

	private static boolean memoryCardExists(Connection conn, String memoryCard){
		String sqlQuery = "SELECT mc_name FROM Memory_Card WHERE mc_name = '"+memoryCard+"';";
		return checkIfResultSetMatches(conn, memoryCard, sqlQuery);
	}
	
	// checks to see if the result set generated from the SQL query returns something or nothing
	private static boolean checkIfResultSetMatches(Connection conn, String s, String sqlQuery) {
		ResultSet rs = SQLManager.queryFromDB(conn, sqlQuery);
		try {
			if (rs.next() && rs.getString(1).equals(s)) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		return false;
	}

	private static void updateChaoWrapper(String[] args, Connection conn) throws ArgumentNumberException {
		Chao chao = pullChaoDataFromArgs(args);
		if (chao != null) {
			ChaoStats stats = chao.getStats();
			updateChao(conn, chao.getName(), chao.getMemoryCard(), chao.getGarden(), stats.getSwim(), stats.getFly(), stats.getRun(), 
					stats.getPower(), stats.getTotal(), stats.getSwimStat(), stats.getFlyStat(), stats.getRunStat(), stats.getPowerStat(), stats.getTotalStat());
		}
	}

	private static void addChaoWrapper(String[] args, Connection conn) throws ArgumentNumberException {
		Chao chao = pullChaoDataFromArgs(args);
		if (chao != null) {
			ChaoStats stats = chao.getStats();
			addChao(conn, chao.getName(), chao.getMemoryCard(), chao.getGarden(), stats.getSwim(), stats.getFly(), stats.getRun(), 
					stats.getPower(), stats.getTotal(), stats.getSwimStat(), stats.getFlyStat(), stats.getRunStat(), stats.getPowerStat(), 
					stats.getTotalStat(), stats.getStamina(), stats.getStaminaStat());
		}
	}

	private static Chao pullChaoDataFromArgs(String[] args) throws ArgumentNumberException {
		if (args.length != 14) {
			handleWrongSizeException();
		}
		String name = args[1];
		String memoryCard = args[2];
		String garden = args[3];
		int swim = Integer.parseInt(args[4]);
		int fly = Integer.parseInt(args[5]);
		int run = Integer.parseInt(args[6]);
		int power = Integer.parseInt(args[7]);
		int total = swim+fly+run+power;
		int swimStat = Integer.parseInt(args[8]);
		int flyStat = Integer.parseInt(args[9]);
		int runStat = Integer.parseInt(args[10]);
		int powerStat = Integer.parseInt(args[11]);
		int totalStat = swimStat+flyStat+runStat+powerStat;
		int stamina = Integer.parseInt(args[12]);
		int staminaStat = Integer.parseInt(args[13]);
		Chao chao = new Chao(name, memoryCard, garden, swim, fly, run, power, total, swimStat, flyStat, runStat, powerStat, totalStat, stamina, staminaStat, false, 0, 0, 0);
		return chao;
	}

	private static void handleWrongSizeException()
			throws ArgumentNumberException {
		System.out.println("Error: Wrong number of arguments");
		promptUserForCorrectInput();
		throw new ArgumentNumberException();
	}

	private static void promptUserForCorrectInput() {
		System.out.println("To add a new chao to the DB, enter add_chao name memory_card "
							+ "garden swim fly run power swim_stat fly_stat "
							+ "run_stat power_stat");
		System.out.println("");
		System.out.println("To update a chao in the DB, enter update_chao along with the chaos name "
							+ "and the same information required for adding a new chao");
		System.out.println("");
		System.out.println("To update a garden in the DB, enter update_garden along with the memory card name, garden name,"
							+ "and then the chaos names in the garden separated by spaces");
	}
	
	private static void promptUser() {
		System.out.println("Command options:");
		System.out.println("add_chao");
		System.out.println("add_memory_card");
		System.out.println("update_memory_card");
		System.out.println("write_stats");
		System.out.println("find_swaps");
		System.out.println("add_result");
		System.out.println("predict_result");
		System.out.println("make_swap");
	}

	private static void addChao(Connection conn, String name, String memoryCard, String garden, 
							   int swim, int fly, int run, int power, int total, 
							   int swimStat, int flyStat, int runStat, int powerStat, 
							   int totalStat, int stamina, int staminaStat) {
		
		String sqlQuery = "INSERT INTO Chao "
				+ "(name,memory_card,garden,swim,fly,run,power,total,"
				+ "swim_stat,fly_stat,run_stat,power_stat,total_stat,stamina,stamina_stat)"
				+ " VALUES ('"+name+"','"+memoryCard+"','"+garden+"',"
				+ "'"+swim+"','"+fly+"','"+run+"','"+power+"','"+total+"',"
				+ "'"+swimStat+"','"+flyStat+"','"+runStat+"','"+powerStat
				+"','"+totalStat+"','"+stamina+"','"+staminaStat+"','"+"0"+"','"+"0"+"');";
		SQLManager.updateDB(conn, sqlQuery);
	}
	
	private static void updateChao(Connection conn, String name, String memoryCard, String garden, 
								   int swim, int fly, int run, int power, int total, 
								   int swimStat, int flyStat, int runStat, int powerStat, int totalStat) {
		String sqlQuery = "UPDATE Chao "
						+ "SET memory_card='"+memoryCard+"',garden='"+garden+"',"
						+ "swim='"+swim+"',fly='"+fly+"',run='"+run+"',power='"+power+"',total='"+total+"',"
						+ "swim_stat='"+swimStat+"',fly_stat='"+flyStat+"',run_stat='"+runStat+"',power_stat='"+powerStat+"',"
						+ "total_stat='"+totalStat+"'"
						+ "WHERE name='"+name+"';";
		SQLManager.updateDB(conn, sqlQuery);
	}
	
	
}
