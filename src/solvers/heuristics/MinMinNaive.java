package solvers.heuristics;

import java.util.ArrayList;
import java.util.List;

import core.HCSProblem;
import core.Solution;
import solvers.Solver;

public class MinMinNaive extends Solver {

	public MinMinNaive(HCSProblem problem) {
		super(problem);
	}

	/*
	 * Naive implementation of MinMin
	 * 
	 * Time complexity O(PT^2)
	 */
	public Solution solve() {
		List<Integer> tasksList = new ArrayList<Integer>();
		for (int i = 0; i < problem.T; i++) {
			tasksList.add(i);
		}
		Solution sol = new Solution(problem);
		while (!tasksList.isEmpty()) {
			double min = Double.MAX_VALUE;
			int pmin = -1;
			int tmin = -1;
			for (Integer t : tasksList) {
				for (int p = 0; p < problem.P; p++) {
					double eft = sol.getEFT(p) + problem.getETC(t, p);
					if (eft < min) {
						min = eft;
						tmin = t;
						pmin = p;
					}
				}
			}
			sol.schedule(tmin, pmin);
			tasksList.remove(new Integer(tmin));
		}
		return sol;
	}

	@Override
	public String getName() {
		return "MinMinNaive";
	}

}