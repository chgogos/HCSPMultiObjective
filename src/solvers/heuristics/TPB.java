package solvers.heuristics;

import java.util.Arrays;

import core.HCSProblem;
import core.IntDoubleAscendingComparator;
import core.IntDoublePair;
import core.Solution;
import solvers.Solver;

/**
 * Tenacious Penalty Based
 * 
 * CV (1/5/2015)
 * 
 */
public class TPB extends Solver {
	public TPB(HCSProblem problem) {
		super(problem);
	}

	public Solution solve() {
		Solution sol = new Solution(problem);
		for (int t = 0; t < problem.T; t++) {
			sol.schedule(t, problem.getSI(t, 0));
			// sol.schedule(t, problem.getFastestProcessorForTask(t));
		}
		return solve(sol);
	}

	public Solution solve(Solution sol) {
		IntDoublePair[] proc_ft = new IntDoublePair[problem.P];

		int repetitions = 0;
		outer_loop: while (repetitions < problem.T) {
			repetitions++;
			double min_penalty = Double.MAX_VALUE;
			int selected_task = -1;
			int processor_b = -1;

			for (int p = 0; p < problem.P; p++)
				proc_ft[p] = new IntDoublePair(p, sol.getProcessorFinishTime(p));
			Arrays.sort(proc_ft, new IntDoubleAscendingComparator());

			int max_loaded_processor_a = proc_ft[problem.P - 1].intValue;
			double ca = sol.getEFT(max_loaded_processor_a);
			for (int i = 0; i < problem.P - 1; i++) {
				int b = proc_ft[i].intValue;
				double cb = sol.getProcessorFinishTime(b);
				for (int t : sol.getTasksPerProcessor(max_loaded_processor_a)) {
					if (cb + problem.getETC(t, b) < ca) {
						double penalty = (problem.getETC(t, b) - problem.getETC(t, max_loaded_processor_a))
								/ problem.getETC(t, max_loaded_processor_a);
						if (penalty < min_penalty) {
							min_penalty = penalty;
							selected_task = t;
							processor_b = b;
						}
					}
				}
				if (selected_task != -1) {
					sol.transfer(selected_task, max_loaded_processor_a, processor_b);
					continue outer_loop;
				}
			}
		}
		return sol;
	}
	
	@Override
	public String getName() {
		return "TPB";
	}


}
