package com.sinchana.ai_portfolio_backend.dto;

public class AnswerResponse {
    private String answer;

    public AnswerResponse() {}
    public AnswerResponse(String answer) { this.answer = answer; }

    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }
}
