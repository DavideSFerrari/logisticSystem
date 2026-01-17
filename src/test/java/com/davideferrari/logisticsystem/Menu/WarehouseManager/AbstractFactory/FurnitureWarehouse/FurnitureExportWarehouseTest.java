package com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory.FurnitureWarehouse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerFactory.Container;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerFactory.Container.ContainerState;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerFactory.Container.GoodsType;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.TerminalComposite.ExportSubTerminal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 *  Unit tests for theFurnitureExportWarehouse class.
 *  This suite verifies the concrete implementation of the export workflow for furniture,
 *  ensuring correct state transitions and hand-offs to the terminal.
 */
public class FurnitureExportWarehouseTest 
{

    private FurnitureExportWarehouse warehouse;
    private ExportSubTerminal mockTerminal;
    private Container mockContainer;

    /**
     *  Initializes the warehouse and mocks before each test execution.
     */
    @BeforeEach
    public void setUp() 
    {
        warehouse = new FurnitureExportWarehouse();
        mockTerminal = mock(ExportSubTerminal.class);
        mockContainer = mock(Container.class);
    }

    /**
     *  Verifies that the warehouse successfully requests and stores an empty container.
     */
    @Test
    @DisplayName("Should successfully request and store an empty container from terminal")
    public void testRequest() 
    {
        /**
         *  Arrange: Terminal provides an EMPTY container.
         */
        when(mockTerminal.borrowFromWarehouse()).thenReturn(mockContainer);
        when(mockContainer.getContainerState()).thenReturn(ContainerState.EMPTY);

        /**
         *  Act.
         */
        warehouse.request(mockTerminal);

        /**
         *  Assert.
         */
        assertEquals(1, warehouse.getSize(), "Warehouse should have 1 container in its inventory.");
        assertSame(mockContainer, warehouse.getContainerAt(0));
    }

    /**
     *  Verifies that the load operation correctly sets the goods to FURNITURE 
     *  and the state to FULL_EXPORT.
     */
    @Test
    @DisplayName("Should set goods to FURNITURE and state to FULL_EXPORT during loading")
    public void testLoad() 
    {
        /**
         *  Arrange: Add container to the local list.
         */
        warehouse.furnitureExportWarehouse.add(mockContainer);

        /**
         *  Act.
         */
        warehouse.load();

        /**
         *  Assert.
         */
        verify(mockContainer).setGoods(GoodsType.FURNITURE);
        verify(mockContainer).setContainerState(ContainerState.FULL_EXPORT);
    }

    /**
     *  Verifies that the retrieve operation returns the container to the terminal 
     *  and clears the local warehouse list.
     */
    @Test
    @DisplayName("Should return the container to the terminal and clear internal storage")
    public void testRetrieve() 
    {
        /**
         *  Arrange.
         */
        warehouse.furnitureExportWarehouse.add(mockContainer);

        /**
         *  Act.
         */
        warehouse.retrieve(mockTerminal);

        /**
         *  Assert.
         */
        verify(mockTerminal).addFromWarehouse(mockContainer);
        assertEquals(0, warehouse.getSize(), "Warehouse storage should be cleared.");
    }

    /**
     *  Verifies the full Template Method workflow integration.
     */
    @Test
    @DisplayName("Should execute the full Furniture Export Template workflow successfully")
    public void testFullIntegratedWorkflow() 
    {
        /**
         *  Arrange.
         */
        when(mockTerminal.borrowFromWarehouse()).thenReturn(mockContainer);
        when(mockContainer.getContainerState()).thenReturn(ContainerState.EMPTY);
        when(mockContainer.getContainerCode()).thenReturn("FURN-001");

        /**
         *  Act: Trigger the final method from the abstract parent.
         */
        warehouse.warehouseExport(mockTerminal);

        /**
         *  Assert.
         */
        verify(mockContainer).setGoods(GoodsType.FURNITURE);
        verify(mockTerminal).addFromWarehouse(mockContainer);
        assertTrue(warehouse.furnitureExportWarehouse.isEmpty());
    }
}