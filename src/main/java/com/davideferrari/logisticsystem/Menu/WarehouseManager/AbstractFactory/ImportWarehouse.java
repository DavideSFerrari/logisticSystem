package com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory;

import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.TerminalComposite.ExportSubTerminal;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.TerminalComposite.ImportSubTerminal;
import com.davideferrari.logisticsystem.Utils.Annotations.AppDesignPattern;

/**
 *  This class defines the contract for the "Import Warehouse" product family.
 *  In the Abstract Factory Design Pattern, this class acts as the Abstract Product.
 *  It ensures that every concrete import warehouse (Clothing, Food, etc.) implements the same
 *  high-level logic for processing arriving goods.
 *  It also implements the Template Method Design Pattern via the warehouseImport() method.
 */
@AppDesignPattern(pattern = "Abstract Factory", justification = "Abstract Product")
public abstract class ImportWarehouse
{
    /**
     *  This Template Method defines the rigid workflow for the import process.
     *  Subclasses cannot override this workflow structure, but they
     *  must provide implementations for the specific steps (pick, load, retrieve).
     *  @param imp The import terminal where the final goods will be delivered.
     */
    public final void warehouseImport(ImportSubTerminal imp, ExportSubTerminal exp)
    {
        pick(imp);
        unload();
        retrieve(exp);
    }

    /**
     *  Step 1: Identifies and picks up a specific type of container from the Import Terminal.
     *  @param imp The import terminal to search.
     */
    protected abstract void pick(ImportSubTerminal imp);

    /**
     *  Step 2: Unloads the cargo from the container into the warehouse.
     *  This operation changes the container's state from full for import
     *  to empty.
     */
    protected abstract void unload();

    /**
     *  Step 3: Transports the empty container to the Export Terminal so it can be reused.
     *  @param exp The export terminal to receive the empty container.
     */
    protected abstract void retrieve(ExportSubTerminal exp);

}
