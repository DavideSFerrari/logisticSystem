package com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory.FoodWarehouse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerFactory.Container;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerFactory.Container.ContainerState;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerFactory.Container.GoodsType;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.TerminalComposite.ExportSubTerminal;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.TerminalComposite.ImportSubTerminal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 *  Unit tests for the FoodImportWarehouse class.
 *  This suite verifies the concrete implementation of the food import workflow,
 *  checking that containers are correctly filtered, emptied, and handed off.
 */
public class FoodImportWarehouseTest 
{

    private FoodImportWarehouse warehouse;
    private ImportSubTerminal mockImpTerminal;
    private ExportSubTerminal mockExpTerminal;
    private Container mockContainer;

    /**
     *  Initializes the warehouse and mocks before each test execution.
     */
    @BeforeEach
    public void setUp() 
    {
        warehouse = new FoodImportWarehouse();
        mockImpTerminal = mock(ImportSubTerminal.class);
        mockExpTerminal = mock(ExportSubTerminal.class);
        mockContainer = mock(Container.class);
    }

    /**
     *  Verifies that the warehouse successfully requests and stores a food container.
     */
    @Test
    @DisplayName("Should pick a food container from the Import Terminal")
    public void testPick() 
    {
        /**
         *  Arrange: Terminal returns a container specifically when asked for FOOD.
         */
        when(mockImpTerminal.borrowFromWarehouse(GoodsType.FOOD)).thenReturn(mockContainer);
        when(mockContainer.getContainerCode()).thenReturn("FOOD-IMP-123");

        /**
         *  Act.
         */
        warehouse.pick(mockImpTerminal);

        /**
         *  Assert.
         */
        assertEquals(1, warehouse.getSize(), "Warehouse should have 1 container in its inventory.");
        verify(mockImpTerminal).borrowFromWarehouse(GoodsType.FOOD);
    }

    /**
     *  Verifies that the unloading step changes the container state to EMPTY.
     */
    @Test
    @DisplayName("Should change container state to EMPTY during food unloading")
    public void testUnload() 
    {
        /**
         *  Arrange: Add the container to the internal list manually.
         */
        warehouse.foodImportWarehouse.add(mockContainer);

        /**
         *  Act.
         */
        warehouse.unload();

        /**
         *  Assert.
         */
        verify(mockContainer).setContainerState(ContainerState.EMPTY);
    }

    /**
     *  Verifies that the retrieval step transfers containers to the export terminal
     *  and clears local storage.
     */
    @Test
    @DisplayName("Should return container to export terminal and clear warehouse list")
    public void testRetrieve() 
    {
        /**
         *  Arrange.
         */
        warehouse.foodImportWarehouse.add(mockContainer);

        /**
         *  Act.
         */
        warehouse.retrieve(mockExpTerminal);

        /**
         *  Assert.
         */
        verify(mockExpTerminal).addFromWarehouse(mockContainer);
        assertEquals(0, warehouse.getSize(), "Local food warehouse storage should be empty.");
    }

    /**
     *  Verifies the full Template Method workflow integration.
     */
    @Test
    @DisplayName("Should successfully execute the full integrated Food Import Template Method")
    public void testFullIntegratedWorkflow() 
    {
        /**
         *  Arrange.
         */
        when(mockImpTerminal.borrowFromWarehouse(GoodsType.FOOD)).thenReturn(mockContainer);
        when(mockContainer.getContainerCode()).thenReturn("FOOD-INTEGRATION-TEST");

        /**
         *  Act: Execute the final template method inherited from ImportWarehouse.
         */
        warehouse.warehouseImport(mockImpTerminal, mockExpTerminal);

        /**
         *  Assert.
         */
        verify(mockContainer).setContainerState(ContainerState.EMPTY);
        verify(mockExpTerminal).addFromWarehouse(mockContainer);
        assertTrue(warehouse.foodImportWarehouse.isEmpty());
    }
}