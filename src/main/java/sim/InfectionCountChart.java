/*
 * InfectionCountChart.java
 * infection
 *
 * Copyright (C) 2014  beltex <https://github.com/beltex>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package sim;

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

        Logger.debug("Infection count chart CREATED");
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