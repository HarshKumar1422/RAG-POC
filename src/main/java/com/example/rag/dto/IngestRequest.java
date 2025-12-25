package com.example.rag.dto;

public class IngestRequest {

    private String pdfPath;

    public IngestRequest() {
    }

    public IngestRequest(String pdfPath) {
        this.pdfPath = pdfPath;
    }

    public String getPdfPath() {
        return pdfPath;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }
}
