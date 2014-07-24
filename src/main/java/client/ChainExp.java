package client;

import org.pmw.tinylog.Level;

import sim.AgentDistribution.Distribution;
import sim.ExtendedGraph;
import sim.Simulator;
import sim.Simulator.NodeSelection;

import com.google.common.collect.Range;


/**
 * Chain experiments
 *
 */
public class ChainExp {

    public static void main(String[] args) {

        final Range<Integer> numAgents = Range.closedOpen(1000, 10001);
        final int runs = 10;
        final int A = 4;
        final int B = 0;
        final int maxTimeSteps = 1200000;


        ExtendedGraph g = new ExtendedGraph("Chain Experiment");


        Simulator sim = new Simulator(g, numAgents, A, B, maxTimeSteps, runs, Level.OFF);

        sim.generateGraphChain(2, false, false, false);
        sim.agentDistribution(Distribution.SINGLE);
        sim.nodeSelection(NodeSelection.WEIGHTED);

        sim.execute();
    }
}