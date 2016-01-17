package solvers;

import core.HCSProblem;
import core.Solution;

public abstract class Solver implements ISolver{
	protected HCSProblem problem;

	public Solver(HCSProblem problem) {
		this.problem = problem;
	}
	
	protected Solution scheduleFastBasedOnStaticOrder(int[][] sortedTasksPerProcessor) {
		int[] sortedTasksIndices = new int[problem.P];
		boolean[] scheduledTasks = new boolean[problem.T];
		Solution sol = new Solution(problem);
		for (int i = 0; i < problem.T; i++) {
			for (int p = 0; p < problem.P; p++) {
				int k = sortedTasksIndices[p];
				while (scheduledTasks[sortedTasksPerProcessor[p][k]]) {
					k++;
				}
				sortedTasksIndices[p] = k;
			}
			double min = Double.MAX_VALUE;
			int pmin = -1;
			int tmin = -1;
			for (int p = 0; p < problem.P; p++) {
				int t2 = sortedTasksPerProcessor[p][sortedTasksIndices[p]];
				double eft = sol.getEFT(p) + problem.getETC(t2, p);
				if (eft < min) {
					min = eft;
					tmin = t2;
					pmin = p;
				}
			}
			scheduledTasks[tmin] = true;
			sol.schedule(tmin, pmin);
		}
		return sol;
	}

}
