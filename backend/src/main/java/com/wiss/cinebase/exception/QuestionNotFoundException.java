package com.wiss.cinebase.exception;

public class QuestionNotFoundException extends RuntimeException{

    private final Long questionId;

    public QuestionNotFoundException(Long questionId){
        super("Question with ID: " + questionId + " not found");
        this.questionId = questionId;
    }

    public Long getQuestionId() {
        return questionId;
    }
}
