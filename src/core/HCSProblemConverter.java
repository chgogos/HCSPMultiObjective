package core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HCSProblemConverter {
	private static Logger log = LoggerFactory
			.getLogger(HCSProblemConverter.class);

	HCSProblem originalProblem;
	HCSProblem nzProblem;

	public HCSProblemConverter(HCSProblem problem) {
		this.originalProblem = problem;
	}

	public HCSProblem convert() {
		int zeroExecuteTimeTasks = originalProblem.zeroTimeTasks();
		nzProblem = new HCSProblem(originalProblem.T - zeroExecuteTimeTasks,
				originalProblem.P);
		int t2 = 0;
		for (int t1 = 0; t1 < originalProblem.T; t1++) {
			if (originalProblem.zeroTimeTasks[t1] != -1)
				continue;
			for (int p = 0; p < originalProblem.P; p++)
				nzProblem.etc[t2][p] = originalProblem.etc[t1][p];
			t2++;
		}
		nzProblem.computeHelpValues();
		log.debug("Problem converted tasks with zero execution time removed ="
				+ zeroExecuteTimeTasks);
		return nzProblem;
	}

	public HCSProblem getNonZeroProblem() {
		return nzProblem;
	}
}
