package com.davideferrari.logisticsystem.Menu.CargoShipCaptain;

import java.util.Scanner;
import java.util.logging.Logger;
import com.davideferrari.logisticsystem.Utils.ExceptionHandling.*;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.TerminalComposite.ExportSubTerminal;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.TerminalComposite.ImportSubTerminal;

/**
 *  This class handles the interface and interaction logic for the "Cargo Ship Captain" role.
 *  It manages the ship's lifecycle events, including:
 *  - Checking the ship's current state (Transit, Docked, Waiting).
 *  - Requesting permission to dock at a port.
 *  - Executing Import (Unloading) and Export (Loading) operations.
 *  - Requesting permission to undock and sail to the next destination.
 */
public class CargoShipCaptainMenu
{
    private static final Logger logger = Logger.getLogger(CargoShipCaptainMenu.class.getName());

    /**
     *  This method provides the main execution loop for the Captain's menu, 
     *  keeping the user in the Captain role until they choose to exit. 
     *  It routes user input to specific helper methods (`docking`, `operations`, `undocking`)
     *  and handles any exceptions that occur during these processes via the Exception Shielding Pattern
     *
     *  @param option    The system scanner for reading user input.
     *  @param ship      The instance of the ship currently being controlled.
     *  @param bariImp   The Import Sub-Terminal for Bari.
     *  @param busanImp  The Import Sub-Terminal for Busan.
     *  @param bariExp   The Export Sub-Terminal for Bari.
     *  @param busanExp  The Export Sub-Terminal for Busan.
     */
    public static void menu(Scanner option, CargoShip ship, ImportSubTerminal bariImp, ImportSubTerminal busanImp, ExportSubTerminal bariExp, ExportSubTerminal busanExp)
    {
        boolean menuLoop = true;
        while (menuLoop)
        {
            try
            {                    
                logger.info("\nYou are the cargo ship captain. Select one of the following operations: \n 1. Cargo Ship State. \n 2. Port Docking. \n 3. Operations. \n 4. Display Containers. \n 5. Leave the Port. \n 6. Exit. \nProvide an answer with the number related to the desired option: ");
                String decision = option.nextLine();
                switch (decision)
                {
                    case "1":
                    {
                        logger.info("Selected option: " + decision + ". Cargo ship state.");
                        boolean infoLoop = true;
                        while (infoLoop)
                        {
                            try 
                            {    
                                System.out.println(".\n.\n.\n.\n.");
                                ship.currentState();
                            }
                            catch (Exception e)
                            {
                                ExceptionShieldingHandler.handleException(e);
                            }
                            infoLoop = false;
                        }
                        break;
                    }
                    case "2":
                    {
                        logger.info("Selected option: " + decision + ". Port Docking.");
                            try 
                            {    
                                System.out.println(".\n.\n.\n.\n.");
                                CargoShipCaptainMenu.docking(option, ship);
                                
                            }
                            catch (Exception e)
                            {
                                ExceptionShieldingHandler.handleException(e);
                            }
                            break;
                    }
                    case "3":
                    {
                        logger.info("Selected option: " + decision + ". Operations.");
                        boolean infoLoop = true;
                        while (infoLoop)
                        {
                            try 
                            {    
                                System.out.println(".\n.\n.\n.\n.");
                                CargoShipCaptainMenu.operations(option, ship);
                                
                            }
                            catch (Exception e)
                            {
                                ExceptionShieldingHandler.handleException(e);
                            }
                            infoLoop = false;
                        }
                        break;
                    }
                    case "4":
                    {
                        logger.info("Selected option: " + decision + ". Display Containers.");
                        boolean infoLoop = true;
                        while (infoLoop)
                        {
                            try 
                            {    
                                System.out.println(".\n.\n.\n.\n.");
                                ship.displayCargoContainers();
                                
                            }
                            catch (Exception e)
                            {
                                ExceptionShieldingHandler.handleException(e);
                            }
                            infoLoop = false;
                        }
                        break;
                    }
                    case "5":
                    {
                        logger.info("Selected option: " + decision + ". Leave the port.");
                        boolean infoLoop = true;
                        while (infoLoop)
                        {
                            try 
                            {    
                                System.out.println(".\n.\n.\n.\n.");
                                CargoShipCaptainMenu.undocking(option, ship, busanImp, bariImp, busanExp, bariExp);                                    
                            }
                            catch (Exception e)
                            {
                                ExceptionShieldingHandler.handleException(e);
                            }
                            infoLoop = false;
                        }
                        break;
                    }
                    case "6":
                    {
                        System.out.println(".\n.\n.\n.\n.");
                        logger.info("Exited from the cargo ship captain menu.");
                        menuLoop = false;
                        break;
                    }
                    default:
                    {
                        throw new MenuValidationException("You have selected an invalid option. Please try again.", null);
                    }
                }
            }
            catch (MenuValidationException e)
            {
                ExceptionShieldingHandler.handleException(e);
            }
            catch (Exception e)
            {
                ExceptionShieldingHandler.handleException(e);
            }
        }
    }

