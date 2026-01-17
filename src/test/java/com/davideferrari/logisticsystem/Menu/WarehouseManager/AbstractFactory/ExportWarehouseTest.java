package com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory;

import static org.mockito.Mockito.*;

import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.TerminalComposite.ExportSubTerminal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

/**
 *  Unit tests for the ExportWarehouse abstract class.
 *  This test class verifies the <b>Template Method</b> pattern logic for the export workflow,
 *  ensuring the steps (request, pick, load, retrieve) occur in the mandatory sequence.
 */
class ExportWarehouseTest 
{

    private ExportSubTerminal mockExp;
    private ExportWarehouse spyWarehouse;

    /**
     *  Sets up the test environment by creating mocks and a spy of the abstract product.
     */
    @BeforeEach
    void setUp() 
    {
        mockExp = mock(ExportSubTerminal.class);

        /**
         *  Creating a concrete stub of the Abstract Product to test the template logic.
         */
        ExportWarehouse stubWarehouse = new ExportWarehouse() 
        {
            @Override
            protected void request(ExportSubTerminal exp) 
            {
            }

            @Override
            protected void pick() 
            {
            }

            @Override
            protected void load() 
            {
            }

            @Override
            protected void retrieve(ExportSubTerminal exp) 
            {
            }
        };

        /**
         *  Wrapping the stub in a spy to verify internal calls to protected methods.
         */
        spyWarehouse = spy(stubWarehouse);
    }

    /**
     *  Verifies that the warehouseExport method enforces the correct
     *  algorithmic skeleton: Request -> Pick -> Load -> Retrieve.
     */
    @Test
    @DisplayName("Should execute the export steps in the exact mandated sequence")
    void testExportWorkflowSequence() 
    {
        /**
         *  Act: Execute the final Template Method.
         */
        spyWarehouse.warehouseExport(mockExp);

        /**
         *  Assert: Create an InOrder verifier to check the sequence of execution.
         */
        InOrder inOrder = inOrder(spyWarehouse);
        
        /**
         *  We verify the internal protected hooks. 
         *  Note: We don't call verifyNoMoreInteractions(spyWarehouse) because 
         *  the call to the template method itself counts as an interaction.
         */
        inOrder.verify(spyWarehouse).request(mockExp);
        inOrder.verify(spyWarehouse).pick();
        inOrder.verify(spyWarehouse).load();
        inOrder.verify(spyWarehouse).retrieve(mockExp);
    }

    /**
     *  Verifies that the export terminal dependency is correctly passed to
     *  the requesting and retrieving steps.
     */
    @Test
    @DisplayName("Should pass the ExportSubTerminal reference to the required hook methods")
    void testDependencyPropagation() 
    {
        /**
         *  Act.
         */
        spyWarehouse.warehouseExport(mockExp);

        /**
         *  Assert.
         */
        verify(spyWarehouse).request(mockExp);
        verify(spyWarehouse).retrieve(mockExp);
    }
}