package com.example.rag.rag;

import com.example.rag.dto.QueryResponse;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.output.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.output.Response;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RagService {

    private static final Logger log = LoggerFactory.getLogger(RagService.class);

    private final RetrievalService retrievalService;
    private final ChatLanguageModel chatModel;
    private final PromptBuilder promptBuilder = new PromptBuilder();

    public RagService(RetrievalService retrievalService, ChatLanguageModel chatModel) {
        this.retrievalService = retrievalService;
        this.chatModel = chatModel;
    }

    public QueryResponse answer(String query) {
        List<RetrievalService.ScoredSegment> ctx = retrievalService.retrieve(query);

        String userPrompt = promptBuilder.buildUserPrompt(query, ctx);

        List<ChatMessage> messages = Arrays.asList(
                new SystemMessage(PromptBuilder.SYSTEM),
                new UserMessage(userPrompt)
        );


        Response<AiMessage> response = chatModel.generate(messages);


        String answer = response.content().text();



        // Basic source snippets (can upgrade later with page metadata)
        List<String> sources = ctx.stream()
                .map(s -> "score=" + s.getScore() + " :: " + trim(s.getText(), 220))
                .collect(Collectors.toList());

        log.info("RAG query='{}' retrievedChunks={}", trim(query, 100), ctx.size());

        return new QueryResponse(
                answer,
                sources,
                retrievalService.getMinScore(),
                retrievalService.getTopK()
        );
    }

    private static String trim(String s, int max) {
        if (s == null) return "";
        return s.length() <= max ? s : s.substring(0, max) + "...";
    }
}
