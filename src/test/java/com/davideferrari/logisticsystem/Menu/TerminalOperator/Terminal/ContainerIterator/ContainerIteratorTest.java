package com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerIterator;

import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerFactory.Container;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.NoSuchElementException;

/**
 *  Unit tests for the ContainerIterator interface.
 *  This test uses an anonymous implementation to verify the contract of the Iterator pattern,
 *  ensuring sequential access logic is respected.
 */
class ContainerIteratorTest 
{
    /**
     *  Verifies that the iterator contract correctly handles traversal states
     *  and element retrieval.
     */
    @Test
    @DisplayName("Should respect the Iterator contract for sequential traversal")
    void testIteratorContract() 
    {
        /**
         *  Arrange: Create a stub implementation of the Iterator.
         */
        ContainerIterator stubIterator = new ContainerIterator() 
        {
            private int cursor = 0;
            private final Container[] items =
            {
                new Container()
                {
                    @Override public String getType()
                    { 
                        return "Box";
                    }
                }
            };

            @Override
            public boolean hasNext() 
            {
                return cursor < items.length;
            }

            @Override
            public Container next() 
            {
                if (!hasNext()) 
                {
                    throw new NoSuchElementException();
                }
                return items[cursor++];
            }
        };

        /**
         * Act & Assert.
         */
        Assertions.assertTrue(stubIterator.hasNext(), "Iterator should have elements initially");
        Container first = stubIterator.next();
        Assertions.assertNotNull(first, "First call to next() should return a container");
        Assertions.assertEquals("Box", first.getType(), "Returned container should match the source");
        Assertions.assertFalse(stubIterator.hasNext(), "Iterator should be exhausted after one element");
    }

    /**
     *  Verifies that the iterator behaves correctly when no more elements are available.
     */
    @Test
    @DisplayName("Should throw exception when calling next() on empty iterator")
    void testNextOnEmptyIterator() 
    {
        /**
         *  Arrange: Create an empty iterator.
         */
        ContainerIterator emptyIterator = new ContainerIterator() 
        {
            @Override
            public boolean hasNext()
            { 
                return false;
            }
            @Override
            public Container next()
            { 
                throw new NoSuchElementException(); 
            }
        };

        /**
         *  Act & Assert.
         */
        Assertions.assertThrows(NoSuchElementException.class, emptyIterator::next, 
            "Calling next() on an empty iterator must throw NoSuchElementException");
    }
}