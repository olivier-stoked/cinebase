package com.wiss.cinebase.dto;

import java.time.LocalDateTime;

public class ErrorResponseDTO {

    private String error;
    private String message;
    private int status;
    private LocalDateTime timestamp;
    private String path;

    public ErrorResponseDTO(String error, String message, int status, String path){
        this.error = error;
        this.message = message;
        this.status = status;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }

    // Getter und Setter
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
}
