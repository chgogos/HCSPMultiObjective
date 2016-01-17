package solvers.heuristics;

import java.util.Arrays;

import core.HCSProblem;
import core.IntDoubleAscendingComparator;
import core.IntDoublePair;
import core.Solution;
import solvers.Solver;


/**
 * Two stage tpb
 * 
 * The second stage is a swap
 * 
 * @param sol
 * @return
 */
public class TPB2 extends Solver {

	public TPB2(HCSProblem problem) {
		super(problem);
	}

	// ########################################################################
	public Solution solve() {
		Solution sol = new Solution(problem);
		for (int t = 0; t < problem.T; t++) {
			sol.schedule(t, problem.getSI(t, 0));
		}
		return solve(sol);
	}

	public Solution solve(Solution sol) {
		IntDoublePair[] proc_ft = new IntDoublePair[problem.P];

		int repetitions = 0;
		outer_loop: while (repetitions < problem.T) {
			repetitions++;
			double min_penalty = Double.MAX_VALUE;

			int selected_task1 = -1;
			int selected_task2 = -1;
			int processor_b = -1;

			for (int p = 0; p < problem.P; p++)
				proc_ft[p] = new IntDoublePair(p, sol.getProcessorFinishTime(p));
			Arrays.sort(proc_ft, new IntDoubleAscendingComparator());

			// move task
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
							selected_task1 = t;
							processor_b = b;
						}
					}
				}
				if (selected_task1 != -1) {
					sol.transfer(selected_task1, max_loaded_processor_a, processor_b);
					continue outer_loop;
				}
			}

			// swap pair of tasks
			for (int t1 : sol.getTasksPerProcessor(max_loaded_processor_a)) {
				for (int i = 0; i < problem.P - 1; i++) {
					int b = proc_ft[i].intValue;
					double cb = sol.getProcessorFinishTime(b);
					for (int t2 : sol.getTasksPerProcessor(b)) {
						double ca_new = ca - problem.getETC(t1, max_loaded_processor_a)
								+ problem.getETC(t2, max_loaded_processor_a);
						double cb_new = cb - problem.getETC(t2, b) + problem.getETC(t1, b);
						if (ca_new < ca && cb_new < ca) {
							double penalty = (problem.getETC(t1, b) - problem.getETC(t1, max_loaded_processor_a)
									+ problem.getETC(t2, max_loaded_processor_a) - problem.getETC(t2, b))
									/ (problem.getETC(t1, max_loaded_processor_a) + problem.getETC(t2, b));
							if (penalty < min_penalty) {
								min_penalty = penalty;
								selected_task1 = t1;
								selected_task2 = t2;
								processor_b = b;
							}
						}
					}
				}
				if (selected_task1 != -1) {
					sol.transfer(selected_task1, max_loaded_processor_a, processor_b);
					sol.transfer(selected_task2, processor_b, max_loaded_processor_a);
					continue outer_loop;
				}
			}
		}
		return sol;
	}

	@Override
	public String getName() {
		return "Tenacious Penalty Based two stages";
	}

}
