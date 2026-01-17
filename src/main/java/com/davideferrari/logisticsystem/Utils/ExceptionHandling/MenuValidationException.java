package com.davideferrari.logisticsystem.Utils.ExceptionHandling;

/**
 *  This class defines a custom exception extending the Exception class.
 *  It comes into play when the input for the menu is not inserted correctly
 *  (e.g. a number not provided in the main menu),
 *  triggering a personalized message in a user-friendly way.*  
 */
public class MenuValidationException extends Exception
{
    public MenuValidationException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
