package solvers.heuristics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import core.HCSProblem;
import core.IntDoubleAscendingComparator;
import core.IntDoublePair;
import core.Solution;
import solvers.Solver;

public class MinMin extends Solver {

	public MinMin(HCSProblem problem) {
		super(problem);
	}

	public Solution solve() {
		int[][] sortedTasksPerProcessor = new int[problem.P][problem.T];
		for (int p = 0; p < problem.P; p++) {
			List<IntDoublePair> aList = new ArrayList<>();
			for (int t = 0; t < problem.T; t++) {
				aList.add(new IntDoublePair(t, problem.getETC(t, p)));
			}
			Collections.sort(aList, new IntDoubleAscendingComparator());
			for (int t = 0; t < problem.T; t++) {
				sortedTasksPerProcessor[p][t] = aList.get(t).intValue;
			}
		}
		return scheduleFastBasedOnStaticOrder(sortedTasksPerProcessor);
	}
	
	@Override
	public String getName() {
		return "MinMin";
	}


}
