package com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory.FurnitureWarehouse;

import com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory.ExportWarehouse;
import com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory.ImportWarehouse;
import com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory.Warehouse;
import com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory.ClothingWarehouse.ClothingExportWarehouse;
import com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory.ClothingWarehouse.ClothingImportWarehouse;
import com.davideferrari.logisticsystem.Utils.Annotations.AppDesignPattern;

/**
 *  This class represents the Concrete Factory responsible for the "Furniture" product family.
 *  In the Abstract Factory Design Pattern, this class acts as a Concrete Factory.
 *  It implements the creation methods defined in the Warehouse interface to produce
 *  a matched pair of furniture-specific warehouse components.
 */
@AppDesignPattern(pattern = "Abstract Factory", justification = "Concrete Factory")
public class FurnitureWarehouse implements Warehouse
{

    /**
     *  This method creates a warehouse component specialized for importing furniture.
     *  @return A new instance of {@link ClothingImportWarehouse}.
     */
    @Override
    public ImportWarehouse createImportWarehouse()
    {
        return new FurnitureImportWarehouse();
    }

    /**
     *  This method creates a warehouse component specialized for exporting clothing.
     *  @return A new instance of {@link ClothingExportWarehouse}.
     */
    @Override
    public ExportWarehouse createExportWarehouse()
    {
        return new FurnitureExportWarehouse();
    }
    
}
