package sim;

import java.util.HashMap;
import java.util.Iterator;

import org.graphstream.algorithm.ConnectedComponents;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.pmw.tinylog.Logger;

import sim.AgentDistribution.Distribution;
import sim.Simulator.NodeSelection;

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
    private NodeSelection nodeSelection;


    /**
     * Which agent distribution method should be used
     */
    private Distribution agentDistribution;


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


    /**
     * Does this graph have a dead end?
     */
    private boolean hasDeadEnd;


    private HashMap<Integer, Range<Double>> actionProbabilitySpread;


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
        if (agentDistribution == Distribution.SINGLE) {
            single = ";\n SINGLE Node ID: " + SINGLE_nodeID;
        }

        return ";\n Number of Agents: " + numAgents +
               ";\n Agent Distribution Method: " + agentDistribution + single;
    }


    /**
     * Does this graph have a dead end? That is, a node with an out degree of
     * 0, no escape.
     *
     * @return True if the graph has one or more dead end(s), false otherwise
     */
    public boolean hasDeadEnd() {
        hasDeadEnd = false;

        for (Node n : this.getNodeSet()) {

            if (n.getOutDegree() == 0) {
                hasDeadEnd = true;
                return hasDeadEnd;
            }
        }

        return hasDeadEnd;
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


    ///////////////////////////////////////////////////////////////////////////
    // PROTECTED METHODS
    ///////////////////////////////////////////////////////////////////////////


    /**
     * The number of agents infected by the leader across the whole graph
     *
     * @return Infection count
     */
    protected int infectionCount() {
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
    protected int electionCompleteCount() {
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
    protected HashMap<String, Range<Double>> agentProbabilitySpread() {
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

//            Logger.trace("{0}; Probability {1}; Offset {2}; Upper {3}", n,
//                                                                        p,
//                                                                        offset,
//                                                                        upper);

            offset = upper;
        }

        return map;
    }


    /**
     * Check if all agents are in a single node which has an out degree of 0,
     * a dead end. Thus, no agent can escape. If this is the case, traversal
     * actions cannot be attempted.
     *
     * @return True if the graph has hit a dead end, false otherwise
     */
    protected boolean agentDeadEnd() {
        Iterator<ExtendedNode> it = this.getNodeIterator();

        while (it.hasNext()) {
            ExtendedNode n = it.next();

            if (n.getAgentCount() == numAgents && n.getOutDegree() == 0) {
                Logger.warn("ALL AGENTS HAVE HIT A DEAD END - NO MORE " +
                            " TRAVERSE ACTION");

                deadEnd_nodeID = n.getId();
                return true;
            }
        }

        return false;
    }


    /**
     * How many agents are currently in the graph? This is used to check if any
     * agents have fallen off the graph.
     *
     * @return Number of agents across the graph currently
     */
    protected int checkNumAgents() {
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
    protected void reset() {
        Iterator<ExtendedNode> it = this.getNodeIterator();

        while (it.hasNext()) {
            it.next().reset();
        }
    }


    ///////////////////////////////////////////////////////////////////////////
    // PROTECTED METHODS - GETTERS
    ///////////////////////////////////////////////////////////////////////////


    /**
     * The number of agents in the whole graph. This is constant throughout the
     * simulation.
     *
     * @return Number of agents in the graph
     */
    protected int getNumAgents() {
        return numAgents;
    }


    protected String getSINGLE_nodeID() {
        return SINGLE_nodeID;
    }


    protected String getRANDOM_SINGLE_nodeID() {
        return RANDOM_SINGLE_nodeID;
    }


    protected Distribution getAgentDistribution() {
        return agentDistribution;
    }


    protected NodeSelection getNodeSelection() {
        return nodeSelection;
    }


    protected HashMap<Integer, Range<Double>> getActionProbabilitySpread() {
        return actionProbabilitySpread;
    }


    protected String getDeadEnd_nodeID() {
        return deadEnd_nodeID;
    }


    protected boolean getHasDeadEnd() {
        return hasDeadEnd;
    }


    ///////////////////////////////////////////////////////////////////////////
    // PROTECTED METHODS - SETTERS
    ///////////////////////////////////////////////////////////////////////////


    protected void setNumAgents(int numAgents) {
        this.numAgents = numAgents;
    }


    protected void setSINGLE_nodeID(String SINGLE_nodeID) {
        this.SINGLE_nodeID = SINGLE_nodeID;
    }


    protected void setRANDOM_SINGLE_nodeID(String rANDOM_SINGLE_nodeID) {
        RANDOM_SINGLE_nodeID = rANDOM_SINGLE_nodeID;
    }


    protected void setAgentDistribution(Distribution agentDistribution) {
        this.agentDistribution = agentDistribution;
    }


    protected void setNodeSelection(NodeSelection mode) {
        this.nodeSelection = mode;
    }


    protected void setActionProbabilitySpread(
            HashMap<Integer, Range<Double>> agentProbabilitySpread) {
        this.actionProbabilitySpread = agentProbabilitySpread;
    }
}
