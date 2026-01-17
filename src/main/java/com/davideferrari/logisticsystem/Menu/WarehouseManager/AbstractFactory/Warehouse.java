package com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory;

import com.davideferrari.logisticsystem.Utils.Annotations.AppDesignPattern;

/**
 *  This interface represents central interface for the Warehouse Abstract Factory.
 *  In the Abstract Factory Design Pattern, this interface acts as the Abstract Factory.
 *  It defines the methods to create a family of related products,
 *  without specifying their concrete classes.
 *  Concrete implementations (e.g., {@code ClothingWarehouse}, {@code FoodWarehouse})
 *  will implement these methods to produce specific types of warehouses.
 */
@AppDesignPattern(pattern = "Abstract Factory", justification = "Abstract Factory")
public interface Warehouse
{
    /**
     *  This method creates a product dedicated to import operations.
     *  @return A concrete {@link ImportWarehouse} instance (e.g., ClothingImportWarehouse).
     */
    ImportWarehouse createImportWarehouse();

    /**
     *  This method creates a product dedicated to export operations.
     *  @return A concrete {@link ExportWarehouse} instance (e.g., ClothingExportWarehouse).
     */
    ExportWarehouse createExportWarehouse();
}