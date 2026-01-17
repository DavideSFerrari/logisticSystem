package com.davideferrari.logisticsystem.Utils.Reflection;

import java.io.IOException;
import java.util.logging.Logger;
import com.davideferrari.logisticsystem.Menu.Menu;
import com.davideferrari.logisticsystem.Menu.CargoShipCaptain.CargoShip;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerFactory.*;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerIterator.*;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerSingleton.ContainerRegister;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.TerminalComposite.*;
import com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory.*;
import com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory.ClothingWarehouse.*;
import com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory.ElectronicsWarehouse.*;
import com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory.FoodWarehouse.*;
import com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory.FurnitureWarehouse.*;
import com.davideferrari.logisticsystem.Utils.Annotations.AppAuthor;
import com.davideferrari.logisticsystem.Utils.Annotations.AppDesignPattern;
import com.davideferrari.logisticsystem.Utils.Annotations.CapacityLimit;
import com.davideferrari.logisticsystem.Utils.ExceptionHandling.ExceptionShieldingHandler;

/**
 *  This class  performs a Reflection-based analysis of the entire application.
 *  It scans the key classes of the system to detect custom runtime annotations,
 *  and then generates a comprehensive System Architecture & Configuration Report, 
 *  which is printed to the console and saved to a file ({@code system_report.txt}).
 */
public class PatternScanner 
{
    private static final Logger logger = Logger.getLogger(PatternScanner.class.getName());

    /**
     *  This method executes the scanning process, which iterates
     *  through a predefined list of project classes, checks for the presence of 
     *  specific metadata annotations, and formats the findings into a readable table.
     */
    public static void printReport() 
    {
        logger.info("Initiating Reflection Scan for the System Architecture and Configuration Report...");

        try (java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileWriter("system_report.txt"))) 
        {
            writeToBoth(writer, "\n==========================================================================================");
            writeToBoth(writer, "                    SYSTEM ARCHITECTURE & CONFIGURATION REPORT");
            writeToBoth(writer, "==========================================================================================");

            Class<?>[] classesToInspect =
            {
                Menu.class,
                ContainerRegister.class,
                CargoShip.class,
                Terminal.class,
                ImportSubTerminal.class,
                ExportSubTerminal.class,
                Container.class,
                Box.class,
                HighCube.class,
                ContainerCreator.class,
                BoxSelector.class,
                HighCubeSelector.class,
                ContainerCollection.class,
                ContainerIterator.class,
                TerminalContainerIterator.class,
                Warehouse.class,
                ImportWarehouse.class,
                ExportWarehouse.class,
                ClothingWarehouse.class,
                ClothingImportWarehouse.class,
                ClothingExportWarehouse.class,
                FoodWarehouse.class,
                FoodImportWarehouse.class,
                FoodExportWarehouse.class,
                ElectronicsWarehouse.class,
                ElectronicsImportWarehouse.class,
                ElectronicsExportWarehouse.class,
                FurnitureWarehouse.class,
                FurnitureImportWarehouse.class,
                FurnitureExportWarehouse.class
            };

            for (Class<?> clazz : classesToInspect) 
            {
                boolean hasMetadata = false;
                
                if (clazz.isAnnotationPresent(AppDesignPattern.class)) 
                {
                    AppDesignPattern pattern = clazz.getAnnotation(AppDesignPattern.class);
                    String line = String.format("[PATTERN]   %-30s | %-20s | %s", clazz.getSimpleName(), pattern.pattern(), pattern.justification());
                    writeToBoth(writer, line);
                    hasMetadata = true;
                }

                if (clazz.isAnnotationPresent(CapacityLimit.class)) 
                {
                    CapacityLimit limit = clazz.getAnnotation(CapacityLimit.class);
                    String line = String.format("[CONFIG]    %-30s | LIMIT: %-13d | %s", clazz.getSimpleName(), limit.value(), "Value injected via Reflection");
                    writeToBoth(writer, line);
                    hasMetadata = true;
                }

                if (clazz.isAnnotationPresent(AppAuthor.class)) 
                {
                    AppAuthor author = clazz.getAnnotation(AppAuthor.class);
                    String line = String.format("[AUTHOR]    %-30s | %-20s | %s", clazz.getSimpleName(), author.name(), author.role());
                    writeToBoth(writer, line);
                    hasMetadata = true;
                }

                if (hasMetadata) writeToBoth(writer, "------------------------------------------------------------------------------------------");
            }
            writeToBoth(writer, "================================== END OF REPORT =========================================\n");
            
            logger.info("Report saved to 'system_report.txt'");
        } 
        catch (IOException e) 
        {
            ExceptionShieldingHandler.handleException(e);
        }
        catch (Exception e)
        {
            ExceptionShieldingHandler.handleException(e);
        }
    }

    /**
     *  This method outputs data to both the Console and the Log File simultaneously.
     */
    private static void writeToBoth(java.io.PrintWriter writer, String message)
    {
        System.out.println(message);
        writer.println(message);
    }
}