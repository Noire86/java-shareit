package ru.practicum.shareit.validation.annotations;

import ru.practicum.shareit.validation.EmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EmailValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueEmail {

    String message() default "User is not allowed to use an email that is already occupied by another user";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
