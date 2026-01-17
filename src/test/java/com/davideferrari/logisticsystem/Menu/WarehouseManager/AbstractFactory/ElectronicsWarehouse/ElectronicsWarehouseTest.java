package com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory.ElectronicsWarehouse;

import static org.junit.jupiter.api.Assertions.*;
import com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory.ExportWarehouse;
import com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory.ImportWarehouse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 *  Unit tests for the ElectronicsWarehouse concrete factory.
 *  This suite verifies that the factory correctly instantiates the specific
 *  Electronics-related product family.
 */
public class ElectronicsWarehouseTest 
{

    private ElectronicsWarehouse factory;

    /**
     *  Initializes the factory before each test.
     */
    @BeforeEach
    public void setUp() 
    {
        factory = new ElectronicsWarehouse();
    }

    /**
     *  Verifies that the import creation method returns an instance 
     *  of the electronics-specific import warehouse.
     */
    @Test
    @DisplayName("Should create a specific ElectronicsImportWarehouse instance")
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
        assertTrue(result instanceof ElectronicsImportWarehouse, 
            "Factory should produce an instance of ElectronicsImportWarehouse.");
    }

    /**
     *  Verifies that the export creation method returns an instance 
     *  of the electronics-specific export warehouse.
     */
    @Test
    @DisplayName("Should create a specific ElectronicsExportWarehouse instance")
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
        assertTrue(result instanceof ElectronicsExportWarehouse, 
            "Factory should produce an instance of ElectronicsExportWarehouse.");
    }
}