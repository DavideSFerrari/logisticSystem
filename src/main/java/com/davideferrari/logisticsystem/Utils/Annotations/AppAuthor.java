package com.davideferrari.logisticsystem.Utils.Annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *  This annotation provides all the information related to the author
 *  of the application. Its retention is set on RUNTIME, in order to
 *  make sure that it results available during the execution of the program,
 *  so that it is retrieved and displayed dynamically via Java Reflection.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface AppAuthor
{
    String name() default "Davide Stefano Ferrari";
    String role() default "Student";
    String degree() default "Bachelor of Science";
    String course() default "Computer Engineering & AI";
    String school() default "Epicode Institute of Technology";
    String subject() default "Object Oriented Programming";
    String project() default "Logistic System";
}