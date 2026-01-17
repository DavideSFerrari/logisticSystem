package com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory.ClothingWarehouse;

import static org.junit.jupiter.api.Assertions.*;
import com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory.ExportWarehouse;
import com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory.ImportWarehouse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 *  Unit tests for the ClothingWarehouse concrete factory.
 *  This suite verifies that the factory correctly instantiates the specific
 *  Clothing-related product family.
 */
public class ClothingWarehouseTest 
{

    private ClothingWarehouse factory;

    /**
     *  Initializes the factory before each test.
     */
    @BeforeEach
    public void setUp() 
    {
        factory = new ClothingWarehouse();
    }

    /**
     *  Verifies that the import creation method returns an instance 
     *  of the clothing-specific import warehouse.
     */
    @Test
    @DisplayName("Should create a specific ClothingImportWarehouse instance")
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
        assertTrue(result instanceof ClothingImportWarehouse, 
            "Factory should produce an instance of ClothingImportWarehouse.");
    }

    /**
     *  Verifies that the export creation method returns an instance 
     *  of the clothing-specific export warehouse.
     */
    @Test
    @DisplayName("Should create a specific ClothingExportWarehouse instance")
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
        assertTrue(result instanceof ClothingExportWarehouse, 
            "Factory should produce an instance of ClothingExportWarehouse.");
    }
}