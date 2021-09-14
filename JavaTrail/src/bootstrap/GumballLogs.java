package bootstrap;

import java.io.*;
import java.util.logging.*;

public class GumballLogs implements Serializable{
    private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static Logger setupLogger() {
        LogManager.getLogManager().reset();

//        ConsoleHandler ch = new ConsoleHandler();
//        ch.setLevel(Level.FINE);
//        logger.addHandler(ch);

        try{
            FileHandler fh = new FileHandler("D:/Clear/JavaTrail/executionLogs/GumBall.log");
            fh.setFormatter(new SimpleFormatter());
            fh.setLevel(Level.SEVERE);
            logger.addHandler(fh);
        }
        catch (IOException e){
            logger.log(Level.SEVERE,"File Handler not Working");
        }
        return logger;
    }


}
