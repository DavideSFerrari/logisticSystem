package com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerFactory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 *  Unit tests for the HighCubeSelector class.
 *  Verifies the Factory Method implementation for High Cube containers.
 */

class HighCubeSelectorTest
{
    @Test
    @DisplayName("Factory method should create a new HighCube instance")
    void testCreateContainerReturnsHighCube()
    {
        HighCubeSelector selector = new HighCubeSelector();
        Container container = selector.createContainer();
        Assertions.assertNotNull(container, "The factory method should not return null");
        Assertions.assertTrue(container instanceof HighCube, 
            "The created container should be of type HighCube");    
        Assertions.assertEquals("HighCube", container.getType(), 
            "The container type string should be 'HighCube'");
    }

    @Test
    @DisplayName("Created HighCube should have correct physical specifications")
    void testCreatedHighCubeSpecs()
    {
        HighCubeSelector selector = new HighCubeSelector();
        HighCube highCube = selector.createContainer();
        Assertions.assertEquals(3700, highCube.getTareWeight(), "Tare weight should match HighCube specs");
        Assertions.assertEquals(25000, highCube.getMaxPayload(), "Max payload should match HighCube specs");
    }
}