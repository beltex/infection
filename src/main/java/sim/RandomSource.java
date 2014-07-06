package sim;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

import org.graphstream.graph.Edge;
import org.pmw.tinylog.Logger;

import com.google.common.collect.Range;


/**
 * Handles everything related to random generation, be it selecting a random
 * node, pair of agents, etc. Really a wrapper class for SecureRandom so that
 * all random choices are consistent across the code.
 *
 *
 * "Anyone who considers arithmetical methods of producing random digits is, of
 *  course, in a state of sin."
 *
 *  - John Von Neumann, 1951
 */
public class RandomSource {


    ///////////////////////////////////////////////////////////////////////////
    // PRIVATE ATTRIBUTES
    ///////////////////////////////////////////////////////////////////////////


    private ExtendedGraph g;


    private SecureRandom sr;


    /*
     * The algorithm and provider for SecureRandom. We set
     * this make things as consistent as possible.
     *
     * From docs
     *
     * "The name of the pseudo-random number generation (PRNG) algorithm
     * supplied by the SUN provider. This algorithm uses SHA-1 as the
     * foundation of the PRNG. It computes the SHA-1 hash over a true-random
     * seed value concatenated with a 64-bit counter which is incremented by 1
     * for each operation. From the 160-bit SHA-1 output, only 64 bits are used."
     *
     * http://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#SecureRandom
     * http://docs.oracle.com/javase/7/docs/technotes/guides/security/SunProviders.html#SUNProvider
     */
    private static final String PROVIDER = "SUN";
    private static final String ALGORITHM = "SHA1PRNG";


    /**
     * Singleton instance, eager initialization. This is fine since we know we
     * always need an instance.
     */
    private static final RandomSource INSTANCE = new RandomSource();


    private final int NUM_ACTIONS = 2;


    private int numNodes;


    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ///////////////////////////////////////////////////////////////////////////


    public void init(ExtendedGraph g) {
        this.g = g;
        this.numNodes = g.getNodeCount();

        try {
            sr = SecureRandom.getInstance(ALGORITHM, PROVIDER);

            /*
             * Force SecureRandom to seed itself.
             *
             * From Javadoc:
             *
             * "The returned SecureRandom object has not been seeded. To seed the
             *  returned object, call the setSeed method. If setSeed is not
             *  called, the first call to nextBytes will force the SecureRandom
             *  object to seed itself."
             */
            byte[] bytes = new byte[64];
            sr.nextBytes(bytes);
        } catch (NoSuchAlgorithmException e) {
            Logger.error(e);

            /*
             * We exit to prevent running on a machine with a different RNG that
             * may give non-standard results. Fail loudly.
             */
            System.exit(-1);
        } catch (NoSuchProviderException e) {
            Logger.error(e);
            System.exit(-1);
        }
    }


    /**
     * Pick a random agent in the node n
     *
     * @param n Node from which the agent should be picked from
     * @return Randomly chosen index of an agent
     */
    public int nextAgentIndex(ExtendedNode n) {
        return sr.nextInt(n.getAgentCount());
    }

    /**
     * Pick a random agent in the node n
     *
     * @param n Node from which the agent should be picked from
     * @return Randomly chosen Agent object
     */
    public Agent nextAgent(ExtendedNode n) {
        return n.getAgent(sr.nextInt(n.getAgentCount()));
    }


    /**
     * Get a pair of random agents from the given node. Agents are different.
     * Method assumes that node has 2 or more agents.
     *
     * @param n Node to choose agents from
     * @return Array holding two different agents
     */
    public Agent[] nextAgentPair(ExtendedNode n) {
        Agent agent_i;
        Agent agent_j;
        Agent[] agents = new Agent[2];
        int agentCount = n.getAgentCount();

        while (true) {
            agent_i = n.getAgent(sr.nextInt(agentCount));
            agent_j = n.getAgent(sr.nextInt(agentCount));

            if (agent_i.getAID() != agent_j.getAID()) {
                agents[0] = agent_i;
                agents[1] = agent_j;

                return agents;
            }
        }
    }


