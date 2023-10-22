package com.gladun.buscompany.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ScheduleCheckValidator.class)
@Target({ ElementType.TYPE} )
@Retention(RetentionPolicy.RUNTIME)
public @interface ScheduleCheck {

    String message() default "Schedule format is wrong!";

    String schedule();

    String dates();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
