package client;

import sim.*;

public class SampleClient {

    public static void main(String[] args) {

        final int A = 4;
        final int B = 0;
        final int numAgents = 1000;
        final int maxTimeSteps = 100000;
        final int runs = 1;

        ExtendedGraph g = new ExtendedGraph("SampleClient");
        //g.addNode("A");

        // Create a new simulation
        Simulator sim = new Simulator(g, numAgents, A, B, maxTimeSteps, runs);

        // Configure simulation settings

        // Use a graph generator to auto create a graph. In this case, a chain
        sim.generateGraphChain(10, true, true, true);

        // Define how agents should be spread across the graph
        sim.agentDistribution(AgentDistribution.SINGLE);

        // Define how a node should be selected on every time step
        sim.nodeSelection(Simulator.NODE_WEIGHTED);

        // Turn on graph visualization
        //sim.vis();

        // Turn on charts
        //sim.charts();

        // Run the simulation
        sim.execute();
    }
}