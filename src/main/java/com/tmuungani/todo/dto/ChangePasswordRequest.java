package com.tmuungani.todo.dto;

public record ChangePasswordRequest(
        String oldPassword,
        String newPassword,
        String newPasswordConfirmation
) {
}
