package com.example.rag.ingestion;

import java.util.ArrayList;
import java.util.List;

public class TextChunker {

    public static List<String> chunk(
            String text,
            int chunkSize,
            int overlap
    ) {
        String[] words = text.split("\\s+");
        List<String> chunks = new ArrayList<>();

        int start = 0;
        while (start < words.length) {
            int end = Math.min(start + chunkSize, words.length);

            StringBuilder sb = new StringBuilder();
            for (int i = start; i < end; i++) {
                sb.append(words[i]).append(" ");
            }

            chunks.add(sb.toString().trim());
            start += (chunkSize - overlap);
        }
        return chunks;
    }
}
