package com.davideferrari.logisticsystem.Menu.TerminalOperator.Terminal.TerminalComposite;
import com.davideferrari.logisticsystem.Utils.Annotations.AppDesignPattern;

/**
 *  This interface represents the base component interface for the Terminal hierarchy.
 *  In the Composite Design Pattern, this interface acts as the Component,
 *  and it defines the common operations that must be implemented
 *  by both the individual sub-terminals (Leaves) and the main terminals (Composites).
 *  This allows client code to treat all parts of the terminal structure uniformly.
 */
@AppDesignPattern(pattern = "Composite", justification = "Component Interface")
interface TerminalComponent
{
    /**
     *  This method outputs a description of the component to the log/console.
     *  The implementations should print their specific details (e.g., "Export Terminal of Bari")
     *  or recursively describe their children if they are composites.
     */
    public void description();
}
