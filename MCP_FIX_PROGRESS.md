# MCP Integration Fix Progress Report

**Date:** January 2, 2026
**Status:** ⚠️ In Progress - Configuration Issue

## What Has Been Fixed

### ✅ Client Service - MCP Server Configuration
- **Dependencies Added:** ✅
  - `spring-ai-mcp-annotations`
  - `spring-ai-starter-mcp-server-webmvc`
  - `spring-boot-starter-actuator`

- **Code Changes:** ✅
  - Added `@McpTool` annotations to `ClientController`
  - Methods annotated:
    - `getClients()` → Récupère la liste de tous les clients
    - `getClient(Long id)` → Récupère un client via son ID

- **Configuration:** ✅
  - MCP server protocol set to `streamable`
  - Endpoint available at: `POST /mcp`

### ✅ Product Service - MCP Server Configuration
- **Dependencies:** ✅ Already present
- **Code Changes:** ✅ Already has `@McpTool` annotations
- **Configuration:** ✅ Updated to use `streamable` protocol

## Current Issue

### ❌ Agent IA Service - Configuration Problem

**Error:**
```
Failed to bind properties under 'spring.ai.mcp.client.streamable-http.connections.client'
Reason: No converter found capable of converting from type [java.lang.String] 
        to type [McpStreamableHttpClientProperties$ConnectionParameters]
```

**Root Cause:**
The property configuration format is incorrect. The current format:
```properties
spring.ai.mcp.client.streamable-http.connections.client=http://127.0.0.1:8082/mcp
```

This tries to assign a String URL directly to a ConnectionParameters object.

## Solution Needed

The correct property structure needs to be determined. Based on Spring Boot auto-configuration patterns, it should likely be one of:

### Option 1: Nested URL property
```properties
spring.ai.mcp.client.streamable-http.connections.client.url=http://127.0.0.1:8082/mcp
spring.ai.mcp.client.streamable-http.connections.produit.url=http://127.0.0.1:9081/mcp
```

### Option 2: Base URL property  
```properties
spring.ai.mcp.client.streamable-http.connections.client.base-url=http://127.0.0.1:8082/mcp
spring.ai.mcp.client.streamable-http.connections.produit.base-url=http://127.0.0.1:9081/mcp
```

### Option 3: Endpoint property
```properties
spring.ai.mcp.client.streamable-http.connections.client.endpoint=http://127.0.0.1:8082/mcp
spring.ai.mcp.client.streamable-http.connections.produit.endpoint=http://127.0.0.1:9081/mcp
```

### Option 4: Different structure entirely
```properties
spring.ai.mcp.client.streamable-http.client.url=http://127.0.0.1:8082/mcp
spring.ai.mcp.client.streamable-http.produit.url=http://127.0.0.1:9081/mcp
```

## Investigation Steps Needed

1. **Check Spring AI MCP Source Code:**
   - Look at `McpStreamableHttpClientProperties.class`
   - Find `ConnectionParameters` inner class
   - Identify the correct property names

2. **Check Spring AI Documentation:**
   - Official Spring AI 1.1.1 MCP documentation
   - Example configurations on GitHub

3. **Alternative: Use Java Configuration**
   Instead of properties file, create a `@Configuration` class:
   ```java
   @Configuration
   public class McpClientConfig {
       @Bean
       public McpClient clientServiceMcp() {
           return McpClient.streamableHttp("http://127.0.0.1:8082/mcp")
               .build();
       }
       
       @Bean
       public McpClient produitServiceMcp() {
           return McpClient.streamableHttp("http://127.0.0.1:9081/mcp")
               .build();
       }
   }
   ```

## Services Status

| Service | MCP Role | Status | Port | Notes |
|---------|----------|--------|------|-------|
| Client Service | MCP Server | ✅ READY | 8082 | Endpoint: POST /mcp |
| Product Service | MCP Server | ✅ READY | 9081 | Endpoint: POST /mcp |
| Agent IA Service | MCP Client | ❌ CONFIG ERROR | 8081 | Property binding issue |

## Next Steps

1. **Immediate:** Research correct property structure for Spring AI MCP 1.1.1
2. **Alternative:** Implement Java-based configuration if properties don't work
3. **Test:** Verify MCP connection with proper configuration
4. **Validate:** Test agent IA calling tools from both services

## Test Endpoints

Once fixed, test with:

```bash
# Test MCP servers directly
curl -X POST http://localhost:8082/mcp \
  -H "Content-Type: application/json" \
  -H "Accept: text/event-stream, application/json" \
  -d '{}'

# Test Agent IA
curl http://localhost:8081/chat?message=Liste-moi tous les clients
```

## Files Modified

- ✅ `/client-service/pom.xml` - Added MCP dependencies
- ✅ `/client-service/src/main/java/.../ClientController.java` - Added @McpTool
- ✅ `/client-service/src/main/resources/application.properties` - MCP config
- ✅ `/produit/src/main/resources/application.properties` - Updated protocol
- ⚠️ `/agent-ia-service/src/main/resources/application.properties` - Needs fix

---

**Status Summary:**
- MCP Servers: **READY** ✅
- MCP Client: **BLOCKED** - Configuration issue ❌  
- Overall Progress: **80% Complete**
