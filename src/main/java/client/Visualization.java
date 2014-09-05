/*
 * Visualization.java
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

package client;

import org.pmw.tinylog.Level;

import sim.AgentDistribution.Distribution;
import sim.ExtendedGraph;
import sim.Simulator;
import sim.Simulator.NodeSelection;

import com.google.common.collect.Range;


/**
 * Visualizing a graph. This is recommended to be done only for single
 * simulation runs, not large scale testing due to the performance overhead.
 *
 */
public class Visualization {

    public static void main(String[] args) {

        final Range<Integer> numAgents = Range.closedOpen(1000, 1001);
        final int runs = 1;
        final int A = 4;
        final int B = 0;
        final int maxTimeSteps = 100000;
        final double interactProbability = 0.25;
        final double traversalProbability = 0.75;


        ExtendedGraph g = new ExtendedGraph("Visualization");
        Simulator sim = new Simulator(g, numAgents, A, B, maxTimeSteps, runs, Level.INFO);

        sim.generateGraphChain(10, true, true, false);
        sim.agentDistribution(Distribution.EVEN_SPREAD);
        sim.nodeSelection(NodeSelection.NON_WEIGHTED);
        sim.setActionProbabilites(interactProbability, traversalProbability);
        sim.simDescription("Description about the test");

        /*
         * Turn on graph visualization
         */
        sim.vis();

        /*
         * Turn on charts, as in display them
         */
        sim.charts();

        sim.execute();
    }
}