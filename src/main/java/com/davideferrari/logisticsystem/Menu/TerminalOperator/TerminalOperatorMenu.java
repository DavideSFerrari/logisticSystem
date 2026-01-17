package com.davideferrari.logisticsystem.Menu.TerminalOperator;

import java.util.Scanner;
import java.util.logging.Logger;
import com.davideferrari.logisticsystem.Menu.CargoShipCaptain.CargoShip;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerFactory.BoxSelector;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerFactory.Container;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerFactory.HighCubeSelector;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.TerminalComposite.ExportSubTerminal;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.TerminalComposite.ImportSubTerminal;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.TerminalComposite.Terminal;
import com.davideferrari.logisticsystem.Utils.ExceptionHandling.ContainerValidationException;
import com.davideferrari.logisticsystem.Utils.ExceptionHandling.ExceptionShieldingHandler;
import com.davideferrari.logisticsystem.Utils.ExceptionHandling.MenuValidationException;

/**
 *  This class handles the interface and interaction logic for the "Terminal Operato Menu" role.
 *  It acts as the control center for managing port operations, Allowing the operator to:
 *  - Inspect Terminal status and hierarchy.
 *  - Create and Add new containers using the Factory Pattern.
 *  - Remove containers from specific terminals.
 *  - Manage Docking and Undocking requests from the {@link CargoShip}.
 */
public class TerminalOperatorMenu
{

    private static final Logger logger = Logger.getLogger(TerminalOperatorMenu.class.getName());
    private static boolean confirmation;

