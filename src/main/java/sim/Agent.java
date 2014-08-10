/*
 * Agent.java
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

/**
 * Representation of an autonomous agent
 *
 */
public class Agent {


    ///////////////////////////////////////////////////////////////////////////
    // PRIVATE ATTRIBUTES
    ///////////////////////////////////////////////////////////////////////////


    /**
     * Agent unique identifier
     */
    private int AID;


    /**
     * Who do I believe is the leader? Myself by default (starting)
     */
    private int leaderAID;


    /**
     * Number of agents THIS agent has converted (infected) with it's leaderAID.
     * XOR with metFollowers
     */
    private int conversions;


    /**
     * How many agents has THIS agent interacted with who are following the
     * same leader. XOR with conversions
     */
    private int metFollowers;


    /**
     * Am I the leader? False by default (starting). True if the election
     * completion estimate equation evaluates to true
     */
    private boolean isLeader;


    /**
     * Is the election complete? False by default (starting)
     */
    private boolean electionComplete;


    ///////////////////////////////////////////////////////////////////////////
    // CONSTRUCTOR
    ///////////////////////////////////////////////////////////////////////////


    /**
     * Create an Agent. Sets leader ID to its own AID, conversion and met
     * follower counters to 0, and finally "is leader" and "election complete"
     * flags to false.
     *
     * @param AID Unique Agent ID (must be >= 0 & <= numAgents - 1)
     */
    public Agent(int AID) {
        this.AID = AID;
        leaderAID = AID;

        conversions = 0;
        metFollowers = 0;

        isLeader = false;
        electionComplete = false;
    }


    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ///////////////////////////////////////////////////////////////////////////


//    public String toString() {
//        return "\n\t AID: " + AID +
//                ";\n\t Leader AID:  " + leaderAID +
//                ";\n\t Conversions: " + conversions +
//                ";\n\t Met followers: " + metFollowers +
//                ";\n\t Is leader: " + isLeader +
//                ";\n\t Election Complete: " + electionComplete;
//    }


    /**
     * Determine if this agent is equal to another
     *
     * @param agent Agent to compare with
     * @return True if all attributes equal, false otherwise
     */
    public Boolean equals(Agent agent) {
        if (agent != null &&
            AID == agent.AID &&
            leaderAID == agent.leaderAID &&
            conversions == agent.conversions &&
            metFollowers == agent.metFollowers &&
            isLeader == agent.isLeader &&
            electionComplete == agent.electionComplete) {

            return true;
        }

        return false;
    }


    /**
     * Agent converted (infected) another. Increment the conversions counter
     */
    public void converted() {
        conversions++;
    }


    /**
     * Agent met a follower. Increment the met followers counter
     */
    public void metFollower() {
        metFollowers++;
    }


    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS - GETTERS
    ///////////////////////////////////////////////////////////////////////////


    public int getAID() {
        return AID;
    }


    public int getLeaderAID() {
        return leaderAID;
    }


    public int getConversions() {
        return conversions;
    }


    public int getMetFollowers() {
        return metFollowers;
    }


    public boolean isLeader() {
        return isLeader;
    }


    public boolean isElectionComplete() {
        return electionComplete;
    }


    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS - SETTERS
    ///////////////////////////////////////////////////////////////////////////


    public void setAID(int aID) {
        AID = aID;
    }


    public void setLeaderAID(int leaderAID) {
        this.leaderAID = leaderAID;
    }


    public void setConversions(int conversions) {
        this.conversions = conversions;
    }


    public void setMetFollowers(int metFollowers) {
        this.metFollowers = metFollowers;
    }


    public void setLeader(boolean isLeader) {
        this.isLeader = isLeader;
    }


    public void setElectionComplete(boolean electionComplete) {
        this.electionComplete = electionComplete;
    }
}