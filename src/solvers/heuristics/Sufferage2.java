package solvers.heuristics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import core.HCSProblem;
import core.Solution;
import solvers.Solver;

/**
 * a task that is removed is put back to the list of unassigned tasks of the
 * current round
 * 
 */
public class Sufferage2 extends Solver {

	public Sufferage2(HCSProblem problem) {
		super(problem);
	}

	public Solution solve() {
		List<Integer> tasksList = new ArrayList<Integer>();
		for (int i = 0; i < problem.T; i++) {
			tasksList.add(i);
		}
		Solution sol = new Solution(problem);
		while (!tasksList.isEmpty()) {
			boolean[] assigned_machines = new boolean[problem.P];
			double[] sufferage_values = new double[problem.P];
			int[] current_round_schedule = new int[problem.P];
			List<Integer> tList = new ArrayList<Integer>(tasksList);
			Collections.copy(tList, tasksList);
			while (!tList.isEmpty()) {
				int tk = tList.get(0);
				double min1 = Double.MAX_VALUE;
				double min2 = Double.MAX_VALUE;
				int pmin1 = -1;
				for (int p = 0; p < problem.P; p++) {
					double eft = sol.getEFT(p) + problem.getETC(tk, p);
					if (eft <= min1) {
						min2 = min1;
						min1 = eft;
						pmin1 = p;
					} else if (eft < min2) {
						min2 = eft;
					}
				}
				double sufferage = min2 - min1;
				if (!assigned_machines[pmin1]) {
					sol.schedule(tk, pmin1);
					assigned_machines[pmin1] = true;
					sufferage_values[pmin1] = sufferage;
					current_round_schedule[pmin1] = tk;
				} else if (sufferage_values[pmin1] < sufferage) {
					int ti = current_round_schedule[pmin1];
					sol.unschedule(ti, pmin1);
					sol.schedule(tk, pmin1);
					sufferage_values[pmin1] = sufferage;
					current_round_schedule[pmin1] = tk;
					tList.add(new Integer(ti));
				}
				tList.remove(new Integer(tk));
			}

			// if is needed in the loop because current_round_schedule is
			// initialized with zeros and zero is the id of the first task
			for (int p = 0; p < problem.P; p++) {
				if (assigned_machines[p]) {
					tasksList.remove(new Integer(current_round_schedule[p]));
				}
			}
		}
		return sol;
	}
	
	@Override
	public String getName() {
		return "Sufferage version 2";
	}


}
