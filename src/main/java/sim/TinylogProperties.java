package sim;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.Level;
import org.pmw.tinylog.Logger;
import org.pmw.tinylog.policies.StartupPolicy;
import org.pmw.tinylog.writers.RollingFileWriter;


/**
 * Configure and start tinylog. Originally, these configurations were done via
 * the tinylog.properties file.
 *
 */
public class TinylogProperties {


    ///////////////////////////////////////////////////////////////////////////
    // PRIVATE ATTRIBUTES
    ///////////////////////////////////////////////////////////////////////////


    private Path path;
    private Level logLevel;
    private Date date;
    private DateFormat dateFormat;


    /**
     * Date format pattern. Used for creation of files and directories.
     */
    private String pattern = "yyyy-MM-dd_HH-mm-ss";


    /**
     * Timestamp of THIS simulation run
     */
    private String timestamp;


    private String dirName;


    /**
     * Should logs be written to Standard Output?
     */
    private boolean stdout;


    ///////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    ///////////////////////////////////////////////////////////////////////////


    public TinylogProperties(Level logLevel, boolean stdout) {
        this.logLevel = logLevel;
        this.stdout = stdout;

        createLogsDir();

        if (!stdout) {
            startTinylog();
        }
    }


    ///////////////////////////////////////////////////////////////////////////
    // PRIVATE METHODS
    ///////////////////////////////////////////////////////////////////////////


    /**
     * Create the directory in which all simulation data will be stored in
     */
    private void createLogsDir() {
        // Get timestamp
        date = new Date();
        dateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        timestamp = dateFormat.format(date);

        // Get path
        dirName = "log." + timestamp;
        path = FileSystems.getDefault().getPath("logs");


        try {
            // If the 'logs' directory does not exist, create it
            if (Files.notExists(path)) {
                Files.createDirectory(path);
                Logger.info("Creating 'logs' directory");
            }

            // Create the log directory for THIS specific simulation
            path = FileSystems.getDefault().getPath("logs", dirName);
            Files.createDirectory(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Configure and start tinylog
     */
    private void startTinylog() {
        String logName = path + File.separator + "log." + timestamp + ".txt";
        RollingFileWriter rfw = new RollingFileWriter(logName,
                                                      10,
                                                      new StartupPolicy());

        Configurator.currentConfig()
        .writer(rfw)
        .level(logLevel)
        .activate();

        Logger.info("Logs directory for this simulation created: {0}", path);
    }


    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ///////////////////////////////////////////////////////////////////////////


    public Path getPath() {
        return path;
    }


    public String getTimestamp() {
        return timestamp;
    }


    public Date getDate() {
        return date;
    }


    public String getDirName() {
        return dirName;
    }
}
