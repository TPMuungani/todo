package com.tmuungani.todo.dto;

public record AuthenticationResponse<T>(boolean success, String message, T body) {
    public AuthenticationResponse(boolean success, String message){
        this(success,message, null);
    }
    public AuthenticationResponse(boolean success){
        this(success,null);
    }
}
