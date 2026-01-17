package com.davideferrari.logisticsystem.Utils.ExceptionHandling;

/**
 *  This class defines a custom exception extending the Exception class.
 *  It comes into play when the information regarding a container is not
 *  inserted properly, triggering a personalized message in a user-friendly way.*  
 */
public class ContainerValidationException extends Exception
{
    public ContainerValidationException (String message, Throwable cause)
    {
        super(message, cause);
    }
}
