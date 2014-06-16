package client;

import org.pmw.tinylog.Level;

import sim.AgentDistribution;
import sim.ExtendedGraph;
import sim.Simulator;

import com.google.common.collect.Range;

public class Client {

    public static void main(String[] args) {


        /**
         * How many agents should be used in the simulation?
         *
         * This is defined as a range, and thus could be a single value or
         * more. If its is more than one (a range), for example 5 to 10, then a
         * simulation is performed for each value within that range. See 'runs'
         * variable below for more.
         *
         * The range object is configurable. See details in the link below.
         *
         * https://code.google.com/p/guava-libraries/wiki/RangesExplained#Building_Ranges
         */
        final Range<Integer> numAgents = Range.closedOpen(1000, 1001);


        /**
         * Number of simulation runs to occur at each value in the range of the
         * number of agents. So for example, if the range of the number of
         * agents is from 5 to 10, then 10 simulations will be run
         * for each value of agents within that range.
         */
        final int runs = 1;


        /**
         * Values for terms A & B in the election process completion estimate
         * equation. They are buffer factors. See paper for more.
         *
         * A: multiplicative factor
         * B: additive factor
         */
        final int A = 4;
        final int B = 0;


        /**
         * What is the maximum number of timesteps that a simulation can run
         * for? This is a safety parameter to cut off a simulation that is
         * running too long.
         *
         * Steps is defined as the number of interactions + traversals. So on
         * any iteration of the simulation, there is a 50/50 chance that either
         * an interaction or traversal will occur. That is assuming of course
         * a traversal is possible. If it is not, the simulator will detect
         * this, and all timesteps will be simply interactions.
         */
        final int maxTimeSteps = 100000;


        /**
        *
        */
        ExtendedGraph g = new ExtendedGraph("SampleClient");
        //g.addNode("A");


        // Create a new simulation
        Simulator sim = new Simulator(g, numAgents, A, B, maxTimeSteps, runs, Level.INFO);


        /*
         * Configure simulation settings
         */


        // Use a graph generator to auto create a graph. In this case, a chain
        sim.generateGraphChain(10, true, true, false);


        // Define how agents should be spread across the graph
        sim.agentDistribution(AgentDistribution.SINGLE);


        // Define how a node should be selected on every time step
        sim.nodeSelection(Simulator.NODE_WEIGHTED);

        sim.setActionProbabilites(0.25, 0.75);

        // Turn on graph visualization
        sim.vis();


        // Turn on charts
        sim.charts();


        // Run the simulation
        sim.execute();
    }
}