    /**
     *  This method provides the main execution loop for the Terminal Operator's menu.
     *  It routes user input to specific helper methods
     *  (`terminal info`, `create and add container`, `remove`, `display` `ship requests`)
     *  and handles any exceptions that occur during these processes via the Exception Shielding Pattern
     *  @param option   The system scanner for reading user input.
     *  @param bari     The main Terminal object for Bari.
     *  @param bariExp  The Export Sub-Terminal for Bari.
     *  @param busan    The main Terminal object for Busan.
     *  @param busanExp The Export Sub-Terminal for Busan.
     *  @param bariImp  The Import Sub-Terminal for Bari.
     *  @param busanImp The Import Sub-Terminal for Busan.
     *  @param ship     The active Cargo Ship instance.
     */
    public static void menu(Scanner option, Terminal bari, ExportSubTerminal bariExp, Terminal busan, ExportSubTerminal busanExp, ImportSubTerminal bariImp, ImportSubTerminal busanImp, CargoShip ship)
    {
            boolean menuLoop = true;
            while (menuLoop)
            {
                try
                {                    
                    logger.info("\nYou are the terminal operator. Select one of the following operations: \n 1. Terminal Info. \n 2. Create and Add a Container. \n 3. Remove a Container. \n 4. Display Containers. \n 5. Cargo Ship Requests. \n 6. Exit. \nProvide an answer with the number related to the desired option: ");
                    String decision = option.nextLine();
                    switch (decision)
                    {
                        case "1":
                        {
                            logger.info("Selected option: " + decision + ". Terminal Info.");
                            boolean infoLoop = true;
                            while (infoLoop)
                            {
                                try 
                                {    
                                    System.out.println(".\n.\n.\n.\n.");
                                    TerminalOperatorMenu.selectTerminaForInfo(option, bari, busan);
                                }
                                catch (MenuValidationException e)
                                {
                                    ExceptionShieldingHandler.handleException(e);
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
                            logger.info("Selected option: " + decision + ". Create and Add a Container.");
                            boolean createAddLoop = true;
       	                    while (createAddLoop)
	                        {
                                try
                                {
                                    System.out.println(".\n.\n.\n.\n.");
                                    TerminalOperatorMenu.selectTerminalForCreation(option, bariExp, busanExp);
                                }
                                catch (MenuValidationException e)
                                {
                                    ExceptionShieldingHandler.handleException(e);
                                }
                                catch (Exception e)
                                {
                                    ExceptionShieldingHandler.handleException(e);
                                }
                                createAddLoop = false;
                            }
                            break;
                        }
                        case "3":
                        {
                            logger.info("Selected option: " + decision + ". Remove a Container.");
                            boolean deleteLoop = true;
                            while (deleteLoop)
                            {
                                try
                                {
                                    System.out.println(".\n.\n.\n.\n.");
                                    TerminalOperatorMenu.selectTerminalForRemoval(option, bariExp, busanExp, ship);
                                }
                                catch (MenuValidationException e)
                                {
                                    ExceptionShieldingHandler.handleException(e);
                                }
                                catch (Exception e)
                                {
                                    ExceptionShieldingHandler.handleException(e);
                                }
                                deleteLoop = false;
                            }
                            break;
                        }
                        case "4":
                        {
                            logger.info("Selected option: " + decision + ". Display Containers.");
                            boolean listLoop = true;
                            while (listLoop)
                            {
                                try 
                                {
                                    {
                                        System.out.println(".\n.\n.\n.\n.");
                                        TerminalOperatorMenu.selectTerminalForIteration(option, bariExp, busanExp);
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
                                listLoop = false;
                            }
                            break;
                        }
                        case "5":
                        {
                            logger.info("Selected option: " + decision + ". Cargo Ship Requests.");
                            boolean listLoop = true;
                            while (listLoop)
                            {
                                try 
                                {
                                    {
                                        System.out.println(".\n.\n.\n.\n.");
                                        TerminalOperatorMenu.selectShipRequest(option, ship, bariImp, busanImp, bariExp, busanExp);
                                    }
                                }
                                catch (Exception e)
                                {
                                    ExceptionShieldingHandler.handleException(e);
                                }
                                listLoop = false;
                            }
                            break;
                        }
                        case "6":
                        {
                            System.out.println(".\n.\n.\n.\n.");
                            logger.info("Exited from the terminal operator menu.");
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
     *  This method provides the sub-menu for viewing terminal information.
     *  It allows the user to select between Bari or Busan to view their descriptions and sub-components.
     */
    public static boolean selectTerminaForInfo(Scanner option, Terminal bari, Terminal busan) throws MenuValidationException
    {
        logger.info("From which terminal do you want to retrieve info? \n 1. Bari terminal info. \n 2. Busan terminal info. \n 3. Back to the menu. \nProvide an answer with the number related to the desired option: ");
        String selectedTerminal = option.nextLine();
        System.out.println("-");
        switch (selectedTerminal)
        {
            case "1":
            {
                TerminalOperatorMenu.terminalInfo(option, selectedTerminal, bari, "Bari");
                break;
            }
            case "2":
            {
                TerminalOperatorMenu.terminalInfo(option, selectedTerminal, busan, "Busan");
                break;
            }
            case "3":
            {   
                logger.info("You are going back to the menu.");
                System.out.println(".\n.\n.\n.\n.");
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
     *  This method displays details for a specific terminal using the Composite Pattern's `displaySubTerminals` method.
     */
    public static void terminalInfo(Scanner option, String selectedTerminal, Terminal terminal, String name) throws MenuValidationException
    {
        logger.info("Selected option: " + selectedTerminal + ". " + name  + " terminal.");
        System.out.println(".\n.\n.\n.\n.");
        terminal.description();
        terminal.displaySubTerminals();
        try
        {
            logger.info("Do you wish to look for other terminal info? \n 1. Yes. \n 2. No, take me back to the menu.\n Provide an answer with the number related to the desired option: ");
            String next = option.nextLine();
            switch (next)
            {
                case "1":
                {
                    logger.info("Selected option: " + next + ". You have decided to look for other terminal info.");
                    break;
                }
                case "2":
                {
                    logger.info("You are going back to the menu.");
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

    /**
     *  This method initiates the container creation workflow, asking the user to select the target terminal
     *  (Bari or Busan) before proceeding to container type selection.>
     */
    public static boolean selectTerminalForCreation(Scanner option, ExportSubTerminal bariExp, ExportSubTerminal busanExp) throws MenuValidationException
    {
        logger.info("In which terminal do you want to create and add a container? \n 1. Bari terminal. \n 2. Busan terminal.\n 3. Exit. \nProvide an answer with the number related to the desired option: ");
        String terminalSelection = option.nextLine();
        System.out.println("-");
        switch (terminalSelection)
        {
            case "1":
            {
                logger.info("Selected option: " + terminalSelection + ". Bari terminal. The new container will be created and added here.");
                String name = "Bari";
                System.out.println(".\n.\n.\n.\n.");
                TerminalOperatorMenu.containerTypeSelection(option, bariExp, name);
                break;
            }
            case "2":
            {
                logger.info("Selected option: " + terminalSelection + ". Busan terminal. The new container will be created and added here.");
                String name = "Busan";
                System.out.println(".\n.\n.\n.\n.");
                TerminalOperatorMenu.containerTypeSelection(option, busanExp, name);
                break;
            }
            case "3":
            {
                logger.info("You are going back to the menu.");
                System.out.println(".\n.\n.\n.\n.");
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
     *  This method routes the user to the specific factory method based on the desired container type (High Cube or Box).
     */
    public static void containerTypeSelection(Scanner option, ExportSubTerminal subTerminal, String name) throws MenuValidationException
    {
        logger.info("What type of container do you want to create? \n 1. High Cube. \n 2. Box.\nProvide an answer with the number related to the desired option: ");
        String containerSelection = option.nextLine();
        System.out.println("-");
        switch (containerSelection)
	    {
		    case "1":
            {
                boolean highCubeLoop = true;
                while (highCubeLoop)
                {
                    try 
                    {
                        logger.info("Do you want to create and add a new high cube container to the terminal? \n 1. Yes.\n 2. No, exit.\n Provide an answer with the number related to the desired option: ");
                        String next = option.nextLine();
                        System.out.println("-");
                        switch (next)
                        {
                            case "1":
                            {
                                TerminalOperatorMenu.createHighCube(option, subTerminal, name);
                                break;
                            }
                            case "2":
                            {
                                logger.info("You are going back to the menu.");
                                System.out.println(".\n.\n.\n.\n.");
                                highCubeLoop = false;
                                break;
                            }
                            default:
                            {
                                throw new MenuValidationException("You have selected an invalid option. Please try again.", null);
                            }
                        }    
                    }                            
                    catch (ContainerValidationException e)
                    {
                        ExceptionShieldingHandler.handleException(e);
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
                break;
            }
		    case "2":
            {
                boolean boxLoop = true;
                while (boxLoop)
                {
                    try 
                    {
                        logger.info("Do you want to create and add a new box container to the terminal? \n 1. Yes.\n 2. No, exit.\n Provide an answer with the numberrelated to the desired option: ");
                        String next = option.nextLine();
                        System.out.println("-");
                        switch (next)
                        {
                            case "1":
                            {
                                TerminalOperatorMenu.createBox(option, subTerminal, name);
                                break;
                            }
                            case "2":
                            {
                                logger.info("You are going back to the menu.");
                                System.out.println(".\n.\n.\n.\n.");
                                boxLoop = false;
                                break;
                            }
                            default:
                            {
                                throw new MenuValidationException("You have selected an invalid option. Please try again.", null);
                            }
                        }
                    }                            
                    catch (ContainerValidationException e)
                    {
                        ExceptionShieldingHandler.handleException(e);
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
                break;
            }
            default:
            {
                throw new MenuValidationException("You have selected an invalid option. Please try again.", null);
            }
        }
    }

    /**
     *  This method defines the logic for creating a High Cube container.
     *  Validation Rules:
     *  - Must start with exactly 4 alphabet characters.
     *  - Must be followed by 8 digits (automatically formatted).
     *  If validation passes, the container is created and added to the terminal.
     *  @throws ContainerValidationException If the alphanumeric code format is incorrect.
     */
    public static void createHighCube(Scanner option, ExportSubTerminal subTerminal, String name) throws ContainerValidationException
    {
        logger.info("A new high cube container will be created and added to the terminal of " + name + ".");
        System.out.println(".\n.\n.\n.\n.");
        HighCubeSelector highCube = new HighCubeSelector();
        logger.info("Write the first four symbols representing the container code: They must be characters.");
        logger.info("Characters: ");
        String rawInput = option.nextLine();
        String characterCode = rawInput.toUpperCase().trim().replaceAll("[^A-Z]", "");
        if (characterCode.length() != 4)
        {
    	    throw new ContainerValidationException("The container code must have four initial characters.", null);    
        }
        logger.info("Integers: ");
        int intCode = getSafeIntInput(option);
        String containerCode = characterCode + String.format("%08d", intCode);
        Container highCubeContainer = highCube.registerContainer(containerCode);
        boolean success = subTerminal.addContainer(highCubeContainer, containerCode);
        if (success)
        {
            logger.info(highCubeContainer.getType() + " container " + containerCode + " created and succesfully added to the terminal...");
        } 
        else
        {
            throw new ContainerValidationException(containerCode, null);
        }
    }

    /**
     *  This method defines the logic for creating a Box container.
     *  Validation Rules:
     *  - Must start with exactly 4 alphabet characters.
     *  - Must be followed by 8 digits (automatically formatted).
     *  If validation passes, the container is created and added to the terminal.
     *  @throws ContainerValidationException If the alphanumeric code format is incorrect.
     */
    public static void createBox(Scanner option, ExportSubTerminal subTerminal, String name) throws ContainerValidationException
    {
        logger.info("A new box container will be created and added to the terminal of " + name + ".");
        System.out.println(".\n.\n.\n.\n.");
        BoxSelector box = new BoxSelector();
        logger.info("Write the first four symbols representing the container code: They must be characters.");
        logger.info("Characters: ");
        String rawInput = option.nextLine();
        String characterCode = rawInput.toUpperCase().trim().replaceAll("[^A-Z]", "");
        if (characterCode.length() != 4)
        {
            throw new ContainerValidationException("The container code must have four initial characters.", null);    
        }
        logger.info("Integers: ");
        int intCode = getSafeIntInput(option);      
        String containerCode = characterCode + String.format("%08d", intCode);
        Container boxContainer = box.registerContainer(containerCode);
        boolean success = subTerminal.addContainer(boxContainer, containerCode);
        if (success)
        {
            logger.info(boxContainer.getType() + " container " + containerCode + " created and succesfully added to the terminal...");
        }
        else
        {
            throw new ContainerValidationException(containerCode, null);
        }
    }

    /**
     *  This method provides an interactive menu for removing containers, 
     *  and allows the user to browse containers in Bari, Busan, or Both, and input a code for global removal.
     */
    public static boolean selectTerminalForRemoval(Scanner option, ExportSubTerminal bariExp, ExportSubTerminal busanExp, CargoShip ship) throws MenuValidationException, ContainerValidationException
    {
        logger.info("From which terminal do you want to remove a container? \n 1. Bari terminal. \n 2. Busan terminal. \n 3. Both terminals. \n 4. Exit.\n Provide an answer with the number related to the desired option: ");
        String displayOptions = option.nextLine();
        System.out.println("-");
        switch (displayOptions)
            {
                case "1":
                {
                    logger.info("Selected option: " + displayOptions + ". Bari terminal containers.");
                    bariExp.displayTerminalContainers();
                    System.out.println("---------");
                    logger.info("Write down which container you want to remove from the terminal: ");
                    String rawInput = option.nextLine();
                    String selectedContainer = rawInput.toUpperCase().trim().replaceAll("[^A-Z0-9]", "");
                    Terminal.removeGlobally(selectedContainer, bariExp, busanExp, ship);
                    break;
                }
                case "2":
                {
                    logger.info("Selected option: " + displayOptions + ". Busan terminal containers.");
                    busanExp.displayTerminalContainers();
                    System.out.println("---------");
                    logger.info("Write down which container you want to remove from the terminal: ");
                    String rawInput = option.nextLine();
                    String selectedContainer = rawInput.toUpperCase().trim().replaceAll("[^A-Z0-9]", "");
                    Terminal.removeGlobally(selectedContainer, bariExp, busanExp, ship);
                    break;
                }
                case "3":
                {
                    logger.info("Selected option: " + displayOptions + ". all containers.");
                    Terminal.displayAllContainers();
                    System.out.println("---------");
                    logger.info("Write down which container you want to remove: ");
                    String rawInput = option.nextLine();
                    String selectedContainer = rawInput.toUpperCase().trim().replaceAll("[^A-Z0-9]", "");
                    Terminal.removeGlobally(selectedContainer, bariExp, busanExp, ship);
                    break;
                }
                case "4":
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
     *  This method provides a sub-menu to display container lists using Iterators.
     */
    public static boolean selectTerminalForIteration(Scanner option, ExportSubTerminal bariExp, ExportSubTerminal busanExp) throws MenuValidationException
    {
        logger.info("Which containers would you like to display? \n 1. Bari terminal containers. \n 2. Busan terminal containers. \n 3. Containers of both terminals. \n 4. Exit.\n Provide an answer with the number related to the desired option: ");
        String displayOptions = option.nextLine();
        System.out.println("-");
        switch (displayOptions)
        {
            case "1":
            {
                logger.info("Selected option: " + displayOptions + ". Bari terminal containers.");
                bariExp.displayTerminalContainers();
                System.out.println("---------");
                break;
            }
            case "2":
            {
                logger.info("Selected option: " + displayOptions + ". Busan terminal containers.");
                busanExp.displayTerminalContainers();
                System.out.println("---------");
                break;
            }
            case "3":
            {
                logger.info("Selected option: " + displayOptions + ". all containers from both terminals.");
                Terminal.displayAllContainers();
                System.out.println("---------");
                break;
            }
            case "4":
            {
                logger.info("You are going back to the menu");
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
     *  This method handles Docking/Undocking requests from the Ship,
     *  checking if a request exists in the respective Sub-Terminals and asking the Operator for confirmation.
     */
    public static boolean selectShipRequest(Scanner option, CargoShip ship, ImportSubTerminal bariImp, ImportSubTerminal busanImp, ExportSubTerminal bariExp, ExportSubTerminal busanExp) throws MenuValidationException
    {
        logger.info("Which ship request do you want to consider? \n 1. Docking Request. \n 2. Undocking Request. \n 3. Back to the menu. \nProvide an answer with the number related to the desired option: ");
        String selectedTerminal = option.nextLine();
        System.out.println("-");
        switch (selectedTerminal)
        {
            case "1":
            {
                System.out.println(".\n.\n.\n.\n.");
                ImportSubTerminal.shipRequest(ship, bariImp, busanImp);
                TerminalOperatorMenu.shipDockConfirmation(option, ship, bariImp, busanImp);
                break;
            }
            case "2":
            {
                System.out.println(".\n.\n.\n.\n.");
                ExportSubTerminal.shipRequest(ship, bariExp, busanExp);
                TerminalOperatorMenu.shipUndockConfirmation(option, ship, bariImp, busanImp, busanExp, bariExp);
                break;
            }
            case "3":
            {   
                logger.info("You are going back to the menu.");
                System.out.println(".\n.\n.\n.\n.");
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
     *  This method processes the operator's decision for a Docking Request,
     *  and sets the static `confirmation` flag to true if granted.
     */
    public static boolean shipDockConfirmation(Scanner option, CargoShip ship, ImportSubTerminal bariImp, ImportSubTerminal busanImp) throws MenuValidationException
    {
        if(ImportSubTerminal.sendConfirmation() == true)
        {
            System.out.println(".\n.\n.\n.\n.");
            logger.info("Select the response to give to the cargo ship captain. \n 1. Yes, grant permission. \n 2. No, the ship must still wait. \n 3. Exit.\n Provide an answer with the number related to the desired option: ");
        String displayOptions = option.nextLine();
        System.out.println("-");
        switch (displayOptions)
        {
            case "1":
            {
                logger.info("Selected option: " + displayOptions + ". Yes, grant permission.");
                confirmation = true;
                ImportSubTerminal.resetRequest();
                System.out.println(".\n.\n.\n.\n.");
                return true;
            }
            case "2":
            {
                logger.info("Selected option: " + displayOptions + ". No, permission not granted for now.");
                confirmation = false;
                ImportSubTerminal.resetRequest();
                System.out.println(".\n.\n.\n.\n.");
                break;
            }
            case "3":
            {
                logger.info("You are going back to the menu");
                return false;
            }
            default:
            {
                throw new MenuValidationException("You have selected an invalid option. Please try again.", null);                                  
            }
        }
        }
        return true;
    }

    /**
     *  This method checks if the confirmation flag is set to true,
     *  and is used by the Ship class to verify if it can proceed with docking.
     */
    public static boolean sendDockConfirmationtoShip()
    {
        if (confirmation == true)
        {
            logger.info("The terminal operator has sent the confirmation to the ship captain.");
            confirmation = false;
            return true;
        }
        else
        {
            logger.info("No confirmation can be given to the ship captain. The ship must wait");
        }
        return false;
    }

    /**
     *  This method processes the operator's decision for an Undocking Request.
     */
    public static boolean shipUndockConfirmation(Scanner option, CargoShip ship, ImportSubTerminal bariImp, ImportSubTerminal busanImp, ExportSubTerminal bariExp, ExportSubTerminal busanExp) throws MenuValidationException
    {
        if(ExportSubTerminal.sendConfirmation() == true)
        {
            logger.info("Select the response to give to the cargo ship captain. \n 1. Yes, grant permission. \n 2. No, the ship must still wait. \n 3. Exit.\n Provide an answer with the number related to the desired option: ");
        String displayOptions = option.nextLine();
        System.out.println("-");
        switch (displayOptions)
        {
            case "1":
            {
                logger.info("Selected option: " + displayOptions + ". Yes, grant permission.");
                confirmation = true;
                ExportSubTerminal.resetRequest();
                return true;
            }
            case "2":
            {
                logger.info("Selected option: " + displayOptions + ". No, permission not granted for now.");
                confirmation = false;
                ExportSubTerminal.resetRequest();
                break;
            }
            case "3":
            {
                logger.info("You are going back to the menu");
                return false;
            }
            default:
            {
                throw new MenuValidationException("You have selected an invalid option. Please try again.", null);                                  
            }
        }
        }
        return true;
    }

    /**
     *  This method checks if the confirmation flag is set to true,
     *  and is used by the Ship class to verify if it can leave the port.
     */
    public static boolean sendUndockConfirmationtoShip()
    {
        if (confirmation == true)
        {
            logger.info("The terminal operator has sent the confirmation to the ship captain.");
            confirmation = false;
            return true;
        }
        return false;
    }

    /**
     *  This method helps safely parse Integer inputs from the console,
     *  and prevents the application from crashing if the user enters non-numeric text.
     */
    private static int getSafeIntInput(Scanner scanner)
    {
        while (true)
        {
            String input = scanner.nextLine();
            try
            {
                return Integer.parseInt(input.trim());
            }
            catch (NumberFormatException e)
            {
                logger.warning("Invalid input: '" + input + "' is not a number. Please try again.");
                System.out.print("Please enter a valid integer: ");
            }
        }
    }

}