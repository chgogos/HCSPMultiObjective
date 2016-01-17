package solvers.heuristics;

import java.util.ArrayList;
import java.util.List;

import core.HCSProblem;
import core.Solution;
import solvers.Solver;

/**
 * Relative Cost
 * 
 * Wu, Min-You, and Wei Shu. "A high-performance mapping algorithm for
 * heterogeneous computing systems." Parallel and Distributed Processing
 * Symposium., Proceedings 15th International. IEEE, 2001.
 */
public class RelativeCostNaive extends Solver {

	public RelativeCostNaive(HCSProblem problem) {
		super(problem);
	}

	public Solution solve() {
		double[][] static_relative_cost = new double[problem.T][problem.P];
		for (int i = 0; i < problem.T; i++) {
			double sum = 0.0;
			for (int j = 0; j < problem.P; j++) {
				sum += problem.getETC(i, j);
			}
			for (int j = 0; j < problem.P; j++) {
				static_relative_cost[i][j] = problem.P * problem.getETC(i, j) / sum;
			}
		}

		List<Integer> tasksList = new ArrayList<Integer>();
		for (int i = 0; i < problem.T; i++) {
			tasksList.add(i);
		}
		Solution sol = new Solution(problem);
		while (!tasksList.isEmpty()) {
			double[][] dynamic_relative_cost = new double[problem.T][problem.P];
			for (Integer i : tasksList) {
				double sum = 0.0;
				for (int j = 0; j < problem.P; j++) {
					sum += sol.getEFT(j) + problem.getETC(i, j);
				}
				for (int j = 0; j < problem.P; j++) {
					dynamic_relative_cost[i][j] = problem.P * (sol.getEFT(j) + problem.getETC(i, j)) / sum;
				}
			}

			List<Integer> selected_machines = new ArrayList<Integer>();
			for (Integer i : tasksList) {
				double min1 = Double.MAX_VALUE;
				int pmin1 = -1;
				for (int j = 0; j < problem.P; j++) {
					if (dynamic_relative_cost[i][j] < min1) {
						min1 = dynamic_relative_cost[i][j];
						pmin1 = j;
					}
				}
				selected_machines.add(pmin1);
			}

			double min = Double.MAX_VALUE;
			int pmin = -1;
			int tmin = -1;
			double alpha = 0.5;
			for (Integer i : tasksList) {
				int j = selected_machines.get(tasksList.indexOf(i));
				double v = Math.pow(static_relative_cost[i][j], alpha) * dynamic_relative_cost[i][j];
				if (v < min) {
					min = v;
					tmin = i;
					pmin = j;
				}
			}
			sol.schedule(tmin, pmin);
			tasksList.remove(new Integer(tmin));
		}
		return sol;
	}
	
	@Override
	public String getName() {
		return "Relative Cost Naive";
	}

}
