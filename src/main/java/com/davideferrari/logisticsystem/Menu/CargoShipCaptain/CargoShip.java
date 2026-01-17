package com.davideferrari.logisticsystem.Menu.CargoShipCaptain;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.TerminalOperatorMenu;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerFactory.Container;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerIterator.ContainerCollection;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerIterator.ContainerIterator;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerIterator.TerminalContainerIterator;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.TerminalComposite.ExportSubTerminal;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.TerminalComposite.ImportSubTerminal;
import com.davideferrari.logisticsystem.Utils.Annotations.CapacityLimit;

/**
 *  This class represents Cargo Ship, which is the central entity in the transposrt cycle.
 *  This class acts as the primary "Producer" of containers for the Import cycle and the 
 * "Consumer" for the Export cycle.
 */
@CapacityLimit(value = 10)
public class CargoShip implements ContainerCollection
{
    private static final Logger logger = Logger.getLogger(CargoShip.class.getName());
    private final List<Container> cargoContainers = new ArrayList<>();

    private final String name = "HELEN III";
    private String destination = "Bari";
    private CargoShipState state = CargoShipState.IN_TRANSIT;
    private int maximumCapacity;

    private ImportSubTerminal destinationTerminal;
    private ImportSubTerminal currentImportTerminal;
    private ExportSubTerminal currentExportTerminal;

    private String requestTarget;
    private boolean endImportAndExport;

    /**
     *  Thic method Constructs a new CargoShip instance,
     *  which initializes the ship with a specific route
     *  and retrieves the maximum capacity limit from the annotation via reflection.
     *  @param destinationTerminal      The Import Terminal where the ship is currently headed.
     *  @param currentExportSubTerminal The Export Terminal associated with the destination (for future loading).
     */
    public CargoShip(ImportSubTerminal destinationTerminal, ExportSubTerminal currentExportSubTerminal)
    {
        this.destinationTerminal = destinationTerminal;
        this.currentExportTerminal = currentExportSubTerminal;

        if (this.getClass().isAnnotationPresent(CapacityLimit.class))
        {
            this.maximumCapacity = this.getClass().getAnnotation(CapacityLimit.class).value();
        } 
        else 
        {
            this.maximumCapacity = 10;
        }
    }

    /**
     *  Defines the possible states of the ship during its lifecycle.
     */
    public enum CargoShipState
    {
        IN_TRANSIT,
        WAITING,
        DOCKED_FOR_IMPORT,
        DOCKED_FOR_EXPORT
    }

    public String getName()
    {
        return name;
    }

    public CargoShipState getState()
    {
        return state;
    }

    public String getRequestTarget()
    {
        return requestTarget;
    }

    public String getDestination()
    {
        return destination;
    }

    public boolean isEndImportAndExport()
    {
        return endImportAndExport;
    }

    private void setState(CargoShipState state)
    {
        this.state = state;
    }

    private void setRequestTarget(String requestTarget)
    {
        this.requestTarget = requestTarget;
    }

    public void setDestination(String destination)
    {
        this.destination = destination;
    }

    public void setDestinationTerminal(ImportSubTerminal destinationTerminal)
    {
        this.destinationTerminal = destinationTerminal;
    }

    /**
     *  This method creates an iterator to traverse the containers
     *  currently on board the ship.
     *  @return A concrete {@link TerminalContainerIterator}.
     */
    @Override
    public ContainerIterator createIterator()
    {
        return new TerminalContainerIterator(this);
    }

    @Override
    public int getSize()
    {
        return cargoContainers.size();
    }

    @Override
    public Container getContainerAt(int index)
    {
        return cargoContainers.get(index);
    }

    public boolean containsContainer(Container container)
    {
        return cargoContainers.contains(container);
    }

    /**
     *  This method logs the current status and location of the ship.
     */
    public void currentState()
    {
        if (this.getState() == CargoShipState.IN_TRANSIT)
        {
            logger.info("The " + this.getName() + " ship is currently in transit.\n--- Destination: " + destinationTerminal.getName() + " port.");
        }
        else if (this.getState() == CargoShipState.DOCKED_FOR_IMPORT)
        {
            logger.info("The " + this.getName() + " ship is docked in the port.\n--- Currently residing in " + currentImportTerminal.getName() + " import sub-terminal.");
        }
        else if (this.getState() == CargoShipState.DOCKED_FOR_EXPORT)
        {
            logger.info("The" + this.getName() +  "ship is docked in the port.\n--- Currently residing in " + currentExportTerminal.getName() + " export sub-terminal.");
        }
        else
        {
            logger.info("The " + this.getName() + " ship is outside the port, waiting for confirmation to dock.\n--- Destination: " + destinationTerminal.getName() + " port.");
        }
        System.out.println(".\n.\n.\n.\n.");
    }

