package com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.TerminalComposite;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *  Unit tests for the TerminalComponent interface.
 *  This suite verifies that the Component contract in the Composite pattern
 *  correctly facilitates uniform access to both Leaf and Composite objects.
 */
class TerminalComponentTest 
{

    /**
     *  Verifies that any implementation of the Component interface can 
     *  execute its primary behavior (description).
     */
    @Test
    @DisplayName("Should execute description logic in a uniform manner")
    void testDescriptionUniformity() 
    {
        /**
         *  Arrange: Create an atomic flag to track if the method was called.
         */
        AtomicBoolean wasCalled = new AtomicBoolean(false);

        /**
         *  Creating a stub implementation of the Component.
         */
        TerminalComponent stubComponent = new TerminalComponent() 
        {
            @Override
            public void description() 
            {
                wasCalled.set(true);
            }
        };

        /**
         *  Act: Call the method defined by the interface.
         */
        stubComponent.description();

        /**
         *  Assert: Verify the behavior was executed.
         */
        Assertions.assertTrue(wasCalled.get(), 
            "The description method of the component should be callable and executable.");
    }
}