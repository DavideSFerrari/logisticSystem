package com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 *  Unit tests for the Warehouse abstract factory interface.
 *  This suite verifies the contract of the Abstract Factory, ensuring that 
 *  any implementation can produce the required family of products (Import and Export).
 */
class WarehouseTest 
{

    /**
     *  Verifies that a concrete implementation of the Warehouse factory 
     *  correctly produces the associated product family.
     */
    @Test
    @DisplayName("Should correctly produce the family of warehouse products")
    void testAbstractFactoryContract() 
    {
        /**
         *  Arrange: Create mocks of the abstract products.
         */
        ImportWarehouse mockImportProduct = mock(ImportWarehouse.class);
        ExportWarehouse mockExportProduct = mock(ExportWarehouse.class);

        /**
         *  Create a stub implementation of the Abstract Factory.
         */
        Warehouse warehouseFactory = new Warehouse() 
        {
            @Override
            public ImportWarehouse createImportWarehouse() 
            {
                return mockImportProduct;
            }

            @Override
            public ExportWarehouse createExportWarehouse() 
            {
                return mockExportProduct;
            }
        };

        /**
         *  Act.
         */
        ImportWarehouse producedImport = warehouseFactory.createImportWarehouse();
        ExportWarehouse producedExport = warehouseFactory.createExportWarehouse();

        /**
         *  Assert: Verify that the factory fulfills its contract by returning 
         *  the expected abstract types.
         */
        assertNotNull(producedImport, "Factory should produce an ImportWarehouse.");
        assertNotNull(producedExport, "Factory should produce an ExportWarehouse.");
        
        /**
         *  Ensure the returned objects match our defined products.
         */
        assertSame(mockImportProduct, producedImport);
        assertSame(mockExportProduct, producedExport);
    }
}