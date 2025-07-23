package com.tmuungani.todo.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ValidateEmailTest {

    @Test
    public void testValidateEmail() {
//        Arrange
        String email = "test@test.com";

//        Act
        boolean result = EmailValidator.isValidEmail(email);

//        Assert
        Assertions.assertTrue(result);
    }
}
