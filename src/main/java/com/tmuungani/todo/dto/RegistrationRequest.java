package com.tmuungani.todo.dto;

public record RegistrationRequest(String firstName,
                                  String lastName,
                                  String email,
                                  String password,
                                  String confirmPassword,
                                  String department,
                                  String cellNumber) {
}
