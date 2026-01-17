package com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory.ElectronicsWarehouse;

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
import com.davideferrari.logisticsystem.Menu.WarehouseManager.AbstractFactory.ExportWarehouse;
import com.davideferrari.logisticsystem.Utils.Annotations.AppDesignPattern;

/**
 *  This class represents the specific warehouse department responsible for exporting electronics.
 *  In the Abstract Factory Design Pattern, this class acts as a Concrete Product.
 *  It provides the specific implementation for the export workflow steps defined in the
 *  ExportWarehouse template.
 *  It also implements ContainerCollection to temporarily hold the container while it is
 *  being loaded with goods.
 */
@AppDesignPattern(pattern = "Abstract Factory", justification = "Concrete Product")
public class ElectronicsExportWarehouse extends ExportWarehouse implements ContainerCollection
{
    private static final Logger logger = Logger.getLogger(ElectronicsExportWarehouse.class.getName());
    public List <Container> electronicsExportWarehouse = new ArrayList<>();

    private final GoodsType goods = GoodsType.ELECTRONICS;

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
        return electronicsExportWarehouse.size();
    }

    @Override
    public Container getContainerAt(int index)
    {
        return electronicsExportWarehouse.get(index);
    }

    /**
     *  This method requests an empty container from the Export Sub-Terminal.
     *  If an empty container is available, it is moved to this warehouse's local storage.
     *  @param exp The terminal source.
     */
    @Override
    protected void request(ExportSubTerminal exp)
    {
        Container c = exp.borrowFromWarehouse();
        if (c != null && c.getContainerState() == Container.ContainerState
        .EMPTY)
        {
            electronicsExportWarehouse.add(c);
            logger.info(c.getContainerCode() + " has the right conditions for being borrowed by the electronics warehouse.");

        }
        else
        {
            logger.info("No suitable container was found for the electronics warehouse.");
        }
    }

    /**
     *  This method confirms the container is ready for loading.
     */
    @Override
    protected void pick()
    {
        if(!electronicsExportWarehouse.isEmpty())
        {
            logger.info("The container is now in the electronics warehouse, ready to be loaded with goods for the export operation.");
        }
    }

    /**
     *  This method loads the container with electronic goods.
     */
    @Override
    protected void load()
    {
        logger.info("The container has been loaded with goods from the warehouse.");
        ContainerIterator containerIterator = this.createIterator();
        while(containerIterator.hasNext())
        {
            Container c = containerIterator.next();
            c.setGoods(this.goods);
            c.setContainerState(ContainerState.FULL_EXPORT);
        }
    }

     /**
     *  This method returns the loaded container to the Export Sub-Terminal,
     *  and clears the local warehouse storage after the transfer is complete.
     *  @param exp The terminal destination.
     */
    @Override
    protected void retrieve(ExportSubTerminal exp)
    {
        logger.info("The container is being retrieved by the electronics warehouse, and will be returned to the export sub-terminal.");
        ContainerIterator containerIterator = this.createIterator();
        while(containerIterator.hasNext())
        {
            Container c = containerIterator.next();
            exp.addFromWarehouse(c);
        }
        electronicsExportWarehouse.clear();
    }

}