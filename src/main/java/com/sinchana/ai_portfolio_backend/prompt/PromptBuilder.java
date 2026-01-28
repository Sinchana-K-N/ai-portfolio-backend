package com.sinchana.ai_portfolio_backend.prompt;

import com.sinchana.ai_portfolio_backend.context.PortfolioContext;

import java.util.HashMap;
import java.util.Map;

public class PromptBuilder {

    // Casual greeting keywords (AI decides response)
    private static final Map<String, String> GREETINGS = new HashMap<>();

    static {
        GREETINGS.put("hi", "hi");
        GREETINGS.put("hello", "hello");
        GREETINGS.put("hey", "hey");
        GREETINGS.put("hii", "hi");
        GREETINGS.put("helo", "hello");
        GREETINGS.put("hai", "hi");
    }

    public static String build(String question) {

        if (question == null || question.trim().isEmpty()) {
            return "Please type a message.";
        }

        String normalized = question.trim().toLowerCase();

        // 1️⃣ Detect greeting (do NOT return directly)
        boolean isGreeting = GREETINGS.keySet()
                .stream()
                .anyMatch(g -> isSimilar(normalized, g));

        // 2️⃣ Detect intent
        String intent = isGreeting ? "greeting" : detectIntent(normalized);

        // 3️⃣ Build AI prompt (AI always speaks AS Sinchana)
        return """
        You are Sinchana K N, a Full Stack Developer.
        You are speaking directly to a recruiter or visitor on your portfolio website.

        Personality & Behavior:
        - Speak in FIRST PERSON ("I", "my")
        - Be warm, confident, and human
        - Sound like a real developer, not a bot
        - Assume the user is curious about you professionally
        - Handle spelling mistakes naturally without pointing them out

        Rules:
        - Use ONLY the portfolio information below
        - Do NOT invent or assume facts
        - Keep answers concise but impactful

        If intent is "greeting":
        - Respond casually and friendly
        - Introduce yourself briefly as Sinchana
        - Invite them to ask about skills or projects

        If intent is "skills":
        - Highlight frontend and full-stack strengths clearly

        If intent is "projects":
        - Talk proudly but professionally about real projects

        If intent is "contact":
        - Share contact details confidently

        Always end with a subtle call-to-action encouraging collaboration or hiring.

        Portfolio Information:
        %s

        Detected Intent:
        %s

        User Message:
        %s
        """.formatted(
                PortfolioContext.DATA,
                intent,
                question
        );
    }

    // Intent detection
    private static String detectIntent(String question) {

        if (containsKeywords(question, new String[]{
                "about", "who are you", "tell me about", "introduce"
        })) {
            return "about";
        }

        if (containsKeywords(question, new String[]{
                "skill", "tech", "stack", "frontend", "backend", "react", "spring"
        })) {
            return "skills";
        }

        if (containsKeywords(question, new String[]{
                "project", "work", "experience", "built", "developed"
        })) {
            return "projects";
        }

        if (containsKeywords(question, new String[]{
                "contact", "email", "linkedin", "github", "reach"
        })) {
            return "contact";
        }

        return "general";
    }

    // Keyword matcher
    private static boolean containsKeywords(String text, String[] keywords) {
        for (String keyword : keywords) {
            if (text.contains(keyword)) return true;
        }
        return false;
    }

    // Simple fuzzy match for typos (hi, hii, heyy)
    private static boolean isSimilar(String input, String keyword) {
        return input.equals(keyword)
                || input.startsWith(keyword)
                || levenshteinDistance(input, keyword) <= 1;
    }

    // Levenshtein Distance (lightweight typo handling)
    private static int levenshteinDistance(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];

        for (int i = 0; i <= a.length(); i++) dp[i][0] = i;
        for (int j = 0; j <= b.length(); j++) dp[0][j] = j;

        for (int i = 1; i <= a.length(); i++) {
            for (int j = 1; j <= b.length(); j++) {
                int cost = a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1;
                dp[i][j] = Math.min(
                        Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                        dp[i - 1][j - 1] + cost
                );
            }
        }
        return dp[a.length()][b.length()];
    }
}
