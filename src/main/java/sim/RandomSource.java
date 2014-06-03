package sim;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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


    private int actionsAllowed = 2;


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
    private final String PROVIDER = "SUN";
    private final String ALGORITHM = "SHA1PRNG";


    /**
     * Singleton instance, eager initialization. This is fine since we know we
     * always need an instance.
     */
    private static final RandomSource INSTANCE = new RandomSource();


    ///////////////////////////////////////////////////////////////////////////
    // CONSTRUCTOR
    ///////////////////////////////////////////////////////////////////////////


    private RandomSource() {
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
           e.printStackTrace();

           /*
            * We exit to prevent running on a machine with a different RNG that
            * may give non-standard results. Fail loudly.
            */
           System.exit(-1);
       } catch (NoSuchProviderException e) {
           e.printStackTrace();
           System.exit(-1);
       }
    }


    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ///////////////////////////////////////////////////////////////////////////


    public void init(ExtendedGraph g) {
        this.g = g;
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
     *        // Select a random pair of different agents
        // An agent doesn't interact with itself

     * @param n
     * @return
     */
    public Agent[] nextAgentPair(ExtendedNode n) {
        Agent[] agents = new Agent[2];

        while (true) {
            agents[0] = nextAgent(n);
            agents[1] = nextAgent(n);

            if (agents[0].getAID() != agents[1].getAID()) {
                return agents;
            }

            Logger.trace("Selected same agent twice - {0}, {1}", agents[0],
                                                                 agents[1]);
        }
    }


    /**
     * Pick a random leaving (outgoing) edge from the node n.
     *
     * @param n
     * @return
     */
    public Edge nextLeavingEdge(ExtendedNode n) {
        // Valid edges to choose from
        ArrayList<Edge> list = new ArrayList<Edge>(n.getLeavingEdgeSet());

        // Pick the random edge
        int index = sr.nextInt(list.size());

        // Return the opposite of node n, the outgoing node
        return list.get(index);
    }


    /**
     * Pick a random node in the graph
     *
     * @return ExtendedNode
     */
    public ExtendedNode nextNode() {
        return g.getNode(sr.nextInt(g.getNodeCount()));
    }


    /**
     * Pick a random node in the graph factoring in the constraints of the
     * action.
     *
     * @param action
     * @return
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
     * @param minAgentCount
     * @param flag If the action is a interact or traverse
     * @return
     */
    public ExtendedNode nextNodeWeighted(int action) {
        ExtendedNode n = null;

        // Refuse to pick a node with no agents, hence while loop
        while (true) {
            double r = sr.nextDouble();
            HashMap<String, Range<Double>> map = g.agentSpread();


            // Find the node
            Iterator<String> it_set = map.keySet().iterator();
            while (it_set.hasNext()) {
                String k = it_set.next();

                // If in range
                if (map.get(k).contains(r)) {
                    n = g.getNode(k);
                    Logger.debug("{0} is with range {1}", r, map.get(k));
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
     * Wrapper for nextInt
     *
     * @param range
     * @return
     */
    public int nextAction() {
        // Determine the action. Flip a coin, pick which action, two agents
        // interact, or one moves to another node. 50/50 chance
        return sr.nextInt(actionsAllowed);
    }


    /**
     * Wrapper for nextInt
     *
     * @param range
     * @return
     */
    public int nextInt(int range) {
        return sr.nextInt(range);
    }


    /**
     * Wrapper for nextInt
     *
     * @param range
     * @return
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
