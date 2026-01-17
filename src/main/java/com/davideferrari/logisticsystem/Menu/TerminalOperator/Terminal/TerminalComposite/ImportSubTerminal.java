package com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.TerminalComposite;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.davideferrari.logisticsystem.Menu.CargoShipCaptain.CargoShip;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerFactory.Container;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerIterator.ContainerCollection;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerIterator.ContainerIterator;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerIterator.TerminalContainerIterator;
import com.davideferrari.logisticsystem.Utils.Annotations.AppDesignPattern;
import com.davideferrari.logisticsystem.Utils.Annotations.CapacityLimit;

/**
 *  This class represents a specialized terminal component dedicated to Import operations.
 *  In the Composite Design Pattern, this class acts as a Leaf, and holds containers
 *  that have been unloaded from a ship and are awaiting transport to the Warehouse.
 *  Role:
 *  - Receives containers from CargoShip (Unloading).
 *  - Stores them temporarily.
 *  - Dispatches them to the Warehouse via Trucks (Borrowing).
 */
@AppDesignPattern(pattern = "Iterator - Composite", justification = "Concrete Aggregate - Composite")
@CapacityLimit(value = 15)
public class ImportSubTerminal implements TerminalComponent, ContainerCollection
{
    private static final Logger logger = Logger.getLogger(ImportSubTerminal.class.getName());
    private final List<Container> localRegister = new ArrayList<>();

    private String name;
    /**
     *  The maximum number of containers this terminal can hold. 
     *  Derived from the {@link CapacityLimit} annotation.
     */
    private int minimumCapacity;

    private static boolean confirmation;

    /**
     *  This method constructs a new Import Sub-Terminal.
     *  It initializes the terminal and uses reflection to determine its storage limit.
     *  @param name The name of the parent port (e.g., "Bari", "Busan").
     */
    public ImportSubTerminal(String name)
    {
        this.name = name;

        if (this.getClass().isAnnotationPresent(CapacityLimit.class))
        {
            this.minimumCapacity = this.getClass().getAnnotation(CapacityLimit.class).value();
        }
        else
        {
            this.minimumCapacity = 20;
        }
    }

    public String getName()
    {
        return name;
    }

    @Override
    public void description()
    {
        logger.info("This is the import sub-terminal of " + this.name + " terminal.");
    }

    /**
     *  This method creates an iterator to traverse the containers currently stored in this import bay.
     *  @return A {@link TerminalContainerIterator} for the local list.
     */
    @Override
    public ContainerIterator createIterator()
    {
        return new TerminalContainerIterator(this);
    }

    @Override
    public synchronized int getSize()
    {
        return localRegister.size();
    }

    @Override
    public Container getContainerAt(int index)
    {
        return localRegister.get(index);
    }

    /**
     *  This method resets the static confirmation flag.
     *  It is called after the Terminal Operator has processed a docking request.
     */
    public static void resetRequest()
    {
        confirmation = false;
    }

    /**
     *  This method evaluates a docking request from a Cargo Ship.
     *  Logic:
     *  Checks if the ship is in a valid state.
     *  Verifies if the ship is targeting this specific port ("Bari" or "Busan").
     *  If valid, sets the global confirmation flag to true.
     *  @param ship The ship requesting docking.
     *  @param bariImp The Bari Import Terminal instance.
     *  @param busanImp The Busan Import Terminal instance.
     *  @return {@code true} if the request is valid and accepted for review.
     */
    public static boolean shipRequest(CargoShip ship, ImportSubTerminal bariImp, ImportSubTerminal busanImp)
    {
        if(ship.getRequestTarget() == null)
        {
            logger.info("No request from a ship has been received.");
        }
        else if (ship.getRequestTarget().equals("Bari") && 
                (ship.getState() == CargoShip.CargoShipState.IN_TRANSIT || ship.getState() == CargoShip.CargoShipState.WAITING))
        {
            logger.info("The " + bariImp.getName() + " import terminal has received a request from the ship captain to dock.");
            confirmation = true;
            return true;
        }
        else if(ship.getRequestTarget().equals("Busan") && 
               (ship.getState() == CargoShip.CargoShipState.IN_TRANSIT || ship.getState() == CargoShip.CargoShipState.WAITING))
        {
            logger.info("The " + busanImp.getName() + " import terminal has received a request from the ship captain to dock.");
            confirmation = true;
            return true;
        }
        else
        {
            logger.info("At the moment, the terminal cannot grant any permission.");
        }
        return false;
    }

    /**
     *  This method checks if a valid docking request is pending confirmation.
     *  @return {@code true} if the operator needs to be notified.
     */
    public static boolean sendConfirmation()
    {
        if (confirmation == true)
        {
            logger.info("The import terminal has notified the terminal operator of the request.");
            return true;
        }
        return false;
    }

    /**
     *  This method handles the physical unloading of a container from the ship to the terminal.
     *  Thread Safety: Synchronized to prevent race conditions during rapid unloading.
     *  Validation:
     *  Checks against the CapacityLimit to ensure the terminal isn't full.
     *  @param containerToAdd The container coming off the ship.
     *  @return {@code true} if space was available and the container was accepted.
     */
    public synchronized boolean shipImport(Container containerToAdd)
    {   
        if (localRegister.size() >= minimumCapacity)
        {
            logger.warning("The terminal has reached its maximum capacity. No more containers can be added");
            System.out.println(".\n.\n.\n.\n.");
            return false;
        }
        else
        {
            containerToAdd.setContainerState(Container.ContainerState.FULL_IMPORT);
            containerToAdd.setLocation(this.name + " Import Sub-Terminal");
            localRegister.add(containerToAdd);
            logger.info("Container " + containerToAdd.getContainerCode() + " unloaded successfully.");
            System.out.println(".\n.\n.\n.\n.");
            return true;
        }
    }

    /**
     *  This method Facilitates the transfer of goods from the Terminal to a Warehouse Truck.
     *  @param goods The type of goods the truck is looking for (e.g., FOOD, ELECTRONICS).
     *  @return The matching {@link Container} if found and removed, otherwise {@code null}.
     */
    public synchronized Container borrowFromWarehouse(Container.GoodsType goods)
    {
        ContainerIterator containerIterator = this.createIterator();
        while (containerIterator.hasNext())
        {
            Container c = containerIterator.next();

            if (c.getContainerState() == Container.ContainerState.FULL_IMPORT && c.getGoods() == goods)
            {
                localRegister.remove(c);   
                logger.info("The import sub-terminal has permitted the picking of the following containaer: " + c.getContainerCode());
                return c;
            }
        }
        logger.warning("Warehouse attempted to borrow a container, but no full containers were found in the Import Sub-Terminal.");
        return null;
    }

}