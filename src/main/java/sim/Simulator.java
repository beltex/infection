package sim;

import org.pmw.tinylog.Logger;


/**
 * The controller class. Single point of contact for the user
 *
 */
public class Simulator  {


    ///////////////////////////////////////////////////////////////////////////
    // PRIVATE ATTRIBUTES
    ///////////////////////////////////////////////////////////////////////////


    private ExtendedGraph g;


    /**
     * Graph visualization flag
     */
    private boolean flag_vis = false;


    /**
     * Simulation charts (plots) flags
     */
    private boolean flag_charts = false;


    /**
     * Automatic graph generation flag
     */
    private boolean flag_generateGraph = false;


    /**
     * Values for terms A & B in the election process completion estimate
     * equation. They are buffer factors. See paper for more.
     *
     * A: multiplicative factor
     * B: additive factor
     */
    private int termA;
    private int termB;


    /**
     * Max number of time-steps the simulation should go on for, after which
     * point we exit
     */
    private int maxTimeSteps;


    private int runs;


    ///////////////////////////////////////////////////////////////////////////
    // CONSTRUCTOR
    ///////////////////////////////////////////////////////////////////////////


    /**
     * Create a simulation
     *
     * @param g Graph for the simulation to run on
     * @param numAgents Number of agents to be created. Must be at least >= 2.
     * @param termA Multiplicative factor
     * @param termB Additive factor
     * @param maxTimeSteps
     * @param runs How many times should the simulation be run?
     */
    public Simulator(ExtendedGraph g, int numAgents, int termA,
                                                     int termB,
                                                     int maxTimeSteps,
                                                     int runs) {
        if (numAgents < 2) {
            Logger.error("MUST have >= 2 agents");
            System.exit(-1);
        }

        this.g = g;
        this.g.setNullAttributesAreErrors(true);
        this.g.setNumAgents(numAgents);

        this.termA = termA;
        this.termB = termB;
        this.maxTimeSteps = maxTimeSteps;
        this.runs = runs;

        Logger.info("Simulator INIT: " + toString());
    }


    ///////////////////////////////////////////////////////////////////////////
    // PRIVATE METHODS
    ///////////////////////////////////////////////////////////////////////////


    /**
     * Simulator init operations
     */
    private void init() {
        /*
         * Init helper classes
         */
        RandomSource.getInstance().init(g);
        GraphVis.getInstance().init(g);

        if (flag_vis) {
            GraphVis.getInstance().display();
        }

        if (flag_generateGraph) {
            GraphGeneratorSource.getInstance().generateGraph(g);

            // Only need to generate the graph once
            flag_generateGraph = false;
        }


        /*
         * Check the graph
         */
        if (!g.isConnected()) {
            /*
             * Graph not connected, thus some node(s) not reachable. We can't
             * work with such a graph, as certain agent(s) may not be reachable
             * and so can never be infected
             */
            Logger.error("Graph is NOT connected");
            System.exit(-1);
        }

        if (g.getNodeCount() == 1) {
            Logger.warn("Single node graph - no traverse actions allowed");
        }


        /*
         * Create and distribute the agents
         */
        Logger.info("GRAPH STATE: " + g.checkNumAgents());
        AgentDistribution dist = new AgentDistribution();
        dist.init(g);
        dist.execute();
    }


    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ///////////////////////////////////////////////////////////////////////////


    /**
     * Execute the simulation
     *
     * @throws Exception
     */
    public void execute() {
        for (int y = 0; y < runs; y++) {
            Logger.info("RUN: " + y);

            init();

            TimeStep ts = new TimeStep(g, termA, termB, maxTimeSteps);
            for (int i = 0; i < maxTimeSteps; i++) {
                ts.step();
            }

            ts.end(flag_charts);
        }
    }


    public String toString() {
        return "Graph: " + g +
               "; Term A: " + termA +
               "; Term B: " + termB +
               "; Max interactions: " + maxTimeSteps;
    }


    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS - Simulation Settings
    ///////////////////////////////////////////////////////////////////////////


    /**
     * How should the agents be distributed across the graph? Set an agent
     * distribution algorithm.
     *
     * @param agentDistributionAlgo Agent distribution algorithm to be used. See
     * 								constants in AgentDistribution class
     */
    public void agentDistribution(int agentDistributionAlgo) {
        g.setAgentDistribution(agentDistributionAlgo);
    }


    /**
     * Turn on graph visualization. This is OFF by default, as large scale
     * testing doesn't require visualization. Must be called before execute()
     */
    public void vis() {
        flag_vis = true;
    }


    /**
     * Turn on charts
     */
    public void charts() {
        flag_charts = true;
    }


    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS - GRAPH GENERATORS
    ///////////////////////////////////////////////////////////////////////////


    /**
     * Generate a fully connected graph
     *
     * @param n Number of nodes the graph should have
     * @param directed Should the edges be directed?
     * @param randomlyDirectedEdges Should the edges be randomly directed?
     */
    public void generateGraphFullyConnected(int n, boolean directed, boolean randomlyDirectedEdges) {
        flag_generateGraph = true;
        GraphGeneratorSource.getInstance().graphFullyConnected(n, directed, randomlyDirectedEdges);
    }


    /**
     * Generate a chain graph.
     *
     * If the graph is undirected, nodes are single linked regardless of
     * doublyLinked argument.
     *
     * @param n Number of nodes the graph should have
     * @param directed Should the edges be directed?
     * @param doublyLinked Should the nodes be doubly linked?
     * @param loopBack Should the last node have an edge looping back to the start?
     */
    public void generateGraphChain(int n, boolean directed, boolean doublyLinked, boolean loopBack) {
        flag_generateGraph = true;
        GraphGeneratorSource.getInstance().graphChain(n, directed, doublyLinked, loopBack);
    }


    /**
     * Generate an n * n grid graph
     *
     * @param n How many nodes should this graph have? Since this is a grid,
     * 		    this equates to n * n nodes
     * @param directed Should the graph be directed?
     * @param crossEdges Should the grid have cross edges?
     */
    public void generateGraphGrid(int n, boolean directed, boolean crossEdges) {
        flag_generateGraph = true;
        GraphGeneratorSource.getInstance().graphGrid(n, directed, crossEdges);
    }
}