    /**
     *  This method manages the docking procedure,by allowing the captain
     *  to send a docking request to the target terminal and subsequently
     *  ask for confirmation from the Terminal Operator.
     *  @param option The scanner for user input.
     *  @param ship   The ship attempting to dock.
     *  @return {@code true} if the menu interaction completed successfully, {@code false} to exit.
     *  @throws MenuValidationException If the user selects an invalid menu option.
     */
    public static boolean docking(Scanner option, CargoShip ship) throws MenuValidationException
    {
        logger.info("You decided to dock in the port. What actions do you intend to take?\n 1. Request Docking. \n 2. Ask for Confirmation. \n 3. Exit.\n Provide an answer with the number related to the desired option: ");
        String displayOptions = option.nextLine();
        System.out.println("-");
        switch (displayOptions)
        {
            case "1":
            {
                logger.info("Selected option: " + displayOptions + ". Docking Request.");
                ship.dockingRequest();
                break;
            }
            case "2":
            {
                logger.info("Selected option: " + displayOptions + ". Docking Confirmation.");
                ship.dockingConfirmation();
                break;
            }
            case "3":
            {
                logger.info("You are going back to the menu.");
                return false;
            }
            default:
            {
                throw new MenuValidationException("You have selected an invalid option. Please try again.", null);
            }
        }
        return true;
    }

    /**
     *  This method manages the loading and unloading operations,
     *  and enforces a strict sequence of operations:
     *  - Checks if operations are already concluded.
     *  - Checks if the ship is properly docked.
     *  - Allows the user to trigger Import (unloading to terminal) or Export (loading from terminal).
     * @param option The scanner for user input.
     * @param ship   The ship performing the operations.
     * @return {@code true} if operations proceeded, {@code false} if blocked or exited.
     * @throws MenuValidationException If the user selects an invalid menu option.
     */
    public static boolean operations(Scanner option, CargoShip ship) throws MenuValidationException
    {
        if(ship.isEndImportAndExport())
        {
            logger.warning("The operations for this port have been concluded. The ship must undock and set sail for the new destination");
            return false;
        }
        if(!ship.operationConfirmation())
        {
            return false;
        }
        else
        {
            logger.info("You can decide which operations to implement in the current port.\n 1. Import Operation. \n 2. Export Operation. \n 3. Exit.\n Provide an answer with the number related to the desired option: ");
            String displayOptions = option.nextLine();
            System.out.println("-");
            switch (displayOptions)
            {
                case "1":
                {
                    logger.info("Selected option: " + displayOptions + ". Import Operation.");
                    ship.dropInTerminal();
                    break;
                }
                case "2":
                {
                    logger.info("Selected option: " + displayOptions + ". Export Operation.");
                    if (!ship.terminalSwitchConfirmation())
                    {
                        return false;
                    }
                    else
                    {
                        logger.info("The export operation was succesfull.");
                    }
                    break;
                }
                case "3":
                {
                    logger.info("You are going back to the menu.");
                    return false;
                }
                default:
                {
                    throw new MenuValidationException("You have selected an invalid option. Please try again.", null);
                }
            }
        return true;
        }
    }

    /**
     *  This method manages the undocking procedure,
     *  where the captain is allowed to request permission to leave the port. 
     *  If confirmation is granted, the ship's state is updated as in a transit state, 
     *  and its destination is swapped (e.g., from Bari to Busan or vice-versa).
     *  @param option   The scanner for user input.
     *  @param ship     The ship attempting to leave.
     *  @param busanImp Reference to Busan Import Terminal (for route updates).
     *  @param bariImp  Reference to Bari Import Terminal (for route updates).
     *  @param busanExp Reference to Busan Export Terminal (for route updates).
     *  @param bariExp  Reference to Bari Export Terminal (for route updates).
     *  @return {@code true} if the menu interaction completed successfully.
     *  @throws MenuValidationException If the user selects an invalid menu option.
     */
    public static boolean undocking(Scanner option, CargoShip ship, ImportSubTerminal busanImp, ImportSubTerminal bariImp, ExportSubTerminal busanExp, ExportSubTerminal bariExp) throws MenuValidationException
    {
        logger.info("You decided to leave port. What actions do you intend to take?\n 1. Request Undocking. \n 2. Ask for Confirmation. \n 3. Exit.\n Provide an answer with the number related to the desired option: ");
        String displayOptions = option.nextLine();
        System.out.println("-");
        switch (displayOptions)
        {
            case "1":
            {
                logger.info("Selected option: " + displayOptions + ". Undocking Request.");
                ship.undockingRequest();
                break;
            }
            case "2":
            {
                logger.info("Selected option: " + displayOptions + ". Ask for Confirmation From the Terminal.");
                ship.undockingConfirmation(busanImp, bariImp, busanExp, bariExp);
                break;
            }
            case "3":
            {
                logger.info("You are going back to the menu.");
                return false;
            }
            default:
            {
                throw new MenuValidationException("You have selected an invalid option. Please try again.", null);
            }
        }
    return true;
    }

}