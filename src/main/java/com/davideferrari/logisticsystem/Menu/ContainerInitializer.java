package com.davideferrari.logisticsystem.Menu;

import java.util.logging.Logger;

import com.davideferrari.logisticsystem.Menu.CargoShipCaptain.CargoShip;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerFactory.BoxSelector;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerFactory.Container;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerFactory.ContainerCreator;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerFactory.HighCubeSelector;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerSingleton.ContainerRegister;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.TerminalComposite.ExportSubTerminal;

/**
 *  This class is responsible for populating the logistics system with initial data, 
 *  where containers are created and placed into the Export Sub-Terminals of Bari and Busan ports
 *  and onto the Cargo ship at the start of the application.
 *  The containers are properly initialized in order to to enable the immediate execution of the logistics cycle.
 */
public class ContainerInitializer
{
    private static final Logger logger = Logger.getLogger(ContainerInitializer.class.getName());

    /**
     *  This method centralizes the logic for creating, configuring, and adding a container to a terminal.
     *  @param terminal The export terminal where the container will be stored.
     *  @param creator  The factory (Selector) used to create the specific type of container (Box or HighCube).
     *  @param code     The unique alphanumeric code of the container.
     *  @param goods    The type of goods contained within (e.g., CLOTHING, FOOD).
     */
    private static void createAndAdd(ExportSubTerminal terminal, ContainerCreator<? extends Container> creator, String code, Container.GoodsType goods) 
    {
        Container c = creator.registerContainer(code);
        c.setGoods(goods);
        c.setContainerState(Container.ContainerState.FULL_EXPORT);
        terminal.addContainer(c, code);
    }

    /**
     *  This method initializes the default load for the Bari and Busan Export Sub-Terminals,
     *  by populating both terminals with a diverse mix of 10 containers each.
     *  This method populates both terminals with a diverse mix of 10 containers each.
     *  @param bariExp  The Export Sub-Terminal instance for the port of Bari.
     *  @param busanExp The Export Sub-Terminal instance for the port of Busan.
     */
    public static void initializeTerminalLoad(ExportSubTerminal bariExp, ExportSubTerminal busanExp)
    {
        HighCubeSelector hcSelector = new HighCubeSelector();
        BoxSelector boxSelector = new BoxSelector();

        /**
         *  BARI EXPORT INITIALIZATION
         */

        /**
         *  High Cubes
         */
        createAndAdd(bariExp, hcSelector, "MSDU12345678", Container.GoodsType.CLOTHING);
        createAndAdd(bariExp, hcSelector, "NORU12345678", Container.GoodsType.CLOTHING);
        createAndAdd(bariExp, hcSelector, "EGSU12345678", Container.GoodsType.ELECTRONICS);
        createAndAdd(bariExp, hcSelector, "MDNU12345678", Container.GoodsType.ELECTRONICS);
        createAndAdd(bariExp, hcSelector, "EGHU12345678", Container.GoodsType.FOOD);

        /**
         *  Boxes
         */
        createAndAdd(bariExp, boxSelector, "MSDU87654311", Container.GoodsType.FOOD);
        createAndAdd(bariExp, boxSelector, "NORU87654311", Container.GoodsType.FURNITURE);
        createAndAdd(bariExp, boxSelector, "EGSU87654311", Container.GoodsType.FURNITURE);
        createAndAdd(bariExp, boxSelector, "MDNU87654311", Container.GoodsType.ELECTRONICS);
        createAndAdd(bariExp, boxSelector, "EGHU87654311", Container.GoodsType.ELECTRONICS);


        /**
         *  BUSAN EXPORT INITIALIZATION
         */

        /**
         *  High Cubes
         */
        createAndAdd(busanExp, hcSelector, "TLLU12345678", Container.GoodsType.ELECTRONICS);
        createAndAdd(busanExp, hcSelector, "IMNU12345678", Container.GoodsType.ELECTRONICS);
        createAndAdd(busanExp, hcSelector, "UHHU12345678", Container.GoodsType.CLOTHING);
        createAndAdd(busanExp, hcSelector, "TXGU12345678", Container.GoodsType.CLOTHING);
        createAndAdd(busanExp, hcSelector, "SEKU12345678", Container.GoodsType.FOOD);

        /**
         *  Boxes
         */
        createAndAdd(busanExp, boxSelector, "TLLU87654322", Container.GoodsType.FOOD);
        createAndAdd(busanExp, boxSelector, "IMNU87654322", Container.GoodsType.FURNITURE);
        createAndAdd(busanExp, boxSelector, "UHHU87654322", Container.GoodsType.FURNITURE);
        createAndAdd(busanExp, boxSelector, "TXGU87654322", Container.GoodsType.ELECTRONICS);
        createAndAdd(busanExp, boxSelector, "SEKU87654322", Container.GoodsType.ELECTRONICS);

        logger.info("Terminal containers initialized for Bari and Busan.");
    }

    /**
     *  This method nitializes the Cargo Ship with a pre-defined set of 10 mixed containers.
     *  @param ship The Cargo Ship instance to be loaded.
     */
    public static void initializeShipLoad(CargoShip ship) 
    {
        ContainerRegister globalRegister = ContainerRegister.getInstance();
        BoxSelector shipBoxCreator = new BoxSelector();

        String[] codes =
        {
            "AAAA0001", "AAAA0002", "AAAA0003", "AAAA0004", "AAAA0005",
            "AAAA0006", "AAAA0007", "AAAA0008", "AAAA0009", "AAAA0010"
        };
        
        Container.GoodsType[] goodsList =
        {
            Container.GoodsType.CLOTHING, Container.GoodsType.FOOD, 
            Container.GoodsType.ELECTRONICS, Container.GoodsType.FURNITURE,
            Container.GoodsType.CLOTHING, Container.GoodsType.FOOD,
            Container.GoodsType.ELECTRONICS, Container.GoodsType.FURNITURE,
            Container.GoodsType.CLOTHING, Container.GoodsType.FOOD
        };

        for (int i = 0; i < 10; i++)
        {
            Container c = shipBoxCreator.registerContainer(codes[i]);
            c.setContainerState(Container.ContainerState.FULL_IMPORT);
            c.setGoods(goodsList[i]);
            c.setLocation("HELEN III Cargo Ship");
            globalRegister.addContainer(c);
            ship.pickFromTerminal(c);
        }
        ship.resetOperationStatus();
        
        logger.info("Ship " + ship.getName() + " initialized with 10 mixed-goods containers.");
    }
}
