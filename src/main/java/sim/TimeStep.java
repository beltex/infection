package sim;

import org.graphstream.graph.Edge;
import org.pmw.tinylog.Logger;


/**
 * Heart beat of the simulator (time step). That is, handles each
 * iteration of the simulator.
 *
 */
public class TimeStep {


    ///////////////////////////////////////////////////////////////////////////
    // PRIVATE ATTRIBUTES
    ///////////////////////////////////////////////////////////////////////////


    private ExtendedGraph g;


    private GraphVis gv;


    private RandomSource rs;


    private InfectionCountChart icc;


    private InfectionRateChart irc;


    /**
     * The current time step
     */
    private int step;


    private int termA;
    private int termB;
    private int infectionCounter;
    private int actionInteractCounter;
    private int actionTraverseCounter;
    private int marker_infectionComplete;
    private int marker_leaderElectionComplete;
    private int marker_allElectionComplete;

    private boolean f1 = true;
    private boolean f2 = true;


    ///////////////////////////////////////////////////////////////////////////
    // PROTECTED ATTRIBUTES
    ///////////////////////////////////////////////////////////////////////////


    protected int actionsAllowed = 2;
    protected static final int ACTION_INTERACT = 0;
    protected static final int ACTION_TRAVERSE = 1;


    ///////////////////////////////////////////////////////////////////////////
    // CONSTRUCTOR
    ///////////////////////////////////////////////////////////////////////////


    /**
     *
     *
     * @param g Graph to operate on
     * @param termA
     * @param termB
     */
    public TimeStep(ExtendedGraph g, int termA, int termB, int maxSteps) {
        this.g = g;
        this.termA = termA;
        this.termB = termB;

        step = 0;
        infectionCounter = 1;
        actionInteractCounter = 0;
        actionTraverseCounter = 0;
        marker_infectionComplete = -1;
        marker_leaderElectionComplete = -1;
        marker_allElectionComplete = -1;

        icc = new InfectionCountChart(maxSteps);
        irc = new InfectionRateChart(maxSteps);

        gv = GraphVis.getInstance();
        rs = RandomSource.getInstance();

        Logger.debug("TimeStep INIT");
    }


    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ///////////////////////////////////////////////////////////////////////////


    public void step() {
        Logger.debug("Step: {0} BEGIN", step);


        /*
         * Determine the action to perform. Interact or traverse?
         */
        int action = 0;

        if (actionsAllowed > 1 && g.hasDeadEnd()) {
            // Can only do interact now
            actionsAllowed = 1;
        }
        else if (actionsAllowed > 1) {
            // Graph is safe for traverse action
            action = rs.nextAction();
        }

        Logger.debug("ACTION: {0}", action);


        // Pick a random node
        ExtendedNode n = rs.nextNodeWeighted(action);


        /*
         * Execute the action
         */
        if (action == ACTION_INTERACT) {
            actionInteract(n);
        }
        else if (action == ACTION_TRAVERSE) {
            actionTraverse(n);
        }


        // Marker checks
        if (g.infectionCount() == g.getNumAgents() && f1) {
            marker_infectionComplete = step;
            f1 = false;
        }

        if (g.electionCompleteCount() == g.getNumAgents() && f2) {
            marker_allElectionComplete = step;
            f2 = false;
        }


        Logger.debug("Step: {0} COMPLETE", step);
        step++;
    }


    /**
     * Simulation complete, run cleanup
     */
    public void end(boolean plotChart) {
        Logger.info("Simulation COMPLETE");
        postmortem();

        if (plotChart) {
            icc.plot();
            irc.plot();
        }
    }


    ///////////////////////////////////////////////////////////////////////////
    // PRIVATE METHODS - ACTIONS
    ///////////////////////////////////////////////////////////////////////////


    /**
     * Two agents interact. That is, two agents are selected at random, from
     * node n.
     *
     * This is the interact action from the original paper.
     *
     * @param n Node to selected two random agents from
     */
    private void actionInteract(ExtendedNode n) {
        // Pick a random pair of agents in this node
        Agent[] agent_pair = rs.nextAgentPair(n);

        // The two randomly selected agents that will interact
        Agent agent_i = agent_pair[0];
        Agent agent_j = agent_pair[1];

        Logger.debug("Agent i - {0}", agent_i);
        Logger.debug("Agent j - {0}", agent_j);


        /*
         * If either agent believes that the election is complete, spread the
         * word to the other
         */
        if (agent_i.isElectionComplete() || agent_j.isElectionComplete()) {
            agent_i.setElectionComplete(true);
            agent_j.setElectionComplete(true);
            Logger.debug("Election complete from agents: {0}, {1}", agent_i,
                                                                    agent_j);
        }
        else {
            // Compare who the agents believe is the leader
            int diff = agent_i.getLeaderAID() - agent_j.getLeaderAID();

            if (diff > 0) {
                // Agent i infects agent j
                agentInfection(agent_i, agent_j);
                possibleLeader(agent_i);
            }
            else if (diff < 0) {
                // Agent j infects agent i
                agentInfection(agent_j, agent_i);
                possibleLeader(agent_j);
            }
            else {
                Logger.debug("Tie, both infected by the same agent");

                metFollower(agent_i);
                metFollower(agent_j);

                /*
                 * Key change in logic from the original paper. Without this
                 * check here as well, the leader NEVER determines that the
                 * election is complete
                 */
                isElectionComplete(agent_i);
                isElectionComplete(agent_j);
            }
        }
        actionInteractCounter++;

        // Update the view
        gv.updateNode(n.getId());
    }


