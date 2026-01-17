package com.davideferrari.logisticsystem.Menu.WarehouseManager;

import java.util.logging.Logger;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.TerminalComposite.ExportSubTerminal;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.TerminalComposite.ImportSubTerminal;
import com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory.ClothingWarehouse.ClothingWarehouse;
import com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory.ElectronicsWarehouse.ElectronicsWarehouse;
import com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory.FoodWarehouse.FoodWarehouse;
import com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory.FurnitureWarehouse.FurnitureWarehouse;

/**
 *  This class represents the autonomous logistics vehicle that transports containers between Terminals and Warehouses.
 *  It implements Runnable and runs on a separate background thread,
 *  acting as a Consumer in the system: it monitors the ImportSubTerminal,
 *  and when the capacity limit is breached, it triggers the warehouse processing cycle.
 */
public class WarehouseTruck implements Runnable
{
    private final ImportSubTerminal terminal;
    private final ExportSubTerminal exportTerminal;
    private boolean engineRunning = true;
    
    /**
     *  This latch flag ensures that the truck performs the transport cycle exactly once per ship arrival:
     *  - True: The truck has already cleared the terminal and is waiting for it to be empty.
     *  - False: The truck is ready to work as soon as the terminal fills up.
     */
    private boolean jobDone = false; 

    private static final Logger logger = Logger.getLogger(WarehouseTruck.class.getName());

    /**
     *  This method constructs a new Warehouse Truck daemon.
     *  @param terminal The import terminal to monitor for incoming cargo.
     *  @param exportTerminal The export terminal where processed containers will be delivered.
     */
    public WarehouseTruck(ImportSubTerminal terminal, ExportSubTerminal exportTerminal)
    {
        this.terminal = terminal;
        this.exportTerminal = exportTerminal;
    }

    /**
     *  This method signals the truck thread to terminate safely.
     */
    public void parkTruck()
    {
        this.engineRunning = false;
    }

    /**
     *  This method represents the main execution loop of the truck thread.
     *  Logic:
     *  - Polls the ImportSubTerminal size every 5 seconds.
     *  -Trigger Condition: If size > 5 AND work hasn't been done yet:
     *      a. Executes transportCargo().
     *      b. Sets jobDone = true to prevent infinite looping.>
     *  - Reset Condition: If size <= 5 (Terminal is empty):
     *      a. Resets jobDone = false, re-arming the truck for the next ship.
     */
    @Override
    public void run()
    {
        try
        {
            Thread.sleep(2000);
        }
        catch (InterruptedException e)
        {
            logger.warning("Truck thread interrupted during startup. Shutting down.");
            Thread.currentThread().interrupt(); // Restore interrupt status
            return;
        }
        logger.info("Truck monitor active for " + terminal.getName());

        while (engineRunning)
        {
            try
            {
                Thread.sleep(5000);
                synchronized (terminal)
                {
                    int size = terminal.getSize();
                    if (size > 5 && !jobDone)
                    {
                        logger.info(">>> [TRUCK] Overload detected. Moving cargo...");
                        transportCargo(); 
                        logger.info(">>> [TRUCK] Cargo moved. Standing by.");
                        jobDone = true; // Stop working until reset
                    } 
                    else if (size <= 5)
                    {
                        jobDone = false;
                    }
                }
            }
            catch (InterruptedException e)
            {
                logger.info("Truck thread interrupted. Stopping engine.");
                engineRunning = false;
                Thread.currentThread().interrupt();
            }
            catch (Exception e)
            {
                logger.severe("CRITICAL ERROR in Truck Loop: " + e.getMessage());
            }
        }
    }

    /**
     *  This method executes the full two-phase logistics cycle.
     *  Phase 1: Import
     *  - Iteratively removes containers from the Import Terminal.
     *  - Routes them to the correct Warehouse (Clothing, Food, etc.) based on cargo type.
     *  - Continues until the terminal is empty.
     *  Phase 2: Export
     *  - For every container moved in Phase 1, the truck retrieves a new export load.
     *  - Fills the container with export goods and moves it to the ExportSubTerminal.
     */
    private void transportCargo()
    {
        int initialImportSize = terminal.getSize();
        int currentImportSize;
        
        do
        {
            int sizeBeforePass = terminal.getSize();

            new ClothingWarehouse().createImportWarehouse().warehouseImport(terminal, exportTerminal);
            new FoodWarehouse().createImportWarehouse().warehouseImport(terminal, exportTerminal);
            new ElectronicsWarehouse().createImportWarehouse().warehouseImport(terminal, exportTerminal);
            new FurnitureWarehouse().createImportWarehouse().warehouseImport(terminal, exportTerminal);
            
            currentImportSize = terminal.getSize();
            
            if (currentImportSize == sizeBeforePass) break;
        }
        while (currentImportSize > 0);
        
        int containersMoved = initialImportSize - currentImportSize;
        logger.info(">>> [TRUCK] Import Phase Complete. Moved " + containersMoved + " containers to Export Terminal (EMPTY).");
        
        if (containersMoved > 0)
        {
            logger.info(">>> [TRUCK] Starting Export Refill for " + containersMoved + " containers...");
            
            for (int i = 0; i < containersMoved; i++)
            {
                new ClothingWarehouse().createExportWarehouse().warehouseExport(exportTerminal);
                new FoodWarehouse().createExportWarehouse().warehouseExport(exportTerminal);
                new ElectronicsWarehouse().createExportWarehouse().warehouseExport(exportTerminal);
                new FurnitureWarehouse().createExportWarehouse().warehouseExport(exportTerminal);
            }
            logger.info(">>> [TRUCK] Export Refill Phase Complete. Containers are now FULL_EXPORT.");
        }
        else
        {
            logger.info(">>> [TRUCK] No containers moved, skipping Export Refill.");
        }
    }
}