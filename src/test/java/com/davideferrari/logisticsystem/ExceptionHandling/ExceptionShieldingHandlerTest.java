package com.davideferrari.logisticsystem.ExceptionHandling;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.davideferrari.logisticsystem.Utils.ExceptionHandling.ContainerValidationException;
import com.davideferrari.logisticsystem.Utils.ExceptionHandling.ExceptionShieldingHandler;
import com.davideferrari.logisticsystem.Utils.ExceptionHandling.MenuValidationException;

class ExceptionShieldingHandlerTest
{

    private TestLogHandler logHandler;

    @BeforeEach
    void setUp()
    {
        logHandler = new TestLogHandler();
        Logger logger = ExceptionShieldingHandler.logger;
        logger.addHandler(logHandler);
        logger.setUseParentHandlers(false); // Disable other handlers to avoid console output
    }

    @Test
    void testHandleMenuValidationException()
    {
        Exception e = new MenuValidationException("Menu validation failed", null);
        ExceptionShieldingHandler.handleException(e);
        assertTrue(logHandler.getLastMessage().contains("MENU VALIDATION ERROR: Menu validation failed"));
    }

    @Test
    void testHandleContainerValidationException()
    {
        Exception e = new ContainerValidationException("Container validation failed", null);
        ExceptionShieldingHandler.handleException(e);
        assertTrue(logHandler.getLastMessage().contains("CONTAINER VALIDATION ERROR: Container validation failed"));
    }

    @Test
    void testHandleGenericException()
    {
        Exception e = new Exception("Generic error");
        ExceptionShieldingHandler.handleException(e);
        assertTrue(logHandler.getLastMessage().contains("SYSTEM ERROR: Generic error"));
    }

    private static class TestLogHandler extends Handler {
        private String lastMessage;

        @Override
        public void publish(LogRecord record)
        {
            lastMessage = record.getMessage();
        }

        @Override
        public void flush()
        {
            // No-op
        }

        @Override
        public void close() throws SecurityException
        {
            // No-op
        }

        public String getLastMessage()
        {
            return lastMessage;
        }
    }
}
