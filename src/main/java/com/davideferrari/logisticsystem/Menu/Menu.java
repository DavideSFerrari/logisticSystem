package com.davideferrari.logisticsystem.Menu;

import java.util.logging.Logger;
import com.davideferrari.logisticsystem.Menu.CargoShipCaptain.CargoShip;
import com.davideferrari.logisticsystem.Menu.CargoShipCaptain.CargoShipCaptainMenu;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.TerminalOperatorMenu;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.TerminalComposite.ExportSubTerminal;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.TerminalComposite.ImportSubTerminal;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.TerminalComposite.Terminal;
import com.davideferrari.logisticsystem.Menu.WarehouseManager.WarehouseManagerMenu;
import com.davideferrari.logisticsystem.Menu.WarehouseManager.WarehouseTruck;
import java.util.Scanner;
import com.davideferrari.logisticsystem.Utils.Annotations.AppAuthor;
import com.davideferrari.logisticsystem.Utils.ExceptionHandling.ExceptionShieldingHandler;
import com.davideferrari.logisticsystem.Utils.ExceptionHandling.MenuValidationException;
import com.davideferrari.logisticsystem.Utils.Reflection.PatternScanner;

/** This class runs the main Menu interface, executes via the menu method,
 *  responsible for running the whole application.
 * 
 */
@AppAuthor
public class Menu
{
    private static final Logger logger = Logger.getLogger(Menu.class.getName());

    /** This method runs the whole application, starting from the PatternScanner,
    *   which executes the custom annotations related to the system architecture and configuration report.
    *   Starting from this, the terminals of Bari and Busan are created,
    *   as well as their Export and Import Sub-Terminals, which have been added to the main terminals
    *   followinf the Composite Design Pattern.
    *   The ContainerInitializer is called, in order to call the created and set containers inside for the
    *   terminals and the cargo ship.
    *   The WarehouseTruck objects are created in order to work in background, considering their thread nature.
    *   The CargoShip object is created in order to set a ship which will travel from one port to the other,
    *   in order to determine a continuous cycle of import and export operations within the system, by starting
    *   to import a set of 10 full containers which have been created, set and called via the ContainerInitializer.
    */
    public static void main(String[] args)
    {
        PatternScanner.printReport();

        Terminal bari = new Terminal("Bari");
        ExportSubTerminal bariExp = new ExportSubTerminal("Bari");
        bari.addComponent(bariExp);
        ImportSubTerminal bariImp = new ImportSubTerminal("Bari");
        bari.addComponent(bariImp);

        Terminal busan = new Terminal("Busan");
        ExportSubTerminal busanExp = new ExportSubTerminal("Busan");
        busan.addComponent(busanExp);
        ImportSubTerminal busanImp = new ImportSubTerminal("Busan");
        busan.addComponent(busanImp);

        ContainerInitializer.initializeTerminalLoad(bariExp, busanExp);

        WarehouseTruck bariTruck = new WarehouseTruck(bariImp, bariExp);
        WarehouseTruck busanTruck = new WarehouseTruck(busanImp, busanExp);
        Thread t1 = new Thread(bariTruck);
        Thread t2 = new Thread(busanTruck);
        t1.setDaemon(true); 
        t2.setDaemon(true);
        t1.start();
        t2.start();

        CargoShip ship = new CargoShip(bariImp, bariExp);
        ContainerInitializer.initializeShipLoad(ship);

        try (Scanner option = new Scanner(System.in))
        {
            boolean menuLoop = true;
            while (menuLoop)
            {
                try
                {
                    logger.info("\nYou are in the main menu of a logistic system. Select one of the following roles: \n 1. Terminal Operator. \n 2. Cargo Ship Captain. \n 3. Warehouse Manager. \n 4. Exit. \nProvide an answer with the number related to the desired option: ");
                    String decision = option.nextLine();
                    switch (decision)
                    {
                        case "1":
                        {
                            logger.info("Selected option: " + decision + ". Terminal Operator. You will be moved on this menu.");
                            boolean infoLoop = true;
                            while (infoLoop)
                            {
                                try 
                                {    
                                    System.out.println(".\n.\n.\n.\n.");
                                    TerminalOperatorMenu.menu(option, bari, bariExp, busan, busanExp, bariImp, busanImp, ship);
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
                            logger.info("Selected option: " + decision + ". Cargo Ship Captain. You will be moved on this menu.");
                            boolean infoLoop = true;
                            while (infoLoop)
                            {
                                try 
                                {    
                                    System.out.println(".\n.\n.\n.\n.");
                                    CargoShipCaptainMenu.menu(option, ship, bariImp, busanImp, bariExp, busanExp);
                                }
                                catch (Exception e)
                                {
                                    ExceptionShieldingHandler.handleException(e);
                                }
                                infoLoop = false;
                            }
                            break;
                        }
                        case "3":
                        {
                            logger.info("Selected option: " + decision + ". Warehouse Manager.");
                            boolean infoLoop = true;
                            while (infoLoop)
                            {
                                try 
                                {    
                                    System.out.println(".\n.\n.\n.\n.");
                                    WarehouseManagerMenu.menu(option, bariImp, bariExp, busanImp, busanExp);
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
                            System.out.println(".\n.\n.\n.\n.");
                            logger.info("Exited from the main menu.");
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
    }
}
