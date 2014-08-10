/*
 * SimulatorRun.java
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

import java.util.LinkedHashMap;


/**
 * Hold all data from a single simulation run
 *
 */
public class SimulatorRun {


    ///////////////////////////////////////////////////////////////////////////
    // PRIVATE ATTRIBUTES
    ///////////////////////////////////////////////////////////////////////////


    /**
     * How many agents were there in this simulation?
     */
    private int numAgents;


    /**
     * How many agents were infected?
     */
    private int infections;


    /**
     * How many agents believed that election was complete?
     */
    private int electionCompleteCount;


    /**
     * How many interaction actions occurred?
     */
    private int interactions;


    /**
     * How many traversal actions occurred?
     */
    private int traversals;


    /**
     * At what time step did all agents became infected?
     */
    private int infectionCompleteStep;


    /**
     * At what time step did the leader believe infection was complete?
     */
    private int leaderElectionCompleteStep;


    /**
     * At what time step did all agents believe election was complete?
     */
    private int allElectionCompleteStep;


    /**
     * How many interactions had occurred at the time step in which all agents
     * became infected?
     */
    private int infectionCompleteInteractions;


    /**
     * How many interactions had occurred at the time step in which the leader
     * believed election was complete?
     */
    private int leaderElectionCompleteInteractions;


    /**
     * How many interactions had occurred at the time step in which the all
     * agents believed election was complete?
     */
    private int allElectionCompleteInteractions;


    /**
     * XY pairs - step, infection level for simulation run. This can be used
     * for charts.
     */
    private LinkedHashMap<Integer, Integer> stepInfectionsMap;


    ///////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    ///////////////////////////////////////////////////////////////////////////


    public SimulatorRun() {
        stepInfectionsMap = new LinkedHashMap<Integer, Integer>();

        infectionCompleteStep = 0;
        leaderElectionCompleteStep = 0;
        allElectionCompleteStep = 0;
        infectionCompleteInteractions = 0;
        leaderElectionCompleteInteractions = 0;
        allElectionCompleteInteractions = 0;
    }


    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ///////////////////////////////////////////////////////////////////////////


    public void addInfection(int step, int infectionCount) {
        stepInfectionsMap.put(step, infectionCount);
    }


    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS - GETTERS
    ///////////////////////////////////////////////////////////////////////////


    public int getNumAgents() {
        return numAgents;
    }


    public int getInfections() {
        return infections;
    }


    public int getElectionCompleteCount() {
        return electionCompleteCount;
    }


    public int getInteractions() {
        return interactions;
    }


    public int getTraversals() {
        return traversals;
    }


    public int getInfectionCompleteStep() {
        return infectionCompleteStep;
    }


    public int getLeaderElectionCompleteStep() {
        return leaderElectionCompleteStep;
    }


    public int getAllElectionCompleteStep() {
        return allElectionCompleteStep;
    }


    public int getInfectionCompleteInteractions() {
        return infectionCompleteInteractions;
    }


    public int getLeaderElectionCompleteInteractions() {
        return leaderElectionCompleteInteractions;
    }


    public int getAllElectionCompleteInteractions() {
        return allElectionCompleteInteractions;
    }


    public LinkedHashMap<Integer, Integer> getStepInfectionsMap() {
        return stepInfectionsMap;
    }


    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS - SETTERS
    ///////////////////////////////////////////////////////////////////////////


    public void setNumAgents(int numAgents) {
        this.numAgents = numAgents;
    }


    public void setInfections(int infections) {
        this.infections = infections;
    }


    public void setElectionCompleteCount(int electionCompleteCount) {
        this.electionCompleteCount = electionCompleteCount;
    }


    public void setInteractions(int interactions) {
        this.interactions = interactions;
    }


    public void setTraversals(int traversals) {
        this.traversals = traversals;
    }


    public void setInfectionCompleteStep(int infectionCompleteStep) {
        this.infectionCompleteStep = infectionCompleteStep;
    }


    public void setLeaderElectionCompleteStep(int leaderElectionCompleteStep) {
        this.leaderElectionCompleteStep = leaderElectionCompleteStep;
    }


    public void setAllElectionCompleteStep(int allElectionCompleteStep) {
        this.allElectionCompleteStep = allElectionCompleteStep;
    }


    public void setInfectionCompleteInteractions(int infectionCompleteInteractions) {
        this.infectionCompleteInteractions = infectionCompleteInteractions;
    }


    public void setLeaderElectionCompleteInteractions(
            int leaderElectionCompleteInteractions) {
        this.leaderElectionCompleteInteractions = leaderElectionCompleteInteractions;
    }


    public void setAllElectionCompleteInteractions(
            int allElectionCompleteInteractions) {
        this.allElectionCompleteInteractions = allElectionCompleteInteractions;
    }
}
