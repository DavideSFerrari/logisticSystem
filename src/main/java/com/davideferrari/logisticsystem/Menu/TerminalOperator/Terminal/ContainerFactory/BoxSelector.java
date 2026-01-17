package com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerFactory;
import com.davideferrari.logisticsystem.Utils.Annotations.AppDesignPattern;

/**
 *  This class represents a concrete implementation of the factory.
 *  In the Factory Method Design Pattern, this class acts as a Concrete Creator.
 *  It overrides the factory method to specifically instantiate and return Box objects.
 *  This ensures that the client code (Terminal Operator Menu) acts on the abstraction (`ContainerCreator`)
 *  rather than coupling directly to the specific container classes.
 */
@AppDesignPattern(pattern = "Factory", justification = "Concrete Creator")
public class BoxSelector extends ContainerCreator<Box>
{

    /**
     *  This method represents the implementation of the factory method,
     *  and encapsulates the instantiation logic for a Box container.
     *  @return A new instance of a {@link Box} container.
     */
    @Override
    protected Box createContainer()
    {
        return new Box();
    }
}
