package solvers.heuristics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import core.HCSProblem;
import core.IntDoublePair;
import core.Solution;
import solvers.Solver;

public class LSufferage extends Solver {

	public LSufferage(HCSProblem problem) {
		super(problem);
	}

	public Solution solve() {
		int[][] sortedTasksPerProcessor = new int[problem.P][problem.T];
		double[] min1_etc = new double[problem.T];
		double[] min2_etc = new double[problem.T];
		int[] pmin1_etc = new int[problem.T];
		for (int t = 0; t < problem.T; t++) {
			double min1 = Double.MAX_VALUE;
			double min2 = Double.MAX_VALUE;
			int pmin1 = -1;
			for (int p = 0; p < problem.P; p++) {
				if (problem.getETC(t, p) <= min1) {
					min2 = min1;
					min1 = problem.getETC(t, p);
					pmin1 = p;
				} else if (problem.getETC(t, p) < min2) {
					min2 = problem.getETC(t, p);
				}
			}
			min1_etc[t] = min1;
			min2_etc[t] = min2;
			pmin1_etc[t] = pmin1;
		}

		for (int p = 0; p < problem.P; p++) {
			List<IntDoublePair> aList = new ArrayList<>();
			for (int t = 0; t < problem.T; t++) {
				if (p == pmin1_etc[t]) {
					aList.add(new IntDoublePair(t, min2_etc[t] / problem.getETC(t, p)));
				} else {
					aList.add(new IntDoublePair(t, min1_etc[t] / problem.getETC(t, p)));
				}
			}
			Collections.sort(aList);
			for (int t = 0; t < problem.T; t++) {
				sortedTasksPerProcessor[p][t] = aList.get(t).intValue;
			}
		}
		Solution sol = scheduleFastBasedOnStaticOrder(sortedTasksPerProcessor);
		return sol;
	}

	@Override
	public String getName() {
		return "LSufferage";
	}

}
