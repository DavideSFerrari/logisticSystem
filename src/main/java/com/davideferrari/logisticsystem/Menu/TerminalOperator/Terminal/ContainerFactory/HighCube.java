package com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerFactory;
import com.davideferrari.logisticsystem.Utils.Annotations.AppDesignPattern;

/**
 *  This class represents a standard "High Cube" container.
 *  In the context of the Factory Method Design Pattern, this class acts as a
 *  Concrete Product.
 *  It provides the specific implementation details (dimensions, weight)
 *  for this type of container.
 */
@AppDesignPattern(pattern = "Factory", justification = "Concrete Product")
public
class HighCube extends Container
{

    /**
     *  This method constructs a new High Cube container with standard specifications,
     *  and the access is package-private to ensure instantiation which
     *  only occurs through the Factory.
     *  It initializes the container with specific physical properties:
     *  - Tare Weight: 3700 kg
     *  - Height: 1.7 m
     *  - Max Payload: 25,000 kg
     */
    HighCube()
    {
        this.tareWeight = 3700;
        this.height = 1.7;
        this.maxPayload = 25000;
    }

    @Override
    public String getType()
    {
        return "HighCube";
    }
}