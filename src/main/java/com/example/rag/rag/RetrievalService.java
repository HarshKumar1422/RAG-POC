package com.example.rag.rag;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RetrievalService {

    public static class ScoredSegment {
        private final String text;
        private final double score;

        public ScoredSegment(String text, double score) {
            this.text = text;
            this.score = score;
        }

        public String getText() { return text; }
        public double getScore() { return score; }
    }

    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> embeddingStore;
    private final int topK;
    private final double minScore;

    public RetrievalService(EmbeddingModel embeddingModel,
                            EmbeddingStore<TextSegment> embeddingStore,
                            @Value("${rag.top-k}") int topK,
                            @Value("${rag.min-score}") double minScore) {
        this.embeddingModel = embeddingModel;
        this.embeddingStore = embeddingStore;
        this.topK = topK;
        this.minScore = minScore;
    }

    public List<ScoredSegment> retrieve(String query) {
        Embedding queryEmbedding = embeddingModel.embed(query).content();

        EmbeddingSearchRequest request = EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .maxResults(topK)
                .minScore(minScore)
                .build();

        EmbeddingSearchResult<TextSegment> result = embeddingStore.search(request);

        return result.matches().stream()
                .map(m -> new ScoredSegment(m.embedded().text(), m.score()))
                .collect(Collectors.toList());
    }

    public int getTopK() { return topK; }
    public double getMinScore() { return minScore; }
}
