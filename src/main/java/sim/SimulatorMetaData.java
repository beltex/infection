package sim;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import sim.GraphGeneratorSource.GraphType;

import com.google.common.collect.Range;


/**
 * Holds meta data about the overall simulation run. This class is serialized
 * into JSON. This is the file you look at to get a view of how the simulation
 * turned out.
 *
 */
public class SimulatorMetaData {


    ///////////////////////////////////////////////////////////////////////////
    // PRIVATE ATTRIBUTES
    ///////////////////////////////////////////////////////////////////////////


    /**
     * When was the simulation performed?
     */
    private Date date;


    /**
     * How long did the whole simulation take?
     */
    private String duration;


    private GraphType graphType;
    private int numNodes;

    private String interactProbability;
    private String traversalProbability;

    private double accuracy;

    private Range<Integer> numAgents;
    private int termA;
    private int termB;
    private int maxTimeSteps;
    private int runs;

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
        long seconds = TimeUnit.MILLISECONDS.toSeconds(diff) - minutes;

        duration = String.format("%d hour, %d min, %d sec", hours, minutes,
                                                                   seconds);
    }


    public void setDate(Date date) {
        this.date = date;
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
        this.runs = runs;
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
}
