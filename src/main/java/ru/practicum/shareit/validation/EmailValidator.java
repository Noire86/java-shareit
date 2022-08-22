package ru.practicum.shareit.validation;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.user.dao.UserDAO;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.validation.annotations.UniqueEmail;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class EmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final UserDAO userDAO;

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return userDAO.getAllUsers()
                .stream()
                .map(UserDto::getEmail)
                .noneMatch(email -> email.equals(s));
    }
}
