package test.java.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import main.java.sim.Agent;

import org.junit.Test;


public class AgentTest {


    /**
     * Test if a newly created Agent has its attribute correctly set to defaults
     */
    @Test
    public void initTest() {
        int AID = 7;
        Agent a = new Agent(AID);

        assertEquals(AID, a.getLeaderAID());
        assertEquals(0, a.getConversions());
        assertEquals(0, a.getMetFollowers());
        assertEquals(false, a.isLeader());
        assertEquals(false, a.isElectionComplete());
    }


    /**
     * Test if two agents are equal
     */
    @Test
    public void equalsTest() {
        // Test pass
        Agent a = new Agent(17);
        a.setConversions(2552);
        a.setElectionComplete(true);

        Agent b = new Agent(17);
        b.setConversions(2552);
        b.setElectionComplete(true);

        assertTrue(a.equals(b));


        // Test fail
        b.setLeaderAID(98);
        assertFalse(a.equals(b));
    }
}
