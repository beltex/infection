package sim;

import java.util.ArrayList;
import java.util.Iterator;

import org.pmw.tinylog.Logger;


/**
 * Agent distribution algorithms. Offer multiple ways to spread the agents
 * across the graph before the simulation starts.
 *
 * Agents are given integer IDs from 0 to n - 1, where n is the number of
 * agents. This is the AID.
 *
 */
public class AgentDistribution {


    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC ENUM
    ///////////////////////////////////////////////////////////////////////////


    public enum Distribution {

        /**
         * All agents are placed in a single node which the user sets
         */
        SINGLE,

        /**
         * All agents are placed in a single random node
         */
        RANDOM_SINGLE,

        /**
         * Agents are spread evenly across the graph. If the number of agents is not
         * evenly divisible by the number of nodes, the agent with AID 0 is added to
         * the first node (ID 0), then distribution of the rest of the nodes occurs.
         * This is to make things as deterministic as possible.
         */
        EVEN_SPREAD,

        /**
         * Agents are spread randomly across the graph
         */
        RANDOM_SPREAD,

        /**
         * Agents are split evenly between first and last node of a chain graph.
         * This option can only be used with a chain graph of course.
         *
         * If the number of agents is odd, the tail (last node) will get the
         * remainder.
         */
        CHAIN_ENDS;
    }


    ///////////////////////////////////////////////////////////////////////////
    // PRIVATE ATTRIBUTES
    ///////////////////////////////////////////////////////////////////////////


    private ExtendedGraph g;


    private GraphVis gv = GraphVis.getInstance();


    private RandomSource rs = RandomSource.getInstance();


    private boolean flag_vis;


    ///////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    ///////////////////////////////////////////////////////////////////////////


    public AgentDistribution(boolean flag_vis) {
        this.flag_vis = flag_vis;
    }


    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ///////////////////////////////////////////////////////////////////////////


    public void init(ExtendedGraph g) {
        this.g = g;
        this.g.reset();
        Logger.info("Agent distribution INIT");
    }


    public void execute() {
        /*
         * User sets agent dist algo. Have to check that it has been done, and
         * that its correct.
         */
        Distribution algo = g.getAgentDistribution();

        if (algo == null) {
            Logger.warn("Agent distribution algorithm NOT set or invalid - " +
                        "default to SINGLE with all agents in node 0");

            g.setSINGLE_nodeID(g.getNode(0).getId());
            g.setAgentDistribution(Distribution.SINGLE);
            algo = Distribution.SINGLE;
        }


        Logger.info("{0} distribution of agents - BEGIN", algo);

        switch (algo) {
            case SINGLE:
                single();
                break;
            case RANDOM_SINGLE:
                randomSingle();
                break;
            case EVEN_SPREAD:
                evenSpread();
                break;
            case RANDOM_SPREAD:
                randomSpread();
                break;
            case CHAIN_ENDS:
                chainEnds();
                break;
        }

        Logger.info("{0} distribution of agents - COMPLETE", algo);
    }


    ///////////////////////////////////////////////////////////////////////////
    // PRIVATE METHODS - DISTRIBUTION ALGORITHMS
    ///////////////////////////////////////////////////////////////////////////


    private void single() {
        ExtendedNode n = g.getNode(g.getSINGLE_nodeID());

        // Since the user sets the node id
        if (n == null) {
            Logger.warn("Node ID invalid, either not set or not found. Will "
                        + "choose node at INDEX 0");

            n = g.getNode(0);

            // In case needed for reference later, set it correctly here
            g.setSINGLE_nodeID(n.getId());
        }

        n.setAgents(createAgents());

        if (flag_vis) {
            gv.updateNode(n.getId());
        }
    }


    private void randomSingle() {
        ExtendedNode n = rs.nextNode();
        n.setAgents(createAgents());
        g.setRANDOM_SINGLE_nodeID(n.getId());

        Logger.info("All agents placed in node - {0}", n);

        if (flag_vis) {
            gv.updateNode(n.getId());
        }
    }


