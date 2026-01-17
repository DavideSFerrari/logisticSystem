package com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerFactory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 *  Unit tests for the abstract ContainerCreator class.
 *  This test verifies the "Creator" logic in the Factory Method pattern.
 *  It ensures that the {@code registerContainer} method correctly orchestrates:
 *  1. Creation (via the abstract factory method).
 *  2. Initialization (setting the ID code).
 */
class ContainerCreatorTest
{
    @Test
    @DisplayName("Should create container and assign the correct ID code")
    void testRegisterContainerFlow()
    {
        ContainerCreator<Container> stubCreator = new ContainerCreator<Container>()
        {
            @Override
            protected Container createContainer()
            {
                return new Container()
                {
                    @Override
                    public String getType()
                    {
                        return "TestType";
                    }
                };
            }
        };

        String expectedCode = "TEST-ID-999";
        Container result = stubCreator.registerContainer(expectedCode);
        Assertions.assertNotNull(result, "The factory should return a non-null container");
        Assertions.assertEquals(expectedCode, result.getContainerCode(), 
            "The registerContainer method should have set the code on the new object");
            
        Assertions.assertEquals("TestType", result.getType(), 
            "The object returned should be the one produced by createContainer");
    }
}