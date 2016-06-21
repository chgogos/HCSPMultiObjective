package solvers.heuristics;

import java.util.Random;

import core.HCSProblem;
import core.Solution;
import solvers.Solver;

public class RandomSolver extends Solver {

	public RandomSolver(HCSProblem problem) {
		super(problem);
	}

	@Override
	public Solution solve() {
		Random random = new Random();
		Solution sol = new Solution(problem);
		for (int t = 0; t < problem.T; t++) {
			int p = random.nextInt(problem.P);
			sol.schedule(t, p);
		}
		return sol;
	}

	@Override
	public String getName() {
		return "otinanai";
	}

}
