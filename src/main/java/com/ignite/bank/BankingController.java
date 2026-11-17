package com.ignite.bank;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BankingController {
    private final BankingTools bankingTools;
    private final ChatClient chatClient;

    public BankingController(BankingTools bankingTools, ChatClient.Builder builder) {
        this.bankingTools = bankingTools;
        this.chatClient = builder.build();
    }

    @GetMapping("/banking")
    public String deposit(String message){
        return chatClient.prompt()
                .tools(bankingTools)
                .user(message)
                .call()
                .content();
    }
}
