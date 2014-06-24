package sim;

import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import org.graphstream.algorithm.measure.ChartSeries2DMeasure;
import org.graphstream.algorithm.measure.ChartMeasure.PlotParameters;
import org.graphstream.algorithm.measure.ChartMeasure.PlotType;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeriesCollection;
import org.pmw.tinylog.Logger;


/**
 * Chart of key markers during a simulation.
 *
 */
public class MarkersChart {


    ///////////////////////////////////////////////////////////////////////////
    // PRIVATE ATTRIBUTES
    ///////////////////////////////////////////////////////////////////////////


    private JFreeChart chart;


    private ChartSeries2DMeasure infectionComplete;
    private ChartSeries2DMeasure leaderElectionComplete;
    private ChartSeries2DMeasure allElectionComplete;


    private PlotParameters params;


    ///////////////////////////////////////////////////////////////////////////
    // CONSTRUCTOR
    ///////////////////////////////////////////////////////////////////////////


    public MarkersChart(int maxItemCount, String dirName, String timestamp) {
        params = new PlotParameters();
        params.title = "Simulation Markers Chart";
        params.xAxisLabel = "Number of Agents";
        params.yAxisLabel = "Interaction Step";
        params.type = PlotType.SCATTER;
        params.height = 1080;
        params.width = 1920;

        params.path = System.getProperty("user.dir") +
                      File.separator + "logs" +
                      File.separator + dirName +
                      File.separator + "chart." + timestamp + ".png";

        infectionComplete = new ChartSeries2DMeasure("Infection Count");
        leaderElectionComplete = new ChartSeries2DMeasure("Leader");
        allElectionComplete = new ChartSeries2DMeasure("All");


        // Main chart
        XYSeriesCollection dataset = new XYSeriesCollection();

        dataset.addSeries(infectionComplete.getXYSeries());
        dataset.addSeries(leaderElectionComplete.getXYSeries());
        dataset.addSeries(allElectionComplete.getXYSeries());

        chart = ChartFactory.createScatterPlot(params.title,
                params.xAxisLabel, params.yAxisLabel, dataset,
                params.orientation, params.showLegend, false, false);
        chart.setTextAntiAlias(true);


        Logger.debug("Infection count chart INIT");
    }


    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ///////////////////////////////////////////////////////////////////////////


    public void addDataPoint(int numAgents, int step) {
        infectionComplete.addValue(numAgents, step);
    }

    public void addDataPointLeader(int numAgents, int step) {
        leaderElectionComplete.addValue(numAgents, step);
    }

    public void addDataPointAll(int numAgents, int step) {
        allElectionComplete.addValue(numAgents, step);
    }


    /**
     * Display the chart (plot it)
     */
    public void display() {
        ChartPanel panel = new ChartPanel(chart, params.width, params.height,
                                                               params.width,
                                                               params.height,
                                                               params.width + 50,
                                                               params.height + 50,
                                                               true,
                                                               true,
                                                               true,
                                                               true,
                                                               true,
                                                               true);

        JFrame frame = new JFrame(params.title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }


    /**
     * Save chart to disk as a PNG
     */
    public void save() {
        try {
            ChartUtilities.saveChartAsPNG(new File(params.path), chart,
                                                                 params.width,
                                                                 params.height);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}