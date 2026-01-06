package com.example.agentia.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@RestController
public class ChatController {

    /* ================= SECURITY ================= */

    private static final String JWT_SECRET =
            "secret12345678901234567890123456789012"; // move to env later

    private boolean isAdmin(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return false;
        }

        try {
            String token = authHeader.substring(7);
            Claims claims = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            List<String> roles = claims.get("roles", List.class);
            return roles != null && roles.contains("ADMIN");
        } catch (Exception e) {
            return false;
        }
    }

    private String getUserId(String authHeader) {
        try {
            String token = authHeader.substring(7);
            return Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (Exception e) {
            return "anonymous";
        }
    }

    /* ================= INTENT ================= */

    enum Intent {
        LIST_PRODUCTS,
        GET_PRODUCT,
        LIST_CLIENTS,
        LIST_ORDERS,
        CREATE_PRODUCT,
        UPDATE_PRODUCT,
        DELETE_PRODUCT,
        UNKNOWN
    }

    private Intent classifyIntent(String message) {
        String m = message.toLowerCase();

        if (m.contains("list") && m.contains("product")) return Intent.LIST_PRODUCTS;
        if (m.contains("product") && m.contains("detail")) return Intent.GET_PRODUCT;
        if (m.contains("client")) return Intent.LIST_CLIENTS;
        if (m.contains("order") || m.contains("commande") || m.contains("commandes") || m.contains("ordre") || m.contains("ordres")) return Intent.LIST_ORDERS;
        if (m.contains("create") || m.contains("add")) return Intent.CREATE_PRODUCT;
        if (m.contains("update") || m.contains("modify")) return Intent.UPDATE_PRODUCT;
        if (m.contains("delete") || m.contains("remove")) return Intent.DELETE_PRODUCT;

        return Intent.UNKNOWN;
    }

    private static final Map<Intent, Boolean> USER_PERMISSIONS = Map.of(
            Intent.LIST_PRODUCTS, true,
            Intent.GET_PRODUCT, true
    );

    private boolean isAllowed(boolean admin, Intent intent) {
        if (admin) return true;
        return USER_PERMISSIONS.getOrDefault(intent, false);
    }

    /* ================= PROMPTS ================= */

    private static final String ADMIN_PROMPT = """
        You display business data to administrators.

        Rules:
        - Always use tools to fetch real data
        - Show results as a simple numbered list
        - Never invent data
        - If nothing exists, say so clearly
        """;

    private static final String USER_PROMPT = """
        You display product data to users.

        Rules:
        - You may ONLY show products
        - Always use tools
        - Show results as a simple numbered list
        - Never invent data
        """;

    private static final String ACCESS_DENIED =
            "🚫 Accès refusé\n\n" +
            "Vous n'avez pas les droits nécessaires pour effectuer cette action.";

    /* ================= CHAT CLIENTS ================= */

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder builder,
                          SyncMcpToolCallbackProvider mcpToolCallbackProvider) {

        this.chatClient = builder
                .defaultAdvisors(userMemoryAdvisor())
                .defaultToolCallbacks(mcpToolCallbackProvider)
                .build();
    }

    private MessageChatMemoryAdvisor userMemoryAdvisor() {
        ChatMemory memory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .maxMessages(10)
                .build();

        return MessageChatMemoryAdvisor.builder(memory).build();
    }

    /* ================= ENDPOINT ================= */

    @GetMapping("/chat")
    public String chat(@RequestParam String message,
                       @RequestHeader(value = "Authorization", required = false) String authHeader) {

        boolean admin = isAdmin(authHeader);
        Intent intent = classifyIntent(message);

        if (!isAllowed(admin, intent)) {
            return ACCESS_DENIED;
        }

        String systemPrompt = admin ? ADMIN_PROMPT : USER_PROMPT;

        return chatClient.prompt()
                .system(systemPrompt)
                .user(message)
                .call()
                .content();
    }
}
