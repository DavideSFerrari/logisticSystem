package com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerIterator;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerFactory.Container;
import com.davideferrari.logisticsystem.Utils.Annotations.AppDesignPattern;

/**
 *  This interface defines a collection of containers that can be traversed.
 *  In the Iterator Design Pattern, this interface acts as the Aggregate,
 *  and it defines the contract for creating a compatible ContainerIterator object.
 *  Concrete implementations must implement this interface
 *  to allow their internal container lists to be iterated over without exposing
 *  their underlying structure.
 */
@AppDesignPattern(pattern = "Iterator", justification = "Aggregate")
public interface ContainerCollection
{
    /**
     *  This method that produces an iterator for this collection.
     *  @return A concrete iterator instance (e.g., {@link TerminalContainerIterator}) ready to traverse the collection.
     */
    ContainerIterator createIterator();

    /**
     *  This method retrieves the total number of containers in the collection.
     *  It is used by the iterator to determine the bounds of the traversal.
     *  @return The count of containers.
     */
    int getSize();

    /**
     *  This method retrieves the container at the specified position, 
     *  and it is used by the iterator to fetch elements during traversal.
     *  @param index The zero-based index of the container to retrieve.
     *  @return The {@link Container} object at the given index.
     */
    Container getContainerAt(int index);
}