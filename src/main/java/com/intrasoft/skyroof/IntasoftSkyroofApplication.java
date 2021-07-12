package com.intrasoft.skyroof;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


@SpringBootApplication
public class IntasoftSkyroofApplication
{
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static void main(String[] args)
    {

        try
        {
            SkyroofLogger.setup();
        }
        catch( IOException e)
        {
            e.printStackTrace();
            throw new RuntimeException("Problems with creating the log files");
        }

        LOGGER.setLevel(Level.INFO);
        LOGGER.info("Logging Started");

        SpringApplication.run(IntasoftSkyroofApplication.class, args);
    }
}
