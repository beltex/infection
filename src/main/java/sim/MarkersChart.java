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
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
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


    private ChartSeries2DMeasure c;
    private ChartSeries2DMeasure c2;
    private ChartSeries2DMeasure c3;


    private PlotParameters params;


    ///////////////////////////////////////////////////////////////////////////
    // CONSTRUCTOR
    ///////////////////////////////////////////////////////////////////////////


    public MarkersChart(int maxItemCount) {
        params = new PlotParameters();
        params.title = "Infection Count";
        params.yAxisLabel = "Number of Agents Infected";
        params.xAxisLabel = "Time Step";
        params.type = PlotType.SCATTER;
        params.height = 720;
        params.width = 1280;
        params.path = System.getProperty("user.dir") +
                      File.separator + "logs" + File.separator + "test.png";

        c = new ChartSeries2DMeasure("Infection Count");
        c2 = new ChartSeries2DMeasure("Leader");
        c3 = new ChartSeries2DMeasure("All");
        //chart.setWindowSize(maxItemCount);
        c.getXYSeries().setMaximumItemCount(maxItemCount);

        //XYSeries xy = c.getXYSeries();

        Logger.debug("Infection count chart INIT");
    }


    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ///////////////////////////////////////////////////////////////////////////


    public void addDataPoint(int timeStep, int infectionCounter) {
        c.addValue(timeStep, infectionCounter);
    }

    public void addDataPointLeader(int timeStep, int infectionCounter) {
        c2.addValue(timeStep, infectionCounter);
    }

    public void addDataPointAll(int timeStep, int infectionCounter) {
        c3.addValue(timeStep, infectionCounter);
    }


    /**
     * Generate the chart (show it)
     */
    public void plot() {
            JFreeChart chart = null;
            XYSeriesCollection dataset = new XYSeriesCollection();

            dataset.addSeries(c.getXYSeries());
            dataset.addSeries(c2.getXYSeries());
            dataset.addSeries(c3.getXYSeries());

            chart = ChartFactory.createScatterPlot(params.title,
                    params.xAxisLabel, params.yAxisLabel, dataset,
                    params.orientation, params.showLegend, false, false);



//        //JFreeChart chart = null;
//        try {
//            chart = c.createChart(params);
//        } catch (PlotException e1) {
//            e1.printStackTrace();
//        }


//            ChartPanel panel = new ChartPanel(chart, params.width,
//                    params.height, params.width, params.height,
//                    params.width + 50, params.height + 50, true, true, true,
//                    true, true, true);
//
//            JFrame frame = new JFrame(params.title);
//            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//            frame.setLocationRelativeTo(null);
//            frame.add(panel);
//            frame.pack();
//            frame.setVisible(true);





                try {
                    ChartUtilities.saveChartAsPNG(new File(params.path), chart,
                            params.width, params.height);
                } catch (IOException e) {
                    e.printStackTrace();
                }






    }
}