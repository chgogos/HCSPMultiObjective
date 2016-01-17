package solvers.evolutionary;

import java.io.IOException;

import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;

import core.HCSProblem;
import core.Importer;

/**
 * 
 * objective0: makespan
 * objective1: flowtime
 * 
 */
public class EvolutionaryHcspBiObjective implements Problem {

	private HCSProblem hcsproblem;

	public EvolutionaryHcspBiObjective(String fn) {
		super();
		Importer importer = new Importer(fn);
		try {
			HCSProblem hcsproblem = importer.read_dataset();
			this.hcsproblem = hcsproblem;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() {
	}

	@Override
	public void evaluate(Solution solution) {
		int[] d = EncodingUtils.getInt(solution);
		core.Solution hcsp_sol = new core.Solution(hcsproblem);
		for (int t = 0; t < hcsproblem.T; t++) {
			hcsp_sol.schedule(t, d[t]);
		}
		solution.setObjective(0, hcsp_sol.makespan());
		solution.setObjective(1, hcsp_sol.flowTime());
	}

	@Override
	public String getName() {
		return "HCSP_makespan";
	}

	@Override
	public int getNumberOfConstraints() {
		return 0;
	}

	@Override
	public int getNumberOfObjectives() {
		return 2;
	}

	@Override
	public int getNumberOfVariables() {
		return hcsproblem.T;
	}

	@Override
	public Solution newSolution() {
		Solution solution = new Solution(hcsproblem.T, 2, 0);
		for (int t = 0; t < hcsproblem.T; t++) {
			solution.setVariable(t, EncodingUtils.newInt(0, hcsproblem.P - 1));
		}
		return solution;
	}

}
