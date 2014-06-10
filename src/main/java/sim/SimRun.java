package sim;


/**
 * Hold single simulation data.
 *
 */
public class SimRun {


    ///////////////////////////////////////////////////////////////////////////
    // PRIVATE ATTRIBUTES
    ///////////////////////////////////////////////////////////////////////////


    private int infected;
    private int eleComp;
    private int interactions;
    private int traversals;
    private int marker_infectionComplete;
    private int marker_leaderElectionComplete;
    private int marker_allElectionComplete;


    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS - GETTERS
    ///////////////////////////////////////////////////////////////////////////


    public int getInfected() {
        return infected;
    }


    public int getEleComp() {
        return eleComp;
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
        this.infected = infected;
    }


    public void setEleComp(int eleComp) {
        this.eleComp = eleComp;
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
