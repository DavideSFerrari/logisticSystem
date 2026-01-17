package com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerFactory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 *  Unit tests for the HighCube class.
 *  Verifies that the Concrete Product initializes with the correct
 *  dimensions and weight specifications defined in the requirements.
 */
class HighCubeTest
{
    @Test
    @DisplayName("Should create HighCube with correct standard specifications")
    void testHighCubeInitialization()
    {
        HighCube container = new HighCube();
        Assertions.assertEquals("HighCube", container.getType(), 
            "Container type should be 'HighCube'");
        Assertions.assertEquals(3700, container.getTareWeight(), "Tare weight should be 3,700 kg");
        Assertions.assertEquals(1.7, container.getHeight(), 0.001, "Height should be 1.7 m");
        Assertions.assertEquals(25000, container.getMaxPayload(), "Max payload should be 25,000 kg");
    }
}