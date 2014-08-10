/*
 * OriginalPaper.java
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
 * Original research paper scenario
 *
 */
public class OriginalPaper {

    public static void main(String[] args) {

        final Range<Integer> numAgents = Range.closedOpen(1000, 10001);
        final int runs = 10;
        final int A = 4;
        final int B = 0;
        final int maxTimeSteps = 600000;


        ExtendedGraph g = new ExtendedGraph("Original Paper");
        g.addNode("A");


        Simulator sim = new Simulator(g, numAgents, A, B, maxTimeSteps, runs, Level.OFF);

        sim.agentDistribution(Distribution.SINGLE);
        sim.setSingleAgentDistNodeID("A");
        sim.nodeSelection(NodeSelection.NON_WEIGHTED);

        sim.execute();
    }
}