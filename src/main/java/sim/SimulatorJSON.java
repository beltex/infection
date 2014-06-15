package sim;

import java.util.ArrayList;
import java.util.Date;

public class SimulatorJSON {


    ///////////////////////////////////////////////////////////////////////////
    // PRIVATE ATTRIBUTES
    ///////////////////////////////////////////////////////////////////////////


    /**
     * When was the simulation performed?
     */
    private Date date;


    /**
     * What is the name of the log file for this simulation?
     */
    private String logFile;


    /**
     *
     */
    private ExtendedGraph g;


    private String graphType; // Chain, Grid, Fully Connected, Custom (directed, etc)
    private int numAgents;
    private int termA;
    private int termB;
    private int maxTimeSteps;
    private int runs;
    //private int simVersion;

    // Averages set at the end
    private double infected;
    private double eleComp;
    private double interactions;
    private double traversals;
    private double marker_infectionComplete;
    private double marker_leaderElectionComplete;
    private double marker_allElectionComplete;

    private ArrayList<SimulatorRun> runData;


    ///////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    ///////////////////////////////////////////////////////////////////////////


    public SimulatorJSON(int numAgents, int termA, int termB, int maxTimeSteps, int runs) {
        date = new Date();
        runData = new ArrayList<SimulatorRun>();

        this.numAgents = numAgents;
        this.termA = termA;
        this.termB = termB;
        this.maxTimeSteps = maxTimeSteps;
        this.runs = runs;
    }


    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ///////////////////////////////////////////////////////////////////////////


    public ArrayList<SimulatorRun> getRunData() {
        return runData;
    }


    public boolean addRunData(SimulatorRun e) {
        return runData.add(e);
    }


    public void setInfected(double infected) {
        this.infected = infected;
    }


    public void setEleComp(double eleComp) {
        this.eleComp = eleComp;
    }


    public void setInteractions(double interactions) {
        this.interactions = interactions;
    }


    public void setTraversals(double traversals) {
        this.traversals = traversals;
    }


    public void setMarker_infectionComplete(double marker_infectionComplete) {
        this.marker_infectionComplete = marker_infectionComplete;
    }


    public void setMarker_leaderElectionComplete(
            double marker_leaderElectionComplete) {
        this.marker_leaderElectionComplete = marker_leaderElectionComplete;
    }


    public void setMarker_allElectionComplete(double marker_allElectionComplete) {
        this.marker_allElectionComplete = marker_allElectionComplete;
    }
}
