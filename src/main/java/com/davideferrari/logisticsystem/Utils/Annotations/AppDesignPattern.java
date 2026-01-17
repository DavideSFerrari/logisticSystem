package com.davideferrari.logisticsystem.Utils.Annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *  This annotations displays the information regarding the current pattern
 *  implemented in the designated class.
 *  Its retention is set on RUNTIME, in order to
 *  make sure that it results available during the execution of the program,
 *  so that it is retrieved and displayed dynamically via Java Reflection.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface AppDesignPattern
{
    String pattern();
    String justification();
}