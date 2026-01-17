package com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerSingleton;

import java.util.ArrayList;
import java.util.List;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerFactory.Container;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerIterator.ContainerCollection;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerIterator.ContainerIterator;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerIterator.TerminalContainerIterator;
import com.davideferrari.logisticsystem.Utils.Annotations.AppDesignPattern;
import com.davideferrari.logisticsystem.Utils.Annotations.CapacityLimit;

/**
 *  This class represents the central registry for all containers within the logistics system,
 *  and implements the Singleton Design Pattern to ensure that there is only
 *  one global source of truth for all containers currently in existence. 
 *  It also acts as an Aggregate in the Iterator pattern, allowing the system to traverse the global list.
 *  Thread Safety: Modification methods are synchronized to allow safe concurrent access.
 */
@CapacityLimit(value = 20)
@AppDesignPattern(pattern = "Singleton", justification = "Ensures a single global register")
public class ContainerRegister implements ContainerCollection
{

    private List<Container> commonRegister = new ArrayList<>();

    /**
     *  This method is set to private in order to prevent direct instantiation.
     */
    private ContainerRegister()
    {
        this.commonRegister = new ArrayList<>();
    }

    /**
     *  This inner static class is responsible for holding the Singleton instance.
     *  This implementation (Bill Pugh Singleton) ensures that the instance is created only when
     *  getInstance() is called, providing a thread-safe and efficient lazy-loading mechanism.
     */
    private static class ContainerRegisterHelper
    {
        private static final ContainerRegister INSTANCE = new ContainerRegister();
    }

    /**
     *  This method provides the global access point to the single instance of the ContainerRegister.
     *  @return The unique {@link ContainerRegister} instance.
     */
    public static ContainerRegister getInstance()
    {
        return ContainerRegisterHelper.INSTANCE;
    }

    public List<Container> displayContainers()
    {
        return commonRegister;
    }

    /**
     *  This method adds a container to the global registry,
     *  and it is synchronized to prevent race conditions during concurrent additions.
     *  @param container The container to register.
     */
    public synchronized void addContainer(Container container)
    {
        commonRegister.add(container);
    }

    /**
     *  This method removes a container from the global registry.
     *  and it is synchronized to ensure safe removal in a multi-threaded context.
     *  @param container The container to deregister.
     */
    public synchronized void removeContainer(Container container)
    {
        commonRegister.remove(container);
    }

    /**
     *  This method creates an iterator to traverse the registered containers.
     *  @return A {@link TerminalContainerIterator} for this registry.
     */
    @Override
    public ContainerIterator createIterator()
    {
        return new TerminalContainerIterator(this);
    }

    @Override
    public int getSize()
    {
        return commonRegister.size();
    }

    @Override
    public Container getContainerAt(int index)
    {
        return commonRegister.get(index);
    }

}
