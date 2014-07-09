package sim;

import java.util.ArrayList;
import java.util.Date;

import com.google.common.collect.Range;


public class SimulatorData {


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


    public SimulatorData() {
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
