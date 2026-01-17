
package com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.TerminalComposite;

import java.util.logging.Logger;
import com.davideferrari.logisticsystem.Menu.CargoShipCaptain.CargoShip;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerFactory.Container;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerIterator.ContainerIterator;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerSingleton.ContainerRegister;
import java.util.ArrayList;
import java.util.List;
import com.davideferrari.logisticsystem.Utils.Annotations.AppDesignPattern;
import com.davideferrari.logisticsystem.Utils.Annotations.CapacityLimit;
import com.davideferrari.logisticsystem.Utils.ExceptionHandling.ContainerValidationException;

/**
 *  This class represents a main terminal hub that can contain multiple sub-terminals.
 *  In the <b>Composite Design Pattern</b>, this class acts as the <b>Composite</b>.
 *  It maintains a list of child {@link TerminalComponent} objects (such as {@link ExportSubTerminal})
 *  and implements the component interface methods to delegate operations to its children.
 */
@AppDesignPattern(pattern = "Composite", justification = "Composite")
public class Terminal implements TerminalComponent 
{
    private static final Logger logger = Logger.getLogger(Terminal.class.getName());
    private List<TerminalComponent> components = new ArrayList<>();

    private String name;

    /**
     *  This method constructs a new main Terminal.
     *  @param name The name of the terminal (e.g., "Bari Main Hub").
     */
    public Terminal (String name)
    {
        this.name = name;
    }

    @Override
    public void description()
    {
        logger.info("This is the " + this.name + " terminal.");
    }

    /**
     *  This method adds a sub-component (Leaf or Composite) to this terminal.
     *  @param component The sub-terminal to add.
     */
    public void addComponent(TerminalComponent component)
    {
        components.add(component);
    }

    /**
     *  This method iterates through all child components and triggers their description methods,
     *  demonstrating the recursive nature of the Composite pattern.
     */
    public void displaySubTerminals()
    {
        logger.info("Displaying all sub-terminals:");
        for (TerminalComponent comp : components)
        {
            comp.description();
        }
    }

    /**
     *  This method displays a complete list of all containers in the system.
     *  It accesses the {@link ContainerRegister} Singleton to get the global view of all containers,
     *  regardless of which specific sub-terminal they are currently in.
     */
    public static void displayAllContainers()
    {
        ContainerRegister globalRegister = ContainerRegister.getInstance();

        logger.info("Displaying the containers of both port terminals:");
        ContainerIterator containerIterator = globalRegister.createIterator();
        while (containerIterator.hasNext())
        {
            Container container = containerIterator.next();
            logger.info("Container: " + container.getContainerCode() + " | Type: " + container.getType());
        }
    }

    /**
     *  This method orchestrates the safe removal of a container from the logistics system.
     *  This method acts as a transaction controller, enforcing strict business rules before deletion:
     *  - Validation: Ensures the container exists and is EMPTY.
     *  - Global Capacity: Ensures removal doesn't drop total inventory below the safety threshold (20).
     *  - Location Check: Ensures the container is not currently in transit (on a {@link CargoShip}).
     *  - Execution:</b> Removes the container from the specific sub-terminal (Bari or Busan) and then from the Global Register.
     *  @param containerCode The unique ID of the container to remove.
     *  @param bari The Bari export terminal instance.
     *  @param busan The Busan export terminal instance.
     *  @param ship The cargo ship instance (to check for transit status).
     *  @throws ContainerValidationException If the container cannot be found in the registry.
     */
    public static void removeGlobally(String containerCode, ExportSubTerminal bari, ExportSubTerminal busan, CargoShip ship) throws ContainerValidationException
    {
        ContainerRegister globalRegister = ContainerRegister.getInstance();
        int minimumCapacity = 10;

        Container ctnr = null;

        ContainerIterator containerIterator = globalRegister.createIterator();
        while (containerIterator.hasNext())
        {
            Container container = containerIterator.next();
            if (container.getContainerCode().equals(containerCode))
            {
                ctnr = container;
                break;
            }
        }

        if (ctnr == null)
        {
            logger.warning("\n\n ___ ___ ___ CONTAINER VALIDATION ERROR.");
            throw new ContainerValidationException("Container " + containerCode + " not found.", null);
        }

        if (ctnr.getContainerState() != Container.ContainerState.EMPTY)
        {
            logger.warning("Container is not empty. Deletion denied.");
            return;
        }

        if (ContainerRegister.class.isAnnotationPresent(CapacityLimit.class))
        {
            minimumCapacity = ContainerRegister.class.getAnnotation(CapacityLimit.class).value();
        }
        
        if(globalRegister.getSize() <= minimumCapacity)
        {
            logger.warning("Both terminals must have in total at least 20 containers. Deletion not allowed.");
            return;
        }

        if (ship.containsContainer(ctnr))
        {
            logger.warning("Action Denied: Container is currently on the ship (In Transit).");
            return;
        }

        boolean removedFromBari = bari.removeLocally(ctnr);
        boolean removedFromBusan = false;

        if (!removedFromBari)
        {
            removedFromBusan = busan.removeLocally(ctnr);
        }

        if (removedFromBari || removedFromBusan)
        {
            globalRegister.removeContainer(ctnr);
            logger.info("The " + ctnr.getType() + " container " + containerCode + " has been succesfully deleted.");
            ctnr = null;
        }
        else
        {
            logger.warning("Deletion Denied: Container is currently in an Import Sub-Terminal. Containers can only be deleted from Export Sub-Terminals.");
        }
    }

}

