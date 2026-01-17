package com.davideferrari.logisticsystem.Utils.ExceptionHandling;

import java.util.logging.Logger;

/**
 *  This class defines a proper way to manage and display errors, when an exception
 *  comes into play. Its main objective is to provide an exception shielding pattern,
 *  which represents an intuitive and user-friendly way to notify the error, 
 *  without exposing the underlying mechanisms as well as the exception stack trace.
 */
public class ExceptionShieldingHandler
{
    public static final Logger logger = Logger.getLogger(ExceptionShieldingHandler.class.getName());
    
    /**
     *  This method represents the entry point for handling exceptions caught
     *  throughout the application.
     *  It delegates the processing to specific logging logic based on the exception type.
     *  @param e The exception that was caught.
     */
    public static void handleException(Exception e)
    {
        logException(e);
    }

    /**
     *  This method differentiates logic that inspects the exception type
     *  and logs it with the appropriate severity level.
     *  @param e The exception to inspect and log.
     */
    private static void logException(Exception e)
    {
        if (e instanceof MenuValidationException)
        {
            System.out.println("\n\n\n");
            logger.warning(" ___ ___ ___ MENU VALIDATION ERROR: " + e.getMessage());
            System.out.println("\n\n\n");
        }
        else if (e instanceof ContainerValidationException)
        {
            System.out.println("\n\n\n");
            logger.warning("\n\n ___ ___ ___ CONTAINER VALIDATION ERROR: " + e.getMessage());
            System.out.println("\n\n\n");
        }
        else
        {
            System.out.println("\n\n\n");
            logger.severe("\n\n ___ ___ ___ SYSTEM ERROR: " + e.getMessage());
            System.out.println("\n\n\n");
        }        
    }
}
