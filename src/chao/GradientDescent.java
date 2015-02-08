package chao;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GradientDescent {

	private static final double THETA_INITIAL_VALUE = 0.0;
	private static final int FILTERED_NUM_THETAS = 4;
	private static final int UNFILTERED_NUM_THETAS = 9;
	private static final int MAX_ITERATIONS = 1000;
	private static final double STOPPING_DELTA = 0.001; 
	// adjust the alpha values to tune the speed of gradient descent
	private static final double STOCHASTIC_ALPHA_FILTERED = 0.1; 
	private static final double BATCH_ALPHA_FILTERED = 0.001; 
	private static final double STOCHASTIC_ALPHA_UNFILTERED = 0.1; 
	private static final double BATCH_ALPHA_UNFILTERED = 0.001; 
	
	public static List<Double> getThetasFiltered(Map<Chao, Integer> chaoRecords, List<Map<String, Integer>> minMaxes, boolean batch, String thetaFileName, boolean power) {
		List<Double> thetas = new ArrayList<Double>();
		double delta = Double.MAX_VALUE;
		for (int i = 0; i < FILTERED_NUM_THETAS; i++) {
			thetas.add(THETA_INITIAL_VALUE);
		}
		List<Map<String, Integer>> filteredMinMaxes = minMaxes.subList(0, FILTERED_NUM_THETAS);
		performGradientDescent(chaoRecords, filteredMinMaxes, batch, thetas, delta, STOCHASTIC_ALPHA_FILTERED, BATCH_ALPHA_FILTERED, thetaFileName, power);
		return thetas;
	}

	public static List<Double> getThetasUnfiltered(Map<Chao, Integer> chaoRecords, List<Map<String, Integer>> minMaxes, boolean batch, String thetaFileName, boolean power) {
		List<Double> thetas = new ArrayList<Double>();
		double delta = Double.MAX_VALUE;
		for (int i = 0; i < UNFILTERED_NUM_THETAS; i++) {
			thetas.add(THETA_INITIAL_VALUE);
		}
		performGradientDescent(chaoRecords, minMaxes, batch, thetas, delta, STOCHASTIC_ALPHA_UNFILTERED, BATCH_ALPHA_UNFILTERED, thetaFileName, power);
		return thetas;
	}
	
	private static void performGradientDescent(Map<Chao, Integer> chaoRecords, List<Map<String, Integer>> minMaxes, boolean batch, List<Double> thetas, double delta, double stochasticAlpha, double batchAlpha, String thetaFileName, boolean power) {
		if (chaoRecords.size() != 0) {
			if (batch) {
				batchGradientDescent(chaoRecords, minMaxes, thetas, delta, batchAlpha, thetaFileName, power);
			} else {
				stochasticGradientDescent(chaoRecords, minMaxes, thetas, delta, stochasticAlpha, thetaFileName, power);
			}
		}
	}

	private static void stochasticGradientDescent(Map<Chao, Integer> chaoRecords, List<Map<String, Integer>> minMaxes, List<Double> thetas, double delta, double alpha, String thetaFileName, boolean power) {
		PrintWriter writer = prepareWritingThetasToFile(thetaFileName);
		int iter = 0;
		List<Chao> samples = new ArrayList<Chao>(chaoRecords.keySet());
		while (iter < MAX_ITERATIONS /*&& Math.abs(delta) > STOPPING_DELTA*/) {
			int sampleIndex = iter % chaoRecords.size();
			List<Double> newThetas = new ArrayList<Double>();
			Chao sample = samples.get(sampleIndex);
			double error = findError(chaoRecords, thetas, sample, minMaxes, power);
			for (int thetaIndex = 0; thetaIndex < thetas.size(); thetaIndex++) {
				double x = sample.getStats().get(thetaIndex);
				double xNorm = normalizeX(minMaxes, power, thetaIndex, x);
				newThetas.add(thetas.get(thetaIndex)+(alpha*error*xNorm));
				delta = error;
//				System.out.println("Error: "+delta+"Change in theta: "+(alpha*error*xNorm));
			}
			for (int thetaIndex = 0; thetaIndex < thetas.size(); thetaIndex++) {
				thetas.set(thetaIndex, newThetas.get(thetaIndex));
			}
			if (!power) {
				System.out.println(iter);				
				System.out.println(thetas);
			}
			String csvLine = thetas.toString().replace("[", "").replace("]", "").replace(", ", ",");
			writer.println(csvLine+","+delta);
			iter ++;
		}
		writer.close();
	}
	
	private static void batchGradientDescent(Map<Chao, Integer> chaoRecords, List<Map<String, Integer>> minMaxes, List<Double> thetas, double delta, double alpha, String thetaFileName, boolean power) {
		PrintWriter writer = prepareWritingThetasToFile(thetaFileName);
		int iter = 0;
		List<Chao> samples = new ArrayList<Chao>(chaoRecords.keySet());
		while (iter < MAX_ITERATIONS && Math.abs(delta) > STOPPING_DELTA) {
			List<Double> newThetas = new ArrayList<Double>();
			double cumError = 0.0;
			for (int thetaIndex = 0; thetaIndex < thetas.size(); thetaIndex++) {
				double errorx = 0.0;
				for (Chao sample : samples) {
					double error = findError(chaoRecords, thetas, sample, minMaxes, power);
					cumError += error;
					double x = sample.getStats().get(thetaIndex);
					double xNorm = normalizeX(minMaxes, power, thetaIndex, x);
					errorx += error*xNorm;
				}
				newThetas.add(thetas.get(thetaIndex)+(alpha*errorx));
			}
			for (int thetaIndex = 0; thetaIndex < thetas.size(); thetaIndex++) {
				thetas.set(thetaIndex, newThetas.get(thetaIndex));
			}
			delta = cumError;
			String csvLine = thetas.toString().replace("[", "").replace("]", "").replace(", ", ",");
			writer.println(csvLine+","+delta);
			if (!power) {
				System.out.println(iter);				
				System.out.println("Error: "+delta);
				System.out.println(thetas);
			}
			iter ++;
		}
		writer.close();
	}

	private static PrintWriter prepareWritingThetasToFile(String thetaFileName) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(thetaFileName, "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return writer;
	}

	private static double findError(Map<Chao, Integer> chaoRecords, List<Double> thetas, Chao sample, List<Map<String, Integer>> minMaxes, boolean power) {
		double error = chaoRecords.get(sample); // y
		for (int i = 0; i < thetas.size(); i++) {
			double x = sample.getStats().get(i);
			double xNorm = normalizeX(minMaxes, power, i, x); 
			error -= thetas.get(i) * xNorm;
		}
		return error;
	}

	public static double findScore(Chao chao, List<Double> thetas, List<Map<String, Integer>> minMaxes, boolean power) {
		double score = 0.0;
		for (int i = 0; i < thetas.size(); i++) {
			double x = chao.getStats().get(i);
			double xNorm = normalizeX(minMaxes, power, i, x);
			score += xNorm*thetas.get(i);
		}
		return score;
	}

	private static double normalizeX(List<Map<String, Integer>> minMaxes, boolean power, int i, double x) {
		double min = minMaxes.get(i).get("min");
		double max = minMaxes.get(i).get("max");
		if (power) {
			x = Math.pow(Math.abs(x), Constants.getListOfPowers().get(i))*sign(x);
			min = Math.pow(Math.abs(min), Constants.getListOfPowers().get(i))*sign(min);
			max = Math.pow(Math.abs(max), Constants.getListOfPowers().get(i))* sign(max);
		}
		double xNorm = (x - min)/(max - min+Double.MIN_VALUE);
		return xNorm;
	}

	private static double sign(double num) {
		if (num == 0) {
			return 1;
		}
		return num/Math.abs(num);
	}
	
}