    /**
     * One agent traverses an edge. That is, a randomly selected agent within
     * node n traverses (travels along) an outgoing edge (of this node).
     *
     * This is the newly added action from the original paper, as result of
     * performing the simulation on a graph.
     *
     * @param n Node that will have an agent leave from it
     */
    private void actionTraverse(ExtendedNode n) {
        // Pick a random agent in the current node
        Agent agent = rs.nextAgent(n);

        // Remove it from the current Node
        n.removeAgent(agent);

        // Pick a random out going edge
        Edge e = rs.nextLeavingEdge(n);

        // Get the outgoing node
        ExtendedNode outGoingNode = e.getOpposite(n);

        // Add agent to this node
        outGoingNode.addAgent(agent);

        actionTraverseCounter++;

        // Update the view
        gv.updateNode(n.getId());
        gv.updateEdge(e, true);
        gv.updateNode(outGoingNode.getId());
        gv.updateEdge(e, false);

        Logger.debug("Agent traversed!");
    }


    ///////////////////////////////////////////////////////////////////////////
    // PRIVATE METHODS - ALGO LOGIC
    ///////////////////////////////////////////////////////////////////////////


    private void agentInfection(Agent infector, Agent infected) {
        Logger.debug("Infector agent: " + infector);

        infected.setLeaderAID(infector.getLeaderAID());
        Logger.debug("Infected agent: " + infected);

        if (infector.getLeaderAID() == g.getNumAgents() - 1) {
            infectionCounter++;

            icc.addDataPoint(step, infectionCounter);
            irc.addDataPoint(step, infectionCounter);
        }
    }


    private boolean possibleLeader(Agent agent) {
        // TODO: Explain meaning of boolean return
        // Check if the agent interacted with anyone with a higher AID
        if (agent.getLeaderAID() == agent.getAID()) {
            agent.setConversions(agent.getConversions() + 1);
            Logger.debug("Possible leader: {0}", agent);

            isElectionComplete(agent);

            return true;
        }

        return false;
    }


    private boolean isElectionComplete(Agent agent) {
        if ((termB + (termA * agent.getConversions())) < agent.getMetFollowers()) {
            agent.setLeader(true);
            agent.setElectionComplete(true);

            if (agent.getAID() == g.getNumAgents() - 1) {
                marker_leaderElectionComplete = step;
            }

            Logger.info("Agent believes election is complete and is the leader: {0}", agent);
            return true;
        }

        return false;
    }


    private boolean metFollower(Agent agent) {
        if (agent.getLeaderAID() == agent.getAID()) {
            agent.setMetFollowers(agent.getMetFollowers() + 1);
            Logger.debug("Agent met a follwer: {0}", agent);
            return true;
        }

        return false;
    }


    ///////////////////////////////////////////////////////////////////////////
    // PRIVATE METHODS - HELPERS
    ///////////////////////////////////////////////////////////////////////////


    private void postmortem() {
        Logger.info("Simulation POSTMORTEM - BEGIN");

        SimRun run = new SimRun();
        run.setInfected(g.infectionCount());
        run.setEleComp(g.electionCompleteCount());
        run.setInteractions(actionInteractCounter);
        run.setTraversals(actionTraverseCounter);
        run.setMarker_infectionComplete(marker_infectionComplete);
        run.setMarker_leaderElectionComplete(marker_leaderElectionComplete);
        run.setMarker_allElectionComplete(marker_allElectionComplete);

        Simulator.getRunData().add(run);

        Logger.info("# of INFECTED agents: " + g.infectionCount() + "/" + g.getNumAgents());
        Logger.info("# of agents that believe election is COMPLETE: " + g.electionCompleteCount() + "/" + g.getNumAgents());
        Logger.info("# of agent INTERACTIONS: " + actionInteractCounter);
        Logger.info("# of agent TRAVERSALS: " + actionTraverseCounter);
        Logger.info("MARKER - Infection Complete Step: " + marker_infectionComplete);
        Logger.info("MARKER - Leader Election Complete Step: " + marker_leaderElectionComplete);
        Logger.info("MARKER - All Election Complete Step: " + marker_allElectionComplete);

        Logger.info("Simulation POSTMORTEM - COMPLETE");
    }
}
