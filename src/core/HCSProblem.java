package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HCSProblem {
	public int T;
	public int P;
	// etc T x P stands for estimated time to compute
	double etc[][];

	// si T x P for each task indices of processors in ascending order of time
	// execution for task t
	int[][] si;

	int[] zeroTimeTasks;

	public HCSProblem(int T, int P) {
		this.T = T;
		this.P = P;
		etc = new double[T][P];
		zeroTimeTasks = new int[T];
		for (int t = 0; t < T; t++)
			zeroTimeTasks[t] = -1;
		si = new int[T][P];
	}

	public int getSI(int t, int p) {
		return si[t][p];
	}

	public void computeHelpValues() {
		computeSI();
	}

	private void computeSI() {
		for (int i = 0; i < T; i++)
			si[i] = sortProcessorsByETCForTask(i);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("Tasks= %d  Processor=%d\n", T, P));
		return sb.toString();
	}

	public void display() {
		StringBuilder sb = new StringBuilder(this.toString());
		for (int t = 0; t < T; t++) {
			for (int p = 0; p < P; p++) {
				sb.append(String.format("[%d->%.4f] ", p, etc[t][p]));
			}
			sb.append("\n");
		}
		System.out.println(sb.toString());
	}

	public double getETC(int task_index, int processor_index) {
		return etc[task_index][processor_index];
	}

	public void setZeroTimeTask(int t, int p) {
		zeroTimeTasks[t] = p;
	}

	public int zeroTimeTasks() {
		int c = 0;
		for (int t = 0; t < T; t++)
			if (zeroTimeTasks[t] != -1)
				c++;
		return c;
	}

	public int getFastestProcessorForTask(int t) {
		return si[t][0];
	}

	public int[] sortTasksByETCForProcessor(int p) {
		List<IntDoublePair> aList = new ArrayList<>();
		for (int t = 0; t < T; t++)
			aList.add(new IntDoublePair(t, etc[t][p]));
		Collections.sort(aList, new IntDoubleAscendingComparator());
		int[] x = new int[T];
		// System.out.println("Processor " + p);
		for (int i = 0; i < T; i++) {
			x[i] = aList.get(i).intValue;
			// System.out.printf("%d %.2f\n", x[i], aList.get(i).doubleValue);
		}
		return x;
	}

	public int[] sortProcessorsByETCForTask(int t) {
		List<IntDoublePair> aList = new ArrayList<>();
		for (int p = 0; p < P; p++)
			aList.add(new IntDoublePair(p, etc[t][p]));
		Collections.sort(aList, new IntDoubleAscendingComparator());
		int[] x = new int[P];
		// System.out.println("Task " + t);
		for (int i = 0; i < P; i++) {
			x[i] = aList.get(i).intValue;
			// System.out.printf("%d %.2f\n", x[i], aList.get(i).doubleValue);
		}
		return x;
	}
}
