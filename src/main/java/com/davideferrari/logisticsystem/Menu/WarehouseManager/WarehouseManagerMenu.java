package com.davideferrari.logisticsystem.Menu.WarehouseManager;

import java.util.Scanner;
import java.util.logging.Logger;
import com.davideferrari.logisticsystem.Utils.ExceptionHandling.*;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.TerminalComposite.ExportSubTerminal;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.TerminalComposite.ImportSubTerminal;
import com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory.Warehouse;
import com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory.ClothingWarehouse.ClothingWarehouse;
import com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory.ElectronicsWarehouse.ElectronicsWarehouse;
import com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory.FoodWarehouse.FoodWarehouse;
import com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory.FurnitureWarehouse.FurnitureWarehouse;

/**
 *  This class handles the interface and interaction logic for the "Warehouse Manager" role.
 *  It acts as the Client for the Abstract Factory Design Pattern,
 *  by interacting with the user to select specific cities and cargo types, then dynamically
 *  instantiates the correct Warehouse factory to execute business logic.
 */
public class WarehouseManagerMenu
{
    private static final Logger logger = Logger.getLogger(WarehouseManagerMenu.class.getName());

    /**
     *  This method displays the main entry menu for Warehouse Managers.
     *  It allows the user to select the geographic context (Bari or Busan) for operations.
     *  @param option The input scanner.
     *  @param bariImp The Bari Import Terminal instance.
     *  @param bariExp The Bari Export Terminal instance.
     *  @param busanImp The Busan Import Terminal instance.
     *  @param busanExp The Busan Export Terminal instance.
     */
    public static void menu(Scanner option, ImportSubTerminal bariImp, ExportSubTerminal bariExp, ImportSubTerminal busanImp, ExportSubTerminal busanExp)
    {
        boolean menuLoop = true;
        while (menuLoop)
        {
            try
            {
                logger.info("\nYou are the Warehouse Manager. Select the city you wish to operate in: \n 1. Bari. \n 2. Busan. \n 3. Exit. \nProvide an answer with the number related to the desired option: ");
                String decision = option.nextLine();
                System.out.println("-");
                switch (decision)
                {
                    case "1":
                    {
                        logger.info("Selected option: " + decision + ". Bari Operations.");
                        boolean opLoop = true;
                        while (opLoop)
                        {
                            try 
                            {    
                                System.out.println(".\n.\n.\n.\n.");
                                // Pass Bari terminals
                                opLoop = WarehouseManagerMenu.selectOperationType(option, bariImp, bariExp, "Bari");
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
                        logger.info("Selected option: " + decision + ". Busan Operations.");
                        boolean opLoop = true;
                        while (opLoop)
                        {
                            try 
                            {    
                                System.out.println(".\n.\n.\n.\n.");
                                // Pass Busan terminals
                                opLoop = WarehouseManagerMenu.selectOperationType(option, busanImp, busanExp, "Busan");
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
                    case "3":
                    {
                        System.out.println(".\n.\n.\n.\n.");
                        logger.info("Exited from the Warehouse Manager menu.");
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
     *  This method provides a sub-menu necessary for choosing between Import (Processing) and Export (Shipping) operations.
     *  @param option The input scanner.
     *  @param impTerm The active import terminal.
     *  @param expTerm The active export terminal.
     *  @param cityName The name of the city (for logging).
     *  @return {@code true} to continue the loop, {@code false} to go back.
     *  @throws MenuValidationException If the user enters invalid input.
     */
    public static boolean selectOperationType(Scanner option, ImportSubTerminal impTerm, ExportSubTerminal expTerm, String cityName) throws MenuValidationException
    {
        logger.info("You are operating in " + cityName + ". Select the type of warehouse operation: \n 1. Import (Process arriving goods). \n 2. Export (Prepare goods for departure). \n 3. Back to City Selection. \nProvide an answer with the number related to the desired option: ");
        String opSelection = option.nextLine();
        System.out.println("-");
        
        switch (opSelection)
        {
            case "1":
            {
                logger.info("Selected option: " + opSelection + ". Import Operations.");
                System.out.println(".\n.\n.\n.\n.");
                // True = Import
                WarehouseManagerMenu.selectGoodsTypeAndExecute(option, impTerm, expTerm, true); 
                break;
            }
            case "2":
            {
                logger.info("Selected option: " + opSelection + ". Export Operations.");
                System.out.println(".\n.\n.\n.\n.");
                // False = Export
                WarehouseManagerMenu.selectGoodsTypeAndExecute(option, impTerm, expTerm, false);
                break;
            }
            case "3":
            {
                logger.info("You are going back to the city selection.");
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
     *  This method provides a Sub-menu necessary for selecting the specific category of goods (Clothing, Food, etc.).
     *  Based on the selection, this method instantiates the corresponding Concrete Factory
     */
    public static void selectGoodsTypeAndExecute(Scanner option, ImportSubTerminal impTerm, ExportSubTerminal expTerm, boolean isImport) throws MenuValidationException
    {
        boolean goodsLoop = true;
        while(goodsLoop) 
        {
            logger.info("Select the Warehouse Department (Goods Type): \n 1. Clothing. \n 2. Food. \n 3. Electronics. \n 4. Furniture. \n 5. Back. \nProvide an answer with the number related to the desired option: ");
            String goodsSelection = option.nextLine();
            System.out.println("-");
            
            Warehouse factory = null;
            String typeName = "";

            switch (goodsSelection)
            {
                case "1":
                    factory = new ClothingWarehouse();
                    typeName = "Clothing";
                    break;
                case "2":
                    factory = new FoodWarehouse();
                    typeName = "Food";
                    break;
                case "3":
                    factory = new ElectronicsWarehouse();
                    typeName = "Electronics";
                    break;
                case "4":
                    factory = new FurnitureWarehouse();
                    typeName = "Furniture";
                    break;
                case "5":
                    logger.info("You are going back to the operation selection.");
                    goodsLoop = false;
                    return; // Exit method
                default:
                    throw new MenuValidationException("You have selected an invalid option. Please try again.", null);
            }

            if (factory != null) 
            {
                logger.info("Selected option: " + goodsSelection + ". " + typeName + " Warehouse.");
                executeProcess(factory, impTerm, expTerm, isImport, typeName);
            }
        }
    }

    /**
     *  This method executes the abstract business logic using the provided Factory.
     *  It represents the core of the Abstract Factory Client, and does not know
     *  the concrete type of the warehouse; it simply asks the factory to create the
     *  required product (Import or Export warehouse) and runs it.
     *  @param factory The concrete factory instance (e.g., ClothingWarehouse).
     *  @param impTerm The source terminal for imports.
     *  @param expTerm The destination terminal for exports.
     *  @param isImport {@code true} for Import cycle, {@code false} for Export cycle.
     *  @param typeName The string name of the goods type for logging.
     */
    private static void executeProcess(Warehouse factory, ImportSubTerminal impTerm, ExportSubTerminal expTerm, boolean isImport, String typeName) 
    {
        if (isImport) 
        {
            logger.info("Starting IMPORT cycle for " + typeName + "...");
            // Abstract Factory Pattern: Create Product A (ImportWarehouse)
            // Template Method Pattern: Execute warehouseImport
            factory.createImportWarehouse().warehouseImport(impTerm, expTerm);
            logger.info("IMPORT cycle for " + typeName + " completed.");
        } 
        else 
        {
            logger.info("Starting EXPORT cycle for " + typeName + "...");
            // Abstract Factory Pattern: Create Product B (ExportWarehouse)
            // Template Method Pattern: Execute warehouseExport
            factory.createExportWarehouse().warehouseExport(expTerm);
            logger.info("EXPORT cycle for " + typeName + " completed.");
        }
        System.out.println(".\n.\n.\n.\n.");
    }
}