    /**
     *  This method initiates the docking sequence by changing the state to {@code WAITING}.
     */
    public void dockingRequest()
    {
        logger.info("A request to dock has been made to the terminal of " + destinationTerminal.getName());
        this.requestTarget = destinationTerminal.getName();
        this.setState(CargoShipState.WAITING); 
        System.out.println(".\n.\n.\n.\n.");
    }

    /**
     *  This method finalizes the docking sequence if permission is granted by the Terminal Operator.
     *  @return {@code true} if docked successfully, {@code false} otherwise.
     */
    public boolean dockingConfirmation()
    {
        if (TerminalOperatorMenu.sendDockConfirmationtoShip() == true)
        {
            logger.info("The terminal of " + destinationTerminal.getName() + " has granted the permission to dock. The ship can now proceed and position itself inside the bay.");
            this.setState(CargoShipState.DOCKED_FOR_IMPORT);
            this.currentImportTerminal = this.destinationTerminal;
            this.setRequestTarget(null);
            System.out.println(".\n.\n.\n.\n.");
            return true;
        }
        else
        {
            logger.info("No confirmation was given from the terminal of " + destinationTerminal.getName() + ". The ship must wait.");
            this.setState(CargoShipState.WAITING);
            System.out.println(".\n.\n.\n.\n.");
        }
        return false;
    }

    /**
     * This method validates if the ship is in a correct state to perform cargo operations.
     *  @return {@code true} if docked (Import or Export), {@code false} otherwise.
     */
    public boolean operationConfirmation()
    {
        if (this.getState() == CargoShipState.DOCKED_FOR_IMPORT || this.getState() == CargoShipState.DOCKED_FOR_EXPORT)
        {
            logger.info("The import and export operations are possible. You can proceed.");
            System.out.println(".\n.\n.\n.\n.");
            return true;
        }
        else
        {
            logger.warning("The import and export operations are not possible if the ship is not docked in the appropriate port.");
            System.out.println(".\n.\n.\n.\n.");
        }
        return false;
    }

    /**
     *  This method executes the Import Operation (Unloading), which
     *  iterates through the ship's cargo and attempts to offload each container: 
     *  - If the terminal is full, the container remains on board.
     *  - If all containers are unloaded, the ship automatically transitions to {@code DOCKED_FOR_EXPORT}.
     *  @return {@code true} if the operation completed (even if some containers failed), {@code false} if a critical error occurred.
     */
    public boolean dropInTerminal()
    {
        logger.info("The ship is checking the containers to unload:");
        List<Container> successfullyUnloaded = new ArrayList<>();
        
        if(cargoContainers.isEmpty())
        {
            logger.warning("The cargo ship does not have any containers to import in the terminal. No import operation is required.");
            System.out.println(".\n.");
            logger.info("The ship will move to the export sub-terminal, in order to perform the export operation and load the containers.");
            System.out.println(".\n.\n.\n.\n.");
            this.setState(CargoShipState.DOCKED_FOR_EXPORT);
            return true;
        }
        else
        {
            for (Container container : cargoContainers)
            {
                if (currentImportTerminal.shipImport(container))
                {
                    successfullyUnloaded.add(container);
                }
                else
                {
                    logger.warning("Container " + container.getContainerCode() + " remains on the ship.");
                    System.out.println(".\n.\n.\n.\n.");
                }
            }
            cargoContainers.removeAll(successfullyUnloaded);
            
            if (cargoContainers.isEmpty())
            {
                logger.info("All containers have been unloaded.");
                System.out.println(".\n.\n.\n.\n.");
                this.setState(CargoShipState.DOCKED_FOR_EXPORT);
                return true;
            } 
            else
            {
                logger.warning("Ship still has containers because the terminal was full. The ship will remain at the import sub-terminal. Please free up the terminal space.");
                System.out.println(".\n.\n.\n.\n.");
                return false; 
            }
        }
    }

    public void resetOperationStatus()
    {
        this.endImportAndExport = false;
    }

