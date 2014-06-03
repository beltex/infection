package junit;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import sim.*;

import org.junit.Before;
import org.junit.Test;


public class AgentDistributionTest {

    private ExtendedGraph g;
    private AgentDistribution ad;

    @Before
    public void preTest() {
        g = new ExtendedGraph("Test");

        // Simple sample graph
        g.addNode("A");
        g.addNode("B");
        g.addNode("C");
        g.addNode("D");
        g.addNode("E");
        g.addNode("F");
        g.addEdge("AB", "A", "B");
        g.addEdge("BC", "B", "C");
        g.addEdge("CD", "C", "D");
        g.addEdge("DE", "D", "E");
        g.addEdge("EA", "E", "A");
        g.addEdge("EF", "E", "F");

        /*
         * Have to do this even though we don't need it. A short coming of the
         * design. This is a TODO list item
         */
        GraphVis.getInstance().init(g);

        ad = new AgentDistribution();
    }


    /**
     * Test if SINGLE places all the agents in the correct node
     */
    @Test
    public void singleDist_agentCountTest() {
        // Set the graph up
        int numAgents = 1337;
        g.setNumAgents(numAgents);
        g.setAgentDistribution(AgentDistribution.SINGLE);
        g.setSINGLE_nodeID("E");

        // Init RandomSource and AgentDistribution
        RandomSource.getInstance().init(g);

        ad.init(g);
        ad.execute();

        ExtendedNode n = g.getNode(g.getSINGLE_nodeID());

        assertEquals(numAgents, n.getAgentCount());
    }


    /**
     * Test if RANDOM_SINGLE places all the agents in a single node
     */
    @Test
    public void randomSingleDist_agentCountTest() {
        // Set the graph up
        int numAgents = 117;
        g.setNumAgents(numAgents);
        g.setAgentDistribution(AgentDistribution.RANDOM_SINGLE);

        // Init RandomSource and AgentDistribution
        RandomSource.getInstance().init(g);

        ad.init(g);
        ad.execute();

        ExtendedNode n = g.getNode(g.getRANDOM_SINGLE_nodeID());

        assertEquals(numAgents, n.getAgentCount());
    }


    /**
     * Test if RANDOM_SPREAD does not have any agents fall off the graph as it
     * tries to distribute them
     */
    @Test
    public void randomSpread_agentCountTest() {
        // Set the graph up
        int numAgents = 2552;
        g.setNumAgents(numAgents);
        g.setAgentDistribution(AgentDistribution.RANDOM_SPREAD);

        // Init RandomSource and AgentDistribution
        RandomSource.getInstance().init(g);

        ad.init(g);
        ad.execute();

        // Count the number of agents across the graph
        int agentCount = 0;
        Iterator<ExtendedNode> it = g.getNodeIterator();

        while (it.hasNext()) {
            ExtendedNode n = it.next();
            agentCount += n.getAgentCount();
        }

        assertEquals(numAgents, agentCount);
    }


    /**
     * Test if EVEN_SPREAD does not have any agents fall off the graph as it
     * tries to distribute them
     */
    @Test
    public void evenSpread_agentCountTest() {
        int numAgents = 8573;
        RandomSource.getInstance().init(g);
        g.setNumAgents(numAgents);
        g.setAgentDistribution(AgentDistribution.EVEN_SPREAD);

        ad.init(g);
        ad.execute();

        int agentCount = 0;
        Iterator<ExtendedNode> it = g.getNodeIterator();

        // For each node
        while (it.hasNext()) {
            ExtendedNode n = it.next();
            agentCount += n.getAgentCount();
        }

        assertEquals(numAgents, agentCount);
    }
}
