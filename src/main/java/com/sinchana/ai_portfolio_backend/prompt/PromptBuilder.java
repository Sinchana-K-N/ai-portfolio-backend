package com.sinchana.ai_portfolio_backend.prompt;

import com.sinchana.ai_portfolio_backend.context.PortfolioContext;

import java.util.HashMap;
import java.util.Map;

public class PromptBuilder {

    // Casual greetings map
    private static final Map<String, String> casualResponses = new HashMap<>();

    static {
        casualResponses.put("hi", "Hey! I’m Sinchana’s AI assistant 👋");
        casualResponses.put("hello", "Hey! I’m Sinchana’s AI assistant 👋");
        casualResponses.put("hey", "Hey! I’m Sinchana’s AI assistant 👋");
    }

    public static String build(String question) {
        if (question == null || question.trim().isEmpty()) {
            return "Please enter a message.";
        }

        String normalizedQuestion = question.trim().toLowerCase();

        // 1️⃣ Handle casual greetings first
        if (casualResponses.containsKey(normalizedQuestion)) {
            return casualResponses.get(normalizedQuestion);
        }

        // 2️⃣ Detect intent keywords
        String intent = detectIntent(normalizedQuestion);


        // 3️⃣ Build AI prompt
        return """
                You are PortfolioCopilot, an AI assistant.

                Rules:
                - Use ONLY the information provided
                - Do NOT invent or assume anything
                - Be concise, confident, and professional
                - Always end answers with a friendly "Hire Me" CTA

                Portfolio Information:
                %s

                Question:
                %s

                Intent:
                %s
                """.formatted(PortfolioContext.DATA, question, intent);
    }

    // Detects intent based on keywords
    private static String detectIntent(String question) {
        if (containsKeywords(question, new String[]{"about", "who is", "tell me"})) {
            return "about";
        } else if (containsKeywords(question, new String[]{"skill", "tech stack", "technology", "tools"})) {
            return "skills";
        } else if (containsKeywords(question, new String[]{"project", "work", "experience"})) {
            return "projects";
        } else if (containsKeywords(question, new String[]{"contact", "email", "linkedin", "github"})) {
            return "contact";
        } else {
            return "general";
        }
    }

    // Utility method to check if question contains any keywords
    private static boolean containsKeywords(String question, String[] keywords) {
        for (String keyword : keywords) {
            if (question.contains(keyword)) return true;
        }
        return false;
    }
}
