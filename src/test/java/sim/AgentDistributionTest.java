/*
 * AgentDistributionTest.java
 * infection
 *
 * Copyright (C) 2014  beltex <https://github.com/beltex>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package sim;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import sim.AgentDistribution.Distribution;

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

        ad = new AgentDistribution(false);
    }


    /**
     * Test if SINGLE places all the agents in the correct node
     */
    @Test
    public void singleDist_agentCountTest() {
        // Set the graph up
        int numAgents = 1337;
        g.setNumAgents(numAgents);
        g.setAgentDistribution(Distribution.SINGLE);
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
        g.setAgentDistribution(Distribution.RANDOM_SINGLE);

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
        g.setAgentDistribution(Distribution.RANDOM_SPREAD);

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
        g.setAgentDistribution(Distribution.EVEN_SPREAD);

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


    @Test
    public void chainEnds_agentCountTest() {
        int numAgents = 1017;
        RandomSource.getInstance().init(g);
        g.setNumAgents(numAgents);
        g.setAgentDistribution(Distribution.CHAIN_ENDS);

        ad.init(g);
        ad.execute();


        int agentCount = 0;
        ExtendedNode head = g.getNode(0);
        ExtendedNode tail = g.getNode(g.getNodeCount() - 1);
        agentCount = head.getAgentCount() + tail.getAgentCount();

        assertEquals(numAgents, agentCount);
    }
}
