package com.example.rag.dto;

import java.util.List;

public class QueryResponse {

    private String answer;
    private List<String> sources;
    private double similarityMinScore;
    private int topK;

    public QueryResponse() {}

    public QueryResponse(String answer, List<String> sources, double similarityMinScore, int topK) {
        this.answer = answer;
        this.sources = sources;
        this.similarityMinScore = similarityMinScore;
        this.topK = topK;
    }

    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }

    public List<String> getSources() { return sources; }
    public void setSources(List<String> sources) { this.sources = sources; }

    public double getSimilarityMinScore() { return similarityMinScore; }
    public void setSimilarityMinScore(double similarityMinScore) { this.similarityMinScore = similarityMinScore; }

    public int getTopK() { return topK; }
    public void setTopK(int topK) { this.topK = topK; }
}
