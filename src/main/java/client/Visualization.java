package client;

import org.pmw.tinylog.Level;

import sim.AgentDistribution;
import sim.ExtendedGraph;
import sim.Simulator;

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
        sim.agentDistribution(AgentDistribution.SINGLE);
        sim.nodeSelection(Simulator.NODE_WEIGHTED);
        sim.setActionProbabilites(interactProbability, traversalProbability);

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