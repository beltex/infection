package sim;


/**
 * Hold all data from a single simulation run
 *
 */
public class SimulatorRun {


    ///////////////////////////////////////////////////////////////////////////
    // PRIVATE ATTRIBUTES
    ///////////////////////////////////////////////////////////////////////////


    /**
     * How many agents were infected?
     */
    private int infectionCount;


    /**
     * How many agents believed that election was complete?
     */
    private int electionCompleteCount;


    /**
     * How many interaction actions occurred?
     */
    private int interactions;


    /**
     * How many traversal actions occured?
     */
    private int traversals;


    /**
     * At what time step did all agents became infected?
     */
    private int marker_infectionComplete;


    /**
     * At what time step did the leader believe infection was complete?
     */
    private int marker_leaderElectionComplete;


    /**
     * At what time step did all agents believe election was complete?
     */
    private int marker_allElectionComplete;


    /**
     * How many interactions had occurred at the time step in which all agents
     * became infected?
     */
    private int marker_infectionComplete_interact;


    /**
     * How many interactions had occurred at the time step in which the leader
     * believed election was complete?
     */
    private int marker_leaderElectionComplete_interact;


    /**
     * How many interactions had occurred at the time step in which the all
     * agents believed election was complete?
     */
    private int marker_allElectionComplete_interact;


    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS - GETTERS
    ///////////////////////////////////////////////////////////////////////////


    public SimulatorRun() {

    }


    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS - GETTERS
    ///////////////////////////////////////////////////////////////////////////


    public int getMarker_infectionComplete_interact() {
        return marker_infectionComplete_interact;
    }


    public int getMarker_leaderElectionComplete_interact() {
        return marker_leaderElectionComplete_interact;
    }


    public int getMarker_allElectionComplete_interact() {
        return marker_allElectionComplete_interact;
    }


    public void setMarker_infectionComplete_interact(
            int marker_infectionComplete_interact) {
        this.marker_infectionComplete_interact = marker_infectionComplete_interact;
    }


    public void setMarker_leaderElectionComplete_interact(
            int marker_leaderElectionComplete_interact) {
        this.marker_leaderElectionComplete_interact = marker_leaderElectionComplete_interact;
    }


    public void setMarker_allElectionComplete_interact(
            int marker_allElectionComplete_interact) {
        this.marker_allElectionComplete_interact = marker_allElectionComplete_interact;
    }


    public int getInfected() {
        return infectionCount;
    }


    public int getEleComp() {
        return electionCompleteCount;
    }


    public int getInteractions() {
        return interactions;
    }


    public int getTraversals() {
        return traversals;
    }


    public int getMarker_infectionComplete() {
        return marker_infectionComplete;
    }


    public int getMarker_leaderElectionComplete() {
        return marker_leaderElectionComplete;
    }


    public int getMarker_allElectionComplete() {
        return marker_allElectionComplete;
    }


    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS - SETTERS
    ///////////////////////////////////////////////////////////////////////////


    public void setInfected(int infected) {
        this.infectionCount = infected;
    }


    public void setEleComp(int eleComp) {
        this.electionCompleteCount = eleComp;
    }


    public void setInteractions(int interactions) {
        this.interactions = interactions;
    }


    public void setTraversals(int traversals) {
        this.traversals = traversals;
    }


    public void setMarker_infectionComplete(int marker_infectionComplete) {
        this.marker_infectionComplete = marker_infectionComplete;
    }


    public void setMarker_leaderElectionComplete(int marker_leaderElectionComplete) {
        this.marker_leaderElectionComplete = marker_leaderElectionComplete;
    }


    public void setMarker_allElectionComplete(int marker_allElectionComplete) {
        this.marker_allElectionComplete = marker_allElectionComplete;
    }
}
