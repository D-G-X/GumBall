package bootstrap;

import java.io.*;
import java.util.logging.*;

public class GumballLogs implements Serializable{
    private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static Logger setupLogger() {
        LogManager.getLogManager().reset();

        try{
//            FileHandler fh = new FileHandler("/home/drgx/Documents/workspace/GumBall/src/main/executionLogs/GumBall.log");
            FileHandler fh = new FileHandler("D:/Hogwarts Engineering/GumBall/src/main/executionLogs/GumBall.log");
            fh.setFormatter(new SimpleFormatter());
            fh.setLevel(Level.ALL);
            logger.addHandler(fh);
        }
        catch (IOException e){
            System.out.println(e.getLocalizedMessage());
            logger.log(Level.SEVERE,"ERROR:\t File Handler not Working.\n");
        }
        return logger;
    }


}
