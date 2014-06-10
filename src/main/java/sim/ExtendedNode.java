package sim;

import java.util.ArrayList;

import org.graphstream.graph.implementations.AbstractGraph;
import org.graphstream.graph.implementations.SingleNode;


/**
 * Node used with ExtendedGraph
 *
 */
public class ExtendedNode extends SingleNode {


    ///////////////////////////////////////////////////////////////////////////
    // PRIVATE ATTRIBUTES
    ///////////////////////////////////////////////////////////////////////////


    /**
     * The graph to which this node belongs to
     */
    private ExtendedGraph graph;


    /**
     * List of agents currently in this node
     */
    private ArrayList<Agent> agents;


    ///////////////////////////////////////////////////////////////////////////
    // CONSTRUCTOR
    ///////////////////////////////////////////////////////////////////////////


    protected ExtendedNode(AbstractGraph graph, String id) {
        super(graph, id);

        // Maintain a casted reference to the graph which this node belongs to.
        // Same as: (ExtendedGraph) super.graph
        this.graph = (ExtendedGraph) graph;

        agents = new ArrayList<Agent>();
    }


    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ///////////////////////////////////////////////////////////////////////////


    /**
     * Get the number of agents infected by the leader in the this node
     *
     * @return Number of agents infected
     */
    public int infectionCount() {
        int count = 0;
        int leaderAID = graph.getNumAgents() - 1;

        for (Agent a : agents) {
            if (a.getLeaderAID() == leaderAID) {
                count++;
            }
        }

        return count;
    }


    /**
     * Get the number of agents that believe leader election is complete in
     * this node. That is, have the electionComplete flag set to true.
     *
     * @return Number of agents that believe election is complete
     */
    public int electionCompleteCount() {
        int count = 0;

        for (Agent a : agents) {
            if (a.isElectionComplete()) {
                count++;
            }
        }

        return count;
    }


    /**
     * Check if this node contains the leader
     *
     * @return True if it does contain the leader, false otherwise
     */
    public boolean containsLeader() {
        int leaderAID = graph.getNumAgents() - 1;

        for (Agent a : agents) {
            if (a.getAID() == leaderAID) {
                return true;
            }
        }

        return false;
    }


    /**
     * Clear the node (reset). This is done before a new run of a simulation.
     */
    public void reset() {
        agents = new ArrayList<Agent>();
    }


    public String toString() {
        return "ID: " + super.id +
                "; Agent count: " + agents.size() +
                "; Infection count: " + infectionCount() +
                "; Election complete count: " + electionCompleteCount();
    }


    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS - GETTERS
    ///////////////////////////////////////////////////////////////////////////


    public Agent getAgent(int index) {
        return agents.get(index);
    }


    public int getAgentCount() {
        return agents.size();
    }


    public ArrayList<Agent> getAgents() {
        return agents;
    }


    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS - SETTERS & MUTATORS
    ///////////////////////////////////////////////////////////////////////////


    /**
     * Add an agent to this node
     *
     * @param agent Agent to be added
     * @return True if the agent was added successfully to the node, false
     * 		   otherwise
     */
    public Boolean addAgent(Agent agent) {
        return agents.add(agent);
    }


    /**
     * Add a list of agents to the node. This is an additive operation, not to
     * be confused with the setAgents() method
     *
     *
     * @param agentList List of agents to be added
     * @return True if the agents were added successfully to the node, false
     * 		   otherwise
     */
    public Boolean addAgents(ArrayList<Agent> agentList) {
        return agents.addAll(agentList);
    }


    /**
     * Remove an agent from the node
     *
     * @param agent Agent to be removed
     * @return True if the agent was removed successfully from the node, false
     * 		   otherwise
     */
    public Boolean removeAgent(Agent agent) {
        return agents.remove(agent);
    }


    /**
     * Set the list of agents that this node contains, as in replace completely.
     * Not to be confused with the addAgents() method
     *
     * @param agents List of agents to be set
     */
    public void setAgents(ArrayList<Agent> agents) {
        this.agents = agents;
    }
}