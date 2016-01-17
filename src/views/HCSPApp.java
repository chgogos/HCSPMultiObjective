package views;

import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;

import core.HCSProblem;
import core.Importer;
import core.Solution;
import solvers.evolutionary.EvolutionaryHcspBiObjective;
import solvers.heuristics.MinMinPlus;

public class HCSPApp {

	public static void main(String[] args) {
		String fn = "datasets//Braun_et_al//u_s_lolo.0";
		HCSPApp app = new HCSPApp();
		app.simpleTest(fn);
		app.testBiObjective(fn, "NSGAII", 200000);

	}

	private void simpleTest(String fn) {
		StringBuilder sb = new StringBuilder();
		Importer importer = new Importer(fn);

		HCSProblem problem;
		try {
			problem = importer.read_dataset();
			MinMinPlus solver = new MinMinPlus(problem);
			Solution sol = solver.solve();
			sb.append(String.format("MinMin makespan=%.1f\n", sol.makespan()));
			sb.append(String.format("MinMin flowtime=%.1f\n", sol.flowTime()));
			sb.append(String.format("MinMin resourceutilisation=%.1f\n", sol.resourceUtilization()));
			sb.append(String.format("MinMin matchingproximity=%.1f\n", sol.matchingProximity()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(sb.toString());
	}

	private void testBiObjective(String fn, String algorithm, int maxEvaluations) {
		NondominatedPopulation result = new Executor().withProblemClass(EvolutionaryHcspBiObjective.class, fn)
				.withAlgorithm(algorithm).withMaxEvaluations(maxEvaluations).distributeOnAllCores().run();
		double values[][] = new double[result.size()][2];
		for (int i = 0; i < result.size(); i++) {
			org.moeaframework.core.Solution solution = result.get(i);
			double objectives[] = solution.getObjectives();
			System.out.printf("Solution makespan=%.2f flowtime=%.2f\n", objectives[0], objectives[1]);
			values[i][0] = objectives[0];
			values[i][1] = objectives[1];
		}
		showGraph(values);
	}

	private static void showGraph(double[][] values) {
		XYSeriesCollection dataset = new XYSeriesCollection();
		XYSeries data = new XYSeries("Makespan vs Flowtime");
		for (int i = 0; i < values.length; i++) {
			double x0 = values[i][0]; // makespan
			double x1 = values[i][1]; // flowtime
			data.add(x0, x1);
		}
		dataset.addSeries(data);
		final JFreeChart chart = createChart(dataset);
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		final ApplicationFrame frame = new ApplicationFrame("Title");
		frame.setContentPane(chartPanel);
		frame.pack();
		frame.setVisible(true);
	}

	private static JFreeChart createChart(final XYDataset dataset) {
		final JFreeChart chart = ChartFactory.createScatterPlot("Makespan vs Flowtime chart", // title
				"Makespan 1", // x axis label
				"Flowtime", // y axis label
				dataset, // data
				PlotOrientation.VERTICAL, true, // include legend
				true, // tooltips
				false // urls
		);
		XYPlot plot = (XYPlot) chart.getPlot();
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setSeriesLinesVisible(0, true);
		plot.setRenderer(renderer);
		return chart;
	}

}
