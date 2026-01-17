package com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.ContainerFactory;
import com.davideferrari.logisticsystem.Utils.Annotations.AppDesignPattern;

/**
 *  This class represents the abstract base class for all container factories,
 *  which in the Factory Method Design Pattern serves as the Creator.
 *  Subclasses like BoxSelector and HighCubeSelector override this method to return
 *  specific instances of the product.
 *  It uses Java Generics to ensure type safety, guaranteeing that the factory
 *  only produces valid container objects.
 *  @param <T> The specific type of Container this factory creates.
 */
@AppDesignPattern(pattern = "Factory", justification = "Creator")
public abstract class ContainerCreator<T extends Container>
{
    /**
     *  This "Factory Method" must be implemented by concrete creators.
     *  It  encapsulates the instantiation logic, deferring the choice of the
     *  specific class to the subclasses.
     *  @return A new instance of a Container subclass.
     */
    protected abstract T createContainer();

    /**
     *  This method xreates a new container and initializes it with a unique identification code.
     *  It orchestrates the creation process:
     *  - Calls createContainer() to get a raw object instance.
     *  - Sets the unique containerCode on that instance.
     *  - Returns the fully initialized object.
     *  @param containerCode The unique 12-character alphanumeric code for the new container.
     *  @return The initialized container ready for use.
     */
    public T registerContainer(String containerCode) {
        T c = createContainer();
        c.setContainerCode(containerCode);
        return c;
    }

}
