package com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory;

import static org.mockito.Mockito.*;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.TerminalComposite.ExportSubTerminal;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.TerminalComposite.ImportSubTerminal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

/**
 *  Unit tests for the ImportWarehouse abstract class.
 *  This test class is placed in the same package as the source class to allow 
 *  access to {@code protected} methods. It verifies that the Template Method 
 *  correctly orchestrates the import workflow.
 */
class ImportWarehouseTest 
{

    private ImportSubTerminal mockImp;
    private ExportSubTerminal mockExp;
    private ImportWarehouse spyWarehouse;

    /**
     *  Sets up the test environment by creating mocks and a spy of the abstract class.
     */
    @BeforeEach
    void setUp() 
    {
        mockImp = mock(ImportSubTerminal.class);
        mockExp = mock(ExportSubTerminal.class);

        /**
         *  We create an anonymous inner class to provide a concrete instance of the 
         *  abstract class for testing purposes.
         */
        ImportWarehouse stubWarehouse = new ImportWarehouse() 
        {
            @Override
            protected void pick(ImportSubTerminal imp) 
            {
                /**
                 *  Method implementation is empty as we only want to track its execution. 
                 */
            }

            @Override
            protected void unload() 
            {
                /**
                 *  Method implementation is empty as we only want to track its execution. 
                 */
            }

            @Override
            protected void retrieve(ExportSubTerminal exp) 
            {
                /**
                 *  Method implementation is empty as we only want to track its execution. 
                 */
            }
        };

        /**
         *  We wrap the stub in a Mockito spy to track interactions with protected methods.
         */
        spyWarehouse = spy(stubWarehouse);
    }

    /**
     *  Verifies that the {@code warehouseImport} method executes the internal 
     *  steps in the mandatory sequence.
     */
    @Test
    @DisplayName("Should execute pick, unload, and retrieve in a strict sequence")
    void testTemplateMethodExecutionOrder() 
    {
        /**
         *  Act: Execute the final Template Method.
         */
        spyWarehouse.warehouseImport(mockImp, mockExp);

        /**
         *  Assert: Create an InOrder verifier to check the sequence.
         */
        InOrder inOrder = inOrder(spyWarehouse);
        
        /**
         *  These methods are visible here because the test is in the same package.
         */
        inOrder.verify(spyWarehouse).pick(mockImp);
        inOrder.verify(spyWarehouse).unload();
        inOrder.verify(spyWarehouse).retrieve(mockExp);
    }

    /**
     *  Verifies that the terminal parameters are correctly passed through the algorithm.
     */
    @Test
    @DisplayName("Should propagate terminal dependencies to internal steps")
    void testParameterPropagation() 
    {
        /**
         *  Act.
         */
        spyWarehouse.warehouseImport(mockImp, mockExp);

        /**
         *  Assert.
         */
        verify(spyWarehouse).pick(mockImp);
        verify(spyWarehouse).retrieve(mockExp);
    }
}