package com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerFactory;
import com.davideferrari.logisticsystem.Utils.Annotations.AppDesignPattern;

@AppDesignPattern(pattern = "Factory", justification = "Concrete Creator")

/**
 *  This class represents a concrete implementation of the factory.
 *  In the Factory Method Design Pattern, this class acts as a Concrete Creator.
 *  It overrides the factory method to specifically instantiate and return High Cube objects.
 *  This ensures that the client code (Terminal Operator Menu) acts on the abstraction (`ContainerCreator`)
 *  rather than coupling directly to the specific container classes.
 */
public class HighCubeSelector extends ContainerCreator<HighCube>
{

    /**
     *  This method represents the implementation of the factory method,
     *  and encapsulates the instantiation logic for a High Cube container.
     *  @return A new instance of a {@link HighCube} container.
     */
    @Override
    protected HighCube createContainer()
    {
        return new HighCube();
    }

}
