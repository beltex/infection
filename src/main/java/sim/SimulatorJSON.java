package sim;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Date;

import org.pmw.tinylog.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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


    /**
    * What is the graph type?
    */
    private String graphType;


    /**
    * How many agents
    */
    private int numAgents;
    private int termA;
    private int termB;
    private int maxTimeSteps;
    private int runs;
    private int simVersion;

    // Averages set at the end
    private double infected;
    private double eleComp;
    private double interactions;
    private double traversals;
    private double marker_infectionComplete;
    private double marker_leaderElectionComplete;
    private double marker_allElectionComplete;


    /**
     * Hold data from individual simulation runs
     */
    private ArrayList<SimulatorRun> runData;


    ///////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    ///////////////////////////////////////////////////////////////////////////


    public SimulatorJSON() {
        runData = new ArrayList<SimulatorRun>();
    }


    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ///////////////////////////////////////////////////////////////////////////


    public boolean addRunData(SimulatorRun e) {
        return runData.add(e);
    }


    public String toJSON() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }


    public void writeJSON(String dirName, String timestamp) {
        String jsonName = "data." + timestamp + ".json";
        Path path = FileSystems.getDefault().getPath("logs", dirName, jsonName);

        try {
            Files.write(path, this.toJSON().getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            Logger.error(e);
        }
    }


    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS - GETTERS
    ///////////////////////////////////////////////////////////////////////////


    public ArrayList<SimulatorRun> getRunData() {
        return runData;
    }


    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS - SETTERS
    ///////////////////////////////////////////////////////////////////////////


    public void setDate(Date date) {
        this.date = date;
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


    public void setLogFile(String logFile) {
        this.logFile = logFile;
    }


    public void setGraphType(String graphType) {
        this.graphType = graphType;
    }


    public void setNumAgents(int numAgents) {
        this.numAgents = numAgents;
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


    public void setSimVersion(int simVersion) {
        this.simVersion = simVersion;
    }


    public void setRunData(ArrayList<SimulatorRun> runData) {
        this.runData = runData;
    }
}
