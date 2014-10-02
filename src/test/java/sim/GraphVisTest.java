/*
 * GraphVisTest.java
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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.graphstream.ui.swingViewer.GraphRenderer;
import org.graphstream.ui.swingViewer.Viewer;
import org.junit.Test;

public class GraphVisTest {

    private ExtendedGraph g;

    // Pulled from GraphVis class
    private static final String UI_STYLESHEET = "ui.stylesheet";
    private static final String cssPath = System.getProperty("user.dir") +
                                          File.separator + "src" +
                                          File.separator + "main" +
                                          File.separator + "resources" +
                                          File.separator + "style.css";


    /**
     * Test if CSS parses without exception.
     */
    @Test
    public void CSSParseTest() {
        // Setup
        g = new ExtendedGraph("Test");

        g.addNode("A");
        g.addNode("B");
        g.addEdge("AB", "A", "B");

        GraphVis gv = GraphVis.getInstance();
        gv.init(g);


        // Read in CSS - based on GraphVis.applyCSS() private method
        Scanner s = null;
        String css = "";

        try {
            s = new Scanner(new File(cssPath));

            // Read in the CSS
            while (s.hasNext()) {
                css = css.concat(s.nextLine());
            }
            g.addAttribute(UI_STYLESHEET, css);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            s.close();
        }


        /*
         * Instead of Viewer viewer = g.display(true); Based off
         * AbstractGraph.display(). We need to do this to have the CSS take
         * effect to catch any issues. However, we don't actually want to
         * display it (make the JFrame visible), as this will cause an error in
         * a headless environment. Thus, we prevent the display by calling
         * addView ourselves with a false flag for openInAFrame.
         *
         * NOTE: Must Swing thread model (main thread) otherwise JUnit will not
         *       pick up the exception.
         */
        Viewer viewer = new Viewer(g,
                                  Viewer.ThreadingModel.GRAPH_IN_SWING_THREAD);
        GraphRenderer renderer = Viewer.newGraphRenderer();
        viewer.addView(Viewer.DEFAULT_VIEW_ID, renderer, false);
    }
}
