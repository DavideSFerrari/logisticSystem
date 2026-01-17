package com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.TerminalComposite.ExportSubTerminal;
import com.davideferrari.logisticsystem.Utils.Annotations.AppDesignPattern;

/**
 *  This class defines the contract for the "Export Warehouse" product family.
 *  In the Abstract Factory Design Pattern, this class acts as the Abstract Product.
 *  It ensures that every concrete export warehouse (Clothing, Food, etc.) implements the same
 *  high-level logic for preparing goods for shipment.
 *  It also implements the Template Method Design Pattern via the warehouseExport() method.
 */
@AppDesignPattern(pattern = "Abstract Factory - Template Method", justification = "Abstract Product - Template Method")
public abstract class ExportWarehouse
{
    /**
     *  This Template Method defines the rigid workflow for the export process.
     *  Subclasses cannot override this workflow structure, but they
     *  must provide implementations for the specific steps (request, pick, load, retrieve).
     *  @param exp The export terminal where the final goods will be delivered.
     */
    public final void warehouseExport(ExportSubTerminal exp)
    {
        request(exp);
        pick();
        load();
        retrieve(exp);
    }

    /**
     *  Step 1: Requests an empty container from the terminal to start the process.
     *  @param exp The terminal source.
     */
    protected abstract void request(ExportSubTerminal exp);

    /**
     *  Step 2: Picks the specific goods (Clothing, Food, etc.) from the warehouse inventory.
     */
    protected abstract void pick();

    /**
     *  Step 3: Physically loads the picked goods into the container, changing its state.
     */
    protected abstract void load();

    /**
     *  Step 4: Delivers the filled container back to the Export Terminal.
     *  @param exp The terminal destination.
     */
    protected abstract void retrieve(ExportSubTerminal exp);
}
