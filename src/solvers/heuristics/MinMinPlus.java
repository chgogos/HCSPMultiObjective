package solvers.heuristics;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import core.HCSProblem;
import core.IntDoubleAscendingComparator;
import core.IntDoublePair;
import core.Solution;
import solvers.Solver;

public class MinMinPlus extends Solver {
	PriorityQueue<IntDoublePair>[] priorityQueuePerProcessor;

	public MinMinPlus(HCSProblem problem) {
		super(problem);
		priorityQueuePerProcessor = new PriorityQueue[problem.P];
		for (int p = 0; p < problem.P; p++) {
			priorityQueuePerProcessor[p] = new PriorityQueue<>(problem.T, new IntDoubleAscendingComparator());
			for (int t = 0; t < problem.T; t++) {
				priorityQueuePerProcessor[p].add(new IntDoublePair(t, problem.getETC(t, p)));
			}
		}
	}

	public Solution solve() {
		Solution sol = new Solution(problem);
		Set<Integer> deletedTasks = new HashSet<Integer>();
		for (int i = 0; i < problem.T; i++) {
			int tmin = -1;
			int pmin = -1;
			double min = Double.MAX_VALUE;
			for (int p = 0; p < problem.P; p++) {
				while (!priorityQueuePerProcessor[p].isEmpty()
						&& deletedTasks.contains(priorityQueuePerProcessor[p].peek().intValue))
					priorityQueuePerProcessor[p].remove();

				if (!priorityQueuePerProcessor[p].isEmpty()) {
					IntDoublePair pair = priorityQueuePerProcessor[p].peek();
					if (pair.doubleValue + sol.getEFT(p) < min) {
						min = pair.doubleValue + sol.getEFT(p);
						tmin = pair.intValue;
						pmin = p;
					}
				}
			}
			priorityQueuePerProcessor[pmin].remove();
			sol.schedule(tmin, pmin);
			deletedTasks.add(tmin);
		}
		return sol;
	}

	public void printPriorityQueues() {
		for (int p = 0; p < problem.P; p++) {
			System.out.printf("pq%d :", p);
			while (!priorityQueuePerProcessor[p].isEmpty()) {
				System.out.print(priorityQueuePerProcessor[p].poll() + " ");
			}
			System.out.println();
		}
	}
	
	@Override
	public String getName() {
		return "MinMin+";
	}


}
