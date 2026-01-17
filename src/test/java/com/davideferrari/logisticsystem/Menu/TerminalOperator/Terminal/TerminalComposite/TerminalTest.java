package com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.TerminalComposite;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.davideferrari.logisticsystem.Menu.CargoShipCaptain.CargoShip;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerFactory.Container;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerSingleton.ContainerRegister;
import com.davideferrari.logisticsystem.Utils.ExceptionHandling.ContainerValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

/**
 *  Unit tests for the Terminal class.
 *  This suite validates the Composite pattern behavior and the static 
 *  orchestration of global container removals.
 */
class TerminalTest 
{
    private Terminal mainHub;
    private ExportSubTerminal bariExport;
    private ExportSubTerminal busanExport;
    private CargoShip mockShip;

    /**
     *  Resets the environment and initializes the Composite structure.
     */
    @BeforeEach
    void setUp() 
    {
        /**
         *  Initializing the Composite instance.
         */
        mainHub = new Terminal("Bari-Busan Hub");
        
        /**
         *  Initializing Leaf mocks and external dependencies.
         */
        bariExport = mock(ExportSubTerminal.class);
        busanExport = mock(ExportSubTerminal.class);
        mockShip = mock(CargoShip.class);

        /**
         *  Clears the Singleton Register to ensure a clean state for unique code checks.
         */
        ContainerRegister register = ContainerRegister.getInstance();
        List<Container> current = new ArrayList<>(register.displayContainers());
        for (Container c : current) 
        {
            register.removeContainer(c);
        }
    }

    /**
     *  Verifies the <b>Composite Pattern</b> delegation logic.
     *  Ensures that calling a method on the main Terminal (Composite) 
     *  triggers the corresponding method on all added Sub-Terminals (Leaves).
     */
    @Test
    @DisplayName("Should delegate description calls to all child components")
    void testCompositeDelegation() 
    {
        /**
         *  Arrange: Building the tree structure.
         */
        mainHub.addComponent(bariExport);
        mainHub.addComponent(busanExport);

        /**
         *  Act: Call method on the Composite.
         */
        mainHub.displaySubTerminals();

        /**
         *  Assert: Verify that both leaves were visited.
         */
        verify(bariExport, times(1)).description();
        verify(busanExport, times(1)).description();
    }

    /**
     *  Verifies the static removal transaction logic.
     *  Ensures that if the container exists, is empty, and inventory is above 20,
     *  the system successfully performs the deletion.
     */
    @Test
    @DisplayName("Should successfully coordinate a global container removal")
    void testRemoveGloballySuccess() throws ContainerValidationException 
    {
        /**
         *  Arrange: Setup target container and populate register to exceed safety threshold.
         */
        String targetCode = "TGT-123";
        Container target = mock(Container.class);
        when(target.getContainerCode()).thenReturn(targetCode);
        when(target.getContainerState()).thenReturn(Container.ContainerState.EMPTY);
        when(target.getType()).thenReturn("Box");

        ContainerRegister register = ContainerRegister.getInstance();
        
        /**
         *  Adding 25 dummy containers with valid codes to satisfy the CapacityLimit check.
         */
        for (int i = 0; i < 25; i++) 
        {
            Container dummy = mock(Container.class);
            when(dummy.getContainerCode()).thenReturn("DUMMY-" + i);
            register.addContainer(dummy);
        }
        register.addContainer(target);

        /**
         *  Stubbing removal interactions.
         */
        when(mockShip.containsContainer(target)).thenReturn(false);
        when(bariExport.removeLocally(target)).thenReturn(true);

        /**
         *  Act: Execute static removal logic.
         */
        Terminal.removeGlobally(targetCode, bariExport, busanExport, mockShip);

        /**
         *  Assert.
         */
        assertFalse(register.displayContainers().contains(target), 
            "Container should no longer exist in the global register.");
    }
}