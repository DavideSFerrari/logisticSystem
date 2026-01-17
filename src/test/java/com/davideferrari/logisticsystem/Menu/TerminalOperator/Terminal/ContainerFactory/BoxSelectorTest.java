package com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerFactory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 *  Unit tests for the BoxSelector class.
 *  Verifies that the Concrete Creator correctly instantiates the "Box" product.
 */
class BoxSelectorTest
{
    @Test
    @DisplayName("Factory method should create a new Box instance")
    void testCreateContainerReturnsBox()
    {
        BoxSelector selector = new BoxSelector();
        Container container = selector.createContainer();
        Assertions.assertNotNull(container, "The factory method should not return null");
        Assertions.assertTrue(container instanceof Box, "The created container should be of type Box");
        Assertions.assertEquals("Box", container.getType(), "The container type string should be 'Box'");
    }

    @Test
    @DisplayName("Created Box should have correct default specifications")
    void testCreatedBoxSpecifications()
    {
        BoxSelector selector = new BoxSelector();
        Box box = selector.createContainer();
        Assertions.assertEquals(2220, box.getTareWeight(), "Tare weight should match Box specs");
        Assertions.assertEquals(21000, box.getMaxPayload(), "Max payload should match Box specs");
    }
}