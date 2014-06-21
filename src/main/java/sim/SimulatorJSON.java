package sim;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Date;

import org.pmw.tinylog.Logger;

import com.google.common.collect.Range;
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
    * How many agents
    */
    private Range<Integer> numAgents;
    private int termA;
    private int termB;
    private int maxTimeSteps;
    private int runs;


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


    /**
     * @param e
     * @return
     */
    public boolean addRunData(SimulatorRun e) {
        return runData.add(e);
    }


    /**
     * @return
     */
    public String toJSON() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }


    /**
     *
     * @param dirName
     * @param timestamp
     */
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


    public void setNumAgents(Range<Integer> numAgents) {
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


    public void setRunData(ArrayList<SimulatorRun> runData) {
        this.runData = runData;
    }
}
