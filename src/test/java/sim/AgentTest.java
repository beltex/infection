/*
 * AgentTest.java
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
