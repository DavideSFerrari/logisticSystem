package com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerIterator;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerFactory.Container;
import com.davideferrari.logisticsystem.Utils.Annotations.AppDesignPattern;

/**
 *  This interface defines the standard contract for traversing a collection of Container objects.
 *  In the Iterator Design Pattern, this interface acts as the Iterator abstraction.
 *  It provides a uniform way to access elements of a ContainerCollection sequentially
 *  without exposing its underlying representation (Array, List, etc.).
 */
@AppDesignPattern(pattern = "Iterator", justification = "Iterator Interface")
public interface ContainerIterator
{
        /**
        *  This method checks if the iteration has more elements.
        *  @return {@code true} if the iterator has more containers to traverse, {@code false} otherwise.
        */
        public boolean hasNext();

        public Container next();
}