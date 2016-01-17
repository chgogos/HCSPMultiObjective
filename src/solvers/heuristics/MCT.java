package solvers.heuristics;

import java.util.ArrayList;
import java.util.List;

import core.HCSProblem;
import core.Solution;
import solvers.Solver;

public class MCT extends Solver {

	public MCT(HCSProblem problem) {
		super(problem);
	}

	public Solution solve() {
		List<Integer> tasksList = new ArrayList<Integer>();
		for (int i = 0; i < problem.T; i++) {
			tasksList.add(i);
		}
		// Collections.shuffle(tasksList, new Random(1234567890L));
		Solution sol = new Solution(problem);

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
			sol.schedule(t, pmin);
		}
		return sol;
	}
	
	@Override
	public String getName() {
		return "MCT";
	}


}
