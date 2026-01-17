package com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory.FoodWarehouse;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerFactory.Container;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerFactory.Container.ContainerState;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerFactory.Container.GoodsType;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerIterator.ContainerCollection;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerIterator.ContainerIterator;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerIterator.TerminalContainerIterator;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.TerminalComposite.ExportSubTerminal;
import com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.TerminalComposite.ImportSubTerminal;
import com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory.ImportWarehouse;
import com.davideferrari.logisticsystem.Utils.Annotations.AppDesignPattern;

/**
 *  This class represents the specific warehouse department responsible for importing food.
 *  In the Abstract Factory Design Pattern, this class acts as a Concrete Product.
 */
@AppDesignPattern(pattern = "Abstract Factory", justification = "Concrete Product")
public class FoodImportWarehouse extends ImportWarehouse implements ContainerCollection
{
    private static final Logger logger = Logger.getLogger(FoodImportWarehouse.class.getName());
    public List <Container> foodImportWarehouse = new ArrayList<>();

    private final GoodsType goods = GoodsType.FOOD;

    /**
     *  This method creates an iterator to traverse the containers currently in this warehouse.
     *  @return A {@link TerminalContainerIterator} for the local list.
     */
    @Override
    public ContainerIterator createIterator()
    {
        return new TerminalContainerIterator(this);
    }

    @Override
    public int getSize()
    {
        return foodImportWarehouse.size();
    }

    @Override
    public Container getContainerAt(int index)
    {
        return foodImportWarehouse.get(index);
    }

    /**
     *  This method is needed for picking up a container from the Import Terminal.
     *  @param imp The import terminal source.
     */
    @Override
    protected void pick(ImportSubTerminal imp)
    {
        Container c = imp.borrowFromWarehouse(this.goods);
        if (c != null)
        {
            foodImportWarehouse.add(c);
            logger.info(c.getContainerCode() + " is now in the food warehouse, ready to be emptied for the import operation. This container will now be emptied of it's goods.");
        }
        else
        {
            logger.info("No suitable container was found for the food warehouse.");
        }
    }

    /**
     *  This method is needed for unloading the goods, emptying the container.
     */
    @Override
    protected void unload()
    {
        logger.info("The container has been unloaded of it's goods from the warehouse.");
        ContainerIterator containerIterator = this.createIterator();
        while(containerIterator.hasNext())
        {
            Container c = containerIterator.next();
            c.setContainerState(ContainerState.EMPTY);
            // c.setGoods(GoodsType.NONE);
        }
    }

    /**
     *  This method returns the empty container to the Export Terminal.
     *  Once the container is cleared from the warehouse list, it enters the export pool
     *  to be picked up by an Export Warehouse later.
     *  @param exp The export terminal destination.
     */
    @Override
    protected void retrieve(ExportSubTerminal exp)
    {
        logger.info("The container is being retrieved by the food warehouse, and it will be moved to the export sub-terminal.");
        ContainerIterator containerIterator = this.createIterator();
        while(containerIterator.hasNext())
        {
            Container c = containerIterator.next();
            exp.addFromWarehouse(c);
        }
        foodImportWarehouse.clear();
    }

}