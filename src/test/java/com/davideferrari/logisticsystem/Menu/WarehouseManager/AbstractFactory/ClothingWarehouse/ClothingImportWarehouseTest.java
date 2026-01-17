package com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory.ClothingWarehouse;

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
 *  Unit tests for the ClothingImportWarehouse class.
 *  This suite verifies the concrete implementation of the import hooks:
 *  picking, unloading, and retrieving containers.
 */
public class ClothingImportWarehouseTest 
{

    private ClothingImportWarehouse warehouse;
    private ImportSubTerminal mockImpTerminal;
    private ExportSubTerminal mockExpTerminal;
    private Container mockContainer;

    /**
     *  Initializes the warehouse and mocked dependencies before each test.
     */
    @BeforeEach
    public void setUp() 
    {
        warehouse = new ClothingImportWarehouse();
        mockImpTerminal = mock(ImportSubTerminal.class);
        mockExpTerminal = mock(ExportSubTerminal.class);
        mockContainer = mock(Container.class);
    }

    /**
     *  Verifies that the warehouse picks up a container from the Import Terminal
     *  specifically filtered by CLOTHING goods.
     */
    @Test
    @DisplayName("Should pick a clothing container from the Import Terminal")
    public void testPick() 
    {
        /**
         *  Arrange: Terminal returns a container when asked for CLOTHING.
         */
        when(mockImpTerminal.borrowFromWarehouse(GoodsType.CLOTHING)).thenReturn(mockContainer);
        when(mockContainer.getContainerCode()).thenReturn("CLO-IMP-99");

        /**
         *  Act.
         */
        warehouse.pick(mockImpTerminal);

        /**
         *  Assert.
         */
        assertEquals(1, warehouse.getSize(), "Warehouse should have taken the container.");
        verify(mockImpTerminal).borrowFromWarehouse(GoodsType.CLOTHING);
    }

    /**
     *  Verifies that unloading the container correctly changes its state to EMPTY.
     */
    @Test
    @DisplayName("Should set container state to EMPTY during unloading")
    public void testUnload() 
    {
        /**
         *  Arrange: Manually add container to the warehouse list.
         */
        warehouse.clothingImportWarehouse.add(mockContainer);

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
     *  Verifies that retrieve moves the container to the Export Terminal
     *  and clears the warehouse's internal storage.
     */
    @Test
    @DisplayName("Should move container to export terminal and clear local list")
    public void testRetrieve() 
    {
        /**
         *  Arrange.
         */
        warehouse.clothingImportWarehouse.add(mockContainer);

        /**
         *  Act.
         */
        warehouse.retrieve(mockExpTerminal);

        /**
         *  Assert.
         */
        verify(mockExpTerminal).addFromWarehouse(mockContainer);
        assertEquals(0, warehouse.getSize(), "Warehouse storage should be empty after retrieval.");
    }

    /**
     *  Verifies the full Template Method execution sequence.
     */
    @Test
    @DisplayName("Should execute the full import template workflow successfully")
    public void testFullTemplateWorkflow() 
    {
        /**
         *  Arrange.
         */
        when(mockImpTerminal.borrowFromWarehouse(GoodsType.CLOTHING)).thenReturn(mockContainer);
        when(mockContainer.getContainerCode()).thenReturn("CLO-TEST");

        /**
         *  Act: Call the final method from the abstract parent.
         */
        warehouse.warehouseImport(mockImpTerminal, mockExpTerminal);

        /**
         *  Assert: Verify the container was processed and moved.
         */
        verify(mockContainer).setContainerState(ContainerState.EMPTY);
        verify(mockExpTerminal).addFromWarehouse(mockContainer);
        assertTrue(warehouse.clothingImportWarehouse.isEmpty());
    }
}