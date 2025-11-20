package com.wiss.cinebase.exception;

public class CategoryNotFoundException extends RuntimeException {

    private final String category;

    public CategoryNotFoundException(String category) {
        super("Categroy '" + category + "' not found");
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
