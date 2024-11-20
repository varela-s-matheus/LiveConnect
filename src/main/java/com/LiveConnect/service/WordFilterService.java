package com.LiveConnect.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class WordFilterService {

    private static final List<String> censuredWords = Arrays.asList("badword1", "badword2", "inappropriate");

    public static String censorWords(String content) {
        for (String word : censuredWords) {
            content = content.replaceAll("(?i)" + word, "****");
        }
        return content;
    }
}
