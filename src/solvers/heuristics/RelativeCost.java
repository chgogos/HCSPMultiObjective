package solvers.heuristics;

import java.util.ArrayList;
import java.util.List;

import core.HCSProblem;
import core.Solution;
import solvers.Solver;

/**
 * Relative Cost
 * 
 * Faster implementation of rc by CV (15-5-2015)
 * 
 * @return
 */
public class RelativeCost extends Solver {

	public RelativeCost(HCSProblem problem) {
		super(problem);
	}

	public Solution solve() {
		double[] task_sum = new double[problem.T];
		double[][] static_relative_cost = new double[problem.T][problem.P];
		double[][] dynamic_relative_cost = new double[problem.T][problem.P];
		double alpha = 0.5;
		for (int i = 0; i < problem.T; i++) {
			task_sum[i] = 0.0;
			for (int j = 0; j < problem.P; j++) {
				task_sum[i] += problem.getETC(i, j);
			}
			for (int j = 0; j < problem.P; j++) {
				static_relative_cost[i][j] = problem.P * problem.getETC(i, j) / task_sum[i];
				dynamic_relative_cost[i][j] = static_relative_cost[i][j];
				static_relative_cost[i][j] = Math.pow(static_relative_cost[i][j], alpha);
			}
		}

		List<Integer> tasksList = new ArrayList<Integer>();
		for (int i = 0; i < problem.T; i++) {
			tasksList.add(i);
		}

		Solution sol = new Solution(problem);

		while (!tasksList.isEmpty()) {
			List<Integer> selected_machines = new ArrayList<Integer>();
			for (Integer i : tasksList) {
				int pmin1 = 0;
				for (int j = 1; j < problem.P; j++)
					if (dynamic_relative_cost[i][j] <= dynamic_relative_cost[i][pmin1])
						pmin1 = j;
				selected_machines.add(pmin1);
			}

			double min = Double.MAX_VALUE;
			int pmin = -1;
			int tmin = -1;
			int t = 0, nmin = -1;
			for (Integer i : tasksList) {
				int j = selected_machines.get(t);
				double v = static_relative_cost[i][j] * dynamic_relative_cost[i][j];
				if (v < min) {
					min = v;
					tmin = i;
					pmin = j;
					nmin = t;
				}
				t++;
			}
			sol.schedule(tmin, pmin);
			tasksList.remove(nmin);

			// update arrays base on pmin
			for (Integer i : tasksList) {
				task_sum[i] += problem.getETC(tmin, pmin);
				for (int j = 0; j < problem.P; j++) {
					dynamic_relative_cost[i][j] = problem.P * (sol.getProcessorFinishTime(j) + problem.getETC(i, j))
							/ task_sum[i];
				}
			}
		}
		return sol;
	}

	@Override
	public String getName() {
		return "RC";
	}

}
