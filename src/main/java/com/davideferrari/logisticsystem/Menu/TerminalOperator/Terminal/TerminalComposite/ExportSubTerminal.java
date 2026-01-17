package com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.TerminalComposite;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import com.davideferrari.logisticsystem.Menu.CargoShipCaptain.CargoShip;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerFactory.Container;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerIterator.ContainerCollection;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerIterator.ContainerIterator;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerIterator.TerminalContainerIterator;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerSingleton.ContainerRegister;
import com.davideferrari.logisticsystem.Utils.Annotations.AppDesignPattern;
import com.davideferrari.logisticsystem.Utils.Annotations.CapacityLimit;
import com.davideferrari.logisticsystem.Utils.ExceptionHandling.ContainerValidationException;

/**
 *  This class represents a specialized terminal component dedicated to Export operations.
 *  In the Composite Design Pattern, this class acts as a Leaf. It cannot contain other
 *  terminals, but it holds the actual list of containers waiting to be loaded onto a ship.
 *  It also implements ContainerCollection, serving as an Aggregate for the Iterator pattern,
 *  allowing traversal of the containers stored specifically in this export bay.
 */
@AppDesignPattern(pattern = "Iterator - Composite", justification = "Concrete Aggregate - Composite")
@CapacityLimit(value = 10)
public class ExportSubTerminal implements TerminalComponent, ContainerCollection
{
    private static final Logger logger = Logger.getLogger(ExportSubTerminal.class.getName());
    private final List<Container> localRegister = new ArrayList<>();
    
    private String name;
    private int minimumCapacity;

    private static boolean confirmation;

    /**
     *  This method constructs a new Export Sub-Terminal.
     *  It initializes the terminal with a name and retrieves the capacity constraints
     *  from the CapacityLimit annotation via reflection.
     *  @param name The name of the parent port (e.g., "Bari", "Busan").
     */
    public ExportSubTerminal(String name)
    {
        this.name = name;
        if (this.getClass().isAnnotationPresent(CapacityLimit.class))
        {
            this.minimumCapacity = this.getClass().getAnnotation(CapacityLimit.class).value();
        }
        else
        {
            this.minimumCapacity = 10;
        }
    }

    public String getName()
    {
        return name;
    }

    /**
     *  This method resets the static confirmation flag,
     *  and it is used after a request has been processed (accepted or denied).
     */
    public static void resetRequest()
    {
        confirmation = false;
    }

    @Override
    public void description()
    {
        logger.info("This is the export sub-terminal of " + this.name + " terminal.");
    }

    /**
     *  This method creates an iterator to traverse the containers in this specific sub-terminal.
     *  @return A {@link TerminalContainerIterator} for the local list.
     */
    @Override
    public ContainerIterator createIterator()
    {
        return new TerminalContainerIterator(this);
    }

    @Override
    public int getSize()
    {
        return localRegister.size();
    }

    @Override
    public Container getContainerAt(int index)
    {
        return localRegister.get(index);
    }

    /**
     *  This method adds a container to this terminal for export.
     *  Thread Safety: Synchronized to handle concurrent additions from the UI or Trucks.
     *  Validation checks:
     *  - Global Uniqueness: Checks against ContainerRegister.
     *  - Capacity: Ensures the terminal is not overfilled.
     *  @param containerToAdd The container object.
     *  @param containerCode  The unique ID to validate.
     *  @return {@code true} if added successfully.
     */
    public synchronized boolean addContainer(Container containerToAdd, String containerCode)
    {   
        ContainerRegister globalRegister = ContainerRegister.getInstance();
        ContainerIterator containerIterator = globalRegister.createIterator();
        while (containerIterator.hasNext())
        {
            Container container = containerIterator.next();
            if (container.getContainerCode().equalsIgnoreCase(containerCode))
            {
                logger.warning("Duplicate container code: " + containerCode + ". This container cannot be created, neither added to the terminal.");
                return false;
            }
        }
        if (localRegister.size() >= minimumCapacity)
        {
            logger.warning("The terminal has reached it's minimum capacity. No containers can be added.");
            return false;
        }
        else
        {
            containerToAdd.setLocation(this.name + " Export Sub-Terminal.");
            localRegister.add(containerToAdd);
            globalRegister.addContainer(containerToAdd);
        }
        return true;
    }

