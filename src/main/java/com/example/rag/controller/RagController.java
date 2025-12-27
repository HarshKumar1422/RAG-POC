package com.example.rag.controller;

import com.example.rag.dto.QueryRequest;
import com.example.rag.dto.QueryResponse;
import com.example.rag.rag.RagService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rag")
public class RagController {

    private final RagService ragService;

    public RagController(RagService ragService) {
        this.ragService = ragService;
    }

    @PostMapping("/query")
    public QueryResponse query(@RequestBody QueryRequest request) {
        return ragService.answer(request.getQuery());
    }
}
