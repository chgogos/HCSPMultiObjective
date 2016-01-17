package solvers.heuristics;

import java.util.ArrayList;
import java.util.List;

import core.HCSProblem;
import core.Solution;
import solvers.Solver;

public class Sufferage extends Solver {

	public Sufferage(HCSProblem problem) {
		super(problem);
	}

	/**
	 * no rounds. The tasks that suffers the most gets scheduled
	 * 
	 * @return
	 */
	public Solution solve() {
		List<Integer> tasksList = new ArrayList<Integer>();
		for (int i = 0; i < problem.T; i++) {
			tasksList.add(i);
		}
		Solution sol = new Solution(problem);
		while (!tasksList.isEmpty()) {
			double suffer_max = -Double.MAX_VALUE;
			int tmax = -1;
			for (Integer t : tasksList) {
				double min1 = Double.MAX_VALUE;
				double min2 = Double.MAX_VALUE;
				for (int p = 0; p < problem.P; p++) {
					double eft = sol.getEFT(p) + problem.getETC(t, p);
					if (eft <= min1) {
						min2 = min1;
						min1 = eft;
					} else if (eft < min2) {
						min2 = eft;
					}
				}
				if ((min2 - min1) > suffer_max) {
					suffer_max = min2 - min1;
					tmax = t;
				}
			}
			double min = Double.MAX_VALUE;
			int pmin = -1;
			for (int p = 0; p < problem.P; p++) {
				double eft = sol.getEFT(p) + problem.getETC(tmax, p);
				if (eft < min) {
					min = eft;
					pmin = p;
				}
			}
			sol.schedule(tmax, pmin);
			tasksList.remove(new Integer(tmax));
		}
		return sol;
	}
	
	@Override
	public String getName() {
		return "Sufferage";
	}

}
