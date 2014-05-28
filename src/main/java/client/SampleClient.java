package main.java.client;

import main.java.sim.AgentDistribution;
import main.java.sim.ExtendedGraph;
import main.java.sim.Simulator;

public class SampleClient {

    public static void main(String[] args) {

        final int A = 4;
        final int B = 0;
        final int numAgents = 1000;
        final int maxInteractions = 100000;

        ExtendedGraph g = new ExtendedGraph("SampleClient");

        // Create a new simulation
        Simulator sim = new Simulator(g, numAgents, A, B, maxInteractions);

        // Configure simulation settings

        // Use a graph generator to auto create a graph. In this case, a chain
        sim.generateGraphChain(10, true, true, false);

        // Define how agents should be spread across the graph
        sim.agentDistribution(AgentDistribution.RANDOM_SPREAD);

        // Turn on graph visualization
        sim.vis();

        // Turn on charts
        sim.charts();

        // Run the simulation
        sim.execute();
    }
}