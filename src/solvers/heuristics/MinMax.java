package solvers.heuristics;

import java.util.ArrayList;
import java.util.List;

import core.HCSProblem;
import core.Solution;
import solvers.Solver;

public class MinMax extends Solver {

	public MinMax(HCSProblem problem) {
		super(problem);
	}

	public Solution solve() {
		List<Integer> tasksList = new ArrayList<Integer>();
		for (int i = 0; i < problem.T; i++) {
			tasksList.add(i);
		}
		Solution sol = new Solution(problem);
		while (!tasksList.isEmpty()) {
			double max = 0;
			int selectedTask = -1;
			int selectedProcessor = -1;
			for (Integer t : tasksList) {
				double min = Double.MAX_VALUE;
				int pmin = -1;
				for (int p = 0; p < problem.P; p++) {
					double eft = sol.getEFT(p) + problem.getETC(t, p);
					if (eft < min) {
						min = eft;
						pmin = p;
					}
				}
				double ratio = problem.getETC(t, problem.getFastestProcessorForTask(t)) / problem.getETC(t, pmin);
				if (ratio > max) {
					max = ratio;
					selectedProcessor = pmin;
					selectedTask = t;
				}
			}
			sol.schedule(selectedTask, selectedProcessor);
			tasksList.remove(new Integer(selectedTask));
		}
		return sol;
	}

	@Override
	public String getName() {
		return "MinMax";
	}

}
