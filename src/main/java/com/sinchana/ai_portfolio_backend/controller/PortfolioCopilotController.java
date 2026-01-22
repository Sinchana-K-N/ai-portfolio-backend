package com.sinchana.ai_portfolio_backend.controller;

import com.sinchana.ai_portfolio_backend.dto.AnswerResponse;
import com.sinchana.ai_portfolio_backend.dto.QuestionRequest;
import com.sinchana.ai_portfolio_backend.prompt.PromptBuilder;
import com.sinchana.ai_portfolio_backend.service.GeminiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioCopilotController {

    private final GeminiService geminiService;

    public PortfolioCopilotController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @PostMapping("/copilot")
    public ResponseEntity<AnswerResponse> ask(@RequestBody QuestionRequest request) {

        if (request.getQuestion() == null || request.getQuestion().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(new AnswerResponse("Question cannot be empty"));
        }

        String prompt = PromptBuilder.build(request.getQuestion());
        String answer = geminiService.askGemini(prompt);

        return ResponseEntity.ok(new AnswerResponse(answer));
    }
}