    private void randomSpread() {
        int remainingAgents = g.getNumAgents();
        ArrayList<Agent> agents = createAgents();

        // While there are still agents to be allocated
        while (remainingAgents > 0) {
            int allocate = 0;
            ExtendedNode n = rs.nextNode();

            if (remainingAgents == 1) {
                // Due to exclusive range of nextInt()
                allocate = 1;
            }
            else {
                /*
                 * Number of agents to allocate
                 *
                 * Dangerous stuff with this plus one... This is because
                 * nextInt() is exclusive is and so is subList(). So we drop
                 * two values, have to add one back
                 */
                allocate = rs.nextInt(agents.size() + 1);
            }

            ArrayList<Agent> sublist = new ArrayList<Agent>(agents.subList(0, allocate));
            n.addAgents(sublist);
            Logger.trace("Allocated {0} agents to {1}", allocate, n);

            agents.removeAll(sublist);
            remainingAgents = agents.size();

            if (flag_vis) {
                gv.updateNode(n.getId());
            }
        }

        // Log final starting distribution of agents
        Iterator<ExtendedNode> it = g.getNodeIterator();
        while (it.hasNext()) {
            Logger.info(it.next());
        }
    }


    private void evenSpread() {
        int numNodes = g.getNodeCount();
        ArrayList<Agent> agents = createAgents();


        /*
         * Handle possible remainder. Check if number of agents can be evenly
         * spread across the graph
         */
        int remainder = g.getNumAgents() % numNodes;

        if (remainder != 0) {
            Logger.info("Number of agents NOT evenly divisible by the number " +
                        "of nodes; First {0} nodes will have an extra agent " +
                        "added to them", remainder);

            for (int i = 0; i < remainder; i++) {
                ExtendedNode n = g.getNode(i);

                n.addAgent(agents.get(i));
                agents.remove(i);
            }
        }


        /*
         * Now we can evenly distribute the remaining agents
         */
        int allocation = agents.size() / numNodes;
        Logger.info("Adding {0} agents to each node", allocation);

        Iterator<ExtendedNode> it = g.getNodeIterator();

        while (it.hasNext()) {
            ExtendedNode n = it.next();

            // Sublist is inclusive to exclusive
            ArrayList<Agent> sublist = new ArrayList<Agent>(agents.subList(0, allocation));
            n.addAgents(sublist);

            agents.removeAll(sublist);

            if (flag_vis) {
                gv.updateNode(n.getId());
            }
        }
    }


    public void chainEnds() {
        ExtendedNode head = g.getNode(0);
        ExtendedNode tail = g.getNode(g.getNodeCount() - 1);

        ArrayList<Agent> agents = createAgents();

        int alloc = 0;
        int numAgents = g.getNumAgents();

        if ((numAgents % 2) != 0) {
            // Odd number of agents
            alloc = (numAgents - 1) / 2;
        }
        else {
            // Even number of agents
            alloc = numAgents / 2;
        }

        head.setAgents(new ArrayList<Agent>(agents.subList(0, alloc)));

        // Tail takes on the remainder - sublist to end
        tail.setAgents(new ArrayList<Agent>(agents.subList(alloc, numAgents)));
    }


    ///////////////////////////////////////////////////////////////////////////
    // PRIVATE METHODS - HELPER METHODS
    ///////////////////////////////////////////////////////////////////////////


    /**
     * Create the agents. AID's start from 0 and go to n - 1
     *
     * @return Array list of the agent(s)
     */
    private ArrayList<Agent> createAgents() {
        int numAgents = g.getNumAgents();

        ArrayList<Agent> agents = new ArrayList<Agent>(numAgents);

        for (int i = 0; i < numAgents; i++) {
            agents.add(new Agent(i));
        }

        return agents;
    }
}