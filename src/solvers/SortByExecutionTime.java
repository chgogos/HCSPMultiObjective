package solvers;

import java.util.Arrays;
import java.util.List;

import core.HCSProblem;
import core.IntDoubleAscendingComparator;
import core.IntDoublePair;
import core.Solution;

public class SortByExecutionTime extends Solver {
	Solution initial_solution;

	public SortByExecutionTime(HCSProblem problem, Solution sol) {
		super(problem);
		this.initial_solution = sol;
	}

	public Solution solve() {
		Solution newSol = new Solution(problem);
		for (int p = 0; p < problem.P; p++) {
			List<Integer> tasks = initial_solution.getTasksPerProcessor(p);
			IntDoublePair a[] = new IntDoublePair[tasks.size()];
			int i = 0;
			for (int t : tasks) {
				a[i] = new IntDoublePair(t, problem.getETC(t, p));
				i++;
			}
			Arrays.sort(a, new IntDoubleAscendingComparator());
			for (i = 0; i < a.length; i++)
				newSol.schedule(a[i].intValue,p);
		}
		return newSol;
	}

	@Override
	public String getName() {
		return "SortByExecutionTime";
	}

}
