/*
 * SimulatorMetaData.java
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

import java.util.Date;
import java.util.concurrent.TimeUnit;

import sim.AgentDistribution.Distribution;
import sim.GraphGeneratorSource.GraphType;
import sim.Simulator.NodeSelection;

import com.google.common.collect.Range;


/**
 * Holds meta data about the overall simulation run. This class is serialized
 * into JSON. This is the file you look at to get a view of how the simulation
 * turned out.
 *
 */
@SuppressWarnings("unused")
public class SimulatorMetaData {


    ///////////////////////////////////////////////////////////////////////////
    // PRIVATE ATTRIBUTES
    ///////////////////////////////////////////////////////////////////////////


    /**
     * When was the simulation performed?
     */
    private Date date;


    /**
     * Simulator version (infection).
     */
    private final String version = "0.0.5";


    /**
     * Optional description about the simulation.
     */
    private String description;


    /**
     * How long did the whole simulation take?
     */
    private String duration;


    private GraphType graphType;
    private NodeSelection nodeSelection;
    private Distribution agentDistribution;
    private String agentDistSINGLE_nodeID;
    private int numNodes;

    private String interactProbability;
    private String traversalProbability;

    private Range<Integer> numAgents;
    private int termA;
    private int termB;
    private int maxTimeSteps;
    private int runsPerPopulation;
    private int totalRuns;

    private String avg_infectionLevel;
    private String avg_leaderError;


    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ///////////////////////////////////////////////////////////////////////////


    /**
     * Set the duration of the simulation. Call this at the end, as the duration
     * is computed here.
     *
     * @param start When did in the simulation start? In ms.
     */
    public void setDuration(long start) {
        long end = new Date().getTime();
        long diff = end - start;

        long hours = TimeUnit.MILLISECONDS.toHours(diff);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff) -
                       TimeUnit.HOURS.toMinutes(hours);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(diff) -
                       TimeUnit.HOURS.toSeconds(hours) -
                       TimeUnit.MINUTES.toSeconds(minutes);

        duration = String.format("%d hour, %d min, %d sec", hours, minutes,
                                                                   seconds);
    }


    public void setDate(Date date) {
        this.date = date;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public void setNumNodes(int numNodes) {
        this.numNodes = numNodes;
    }


    public void setInteractProbability(double interactProbability) {
        this.interactProbability = Double.toString(interactProbability * 100) + "%";
    }


    public void setTraversalProbability(double traversalProbability) {
        this.traversalProbability = Double.toString(traversalProbability * 100) + "%";
    }


    public void setTermA(int termA) {
        this.termA = termA;
    }


    public void setTermB(int termB) {
        this.termB = termB;
    }


    public void setMaxTimeSteps(int maxTimeSteps) {
        this.maxTimeSteps = maxTimeSteps;
    }


    public void setRuns(int runs) {
        this.runsPerPopulation = runs;
    }


    public void setAvgInfectionLevel(double level){
        this.avg_infectionLevel = String.format("%.2f", level) + "%";
    }


    public void setAvgLeaderError(double error){
        this.avg_leaderError = String.format("%.2f", error) + "%";
    }


    public void setGraphType(GraphType graphType) {
        this.graphType = graphType;
    }


    public void setTotalRuns(int totalRuns) {
        this.totalRuns = totalRuns;
    }


    public void setNumAgents(Range<Integer> numAgents) {
        this.numAgents = numAgents;
    }


    public void setNodeSelection(NodeSelection nodeSelection) {
        this.nodeSelection = nodeSelection;
    }


    public void setAgentDistribution(Distribution agentDistribution) {
        this.agentDistribution = agentDistribution;
    }


    public void setAgentDistSINGLE_nodeID(String id) {
        this.agentDistSINGLE_nodeID = id;
    }
}
