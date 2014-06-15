package sim;

import java.util.ArrayList;

import org.pmw.tinylog.Logger;


/**
 * The "controller" class. Single point of contact for the user.
 *
 */
public class Simulator  {


    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC ATTRIBUTES
    ///////////////////////////////////////////////////////////////////////////


    /**
     * Automatic graph generation flag
     */
    public static final int NODE_NON_WEIGHTED = 0;


    /**
     * Automatic graph generation flag
     */
    public static final int NODE_WEIGHTED = 1;


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


    /**
     * How many times should the simulation be run?
     */
    private int runs;


    /**
     *
     */
    private static SimulatorJSON simJSON = new SimulatorJSON();


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

        Logger.info("Simulator CREATED");
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

            // Only need to display graph once
            flag_vis = false;
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
        AgentDistribution dist = new AgentDistribution();
        dist.init(g);
        dist.execute();
    }


    ///////////////////////////////////////////////////////////////////////////
    // PROTECTED METHODS
    ///////////////////////////////////////////////////////////////////////////


    protected static SimulatorJSON getSimulatoJSON() {
        return simJSON;
    }


    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ///////////////////////////////////////////////////////////////////////////


    /**
     * Execute the simulation
     *
     */
    public void execute() {
        Logger.info("Simulation SETTINGS" + toString());

        for (int y = 0; y < runs; y++) {
            Logger.info("----------------------------------------------------");
            Logger.info("STARTING RUN: " + (y + 1));
            init();

            TimeStep ts = new TimeStep(g, termA, termB, maxTimeSteps);
            for (int i = 0; i < maxTimeSteps; i++) {
                ts.step();

                if (ts.isFlag_infectionComplete() &&
                    ts.isFlag_leaderElectionComplete() &&
                    ts.isFlag_allElectionComplete()) {
                    Logger.info("STEP: {0}; Cutting off simulation - all actions complete", i);
                    break;
                }
            }

            ts.end(flag_charts);
            Logger.info("ENDING RUN: " + (y + 1));
            Logger.info("----------------------------------------------------");
        }

        Logger.info("ALL SIMULATION RUNS COMPLETE");

        stats();
    }


    public void stats() {
        double infected = 0;
        double eleComp = 0;
        double interactions = 0;
        double traversals = 0;
        double marker_infectionComplete = 0;
        double marker_leaderElectionComplete = 0;
        double marker_allElectionComplete = 0;

        double marker_infectionComplete_interact = 0;
        double marker_leaderElectionComplete_interact = 0;
        double marker_allElectionComplete_interact = 0;

        MarkersChart icc = new MarkersChart(maxTimeSteps);
        MarkersChart icc2 = new MarkersChart(maxTimeSteps);

        ArrayList<SimulatorRun> list = simJSON.getRunData();
        for (SimulatorRun r : list) {
            infected += (double)r.getInfected();
            eleComp += (double)r.getEleComp();
            interactions += (double)r.getInteractions();
            traversals += (double)r.getTraversals();
            marker_infectionComplete += (double)r.getMarker_infectionComplete();
            icc.addDataPoint(g.getNumAgents(), r.getMarker_infectionComplete());

            marker_leaderElectionComplete += (double)r.getMarker_leaderElectionComplete();
            icc.addDataPointLeader(g.getNumAgents(), r.getMarker_leaderElectionComplete());


            marker_allElectionComplete += (double)r.getMarker_allElectionComplete();
            icc.addDataPointAll(g.getNumAgents(), r.getMarker_allElectionComplete());


            ///

            marker_infectionComplete_interact += (double)r.getMarker_infectionComplete_interact();
            icc2.addDataPoint(g.getNumAgents(), r.getMarker_infectionComplete_interact());

            marker_leaderElectionComplete_interact += (double)r.getMarker_leaderElectionComplete_interact();
            icc2.addDataPointLeader(g.getNumAgents(), r.getMarker_leaderElectionComplete_interact());


            marker_allElectionComplete_interact += (double)r.getMarker_allElectionComplete_interact();
            icc2.addDataPointAll(g.getNumAgents(), r.getMarker_allElectionComplete_interact());
        }

        //icc.plot();
        icc2.plot();

        Logger.info("# of INFECTED agents: " + (infected / runs));
        Logger.info("# of agents that believe election is COMPLETE: " + (eleComp/runs));
        Logger.info("# of agent INTERACTIONS: " + (interactions / runs));
        Logger.info("# of agent TRAVERSALS: " + (traversals/runs));
        Logger.info("MARKER - Infection Complete Step: " + (marker_infectionComplete / runs));
        Logger.info("MARKER - Leader Election Complete Step: " + (marker_leaderElectionComplete / runs));
        Logger.info("MARKER - All Election Complete Step: " + (marker_allElectionComplete/runs));

        Logger.info("MARKER - Infection Complete INTERACT: " + (marker_infectionComplete_interact / runs));
        Logger.info("MARKER - Leader Election Complete INTERACT: " + (marker_leaderElectionComplete_interact / runs));
        Logger.info("MARKER - All Election Complete INTERACT: " + (marker_allElectionComplete_interact/runs));

        simJSON.exportToJSON();
    }


    public String toString() {
        return "\n\t Term A: " + termA +
               ";\n\t Term B: " + termB +
               ";\n\t Max interactions: " + maxTimeSteps;
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
     * How should a node randomly be selected? Set node selection method.
     *
     * @param nodeSelectionMethod Node selection method
     */
    public void nodeSelection(int nodeSelectionMethod) {
        g.setNodeSelection(nodeSelectionMethod);
    }


    /**
     * How long should the simulator sleep between each graph action? This is
     * default 0 ms, however if you would like to slow down the simulation to
     * better view the visualization, then you will want to increase this.
     *
     * @param sleep How many milliseconds the simulation should sleep between
     * 			    each event
     */
    public void visSleep(int sleep) {
        Sleep.SLEEP = sleep;
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
