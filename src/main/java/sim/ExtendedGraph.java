package sim;

import java.util.HashMap;
import java.util.Iterator;

import org.graphstream.algorithm.ConnectedComponents;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.pmw.tinylog.Logger;

import com.google.common.collect.Range;


/**
 * Extension of SingleGraph with added attributes and methods. Done for
 * convenience and simplicity. No need to continually call set and get
 * attribute with casts from Graph class.
 *
 */
public class ExtendedGraph extends SingleGraph {


    ///////////////////////////////////////////////////////////////////////////
    // PRIVATE ATTRIBUTES
    ///////////////////////////////////////////////////////////////////////////


    /**
     * Number of agents in the whole graph (static)
     */
    private int numAgents;


    /**
     * Which node selection method should be used?
     */
    private int nodeSelection;


    /**
     * Which agent distribution method should be used
     */
    private int agentDistribution;


    /**
     * For SINGLE agent distribution. Which node should have all agents. See
     * AgentDistribution class for more.
     */
    private String SINGLE_nodeID;


    /**
     * ID of the node select by RANDOM_SINGLE agent distribution. This is for
     * internal reference. See AgentDistribution class for more.
     */
    private String RANDOM_SINGLE_nodeID;


    /**
     * ID of the node that all agents have hit a dead end in.
     */
    private String deadEnd_nodeID;


    private HashMap<Integer, Range<Double>> actionProbabilitySpread;


    private boolean hasDeadEnd;


    ///////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    ///////////////////////////////////////////////////////////////////////////


    public ExtendedGraph(String id) {
        super(id);

        // Set custom node type for graph
        setNodeFactory(new ExtendedNodeFactory());
    }


    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ///////////////////////////////////////////////////////////////////////////


    public String toString() {
        // Info only relevant if SINGLE agent distribution being used
        String single = "";
        if (agentDistribution == AgentDistribution.SINGLE) {
            single = ";\n SINGLE Node ID: " + SINGLE_nodeID;
        }

        return ";\n Number of Agents: " + numAgents +
               ";\n Agent Distribution Method: " +
                  AgentDistribution.map.get(agentDistribution) + single;
    }


    /**
     * The number of agents infected by the leader across the whole graph
     *
     * @return Infection count
     */
    public int infectionCount() {
        int count = 0;
        Iterator<ExtendedNode> it = this.getNodeIterator();

        while (it.hasNext()) {
            ExtendedNode n = it.next();
            count += n.infectionCount();
        }

        return count;
    }


    /**
     * The number of agents that believe leader election is complete across the
     * whole graph
     *
     * @return Count of agents that believe election is complete
     */
    public int electionCompleteCount() {
        int count = 0;
        Iterator<ExtendedNode> it = this.getNodeIterator();

        while (it.hasNext()) {
            ExtendedNode n = it.next();
            count += n.electionCompleteCount();
        }

        return count;
    }


    /**
     * Returns the probability spread across the graph. This is used for making
     * for a weighted random node selection.
     *
     *
     * @return HashMap with each node's ID and it's probability given as a
     *         Range object
     */
    public HashMap<String, Range<Double>> agentProbabilitySpread() {
        double offset = 0.0;
        HashMap<String, Range<Double>> map = new HashMap<String, Range<Double>>();


        Iterator<ExtendedNode> it = this.getNodeIterator();
        while (it.hasNext()) {
            ExtendedNode n = it.next();

            // Probability of the node being selected
            double p = (double) n.getAgentCount() / (double) numAgents;

            // Upper bound for the range of the this node
            double upper = offset + p;

            // https://code.google.com/p/guava-libraries/wiki/RangesExplained
            map.put(n.getId(), Range.closedOpen(offset, upper));

            Logger.trace("{0}; Probability {1}; Offset {2}; Upper {3}", n,
                                                                        p,
                                                                        offset,
                                                                        upper);

            offset = upper;
        }

        return map;
    }


