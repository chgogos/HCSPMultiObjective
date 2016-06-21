package core;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.google.common.primitives.Doubles;

public class Solution {
	HCSProblem problem;
	// schedule is an array with length T
	int[] schedule;
	// eft = earliest finish time
	double[] eft;

	List<Integer>[] tasksPerProcessor;

	public Solution(HCSProblem problem) {
		this.problem = problem;
		this.schedule = new int[problem.T];
		this.eft = new double[problem.P];
		for (int t = 0; t < problem.T; t++) {
			this.schedule[t] = -1;
		}
		for (int p = 0; p < problem.P; p++) {
			this.eft[p] = 0.0f;
		}
		this.tasksPerProcessor = new ArrayList[problem.P];
		for (int p = 0; p < problem.P; p++) {
			tasksPerProcessor[p] = new ArrayList<Integer>();
		}
	}

	public double makespan() {
		return Doubles.max(eft);
	}

	/**
	 * flowtime is the sum of the finishing times of jobs.
	 * 
	 * @return
	 */
	public double flowTime() {
		double sum = 0.0;
		for (int p = 0; p < problem.P; p++) {
			double finishTime = 0.0;
			for (Integer t : getTasksPerProcessor(p)) {
				double x = finishTime + problem.etc[t][p];
				finishTime += x;
			}
			sum += finishTime;
		}
		return sum;
	}

	/**
	 * resourceUtilisation is the degree of utilization with respect to the
	 * schedule
	 * 
	 * @return
	 */
	public double resourceUtilization() {
		double sum = 0.0;
		for (int p = 0; p < problem.P; p++) {
			sum += eft[p];
		}
		return sum / (makespan() * problem.P);
	}

	/**
	 * mathcingproximity is the degree of proximity of a given schedule to the
	 * schedule produced by the MET minimum execution time
	 * 
	 * @return
	 */
	public double matchingProximity() {
		double sum1 = 0.0;
		double sum2 = 0.0;
		for (int t = 0; t < problem.T; t++) {
			int p = schedule[t];
			sum1 += problem.etc[t][p];
			int greedy_p = problem.getFastestProcessorForTask(t);
			sum2 += problem.etc[t][greedy_p];
		}
		return sum1 / sum2;
	}

	public void schedule(int t, int p) {
		schedule[t] = p;
		eft[p] += problem.etc[t][p];
		tasksPerProcessor[p].add(t);
	}

	public void unschedule(int t, int p) {
		schedule[t] = -1;
		eft[p] -= problem.etc[t][p];
		tasksPerProcessor[p].remove(new Integer(t));
	}

	public void transfer(int t, int from_processor, int to_processor) {
		unschedule(t, from_processor);
		schedule(t, to_processor);
	}

	public int getProcessorForTask(int t) {
		return schedule[t];
	}

	public void printSolution() {
		System.out.println(this.toString());
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		// sb.append(String.format("Makespan: %.1f Flowtime: %.1f ", makespan(),
		// computeflowtime()));
		sb.append(String.format("Makespan: %.1f ", makespan()));
		int[] frequency = new int[problem.P];
		for (int t = 0; t < problem.T; t++) {
			frequency[schedule[t]]++;
			sb.append(String.format("\nTask: %d Processor: %d", t, schedule[t]));
		}
		sb.append(String.format("\n Frequency:%s", frequency));
		return sb.toString();
	}

	public void exportToFile(String fn) {
		try {
			PrintWriter out = new PrintWriter(fn);
			for (int t = 0; t < problem.T; t++)
				out.print(schedule[t] + " ");
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public void loadFromFile(String solfn) {
		try {
			Scanner in = new Scanner(new FileReader(solfn));
			int t = 0;
			while (in.hasNextInt()) {
				int p = in.nextInt();
				schedule(t, p);
				t++;
			}
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public int getProcessorWithLatestFinishTime() {
		double max = eft[0];
		int pmax = 0;
		for (int p = 1; p < problem.P; p++) {
			if (eft[p] > max) {
				max = eft[p];
				pmax = p;
			}
		}
		return pmax;
	}

	public double getProcessorFinishTime(int p) {
		return eft[p];
	}

	public List<Integer> getTasksPerProcessor(int p) {
		return tasksPerProcessor[p];
	}

	public Solution copy() {
		Solution sol = new Solution(problem);
		for (int t = 0; t < problem.T; t++)
			sol.schedule(t, this.schedule[t]);
		return sol;
	}

	public String getNumberOfUnscheduledTasksDebug() {
		List<Integer> aList = new ArrayList<>();
		for (int i = 0; i < problem.T; i++) {
			if (schedule[i] == -1)
				aList.add(i);

		}
		return aList.toString();
	}

	public double getEFT(int p) {
		return eft[p];
	}

}
