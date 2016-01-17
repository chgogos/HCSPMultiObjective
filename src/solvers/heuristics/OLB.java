package solvers.heuristics;

import java.util.ArrayList;
import java.util.List;

import core.HCSProblem;
import core.Solution;
import solvers.Solver;

/**
 * Opportunistic Load Balancing
 */
public class OLB extends Solver {

	public OLB(HCSProblem problem) {
		super(problem);
	}

	public Solution solve() {
		Solution sol = new Solution(problem);
		List<Integer> tasksList = new ArrayList<Integer>();
		for (int i = 0; i < problem.T; i++) {
			tasksList.add(i);
		}
		for (Integer t : tasksList) {
			int pmin = 0;
			for (int p = 1; p < problem.P; p++) {
				if (sol.getEFT(p) < sol.getEFT(pmin)) {
					pmin = p;
				}
			}
			sol.schedule(t, pmin);
		}
		return sol;
	}
	
	@Override
	public String getName() {
		return "OLB";
	}

}
