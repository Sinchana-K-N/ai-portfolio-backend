package com.sinchana.ai_portfolio_backend.dto;

public class QuestionRequest {
    private String question;

    public QuestionRequest() {}
    public QuestionRequest(String question) { this.question = question; }

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }
}
