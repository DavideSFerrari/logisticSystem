package com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory.ElectronicsWarehouse;

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
 *  Unit tests for the ElectronicsImportWarehouse class.
 *  This suite verifies the concrete implementation of the electronics import workflow,
 *  ensuring the warehouse correctly interacts with terminal components.
 */
public class ElectronicsImportWarehouseTest 
{

    private ElectronicsImportWarehouse warehouse;
    private ImportSubTerminal mockImpTerminal;
    private ExportSubTerminal mockExpTerminal;
    private Container mockContainer;

    /**
     *  Initializes the warehouse and mocked dependencies before each test.
     */
    @BeforeEach
    public void setUp() 
    {
        warehouse = new ElectronicsImportWarehouse();
        mockImpTerminal = mock(ImportSubTerminal.class);
        mockExpTerminal = mock(ExportSubTerminal.class);
        mockContainer = mock(Container.class);
    }

    /**
     *  Verifies that the warehouse picks up a container from the Import Terminal
     *  specifically filtered by ELECTRONICS goods.
     */
    @Test
    @DisplayName("Should pick an electronics container from the Import Terminal")
    public void testPick() 
    {
        /**
         *  Arrange: Terminal returns a container when asked for ELECTRONICS.
         */
        when(mockImpTerminal.borrowFromWarehouse(GoodsType.ELECTRONICS)).thenReturn(mockContainer);
        when(mockContainer.getContainerCode()).thenReturn("ELEC-IMP-001");

        /**
         *  Act.
         */
        warehouse.pick(mockImpTerminal);

        /**
         *  Assert.
         */
        assertEquals(1, warehouse.getSize(), "Warehouse should have taken the container.");
        verify(mockImpTerminal).borrowFromWarehouse(GoodsType.ELECTRONICS);
    }

    /**
     *  Verifies that unloading the container correctly changes its state to EMPTY.
     */
    @Test
    @DisplayName("Should set container state to EMPTY during electronics unloading")
    public void testUnload() 
    {
        /**
         *  Arrange: Manually add container to the internal warehouse list.
         */
        warehouse.electronicsImportWarehouse.add(mockContainer);

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
    @DisplayName("Should move empty electronics container to export terminal")
    public void testRetrieve() 
    {
        /**
         *  Arrange.
         */
        warehouse.electronicsImportWarehouse.add(mockContainer);

        /**
         *  Act.
         */
        warehouse.retrieve(mockExpTerminal);

        /**
         *  Assert.
         */
        verify(mockExpTerminal).addFromWarehouse(mockContainer);
        assertEquals(0, warehouse.getSize(), "Electronics storage should be cleared.");
    }

    /**
     *  Verifies the full workflow integration defined by the Template Method in ImportWarehouse.
     */
    @Test
    @DisplayName("Should execute the full Electronics Import Template Method workflow")
    public void testFullIntegratedWorkflow() 
    {
        /**
         *  Arrange.
         */
        when(mockImpTerminal.borrowFromWarehouse(GoodsType.ELECTRONICS)).thenReturn(mockContainer);
        when(mockContainer.getContainerCode()).thenReturn("ELEC-FULL-TEST");

        /**
         *  Act: Trigger the final method from the abstract parent class.
         */
        warehouse.warehouseImport(mockImpTerminal, mockExpTerminal);

        /**
         *  Assert: Verify state transition and hand-off.
         */
        verify(mockContainer).setContainerState(ContainerState.EMPTY);
        verify(mockExpTerminal).addFromWarehouse(mockContainer);
        assertTrue(warehouse.electronicsImportWarehouse.isEmpty());
    }
}