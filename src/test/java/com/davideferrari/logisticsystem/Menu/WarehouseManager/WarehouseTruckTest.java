package com.davideferrari.logisticsystem.Menu.WarehouseManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.TerminalComposite.ExportSubTerminal;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.TerminalComposite.ImportSubTerminal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import java.util.concurrent.TimeUnit;

/**
 *  Unit tests for the WarehouseTruck background worker thread.
 *  This test suite validates the monitoring behavior of the autonomous truck,
 *  specifically ensuring it correctly polls the terminal for overload conditions
 *  and handles thread lifecycle events (startup and shutdown).
 */
public class WarehouseTruckTest
{
    /** 
     *  The instance of the truck under test.    
     */
    private WarehouseTruck truck;

    /** 
     *  Mocked import terminal used to simulate cargo levels.
     */
    private ImportSubTerminal mockImportTerminal;

    /**
     *  Mocked export terminal used as a destination for processed goods.
     */
    private ExportSubTerminal mockExportTerminal;

    /**
     *  Sets up the test environment before each individual test.
     *  Initializes mocks and provides default behavior for required methods 
     *  to avoid NullPointerExceptions during logging.
     */
    @BeforeEach
    public void setUp()
    {
        mockImportTerminal = mock(ImportSubTerminal.class);
        mockExportTerminal = mock(ExportSubTerminal.class);
        when(mockImportTerminal.getName()).thenReturn("Bari-Import-Terminal");
    }

    /**
     *  Verifies that the truck correctly accesses the terminal size after its initial sleep cycles.
     *  Logic Sequence:
     *  1. Startup sleep: 2000ms
     *  2. First loop iteration sleep: 5000ms
     *  This test waits 8000ms to ensure the truck thread has moved past these sleeps 
     *  and executed its first getSize() check.
     *  @throws InterruptedException if the test thread is interrupted during the wait period.
     */
    @Test
    @Timeout(value = 15, unit = TimeUnit.SECONDS)
    @DisplayName("Should verify truck calls getSize after initial delays")
    public void testTruckInteraction() throws InterruptedException
    {
        when(mockImportTerminal.getSize()).thenReturn(0);
        truck = new WarehouseTruck(mockImportTerminal, mockExportTerminal);

        Thread truckThread = new Thread(truck);
        truckThread.start();

        /** Logic breakdown:
         *  1. 2000ms: Initial startup sleep
         *  2. 5000ms: First iteration sleep
         *  Total: 7000ms. We wait 8000ms to be safe.
         */
        Thread.sleep(8000);

        verify(mockImportTerminal, atLeastOnce()).getSize();

        truck.parkTruck();
        truckThread.interrupt();
        truckThread.join(2000);
    }

    /**
     *  Validates that calling parkTruck() and interrupting the 
     *  thread successfully terminates the execution loop.
     *  This test ensures that the "engineRunning" flag is respected and that the 
     *  thread does not hang indefinitely.
     *  @throws InterruptedException if the test thread is interrupted while waiting for the truck to stop.
     */
    @Test
    @Timeout(value = 15, unit = TimeUnit.SECONDS)
    @DisplayName("Should handle truck shutdown via parkTruck")
    public void testParkTruckStopsThread() throws InterruptedException
    {
        truck = new WarehouseTruck(mockImportTerminal, mockExportTerminal);
        Thread truckThread = new Thread(truck);
        
        truckThread.start();
        assertTrue(truckThread.isAlive(), "Thread should be running");

        truck.parkTruck();        
        truckThread.interrupt(); 
        truckThread.join(3000);
        assertFalse(truckThread.isAlive(), "Thread should have terminated");
    }
}