package sim;

import java.util.ArrayList;
import java.util.HashMap;

import org.pmw.tinylog.Level;
import org.pmw.tinylog.Logger;

import com.google.common.collect.Range;


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


    private TinylogProperties tinylog;


    private Range<Integer> numAgents;


    private HashMap<Integer, Double> actionProbability;


    ///////////////////////////////////////////////////////////////////////////
    // CONSTRUCTOR
    ///////////////////////////////////////////////////////////////////////////


    /**
     * Create a simulation
     *
     * @param g Graph for the simulation to run on
     * @param numAgents Range of agents to be created
     * @param termA Multiplicative factor
     * @param termB Additive factor
     * @param maxTimeSteps Max number of time steps to be performed. That is
     * 					   interacts + traversals.
     * @param runs How many times should the simulation be run?
     */
    public Simulator(ExtendedGraph g, Range<Integer> numAgents,
                                                     int termA,
                                                     int termB,
                                                     int maxTimeSteps,
                                                     int runs,
                                                     Level level) {
        // Init logging
        tinylog = new TinylogProperties(level);

        this.g = g;
        this.numAgents = numAgents;
        this.g.setNullAttributesAreErrors(true);

        this.termA = termA;
        this.termB = termB;
        this.maxTimeSteps = maxTimeSteps;
        this.runs = runs;

        simJSON.setDate(tinylog.getDate());
        simJSON.setTermA(termA);
        simJSON.setTermB(termB);
        simJSON.setRuns(runs);
        simJSON.setMaxTimeSteps(maxTimeSteps);
        simJSON.setNumAgents(numAgents);

        actionProbability = new HashMap<Integer, Double>();
        actionProbability.put(TimeStep.ACTION_INTERACT, 0.5);
        actionProbability.put(TimeStep.ACTION_TRAVERSE, 0.5);

        Logger.info("Simulator CREATED");
    }


    ///////////////////////////////////////////////////////////////////////////
    // PRIVATE METHODS
    ///////////////////////////////////////////////////////////////////////////


    private HashMap<Integer, Range<Double>> actionProbabilitySpread() {
        double offset = 0.0;
        HashMap<Integer, Range<Double>> map = new HashMap<Integer, Range<Double>>();

        for (Integer key : actionProbability.keySet()) {
            // Probability of the action
            double p = actionProbability.get(key);

            // Upper bound for the range of the this node
            double upper = offset + p;

            // https://code.google.com/p/guava-libraries/wiki/RangesExplained
            map.put(key, Range.closedOpen(offset, upper));

            Logger.trace("{0}; Probability {1}; Offset {2}; Upper {3}", key,
                                                                        p,
                                                                        offset,
                                                                        upper);

            offset = upper;
        }

        return map;
    }


    /**
     * Simulator init operations
     */
    private void init() {
        g.setActionProbabilitySpread(actionProbabilitySpread());

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


    private void execute(int numAgents) {
        g.setNumAgents(numAgents);

        for (int y = 0; y < runs; y++) {
            Logger.info("----------------------------------------------------");
            Logger.info("STARTING RUN: " + (y + 1));
            init();

            TimeStep ts = new TimeStep(g, termA, termB);
            for (int i = 0; i < maxTimeSteps; i++) {
                ts.step();

                if (ts.isFlag_infectionComplete() &&
                    ts.isFlag_leaderElectionComplete() &&
                    ts.isFlag_allElectionComplete()) {
                    Logger.info("STEP: {0}; Cutting off simulation - all actions complete", i);
                    break;
                }
            }

            ts.end();
            Logger.info("ENDING RUN: " + (y + 1));
            Logger.info("----------------------------------------------------");
        }
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

        for (int y = numAgents.lowerEndpoint(); y < numAgents.upperEndpoint(); y++) {
            execute(y);
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
            infected += (double)r.getInfections();
            eleComp += (double)r.getElectionCompleteCount();
            interactions += (double)r.getInteractions();
            traversals += (double)r.getTraversals();
            marker_infectionComplete += (double)r.getInfectionCompleteStep();
//            icc.addDataPoint(g.getNumAgents(), r.getMarker_infectionComplete());

            marker_leaderElectionComplete += (double)r.getLeaderElectionCompleteStep();
//            icc.addDataPointLeader(g.getNumAgents(), r.getMarker_leaderElectionComplete());


            marker_allElectionComplete += (double)r.getAllElectionCompleteStep();
//            icc.addDataPointAll(g.getNumAgents(), r.getMarker_allElectionComplete());


            ///

            marker_infectionComplete_interact += (double)r.getInfectionCompleteInteractions();
            icc2.addDataPoint(r.getNumAgents(), r.getInfectionCompleteInteractions());

            marker_leaderElectionComplete_interact += (double)r.getLeaderElectionCompleteInteractions();
            icc2.addDataPointLeader(r.getNumAgents(), r.getLeaderElectionCompleteInteractions());


            marker_allElectionComplete_interact += (double)r.getAllElectionCompleteInteractions();
            icc2.addDataPointAll(r.getNumAgents(), r.getAllElectionCompleteInteractions());
        }

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

        //simJSON.writeJSON(tinylog.getDirName(), tinylog.getTimestamp());
    }


    public String toString() {
        return "\n\t Term A: " + termA +
               ";\n\t Term B: " + termB +
               ";\n\t Max interactions: " + maxTimeSteps;
    }


    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS - SIMULATION SETTINGS
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
     * Define the probabilities for interaction and traversal actions. By
     * default, this is 50/50. Probabilities are specified as values between 0
     * and 1. Thus, the values passed must sum to 1.
     *
     * @param interaction Probability of interaction action
     * @param traversal Probability of traversal action.
     */
    public void setActionProbabilites(double interaction, double traversal) {
        actionProbability.put(TimeStep.ACTION_INTERACT, interaction);
        actionProbability.put(TimeStep.ACTION_TRAVERSE, traversal);
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
     * Turn on charts for viewing at the end of the simulation. This is OFF by
     * default and the chart data that is saved to disk is not affected by this
     * setting.
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
