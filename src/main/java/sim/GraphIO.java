package sim;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.graphstream.stream.file.FileSink;
import org.graphstream.stream.file.FileSinkFactory;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceFactory;
import org.pmw.tinylog.Logger;


/**
 * Handles reading (from a file) and writing (to a file) of a graph. Wrapper
 * around GraphStream FileSource & FileSink classes.
 *
 */
public class GraphIO {


    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ///////////////////////////////////////////////////////////////////////////


    /**
     * Read a graph from a file. Supported graph file formats:
     *
     *     DGS
     *     GML
     *     DOT
     *     GEXF
     *     SVG
     *
     * @param filePath Path to graph file
     * @return ExtendedGraph object generated based on the given file. Null if
     *         unsupported format or not found
     */
    public static ExtendedGraph readGraph(String filePath) {
        FileSource fSource;

        try {
            /*
             * This will determine the correct FileSource subclass to return
             * based on the graph file format
             */
            fSource = FileSourceFactory.sourceFor(filePath);
            Logger.info("Graph file format determined: {0}",
                        fSource.getClass());
        } catch (IOException e) {
            Logger.error(e, "GRAPH FILE ISSUE - either not found or format not"
                         + "recongized (check the extenstion)");
            return null;
        }

        // Set the sink - graph object to add
        ExtendedGraph g = new ExtendedGraph("Graph");
        fSource.addSink(g);

        try {
            // Read the graph
            fSource.readAll(filePath);
        } catch (IOException e) {
            Logger.error(e);
            fSource.removeSink(g);
            return null;
        }

        fSource.removeSink(g);
        return g;
    }


    /**
     * Write a graph to disk. Format will be based on the extension given.
     *
     * @param g ExtendedGraph to be saved
     * @param filePath Path and filename to save graph to
     * @return True if write successful, false otherwise
     */
    public static boolean writeGraph(ExtendedGraph g, String dirName, String timestamp) {
        FileSink fSink;

        String fileName = "graph." + timestamp + ".gml";
        Path path = FileSystems.getDefault().getPath("logs", dirName, fileName);
        String filePath = path.toString();


        // This will determine the correct FileSink subclass to return based on
        // the graph file format
        fSink = FileSinkFactory.sinkFor(filePath);

        if (fSink == null) {
            Logger.error("FILE FORMAT NOT SUPPORTED");
            return false;
        }

        try {
            fSink.writeAll(g, filePath);
        } catch (IOException e) {
            Logger.error(e, "Writing graph - PATH NOT FOUND");
            return false;
        }

        Logger.info("Graph written to disk: {0}", filePath);
        return true;
    }
}
