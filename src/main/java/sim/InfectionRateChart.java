package sim;

import org.graphstream.algorithm.measure.ChartMeasure.PlotException;
import org.graphstream.algorithm.measure.ChartSeries2DMeasure;
import org.graphstream.algorithm.measure.ChartMeasure.PlotParameters;
import org.graphstream.algorithm.measure.ChartMeasure.PlotType;
import org.pmw.tinylog.Logger;


/**
 * Infection rate plot (chart) of simulation.
 *
 */
public class InfectionRateChart {


    ///////////////////////////////////////////////////////////////////////////
    // PRIVATE ATTRIBUTES
    ///////////////////////////////////////////////////////////////////////////


    private ChartSeries2DMeasure chart;


    private PlotParameters params;


    private double lastInfectionCount;


    ///////////////////////////////////////////////////////////////////////////
    // CONSTRUCTOR
    ///////////////////////////////////////////////////////////////////////////


    public InfectionRateChart(int maxItemCount) {
        chart = new ChartSeries2DMeasure("Infection Rate");
        chart.getXYSeries().setMaximumItemCount(maxItemCount);

        params = new PlotParameters();
        params.title = "Infection Rate";
        params.type = PlotType.LINE;
        params.yAxisLabel = "Infection Count";
        params.xAxisLabel = "Time Step";
        params.height = 720;
        params.width = 1280;

        lastInfectionCount = 1.0;

        Logger.debug("Infection rate chart INIT");
    }

    public void addDataPoint(int timeStep, double infectionCount) {
        double rate = ((infectionCount - lastInfectionCount) / lastInfectionCount) * 100.0;
        lastInfectionCount = infectionCount;

        chart.addValue(timeStep, rate);
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
