package com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory.ElectronicsWarehouse;

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
 *  Unit tests for the ElectronicsExportWarehouse class.
 */
public class ElectronicsExportWarehouseTest 
{

    private ElectronicsExportWarehouse warehouse;
    private ExportSubTerminal mockTerminal;
    private Container mockContainer;

    @BeforeEach
    public void setUp() 
    {
        warehouse = new ElectronicsExportWarehouse();
        mockTerminal = mock(ExportSubTerminal.class);
        mockContainer = mock(Container.class);
    }

    /**
     *  Verifies that an empty container is correctly requested and accepted.
     */
    @Test
    @DisplayName("Should accept an empty container from the export terminal")
    public void testRequestSuccess() 
    {
        when(mockTerminal.borrowFromWarehouse()).thenReturn(mockContainer);
        when(mockContainer.getContainerState()).thenReturn(ContainerState.EMPTY);

        warehouse.request(mockTerminal);

        assertEquals(1, warehouse.getSize(), "Warehouse should have 1 container.");
        assertSame(mockContainer, warehouse.getContainerAt(0));
    }

    /**
     *  Verifies that the container is correctly updated with Electronics goods.
     */
    @Test
    @DisplayName("Should set goods to ELECTRONICS and state to FULL_EXPORT")
    public void testLoadWorkflow() 
    {
        warehouse.electronicsExportWarehouse.add(mockContainer);

        warehouse.load();

        verify(mockContainer).setGoods(GoodsType.ELECTRONICS);
        verify(mockContainer).setContainerState(ContainerState.FULL_EXPORT);
    }

    /**
     *  Verifies that the container is returned to the terminal and local storage is cleared.
     */
    @Test
    @DisplayName("Should return the container and clear local warehouse storage")
    public void testRetrieveWorkflow() 
    {
        warehouse.electronicsExportWarehouse.add(mockContainer);

        warehouse.retrieve(mockTerminal);

        verify(mockTerminal).addFromWarehouse(mockContainer);
        assertEquals(0, warehouse.getSize(), "Warehouse list should be empty after retrieval.");
    }

    /**
     *  Verifies the integration of the full template method workflow.
     */
    @Test
    @DisplayName("Should successfully execute the integrated export template workflow")
    public void testFullExportWorkflow() 
    {
        when(mockTerminal.borrowFromWarehouse()).thenReturn(mockContainer);
        when(mockContainer.getContainerState()).thenReturn(ContainerState.EMPTY);

        warehouse.warehouseExport(mockTerminal);

        verify(mockContainer).setGoods(GoodsType.ELECTRONICS);
        verify(mockTerminal).addFromWarehouse(mockContainer);
        assertTrue(warehouse.electronicsExportWarehouse.isEmpty());
    }
}