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
 *  Unit tests for the ExportSubTerminal class.
 *  This test verifies that the terminal respects the CapacityLimit 10
 *  both during addition and during the export safety check.
 */
class ExportSubTerminalTest 
{
    private ExportSubTerminal exportTerminal;
    private CargoShip mockShip;

    /**
     *  Resets the Singleton Register and the Terminal instance.
     */
    @BeforeEach
    void setUp() 
    {
        exportTerminal = new ExportSubTerminal("Bari");
        mockShip = mock(CargoShip.class);

        /**
         *  Clear global registry to ensure fresh ID validation.
         */
        ContainerRegister register = ContainerRegister.getInstance();
        List<Container> current = new ArrayList<>(register.displayContainers());
        for (Container c : current) 
        {
            register.removeContainer(c);
        }
    }

    /**
     *  Verifies that the terminal stops export immediately if it is already 
     *  at its minimum capacity (10).
     */
    @Test
    @DisplayName("Should prevent export if terminal is already at minimum capacity")
    void testShipExportCycleAtLimit() throws ContainerValidationException 
    {
        /**
         *  Arrange: Fill the terminal to its maximum allowed capacity (10).
         */
        String codeBase = "MAX-CAP-";
        for (int i = 0; i < 10; i++) 
        {
            Container c = mock(Container.class);
            String code = codeBase + i;
            when(c.getContainerCode()).thenReturn(code);
            when(c.getType()).thenReturn("Box");
            boolean added = exportTerminal.addContainer(c, code);
            assertTrue(added, "Should allow adding up to 10 containers.");
        }

        /**
         *  Confirm the terminal is at the limit.
         */
        assertEquals(10, exportTerminal.getSize());

        /**
         *  Act: Attempt to export.
         */
        exportTerminal.shipExport(mockShip);

        /**
         *  Assert:
         *  Size is 10, minCapacity is 10. 
         *  Logic: (10 - 0 <= 10) is TRUE -> Loop breaks immediately.
         */
        assertEquals(10, exportTerminal.getSize(), "Size should remain 10 as it is at minimum capacity.");
        
        /**
         *  Verify: The ship was never asked to pick a container.
         */
        verify(mockShip, never()).pickFromTerminal(any(Container.class));
    }
}