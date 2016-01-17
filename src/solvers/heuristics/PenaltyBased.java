package solvers.heuristics;

import core.HCSProblem;
import core.Solution;
import solvers.Solver;

public class PenaltyBased extends Solver {

	public PenaltyBased(HCSProblem problem) {
		super(problem);
	}

	/**
	 * Penalty based
	 * 
	 * Chaturvedi, Anand K., and Rajendra Sahu. "New heuristic for scheduling of
	 * independent tasks in computational grid." International Journal of Grid
	 * and Distributed Computing 4.3 (2011): 25-36.
	 * 
	 * simplified (1/5/2015)
	 */
	public Solution solve() {
		Solution sol = new Solution(problem);
		for (int t = 0; t < problem.T; t++) {
			sol.schedule(t, problem.getSI(t, 0));
			// sol.schedule(t, problem.getFastestProcessorForTask(t));
		}
		int repetitions = 0;
		while (repetitions < problem.T) {
			double min_penalty = Double.MAX_VALUE;
			int selected_task = -1;
			int max_loaded_processor_A = 0;
			int min_loaded_processor_B = 0;
			for (int p = 1; p < problem.P; p++) {
				if (sol.getEFT(p) > sol.getEFT(max_loaded_processor_A))
					max_loaded_processor_A = p;
				if (sol.getEFT(p) < sol.getEFT(min_loaded_processor_B))
					min_loaded_processor_B = p;
			}
			double ca = sol.getEFT(max_loaded_processor_A);
			double cb = sol.getEFT(min_loaded_processor_B);
			for (int t : sol.getTasksPerProcessor(max_loaded_processor_A))
				if (cb + problem.getETC(t, min_loaded_processor_B) <= ((ca + cb) / 2.0)) {
					double penalty = (problem.getETC(t, min_loaded_processor_B)
							- problem.getETC(t, max_loaded_processor_A)) / problem.getETC(t, max_loaded_processor_A);
					if (penalty < min_penalty) {
						min_penalty = penalty;
						selected_task = t;
					}
				}
			if (selected_task != -1)
				sol.transfer(selected_task, max_loaded_processor_A, min_loaded_processor_B);
			repetitions++;
		}
		return sol;
	}

	@Override
	public String getName() {
		return "PB";
	}

}
