/*
 * JSONUtil.java
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

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.pmw.tinylog.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * Utility class for JSON related tasks
 *
 */
public class JSONUtil {


    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ///////////////////////////////////////////////////////////////////////////


    /**
     * Convert object to JSON
     *
     * @param o Object to convert
     * @param pretty Should the JSON be pretty printed?
     * @return JSON string
     */
    public static String toJSON(Object o, boolean pretty) {
        Gson gson;
        if (pretty) {
            gson = new GsonBuilder().setPrettyPrinting().create();
        }
        else {
            gson = new Gson();
        }

        return gson.toJson(o);
    }


    /**
     * Convert object to JSON and write to logs dir
     *
     * @param dirName Path to logs dir
     * @param fileName Name of the file to be made
     * @param timestamp Simulation timestamp
     * @param o Object to convert
     * @param pretty Should the JSON be pretty printed?
     * @return JSON String
     */
    public static void writeJSON(String dirName, String fileName,
                                                 String timestamp,
                                                 Object o,
                                                 boolean pretty) {
        String jsonName = fileName + "." + timestamp + ".json";
        Path path = FileSystems.getDefault().getPath("logs", dirName, jsonName);

        try {
            Files.write(path, toJSON(o, pretty).getBytes(),
                              StandardOpenOption.CREATE);
        } catch (IOException e) {
            Logger.error(e);
        }
    }
}
