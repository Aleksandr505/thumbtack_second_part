package com.gladun.buscompany.validation;

import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ScheduleCheckValidator implements ConstraintValidator<ScheduleCheck, Object> {

    private String schedule;
    private String dates;

    @Override
    public void initialize(ScheduleCheck constraintAnnotation) {
        this.schedule = constraintAnnotation.schedule();
        this.dates = constraintAnnotation.dates();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object scheduleValue = new BeanWrapperImpl(value).getPropertyValue(schedule);
        Object datesValue = new BeanWrapperImpl(value).getPropertyValue(dates);
        if (scheduleValue != null && datesValue == null) {
            return true;
        } else {
            return scheduleValue == null && datesValue != null;
        }
    }
}
