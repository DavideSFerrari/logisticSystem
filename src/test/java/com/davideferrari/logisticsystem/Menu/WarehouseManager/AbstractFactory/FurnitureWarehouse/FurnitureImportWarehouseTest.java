package com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory.FurnitureWarehouse;

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
 *  Unit tests for the FurnitureImportWarehouse class.
 *  This class verifies the concrete implementation of the import hooks:
 *  picking, unloading, and retrieving furniture containers.
 */
public class FurnitureImportWarehouseTest 
{

    private FurnitureImportWarehouse warehouse;
    private ImportSubTerminal mockImpTerminal;
    private ExportSubTerminal mockExpTerminal;
    private Container mockContainer;

    /**
     *  Initializes the warehouse and mocks before each test execution.
     */
    @BeforeEach
    public void setUp() 
    {
        warehouse = new FurnitureImportWarehouse();
        mockImpTerminal = mock(ImportSubTerminal.class);
        mockExpTerminal = mock(ExportSubTerminal.class);
        mockContainer = mock(Container.class);
    }

    /**
     *  Verifies that the warehouse picks up a container from the Import Terminal
     *  specifically filtered by FURNITURE goods.
     */
    @Test
    @DisplayName("Should pick a furniture container from the Import Terminal")
    public void testPick() 
    {
        /**
         *  Arrange: The terminal should return a container when asked for FURNITURE.
         */
        when(mockImpTerminal.borrowFromWarehouse(GoodsType.FURNITURE)).thenReturn(mockContainer);
        when(mockContainer.getContainerCode()).thenReturn("FURN-IMP-01");

        /**
         *  Act.
         */
        warehouse.pick(mockImpTerminal);

        /**
         *  Assert.
         */
        assertEquals(1, warehouse.getSize(), "Warehouse should have taken the container.");
        verify(mockImpTerminal).borrowFromWarehouse(GoodsType.FURNITURE);
    }

    /**
     *  Verifies that unloading the container correctly transitions its state to EMPTY.
     */
    @Test
    @DisplayName("Should set container state to EMPTY during unloading")
    public void testUnload() 
    {
        /**
         *  Arrange: Place the container manually in the warehouse list.
         */
        warehouse.furnitureImportWarehouse.add(mockContainer);

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
     *  and clears the local warehouse storage.
     */
    @Test
    @DisplayName("Should move container to export terminal and clear internal list")
    public void testRetrieve() 
    {
        /**
         *  Arrange.
         */
        warehouse.furnitureImportWarehouse.add(mockContainer);

        /**
         *  Act.
         */
        warehouse.retrieve(mockExpTerminal);

        /**
         *  Assert.
         */
        verify(mockExpTerminal).addFromWarehouse(mockContainer);
        assertEquals(0, warehouse.getSize(), "Local storage should be empty after retrieval.");
    }

    /**
     *  Verifies the full integrated workflow inherited from the parent Template.
     */
    @Test
    @DisplayName("Should execute the full Furniture Import workflow sequence")
    public void testFullIntegratedWorkflow() 
    {
        /**
         *  Arrange.
         */
        when(mockImpTerminal.borrowFromWarehouse(GoodsType.FURNITURE)).thenReturn(mockContainer);
        when(mockContainer.getContainerCode()).thenReturn("FURN-FULL-TEST");

        /**
         *  Act: Call the final method from the abstract parent.
         */
        warehouse.warehouseImport(mockImpTerminal, mockExpTerminal);

        /**
         *  Assert: Verify state change and successful hand-off.
         */
        verify(mockContainer).setContainerState(ContainerState.EMPTY);
        verify(mockExpTerminal).addFromWarehouse(mockContainer);
        assertTrue(warehouse.furnitureImportWarehouse.isEmpty());
    }
}