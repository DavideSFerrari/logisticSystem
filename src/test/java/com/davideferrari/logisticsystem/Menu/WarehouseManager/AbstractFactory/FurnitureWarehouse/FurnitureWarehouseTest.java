package com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory.FurnitureWarehouse;

import static org.junit.jupiter.api.Assertions.*;
import com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory.ExportWarehouse;
import com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory.ImportWarehouse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 *  Unit tests for the FurnitureWarehouse concrete factory.
 *  This suite verifies that the factory correctly instantiates the specific
 *  Furniture-related product family.
 */
public class FurnitureWarehouseTest 
{

    private FurnitureWarehouse factory;

    /**
     *  Initializes the factory before each test.
     */
    @BeforeEach
    public void setUp() 
    {
        factory = new FurnitureWarehouse();
    }

    /**
     *  Verifies that the factory produces a FurnitureImportWarehouse instance.
     */
    @Test
    @DisplayName("Should create a specific FurnitureImportWarehouse instance")
    public void testCreateImportWarehouse() 
    {
        /**
         *  Act.
         */
        ImportWarehouse result = factory.createImportWarehouse();

        /**
         *  Assert.
         */
        assertNotNull(result, "Factory should not return null.");
        assertTrue(result instanceof FurnitureImportWarehouse, 
            "Factory should produce an instance of FurnitureImportWarehouse.");
    }

    /**
     *  Verifies that the factory produces a FurnitureExportWarehouse instance.
     */
    @Test
    @DisplayName("Should create a specific FurnitureExportWarehouse instance")
    public void testCreateExportWarehouse() 
    {
        /**
         *  Act.
         */
        ExportWarehouse result = factory.createExportWarehouse();

        /**
         *  Assert.
         */
        assertNotNull(result, "Factory should not return null.");
        assertTrue(result instanceof FurnitureExportWarehouse, 
            "Factory should produce an instance of FurnitureExportWarehouse.");
    }
}