package com.tmuungani.todo.controller.authentication.dto;

public record ChangePasswordRequest(
        String oldPassword,
        String newPassword,
        String newPasswordConfirmation
) {
}
