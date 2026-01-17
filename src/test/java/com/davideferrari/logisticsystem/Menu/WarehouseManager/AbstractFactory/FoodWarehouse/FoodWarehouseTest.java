package com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory.FoodWarehouse;

import static org.junit.jupiter.api.Assertions.*;
import com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory.ExportWarehouse;
import com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory.ImportWarehouse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 *  Unit tests for the FoodWarehouse concrete factory.
 *  This suite verifies that the factory correctly instantiates the specific
 *  Food-related product family.
 */
public class FoodWarehouseTest 
{

    private FoodWarehouse factory;

    /**
     *  Initializes the factory before each test execution.
     */
    @BeforeEach
    public void setUp() 
    {
        factory = new FoodWarehouse();
    }

    /**
     *  Verifies that the factory produces a FoodImportWarehouse instance.
     */
    @Test
    @DisplayName("Should create a specific FoodImportWarehouse instance")
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
        assertTrue(result instanceof FoodImportWarehouse, 
            "Factory should produce an instance of FoodImportWarehouse.");
    }

    /**
     *  Verifies that the factory produces a FoodExportWarehouse instance.
     */
    @Test
    @DisplayName("Should create a specific FoodExportWarehouse instance")
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
        assertTrue(result instanceof FoodExportWarehouse, 
            "Factory should produce an instance of FoodExportWarehouse.");
    }
}