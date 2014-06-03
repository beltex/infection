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


    public String toString() {
        return "AID: " + AID +
                "; Leader AID:  " + leaderAID +
                "; Conversions: " + conversions +
                "; Met followers: " + metFollowers +
                "; Is leader: " + isLeader +
                "; Election Complete: " + electionComplete;
    }


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