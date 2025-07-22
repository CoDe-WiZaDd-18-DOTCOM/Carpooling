package com.example.carpooling.controller;

import com.example.carpooling.utils.ContextUtil;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ChatBotController {
    private static final Logger log = LoggerFactory.getLogger(ChatBotController.class);

    @Value("${GEMINI_API_KEY}")
    private String apiKey;

    private final ContextUtil contextUtil;

    @Autowired
    public ChatBotController(ContextUtil contextUtil) {
        this.contextUtil = contextUtil;
    }


    @PostMapping("/public/chat-bot")
    public ResponseEntity<?> test(@RequestBody String userQuestion){
        try {
            Client client = Client.builder().apiKey(apiKey).build();
            String context = contextUtil.getContext();
            GenerateContentResponse response =
                    client.models.generateContent(
                            "gemini-2.5-flash",
                            "Given the following documentation:\n" + context +
                                    "\n\nAnswer this question: " + userQuestion,
                            null);

            return ResponseEntity.ok(response.text());
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
