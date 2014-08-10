/*
 * Sleep.java
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

import org.pmw.tinylog.Logger;


/**
 * Handles all simulator sleeps. Used for graph visualization, slowing things
 * down to see the simulation run.
 *
 */
public class Sleep {


    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC ATTRIBUTES
    ///////////////////////////////////////////////////////////////////////////


    /**
     * Default sleep value used across the simulator. This is used to slow down
     * the simulation for better viewing of graph visualization.
     */
    protected static int SLEEP = 0;


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