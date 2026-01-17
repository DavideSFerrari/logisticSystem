package com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerFactory;
import com.davideferrari.logisticsystem.Utils.Annotations.AppDesignPattern;

/**
 *  This abstract base representa a shipping container.
 *  In the context of the Factory Method Design Pattern, this class serves as the
 *  Product interface. It defines the common properties (weight, dimensions, state)
 *  and behaviors that all specific container types must inherit.
 */
@AppDesignPattern(pattern = "Factory", justification = "Product")
public abstract class Container
{

    protected int tareWeight;
    protected double height;
    protected int maxPayload;
    protected ContainerState state = ContainerState.EMPTY;
    protected String location;
    protected String containerCode;
    protected GoodsType goods = GoodsType.NONE;

    /**
     *  Enum representing the lifecycle state of a container.
     */
    public enum ContainerState
    {
        EMPTY,
        FULL_IMPORT,
        FULL_EXPORT
    }

    /**
     *  Enum representing the category of goods stored in the container.
     *  Used by the Warehouse system to route containers to the correct storage facility.
     */
    public enum GoodsType
    {
        CLOTHING,
        ELECTRONICS,
        FOOD,
        FURNITURE,
        NONE
    }

    public double getMaxPayload()
    {
        return this.maxPayload;
    }

    /**
     *  This method updates the operational state of the container.
     *  @param state The new state (e.g., FULL_IMPORT).
     */
    public void setContainerState(ContainerState state)
    {
        this.state = state;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }
    
    protected void setContainerCode(String containerCode)
    {
        this.containerCode = containerCode;
    }
    
    public ContainerState getContainerState()
    {
        return this.state;
    }

    public String getLocation()
    {
        return this.location;
    }
 
    public String getContainerCode()
    {
        return this.containerCode;
    }

    public double getHeight()
    {
        return height;
    }
    
    public int getTareWeight(){
        return tareWeight;
    }

    public GoodsType getGoods()
    {
        return goods;
    }

    public void setGoods(GoodsType goods)
    {
        this.goods = goods;
    }
    
    /**
     *  This abstract method retrieves the specific type name of the container.
     *  Subclasses must implement this to identify themselves
     *  (e.g., returning "Box" or "HighCube").
     *  @return The string representation of the container type.
     */
    public abstract String getType();

}
