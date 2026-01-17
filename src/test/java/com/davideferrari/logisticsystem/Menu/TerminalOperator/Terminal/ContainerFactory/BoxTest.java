package com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerFactory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 *  Unit tests for the Box class.
 *  Tests the creation and initialization of the Concrete Product "Box".
 *  Since the constructor is package-private, this test must be in the same package.
 */
class BoxTest
{
    @Test
    @DisplayName("Should create a Box container with correct standard specifications")
    void testBoxInitialization()
    {
        Box box = new Box();
        Assertions.assertEquals("Box", box.getType(), "Container type should be 'Box'");
        Assertions.assertEquals(2220, box.getTareWeight(), "Tare weight should be 2,220 kg");
        Assertions.assertEquals(1.2, box.getHeight(), 0.001, "Height should be 1.2 m");
        Assertions.assertEquals(21000, box.getMaxPayload(), "Max payload should be 21,000 kg");
    }
}