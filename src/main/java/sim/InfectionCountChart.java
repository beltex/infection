package main.java.sim;

import org.graphstream.algorithm.measure.ChartMeasure.PlotException;
import org.graphstream.algorithm.measure.ChartSeries2DMeasure;
import org.graphstream.algorithm.measure.ChartMeasure.PlotParameters;
import org.graphstream.algorithm.measure.ChartMeasure.PlotType;
import org.pmw.tinylog.Logger;


/**
 * Infection count plot (chart) of simulation.
 *
 */
public class InfectionCountChart {


    ///////////////////////////////////////////////////////////////////////////
    // PRIVATE ATTRIBUTES
    ///////////////////////////////////////////////////////////////////////////


    private ChartSeries2DMeasure chart;


    private PlotParameters params;


    ///////////////////////////////////////////////////////////////////////////
    // CONSTRUCTOR
    ///////////////////////////////////////////////////////////////////////////


    public InfectionCountChart(int maxItemCount) {
        params = new PlotParameters();
        params.title = "Infection Count";
        params.yAxisLabel = "Number of Agents Infected";
        params.xAxisLabel = "Time Step";
        params.type = PlotType.SCATTER;
        params.height = 720;
        params.width = 1280;

        chart = new ChartSeries2DMeasure("Infection Count");
        chart.getXYSeries().setMaximumItemCount(maxItemCount);

        Logger.info("Infection count chart CREATED");
    }


    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ///////////////////////////////////////////////////////////////////////////


    public void addDataPoint(int timeStep, int infectionCounter) {
        chart.addValue(timeStep, infectionCounter);
    }


    /**
     * Generate the chart (show it)
     */
    public void plot() {
        try {
            chart.plot(params);
        } catch (PlotException e) {
            Logger.error(e, "PLOT ERROR");
        }
    }
}