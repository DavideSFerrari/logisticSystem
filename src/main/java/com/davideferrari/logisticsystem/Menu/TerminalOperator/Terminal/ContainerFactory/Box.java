package com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerFactory;
import com.davideferrari.logisticsystem.Utils.Annotations.AppDesignPattern;

/**
 *  This class represents a standard "Box" container (often referred to as a Dry Van).
 *  In the context of the Factory Method Design Pattern, this class acts as a
 *  Concrete Product.
 *  It provides the specific implementation details (dimensions, weight)
 *  for this type of container.
 */
@AppDesignPattern(pattern = "Factory", justification = "Concrete Product")
public
class Box extends Container
{

/**
     *  This method constructs a new Box container with standard specifications,
     *  and the access is package-private to ensure instantiation which
     *  only occurs through the Factory.
     *  It initializes the container with specific physical properties:
     *  - Tare Weight: 2,220 kg
     *  - Height: 1.2 m
     *  - Max Payload: 21,000 kg
     */
    Box()
    {
        this.tareWeight = 2220;
        this.height = 1.2;
        this.maxPayload = 21000;
    }

    @Override
    public String getType()
    {
        return "Box";
    }
}