package com.davideferrari.logisticsystem.Menu.CargoShipCaptain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerFactory.Container;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.TerminalComposite.ExportSubTerminal;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.TerminalComposite.ImportSubTerminal;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.TerminalOperatorMenu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import java.lang.reflect.Field;

public class CargoShipTest
{

    private CargoShip ship;
    private ImportSubTerminal mockImp;
    private ExportSubTerminal mockExp;
    private Container mockContainer;

    @BeforeEach
    public void setUp()
    {
        mockImp = mock(ImportSubTerminal.class);
        mockExp = mock(ExportSubTerminal.class);
        mockContainer = mock(Container.class);
        
        when(mockImp.getName()).thenReturn("Bari Port");
        when(mockExp.getName()).thenReturn("Bari Export");
        
        ship = new CargoShip(mockImp, mockExp);
    }

    @Test
    @DisplayName("Should initialize with correct capacity from annotation")
    public void testCapacityReflection() throws Exception
    {
        Field capacityField = CargoShip.class.getDeclaredField("maximumCapacity");
        capacityField.setAccessible(true);
        int capacity = (int) capacityField.get(ship);
        
        assertEquals(10, capacity, "Capacity should be 10 as defined in @CapacityLimit");
    }

    @Test
    @DisplayName("Should transition to WAITING state on docking request")
    public void testDockingRequest()
    {
        ship.dockingRequest();
        assertEquals(CargoShip.CargoShipState.WAITING, ship.getState());
        assertEquals("Bari Port", ship.getRequestTarget());
    }

    @Test
    @DisplayName("Should transition to DOCKED_FOR_IMPORT when docking is confirmed")
    public void testDockingConfirmation()
    {
        try (MockedStatic<TerminalOperatorMenu> menuMock = mockStatic(TerminalOperatorMenu.class))
        {
            menuMock.when(TerminalOperatorMenu::sendDockConfirmationtoShip).thenReturn(true);
            
            boolean result = ship.dockingConfirmation();
            
            assertTrue(result);
            assertEquals(CargoShip.CargoShipState.DOCKED_FOR_IMPORT, ship.getState());
        }
    }

    @Test
    @DisplayName("Should unload containers and transition to DOCKED_FOR_EXPORT")
    public void testDropInTerminal()
    {
        try (MockedStatic<TerminalOperatorMenu> menuMock = mockStatic(TerminalOperatorMenu.class))
        {
            menuMock.when(TerminalOperatorMenu::sendDockConfirmationtoShip).thenReturn(true);
            
            ship.dockingConfirmation();
            
            ship.pickFromTerminal(mockContainer);
            when(mockImp.shipImport(mockContainer)).thenReturn(true);
            
            boolean result = ship.dropInTerminal();
            
            assertTrue(result, "Unloading should complete successfully");
            assertEquals(0, ship.getSize(), "Ship should be empty");
            assertEquals(CargoShip.CargoShipState.DOCKED_FOR_EXPORT, ship.getState());
        }
    }

    @Test
    @DisplayName("Should fail loading if capacity is exceeded")
    public void testCapacityLimitEnforcement()
    {
        for (int i = 0; i < 10; i++)
        {
            ship.pickFromTerminal(mock(Container.class));
        }
        
        boolean result = ship.pickFromTerminal(mockContainer);
        
        assertFalse(result, "Should block loading beyond capacity");
        assertEquals(10, ship.getSize());
    }
}