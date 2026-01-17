package com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory.ClothingWarehouse;

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
 *  Unit tests for the ClothingExportWarehouse class.
 *  This suite verifies the concrete implementation of the export workflow,
 *  specifically checking state transitions and terminal interactions.
 */
class ClothingExportWarehouseTest 
{

    private ClothingExportWarehouse warehouse;
    private ExportSubTerminal mockTerminal;
    private Container mockContainer;

    /**
     *  Sets up the test environment by initializing the warehouse and mocks.
     */
    @BeforeEach
    void setUp() 
    {
        warehouse = new ClothingExportWarehouse();
        mockTerminal = mock(ExportSubTerminal.class);
        mockContainer = mock(Container.class);
    }

    /**
     *  Verifies that the warehouse correctly requests and stores an empty container.
     */
    @Test
    @DisplayName("Should successfully request and store an empty container from terminal")
    void testRequestSuccessful() 
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
        assertEquals(1, warehouse.getSize(), "Warehouse should contain 1 container.");
        assertSame(mockContainer, warehouse.getContainerAt(0));
    }

    /**
     *  Verifies that the load operation updates the container with the correct 
     *  goods type and state.
     */
    @Test
    @DisplayName("Should update container state and goods type during loading")
    void testLoadOperation() 
    {
        /**
         *  Arrange: Manually add container to local storage for the test.
         */
        warehouse.clothingExportWarehouse.add(mockContainer);

        /**
         *  Act.
         */
        warehouse.load();

        /**
         *  Assert.
         */
        verify(mockContainer).setGoods(GoodsType.CLOTHING);
        verify(mockContainer).setContainerState(ContainerState.FULL_EXPORT);
    }

    /**
     *  Verifies that the retrieve operation returns the container to the terminal 
     *  and clears local storage.
     */
    @Test
    @DisplayName("Should return container to terminal and clear local storage")
    void testRetrieveOperation() 
    {
        /**
         *  Arrange.
         */
        warehouse.clothingExportWarehouse.add(mockContainer);

        /**
         *  Act.
         */
        warehouse.retrieve(mockTerminal);

        /**
         *  Assert.
         */
        verify(mockTerminal).addFromWarehouse(mockContainer);
        assertEquals(0, warehouse.getSize(), "Local storage should be cleared after retrieval.");
    }

    /**
     *  Verifies the full workflow integration defined by the Template Method.
     */
    @Test
    @DisplayName("Should execute the full export workflow correctly")
    void testFullWorkflow() 
    {
        /**
         *  Arrange: Configure mocks for a complete successful run.
         */
        when(mockTerminal.borrowFromWarehouse()).thenReturn(mockContainer);
        when(mockContainer.getContainerState()).thenReturn(ContainerState.EMPTY);
        when(mockContainer.getContainerCode()).thenReturn("CLO-12345");

        /**
         *  Act: Trigger the final Template Method.
         */
        warehouse.warehouseExport(mockTerminal);

        /**
         *  Assert: Verify key state changes and terminal interactions.
         */
        verify(mockContainer).setGoods(GoodsType.CLOTHING);
        verify(mockTerminal).addFromWarehouse(mockContainer);
        assertEquals(0, warehouse.getSize(), "Warehouse must be empty at the end of the process.");
    }
}