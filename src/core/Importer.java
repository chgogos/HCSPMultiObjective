package core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Importer {

	private static Logger log = LoggerFactory.getLogger(Importer.class);
	private String fn;

	public Importer(String fn) {
		this.fn = fn;
	}

	public HCSProblem read_dataset() throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(fn));
		String line1 = in.readLine();
		if (line1 == null) {
			in.close();
			throw new IllegalStateException("Dataset corrupt (first line)");
		}
		String s[] = line1.split(" ");
		HCSProblem problem = new HCSProblem(Integer.parseInt(s[0]), Integer.parseInt(s[1]));

		int linenum = 1;
		int lineZero = 0;
		for (int t = 0; t < problem.T; t++) {
			for (int p = 0; p < problem.P; p++) {
				String line = in.readLine();
				linenum++;
				if (line == null) {
					in.close();
					throw new IllegalStateException("Dataset corrupt");
				}
				double d = Double.valueOf(line);
				if (d < 0.0001) {
					// System.out.printf(
					// "Value %f considered as zero at line %d\n", d,
					// linenum);
					problem.setZeroTimeTask(t, p);
					lineZero++;
					// throw new IllegalStateException("Dataset corrupt");
				}
				// problem.etc[t][p] = d *100 ; // Nesmanchow seems to multiply
				// etc values by 100 !!!
				problem.etc[t][p] = d;
			}
		}
		in.close();
		problem.computeHelpValues();
		if (lineZero > 0) {
			log.debug("zero lines =  " + lineZero);
			HCSProblemConverter converter = new HCSProblemConverter(problem);
			problem = converter.convert();
		}

		// point.collect();
		return problem;
	}
}
