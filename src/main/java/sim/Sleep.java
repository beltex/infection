package main.java.sim;

import org.pmw.tinylog.Logger;


/**
 * Handles all simulator sleeps. Used for graph visualization, slowing things
 * down to see the simulation run.
 *
 */
public class Sleep {


    ///////////////////////////////////////////////////////////////////////////
    // PRIVATE ATTRIBUTES
    ///////////////////////////////////////////////////////////////////////////


    /**
     * Default sleep value used across the simulator
     */
    public static final int SLEEP = 0;


    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ///////////////////////////////////////////////////////////////////////////


    /**
     * Sleep with default duration
     */
    public static void sleep() {
        try {
            Thread.sleep(SLEEP);
        } catch (InterruptedException e) {
            Logger.error(e);
        }
    }


    /**
     * Sleep with custom duration
     *
     * @param ms Number of milliseconds to sleep
     */
    public static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Logger.error(e);
        }
    }
}