package com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerIterator;

import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerFactory.Container;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;

/**
 *  Unit tests for the TerminalContainerIterator class.
 *  This test verifies the <b>Concrete Iterator</b> logic, ensuring it correctly
 *  tracks the cursor and interacts with the <b>Aggregate</b> (ContainerCollection).
 */
class TerminalContainerIteratorTest 
{
    private ContainerCollection mockCollection;
    private Container mockContainer1;
    private Container mockContainer2;

    /**
     *  Initializes the mocks before each test execution.
     */
    @BeforeEach
    void setUp() 
    {
        mockCollection = Mockito.mock(ContainerCollection.class);
        mockContainer1 = Mockito.mock(Container.class);
        mockContainer2 = Mockito.mock(Container.class);
    }

    /**
     *  Verifies that the iterator correctly identifies when more elements are available
     *  and when the end of the collection is reached.
     */
    @Test
    @DisplayName("Should correctly identify availability of next elements")
    void testHasNext() 
    {
        /**
         *  Arrange: Configure collection to have 1 element.
         */
        when(mockCollection.getSize()).thenReturn(1);
        TerminalContainerIterator iterator = new TerminalContainerIterator(mockCollection);

        /**
         *  Act & Assert.
         */
        Assertions.assertTrue(iterator.hasNext(), "Should have next element when index < size");
        iterator.next(); // Advance cursor
        Assertions.assertFalse(iterator.hasNext(), "Should not have next element after reaching size");
    }

    /**
     *  Verifies that the iterator retrieves elements in the correct sequential order
     *  and properly increments the internal cursor.
     */
    @Test
    @DisplayName("Should traverse the collection sequentially and return correct elements")
    void testSequentialTraversal() 
    {
        /**
         *  Arrange: Mock a collection with two specific containers.
         */
        when(mockCollection.getSize()).thenReturn(2);
        when(mockCollection.getContainerAt(0)).thenReturn(mockContainer1);
        when(mockCollection.getContainerAt(1)).thenReturn(mockContainer2);
        TerminalContainerIterator iterator = new TerminalContainerIterator(mockCollection);

        /**
         *  Act & Assert: First element.
         */
        Container first = iterator.next();
        Assertions.assertEquals(mockContainer1, first, "First element returned should be from index 0");

        /**
         *  Act & Assert: Second element.
         */
        Container second = iterator.next();
        Assertions.assertEquals(mockContainer2, second, "Second element returned should be from index 1");
    }

    /**
     *  Verifies that calling next() increments the index, causing hasNext() to change state.
     */
    @Test
    @DisplayName("Should increment current index after each call to next")
    void testCursorIncrement() 
    {
        /**
         *  Arrange.
         */
        when(mockCollection.getSize()).thenReturn(5);
        TerminalContainerIterator iterator = new TerminalContainerIterator(mockCollection);

        /**
         *  Act: Move cursor forward twice.
         */
        iterator.next();
        iterator.next();

        /**
         *  Assert: Iterator should still have elements, and the next call should fetch index 2.
         */
        Assertions.assertTrue(iterator.hasNext());
    }
}