    /**
     * Does this graph have a dead end? That is, a node with an out degree of
     * 0, no escape.
     *
     * @return True if the graph has one or more dead end(s), false otherwise
     */
    public boolean hasDeadEnd() {
        for (Node n : this.getNodeSet()) {

            if (n.getOutDegree() == 0) {
                hasDeadEnd = true;
            }
        }

        hasDeadEnd = false;
        return hasDeadEnd;
    }


    /**
     * Check if all agents are in a single node which has an out degree of 0,
     * a dead end. Thus, no agent can escape. If this is the case, traversal
     * actions cannot be attempted.
     *
     * @return True if the graph has hit a dead end, false otherwise
     */
    public boolean agentDeadEnd() {
        Iterator<ExtendedNode> it = this.getNodeIterator();

        while (it.hasNext()) {
            ExtendedNode n = it.next();

            if (n.getAgentCount() == numAgents && n.getOutDegree() == 0) {
                Logger.warn("ALL AGENTS HAVE HIT A DEAD END - NO MORE TRAVERSE ACTION");

                deadEnd_nodeID = n.getId();
                return true;
            }
        }

        return false;
    }


    /**
     * Is the graph connected?
     *
     * @return True if the graph is connected, false otherwise
     */
    public boolean isConnected() {
        ConnectedComponents cc = new ConnectedComponents();
        cc.init(this);

        if (cc.getConnectedComponentsCount() == 1) {
            return true;
        }

        return false;
    }


    /**
     * Is the graph directed?
     *
     * @return True if all edges are directed, false otherwise.
     */
    public boolean isDirected() {
        for (Edge e: this.getEdgeSet()) {
            if (!e.isDirected()) {
                return false;
            }
        }

        return true;
    }


    /**
     * How many agents are currently in the graph? This is used to check if any
     * agents have fallen off the graph.
     *
     * @return Number of agents across the graph currently
     */
    public int checkNumAgents() {
        int count = 0;
        Iterator<ExtendedNode> it = this.getNodeIterator();

        while (it.hasNext()) {
            ExtendedNode n = it.next();
            count += n.getAgentCount();
        }

        return count;
    }


    /**
     * Clear the graph (reset it).
     *
     */
    public void reset() {
        Iterator<ExtendedNode> it = this.getNodeIterator();

        while (it.hasNext()) {
            it.next().reset();
        }
    }


    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS - GETTERS
    ///////////////////////////////////////////////////////////////////////////


    /**
     * The number of agents in the whole graph. This is constant throughout the
     * simulation.
     *
     * @return Number of agents in the graph
     */
    public int getNumAgents() {
        return numAgents;
    }


    public String getSINGLE_nodeID() {
        return SINGLE_nodeID;
    }


    public String getRANDOM_SINGLE_nodeID() {
        return RANDOM_SINGLE_nodeID;
    }


    public int getAgentDistribution() {
        return agentDistribution;
    }


    public int getNodeSelection() {
        return nodeSelection;
    }


    public HashMap<Integer, Range<Double>> getActionProbabilitySpread() {
        return actionProbabilitySpread;
    }


    public String getDeadEnd_nodeID() {
        return deadEnd_nodeID;
    }


    public boolean getHasDeadEnd() {
        return hasDeadEnd;
    }


    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS - SETTERS
    ///////////////////////////////////////////////////////////////////////////


    public void setNumAgents(int numAgents) {
        this.numAgents = numAgents;
    }


    public void setSINGLE_nodeID(String SINGLE_nodeID) {
        this.SINGLE_nodeID = SINGLE_nodeID;
    }


    public void setRANDOM_SINGLE_nodeID(String rANDOM_SINGLE_nodeID) {
        RANDOM_SINGLE_nodeID = rANDOM_SINGLE_nodeID;
    }


    public void setAgentDistribution(int agentDistribution) {
        this.agentDistribution = agentDistribution;
    }


    public void setNodeSelection(int nodeSelection) {
        this.nodeSelection = nodeSelection;
    }


    public void setActionProbabilitySpread(
            HashMap<Integer, Range<Double>> agentProbabilitySpread) {
        this.actionProbabilitySpread = agentProbabilitySpread;
    }
}
