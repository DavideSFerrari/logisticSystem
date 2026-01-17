package com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerFactory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerFactory.Container.ContainerState;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerFactory.Container.GoodsType;

/**
 *  Unit tests for the abstract Container class.
 *  Uses an anonymous subclass to verify the shared logic (state management,
 *  location tracking, goods handling) inherited by all concrete products.
 */

class ContainerTest
{
    @Test
    @DisplayName("Should initialize with default state EMPTY and no goods")
    void testDefaultInitialization()
    {
        Container container = new Container()
        {
            @Override
            public String getType()
            {
                return "TestContainer";
            }
        };
        Assertions.assertEquals(ContainerState.EMPTY, container.getContainerState(), "Default state should be EMPTY");
        Assertions.assertEquals(GoodsType.NONE, container.getGoods(), "Default goods should be NONE");
        Assertions.assertNull(container.getLocation(), "Location should be null initially");
    }

    @Test
    @DisplayName("Should correctly update Container State lifecycle")
    void testStateTransitions()
    {
        Container container = new Container()
        {
            @Override
            public String getType()
            {
                return "Test";
            }
        };
        container.setContainerState(ContainerState.FULL_IMPORT);
        Assertions.assertEquals(ContainerState.FULL_IMPORT, container.getContainerState());
        container.setContainerState(ContainerState.FULL_EXPORT);
        Assertions.assertEquals(ContainerState.FULL_EXPORT, container.getContainerState());
    }

    @Test
    @DisplayName("Should correctly handle Goods assignment")
    void testGoodsManagement()
    {
        Container container = new Container()
        {
            @Override
            public String getType()
            { 
                return "Test";
            }
        };

        container.setGoods(GoodsType.ELECTRONICS);
        Assertions.assertEquals(GoodsType.ELECTRONICS, container.getGoods());
    }

    @Test
    @DisplayName("Should allow setting protected code via subclass or package access")
    void testProtectedSetters()
    {
        Container container = new Container()
        {
            @Override
            public String getType()
            {
                return "Test"; 
            }
        };
        container.setContainerCode("TEST-1234");
        container.setLocation("Bari Terminal");
        Assertions.assertEquals("TEST-1234", container.getContainerCode());
        Assertions.assertEquals("Bari Terminal", container.getLocation());
    }
}