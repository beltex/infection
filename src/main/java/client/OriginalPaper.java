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
        sim.nodeSelection(NodeSelection.NON_WEIGHTED);

        sim.execute();
    }
}