package com.example.rag.ingestion;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class PdfIngestionService {

    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> embeddingStore;

    public PdfIngestionService(
            EmbeddingModel embeddingModel,
            EmbeddingStore<TextSegment> embeddingStore
    ) {
        this.embeddingModel = embeddingModel;
        this.embeddingStore = embeddingStore;
    }

    public int ingest(Path pdfPath) {

        // Extract text from PDF
        String text = extractTextFromPdf(pdfPath.toFile());

        //Chunk text (custom splitter)
        List<String> chunks =
                TextChunker.chunk(text, 500, 100);

        // Convert chunks → TextSegments (Java 11 safe)
        List<TextSegment> segments = chunks.stream()
                .map(TextSegment::from)
                .collect(Collectors.toList());

        // Embed TextSegments (CORRECT API)
        List<Embedding> embeddings =
                embeddingModel.embedAll(segments).content();

        // Store embeddings in Qdrant
        IntStream.range(0, segments.size()).forEach(i ->
                embeddingStore.add(
                        embeddings.get(i),
                        segments.get(i)
                )
        );

        return segments.size();
    }

    private String extractTextFromPdf(File pdfFile) {
        try (PDDocument doc = PDDocument.load(pdfFile)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(doc);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to read PDF: " + pdfFile.getAbsolutePath(), e
            );
        }
    }
}