    /**
     *  This method removes a container from the local storage.
     *  Constraint: Prevents removal if it would drop the inventory below a certain level of capacity.
     */
    public synchronized boolean removeLocally(Container container)
    {
        if(localRegister.size() <= minimumCapacity)
        {
            logger.warning("The terminal must maintain its minimum container capacity.");
            return false;
        }
        return localRegister.remove(container);
    }

    public void displayTerminalContainers()
    {
        logger.info("Displaying the containers of " + this.name + " port terminal:");
        logger.info("Current terminal capacity:" + localRegister.size());
        ContainerIterator containerIterator = this.createIterator();
        while (containerIterator.hasNext())
        {
            Container container = containerIterator.next();
            logger.info("Container: " + container.getContainerCode() + " | Type: " + container.getType());
        }
    }

    /**
     *  This method orchestrates the transfer of containers from the Terminal to the Ship,
     *  by iterating through available containers and attempting to load them onto the CargoShip.
     *  It stops if the ship becomes full or the terminal reaches its minimum stock level.
     *  @param ship The ship currently docked for export.
     */
    public void shipExport(CargoShip ship) throws ContainerValidationException
    {
        ContainerIterator containerIterator = this.createIterator();
        List<Container> containersToRemove = new ArrayList<>();
        while (containerIterator.hasNext())
        {
            if (this.getSize() - containersToRemove.size() <= minimumCapacity)
            {
                logger.info("Terminal minimum capacity limit reached. Stopping export.");
                break;
            }
            Container container = containerIterator.next();
            boolean shipAccepted = ship.pickFromTerminal(container);

            if (shipAccepted)
            {
                containersToRemove.add(container);
            }
            else
            {
                logger.info("Ship capacity reached. Stopping export operation.");
                break;
            }
        }
        for (Container c : containersToRemove)
        {
            localRegister.remove(c);
        }
        logger.info("Export Operation Finished. Remaining Terminal Capacity: " + this.getSize());
    }

    /**
     *  This method handles the undocking request handshake,
     *  by verifying if the ship is in a valid state (DOCKED or WAITING) and targeted at this specific terminal.
     *  If valid, it sets the confirmation flag for the Terminal Operator Menu.
     */
    public static boolean shipRequest(CargoShip ship, ExportSubTerminal bariExp, ExportSubTerminal busanExp)
    {
        if(ship.getRequestTarget() == null)
        {
            logger.info("No request from a ship has been received.");
        }
        else if (ship.getState() != CargoShip.CargoShipState.DOCKED_FOR_EXPORT && 
                 ship.getState() != CargoShip.CargoShipState.WAITING)
        {
            logger.info("No valid undocking request received (Ship state invalid).");
            return false;
        }
        else if (ship.getRequestTarget().equals("Bari"))
        {
            logger.info("The " + bariExp.getName() + " export terminal has received a request from the ship captain to undock.");
            confirmation = true;
            return true;
        }
        else if(ship.getRequestTarget().equals("Busan"))
        {
            logger.info("The " + busanExp.getName() + " export terminal has received a request from the ship captain to undock.");
            confirmation = true;
            return true;
        }
        else
        {
            logger.info("At the moment, the terminal cannot grant any permission.");
        }
        return false;
    }

    public static boolean sendConfirmation()
    {
        if (confirmation == true)
        {
            logger.info("The export terminal has notified the terminal operator of the request.");
            return true;
        }
        return false;
    }

    /**
     *  This method is used by the WarehouseTruck to pick up an EMPTY container for processing.
     *  Thread Safety: Synchronized to prevent multiple trucks from grabbing the same container.
     *  @return An {@code EMPTY} container if available, otherwise {@code null}.
     */
    public synchronized Container borrowFromWarehouse()
    {
        ContainerIterator containerIterator = this.createIterator();
        while (containerIterator.hasNext())
        {
            Container c = containerIterator.next();
            
            if (c.getContainerState() == Container.ContainerState.EMPTY)
            {
                localRegister.remove(c);   
                logger.info("Warehouse borrowed container " + c.getContainerCode() + ". It is now being processed.");
                return c;
            }
        }
        logger.warning("Warehouse attempted to borrow a container, but no EMPTY containers were found in the Export Terminal.");
        return null;
    }

    /**
     *  This method receives a processed container from the Warehouse,
     *  by updating the container's location and adds it to the export queue.
     *  @param container The container returned by the truck.
     */
    public synchronized void addFromWarehouse(Container container)
    {
        container.setLocation(this.getName() + " Export Sub-Terminal");
        localRegister.add(container);
        logger.info("Container " + container.getContainerCode() + " added from Warehouse. State: " + container.getContainerState());
    }

}