    /**
     * Pick a random leaving (outgoing) edge from the node n.
     *
     * @param n Node to choose leaving edge from
     * @return Edge Outgoing edge
     */
    public Edge nextLeavingEdge(ExtendedNode n) {
        // Valid edges to choose from

        // Pick the random edge
//        if (n.getOutDegree() != n.getLeavingEdgeSet().size()) {
//            System.out.println("NOT EQUAL");
//            System.exit(-1);
//        }

        // Return the opposite of node n, the outgoing node

        return n.getLeavingEdge(sr.nextInt(n.getOutDegree()));
    }


    /**
     * Pick a random node in the graph
     *
     * @return ExtendedNode Random node
     */
    public ExtendedNode nextNode() {
        return g.getNode(sr.nextInt(numNodes));
    }


    /**
     * Pick a random node in the graph factoring in the constraints of the
     * action.
     *
     * @param action Which action is this node going to be used for? Interact
     *               or traverse?
     * @return ExtendedNode Random node
     */
    public ExtendedNode nextNode(int action) {
        while (true) {
            ExtendedNode n = nextNode();

            if (action == TimeStep.ACTION_INTERACT && n.getAgentCount() >= 2) {
                Logger.debug("ACTION_INTERACT: Node selected: " + n);
                return n;
            }
            else if (action == TimeStep.ACTION_TRAVERSE && n.getAgentCount() >= 1
                                                        && n.getOutDegree() >= 1) {
                Logger.debug("ACTION_TRAVERSE: Node selected: " + n);
                return n;
            }
        }
    }


    /**
     * Choose node based on the number of agents. So more agents in a node,
     * higher probability, In proportion to size.
     *
     * Make sure that the selected node has at least one agent, otherwise a
     * time-step gets wasted with no action
     *
     * @param action Which action is this node going to be used for? Interact
     *               or traverse?
     * @return ExtendedNode Random node
     */
    public ExtendedNode nextNodeWeighted(int action) {
        ExtendedNode n = null;

        // Refuse to pick a node with no agents, hence while loop
        while (true) {
            double r = sr.nextDouble();
            HashMap<String, Range<Double>> map = g.agentProbabilitySpread();


            // Find the node
            for (Entry<String, Range<Double>> entry: map.entrySet()) {

                if (entry.getValue().contains(r)) {
                    n = g.getNode(entry.getKey());
                    break;
                }

            }


            if (action == TimeStep.ACTION_INTERACT && n.getAgentCount() >= 2) {
                Logger.debug("ACTION_INTERACT: Node selected: " + n);
                return n;
            }
            else if (action == TimeStep.ACTION_TRAVERSE && n.getAgentCount() >= 1
                                                        && n.getOutDegree() >= 1) {
                Logger.debug("ACTION_TRAVERSE: Node selected: " + n);
                return n;
            }

            // This shouldn't even happen because of the weighted probability
            Logger.trace("Selected node has no agents - {0}", n);
        }
    }


    /**
     * Pick a random action to perform, interact or traverse.
     *
     * @return int Random action
     */
    public int nextAction() {
        // Determine the action. Flip a coin, pick which action, two agents
        // interact, or one moves to another node. 50/50 chance
        return sr.nextInt(NUM_ACTIONS);
    }


    /**
     * Pick a random action to perform, interact or traverse, based on the
     * action probability spread.
     *
     * @return int Random action
     */
    public int nextActionWeighted() {
        double r = sr.nextDouble();
        HashMap<Integer, Range<Double>> map = g.getActionProbabilitySpread();

        // Find the action in which the random double r falls within
        if (map.get(TimeStep.ACTION_INTERACT).contains(r)) {
            return TimeStep.ACTION_INTERACT;
        }
        else {
            return TimeStep.ACTION_TRAVERSE;
        }
    }


    /**
     * Wrapper for nextInt
     *
     * @see java.util.Random#nextInt()
     */
    public int nextInt(int range) {
        return sr.nextInt(range);
    }


    /**
     * Wrapper for nextInt
     *
     * @see java.util.Random#nextDouble()
     */
    public double nextDouble() {
        return sr.nextDouble();
    }


    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS - GETTERS
    ///////////////////////////////////////////////////////////////////////////


    public static RandomSource getInstance() {
        return INSTANCE;
    }
}
