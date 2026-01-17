package com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerIterator;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerFactory.Container;
import com.davideferrari.logisticsystem.Utils.Annotations.AppDesignPattern;

/**
 *  This class is a concrete implementation of the ContainerIterator interface.
 *  In the Iterator Design Pattern, this class acts as the Concrete Iterator.
 *  It encapsulates the traversal logic, keeping track of the current position (cursor)
 *  within the ContainerCollection, allowing multiple iterators to traverse
 *  the same collection independently without interfering with each other.
 */
@AppDesignPattern(pattern = "Iterator", justification = "Concrete Iterator")
public class TerminalContainerIterator implements ContainerIterator
{
    private ContainerCollection collection;
    private int currentIndex = 0;

    /**
     *  This class initializes the iterator for a specific container collection.
     *  @param collection The aggregate object (Terminal, Ship, etc.) to be traversed.
     */
    public TerminalContainerIterator(ContainerCollection collection)
    {
        this.collection = collection;
    }

    /**
     *  This class checks if there are more elements in the collection to traverse.
     *  @return {@code true} if the current index is less than the collection size.
     */
    @Override
    public boolean hasNext()
    {
        return currentIndex < collection.getSize();
    }

    /**
     *  This class returns the next {@link Container} in the sequence and advances the cursor.
     *  @return The container at the current index.
     */
    @Override
    public Container next()
    {
        return collection.getContainerAt(currentIndex ++);
    }
}
