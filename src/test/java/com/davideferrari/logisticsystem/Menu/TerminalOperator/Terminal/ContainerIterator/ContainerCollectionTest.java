package com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerIterator;

import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerFactory.Container;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

/**
 *  Unit tests for the ContainerCollection interface.
 *  This test uses a concrete implementation (Stub) to verify that the Aggregate 
 *  correctly exposes the necessary methods for traversal logic.
 */
class ContainerCollectionTest 
{
    /**
     *  Verifies that a concrete implementation of ContainerCollection correctly
     *  returns stored containers and reports accurate sizing.
     */
    @Test
    @DisplayName("Should correctly manage container data and report size")
    void testCollectionDataManagement() 
    {
        /**
         *  Arrange: Create a stub implementation of the Aggregate.
         */
        ContainerCollection stubCollection = new ContainerCollection() 
        {
            private final List<Container> list = new ArrayList<>();

            {
                /**
                 *  Anonymous Container stub for testing.
                 */
                list.add(new Container() 
                {
                    @Override
                    public String getType() { return "Box"; }
                }
                );
            }

            @Override
            public ContainerIterator createIterator() 
            {
                /**
                 *  In a real scenario, this would return a TerminalContainerIterator.
                 */
                return null; 
            }

            @Override
            public int getSize() 
            {
                return list.size();
            }

            @Override
            public Container getContainerAt(int index) 
            {
                return list.get(index);
            }
        };

        /**
         *  Act & Assert.
         */
        Assertions.assertEquals(1, stubCollection.getSize(), "Collection size should be 1");
        Assertions.assertNotNull(stubCollection.getContainerAt(0), "Container at index 0 should not be null");
        Assertions.assertEquals("Box", stubCollection.getContainerAt(0).getType(), "Container type should be 'Box'");
    }
}