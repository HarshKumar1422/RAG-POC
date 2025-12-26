package com.example.rag.controller;

import com.example.rag.dto.IngestRequest;
import com.example.rag.ingestion.PdfIngestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ingest")
public class IngestionController {

    private final PdfIngestionService ingestionService;

    public IngestionController(PdfIngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    @PostMapping("/pdf")
    public ResponseEntity<?> ingestPdf(@RequestBody IngestRequest request) {
        int chunks = ingestionService.ingest(Path.of(request.getPdfPath()));

        Map<String, Object> resp = new HashMap<>();
        resp.put("status", "ok");
        resp.put("chunks_ingested", chunks);
        resp.put("pdfPath", request.getPdfPath());

        return ResponseEntity.ok(resp);
    }
}
