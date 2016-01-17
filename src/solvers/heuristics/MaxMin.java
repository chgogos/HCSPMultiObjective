package solvers.heuristics;

import java.util.ArrayList;
import java.util.List;

import core.HCSProblem;
import core.Solution;
import solvers.Solver;

public class MaxMin extends Solver {

	public MaxMin(HCSProblem problem) {
		super(problem);
	}

	public Solution solve() {
		List<Integer> tasksList = new ArrayList<Integer>();
		for (int i = 0; i < problem.T; i++) {
			tasksList.add(i);
		}
		Solution sol = new Solution(problem);
		while (!tasksList.isEmpty()) {
			double max = 0.0;
			int tmax = -1;
			int pmax = -1;
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
				if (min > max) {
					max = min;
					tmax = t;
					pmax = pmin;
				}
			}
			sol.schedule(tmax, pmax);
			tasksList.remove(new Integer(tmax));
		}
		return sol;
	}

	@Override
	public String getName() {
		return "MaxMin";
	}

}