    /**
     *  This method executes the Export Operation (Loading) by interacting with the Export Terminal.
     *  @return {@code true} if the export process initiates successfully.
     */
    public boolean terminalSwitchConfirmation()
    {
        if (this.getState() == CargoShipState.DOCKED_FOR_EXPORT)
        {
            try
            {
                currentExportTerminal.shipExport(this);
                return true;
            }
            catch (Exception e)
            {
                logger.warning("Export failed: " + e.getMessage());
                System.out.println(".\n.\n.\n.\n.");
                return false;
            }
        } 
        else 
        {
            logger.warning("Cannot start export: Ship is not docked at an Export Terminal.");
            System.out.println(".\n.\n.\n.\n.");
        }
        return false;
    }

    /**
     *  This method loads a single container onto the ship from the terminal,
     *  and checks against the ship's {@code maximumCapacity} before accepting the container.
     *  @param container The container to be loaded.
     *  @return {@code true} if loaded successfully, {@code false} if capacity is full.
     */
    public boolean pickFromTerminal(Container container)
    {
        if (cargoContainers.size() >= maximumCapacity)
        {
            logger.warning("The ship's maximum container capacity has reached its limit. No other containers can be loaded for the export operation.");
            System.out.println(".\n.\n.\n.\n.");
            return false;
        }
            container.setLocation(this.name + " Cargo ship");
            cargoContainers.add(container);
            logger.info("The " + container.getType() + " container " + container.getContainerCode() + " has been successfully loaded on the ship.");
            endImportAndExport = true;
            return true;   
    }

    public void displayCargoContainers()
    {
        logger.info("Displaying the containers of the " + this.name + " cargo ship:");
        logger.info("Current cargo:" + cargoContainers.size());
        ContainerIterator containerIterator = this.createIterator();
        while (containerIterator.hasNext())
        {
            Container container = containerIterator.next();
            logger.info("Container: " + container.getContainerCode() + " | Type: " + container.getType());
        }
    }

    public void undockingRequest()
    {
        if(this.getState() == CargoShipState.IN_TRANSIT)
        {
            logger.warning("Ship is already in transit.");
            System.out.println(".\n.\n.\n.\n.");
        }
        else
        {
            logger.info("A request to undock has been made to " + currentExportTerminal.getName());
            System.out.println(".\n.\n.\n.\n.");
            this.requestTarget = currentExportTerminal.getName();
            this.endImportAndExport = true; 
        }
    }

    /**
     *  This method finalizes the departure of the ship,
     *  given by the confirmation of the Terminal Operator,
     *  where the ship is set to a transit state,
     *  and the destination logic is swapped (e.g. from Bari to Busan).
     *  @param busanImp Busan Import Terminal reference.
     *  @param bariImp  Bari Import Terminal reference.
     *  @param busanExp Busan Export Terminal reference.
     *  @param bariExp  Bari Export Terminal reference.
     *  @return {@code true} if the ship successfully leaves the port.
     */
    public boolean undockingConfirmation(ImportSubTerminal busanImp, ImportSubTerminal bariImp, ExportSubTerminal busanExp, ExportSubTerminal bariExp)
    {
        if (TerminalOperatorMenu.sendUndockConfirmationtoShip() == true)
        {
            logger.info("The terminal of " + currentExportTerminal.getName() + " has granted the permission to undock. The ship can now proceed and leave the bay.");
            System.out.println(".\n.\n.\n.\n.");
            this.setState(CargoShipState.IN_TRANSIT);
            this.endImportAndExport = false;
            this.currentImportTerminal = null;
            this.requestTarget = null;
            if (this.getDestination().equals("Bari"))
            {
                logger.info("The ship is leaving Bari. Next destination: Busan.");
                System.out.println(".\n.\n.\n.\n.");
                this.setDestination("Busan");
                this.setDestinationTerminal(busanImp);
                this.currentExportTerminal = busanExp;
            }
            else
            {
                logger.info("The ship is leaving Busan. Next destination: Bari.");
                System.out.println(".\n.\n.\n.\n.");
                this.setDestination("Bari");
                this.setDestinationTerminal(bariImp);
                this.currentExportTerminal = bariExp;
            }
            return true;
        }
        else
        {
            logger.info("Confirmation negative. The ship must wait.");
            System.out.println(".\n.\n.\n.\n.");
            this.setState(CargoShipState.WAITING);
        }
        return false;
    }
}
