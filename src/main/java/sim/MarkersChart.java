package sim;

import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import org.graphstream.algorithm.measure.ChartMeasure.PlotException;
import org.graphstream.algorithm.measure.ChartSeries2DMeasure;
import org.graphstream.algorithm.measure.ChartMeasure.PlotParameters;
import org.graphstream.algorithm.measure.ChartMeasure.PlotType;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeriesCollection;
import org.pmw.tinylog.Logger;


/**
 * Infection count plot (chart) of simulation.
 *
 */
public class MarkersChart {


    ///////////////////////////////////////////////////////////////////////////
    // PRIVATE ATTRIBUTES
    ///////////////////////////////////////////////////////////////////////////


    private ChartSeries2DMeasure infectionComplete;
    private ChartSeries2DMeasure leaderElectionComplete;
    private ChartSeries2DMeasure allElectionComplete;


    private PlotParameters params;


    ///////////////////////////////////////////////////////////////////////////
    // CONSTRUCTOR
    ///////////////////////////////////////////////////////////////////////////


    public MarkersChart(int maxItemCount) {
        params = new PlotParameters();
        params.title = "Simulation Markers Chart";
        params.yAxisLabel = "Number of Agents";
        params.xAxisLabel = "Time Step";
        params.type = PlotType.SCATTER;
        params.height = 1080;
        params.width = 1920;

        params.path = System.getProperty("user.dir") +
                      File.separator + "logs" + File.separator + "test.png";

        infectionComplete = new ChartSeries2DMeasure("Infection Count");
        leaderElectionComplete = new ChartSeries2DMeasure("Leader");
        allElectionComplete = new ChartSeries2DMeasure("All");
        //chart.setWindowSize(maxItemCount);
        //infectionComplete.getXYSeries().setMaximumItemCount(maxItemCount);

        Logger.debug("Infection count chart INIT");
    }


    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ///////////////////////////////////////////////////////////////////////////


    public void addDataPoint(int timeStep, int infectionCounter) {
        infectionComplete.addValue(timeStep, infectionCounter);
    }

    public void addDataPointLeader(int timeStep, int infectionCounter) {
        leaderElectionComplete.addValue(timeStep, infectionCounter);
    }

    public void addDataPointAll(int timeStep, int infectionCounter) {
        allElectionComplete.addValue(timeStep, infectionCounter);
    }


    /**
     * Generate the chart (show it)
     */
    public void plot() {
            JFreeChart chart = null;
            XYSeriesCollection dataset = new XYSeriesCollection();

            dataset.addSeries(infectionComplete.getXYSeries());
            dataset.addSeries(leaderElectionComplete.getXYSeries());
            dataset.addSeries(allElectionComplete.getXYSeries());

            chart = ChartFactory.createScatterPlot(params.title,
                    params.xAxisLabel, params.yAxisLabel, dataset,
                    params.orientation, params.showLegend, false, false);
            chart.setTextAntiAlias(true);

//            NumberAxis xAxis = new NumberAxis();
//            xAxis.setRange(1000, 1001);
//            xAxis.setTickUnit(new NumberTickUnit(1));
//
//            chart.getXYPlot().setDomainAxis(xAxis);
            NumberAxis xAxis = (NumberAxis) chart.getXYPlot().getDomainAxis();
            xAxis.setTickUnit(new NumberTickUnit(1));




            ChartPanel panel = new ChartPanel(chart, params.width,
                    params.height, params.width, params.height,
                    params.width + 50, params.height + 50, true, true, true,
                    true, true, true);

            JFrame frame = new JFrame(params.title);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.add(panel);
            frame.pack();
            frame.setVisible(true);





//                try {
//                    ChartUtilities.saveChartAsPNG(new File(params.path), chart,
//                            params.width, params.height);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
    }
}