package com.wiss.cinebase.exception;

public class DifficultyNotFoundException extends RuntimeException {

    private final String diffculty;

    public DifficultyNotFoundException(String diffculty) {
        super("Diffculty '" + diffculty + "' not found");
        this.diffculty = diffculty;
    }

    public String getDiffculty() {
        return diffculty;
    }
}
