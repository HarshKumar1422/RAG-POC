package com.example.rag.rag;

import java.util.List;
import java.util.stream.Collectors;

public class PromptBuilder {

    public static final String SYSTEM = ""
            + "You are a Springboot / java / low level design fundamentals assistant.\n"
            + "Answer ONLY using the provided context from the document.\n"
            + "If the answer is not in the context, say:\n"
            + "\"I don't know based on the provided document.\"\n"
            + "Keep the answer clear and structured.\n";

    public String buildUserPrompt(String question, List<RetrievalService.ScoredSegment> ctx) {
        String contextBlock = ctx.stream()
                .map(s -> "- " + s.getText())
                .collect(Collectors.joining("\n\n"));

        return "CONTEXT:\n"
                + contextBlock
                + "\n\nQUESTION:\n"
                + question;
    }
}
