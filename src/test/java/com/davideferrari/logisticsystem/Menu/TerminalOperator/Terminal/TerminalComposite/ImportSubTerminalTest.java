package com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.TerminalComposite;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.davideferrari.logisticsystem.Menu.CargoShipCaptain.CargoShip;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerFactory.Container;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 *  Unit tests for the ImportSubTerminal class.
 *  Verifies the lifecycle of imported cargo, including docking requests,
 *  capacity enforcement, and selective container borrowing.
 */
class ImportSubTerminalTest 
{

    private ImportSubTerminal importTerminal;
    private Container mockContainer;
    private CargoShip mockShip;

    /**
     *  Initializes the test environment with fresh mocks and terminal instances.
     */
    @BeforeEach
    void setUp() 
    {
        importTerminal = new ImportSubTerminal("Bari");
        mockContainer = mock(Container.class);
        mockShip = mock(CargoShip.class);
        ImportSubTerminal.resetRequest();
    }

    /**
     *  Verifies that the CapacityLimit annotation (value = 15) is correctly 
     *  read via reflection in the constructor.
     */
    @Test
    @DisplayName("Should enforce the maximum capacity limit of 15")
    void testCapacityLimitEnforcement() 
    {
        /**
         *  Arrange: Create a loop to fill the terminal to its limit.
         */
        for (int i = 0; i < 15; i++) 
        {
            Container c = mock(Container.class);
            assertTrue(importTerminal.shipImport(c), "Should accept container " + i);
        }

        /**
         *  Act: Attempt to add the 16th container.
         */
        boolean result = importTerminal.shipImport(mockContainer);

        /**
         *  Assert: The terminal should refuse the container.
         */
        assertFalse(result, "Terminal should reject containers beyond capacity (15)");
        assertEquals(15, importTerminal.getSize());
    }

    /**
     *  Verifies that the docking request logic correctly identifies port names 
     *  and ship states.
     */
    @Test
    @DisplayName("Should accept valid ship docking requests for specific ports")
    void testShipRequestLogic() 
    {
        /**
         *  Arrange: Set up a ship targeting Bari while in WAITING state.
         */
        when(mockShip.getRequestTarget()).thenReturn("Bari");
        when(mockShip.getState()).thenReturn(CargoShip.CargoShipState.WAITING);

        /**
         *  Act.
         */
        boolean accepted = ImportSubTerminal.shipRequest(mockShip, importTerminal, mock(ImportSubTerminal.class));

        /**
         *  Assert.
         */
        assertTrue(accepted, "Request should be accepted for Bari in WAITING state");
        assertTrue(ImportSubTerminal.sendConfirmation(), "Confirmation flag should be set to true");
    }

    /**
     *  Verifies that the shipImport method correctly updates the container's 
     *  state and location upon arrival.
     */
    @Test
    @DisplayName("Should update container state to FULL_IMPORT upon unloading")
    void testShipImportStateChanges() 
    {
        /**
         *  Act.
         */
        importTerminal.shipImport(mockContainer);

        /**
         *  Assert: Verify interaction with the mock container.
         */
        verify(mockContainer).setContainerState(Container.ContainerState.FULL_IMPORT);
        verify(mockContainer).setLocation("Bari Import Sub-Terminal");
        assertEquals(1, importTerminal.getSize());
    }

    /**
     *  Verifies that the WarehouseTruck can selectively borrow containers 
     *  based on the GoodsType filter.
     */
    @Test
    @DisplayName("Should allow borrowing only if goods type matches and state is FULL_IMPORT")
    void testBorrowFromWarehouseWithFilter() 
    {
        /**
         *  Arrange: Add a Food container and a Clothing container.
         */
        Container foodContainer = mock(Container.class);
        when(foodContainer.getGoods()).thenReturn(Container.GoodsType.FOOD);
        when(foodContainer.getContainerState()).thenReturn(Container.ContainerState.FULL_IMPORT);
        
        Container clothingContainer = mock(Container.class);
        when(clothingContainer.getGoods()).thenReturn(Container.GoodsType.CLOTHING);
        when(clothingContainer.getContainerState()).thenReturn(Container.ContainerState.FULL_IMPORT);

        importTerminal.shipImport(foodContainer);
        importTerminal.shipImport(clothingContainer);

        /**
         *  Act: Attempt to borrow Food.
         */
        Container result = importTerminal.borrowFromWarehouse(Container.GoodsType.FOOD);

        /**
         *  Assert.
         */
        assertNotNull(result, "Should find and return the food container");
        assertEquals(1, importTerminal.getSize(), "One container should remain in the terminal");
        verify(foodContainer, atLeastOnce()).getGoods();
    }
}