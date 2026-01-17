package com.davideferrari.logisticsystem.Utils.Annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 *  This annotation sets dynamically the value for the maximum and/or
 *  minimum capacity in different sections of the project 
 *  (e.g., Terminals, Ships, Registers).
 *  Its retention is set on RUNTIME, in order to
 *  make sure that it results available during the execution of the program,
 *  so that it enforces limits dynamically via Java Reflection.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CapacityLimit
{
    int value();
}