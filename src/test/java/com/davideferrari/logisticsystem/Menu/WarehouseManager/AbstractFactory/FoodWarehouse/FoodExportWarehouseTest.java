package com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory.FoodWarehouse;

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
 *  Unit tests for the FoodExportWarehouse class.
 *  This suite verifies the concrete implementation of the export workflow for food goods,
 *  ensuring correct state transitions and terminal hand-offs.
 */
public class FoodExportWarehouseTest 
{

    private FoodExportWarehouse warehouse;
    private ExportSubTerminal mockTerminal;
    private Container mockContainer;

    /**
     *  Initializes the warehouse and mocks before each test.
     */
    @BeforeEach
    public void setUp() 
    {
        warehouse = new FoodExportWarehouse();
        mockTerminal = mock(ExportSubTerminal.class);
        mockContainer = mock(Container.class);
    }

    /**
     *  Verifies that the warehouse correctly requests and stores an empty container.
     */
    @Test
    @DisplayName("Should successfully request and store an empty container from terminal")
    public void testRequest() 
    {
        /**
         *  Arrange: Mock terminal to provide an EMPTY container.
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
        assertEquals(1, warehouse.getSize(), "Warehouse should have accepted the container.");
        assertSame(mockContainer, warehouse.getContainerAt(0));
    }

    /**
     *  Verifies that the load operation correctly sets the goods to FOOD 
     *  and the state to FULL_EXPORT.
     */
    @Test
    @DisplayName("Should set goods to FOOD and state to FULL_EXPORT during loading")
    public void testLoad() 
    {
        /**
         *  Arrange: Add container to local list.
         */
        warehouse.foodExportWarehouse.add(mockContainer);

        /**
         *  Act.
         */
        warehouse.load();

        /**
         *  Assert.
         */
        verify(mockContainer).setGoods(GoodsType.FOOD);
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
        warehouse.foodExportWarehouse.add(mockContainer);

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
     *  Verifies the integrated Template Method workflow.
     */
    @Test
    @DisplayName("Should execute the full Food Export Template workflow successfully")
    public void testFullIntegratedWorkflow() 
    {
        /**
         *  Arrange.
         */
        when(mockTerminal.borrowFromWarehouse()).thenReturn(mockContainer);
        when(mockContainer.getContainerState()).thenReturn(ContainerState.EMPTY);
        when(mockContainer.getContainerCode()).thenReturn("FOOD-BOX-1");

        /**
         *  Act: Execute the method from the abstract parent.
         */
        warehouse.warehouseExport(mockTerminal);

        /**
         *  Assert.
         */
        verify(mockContainer).setGoods(GoodsType.FOOD);
        verify(mockTerminal).addFromWarehouse(mockContainer);
        assertTrue(warehouse.foodExportWarehouse.isEmpty());
    }
}