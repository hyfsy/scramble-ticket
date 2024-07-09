
package com.scrambleticket.flow;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import com.scrambleticket.client.Client;
import com.scrambleticket.client.cookie.CookieStorage;
import com.scrambleticket.service.InteractionService;

import lombok.Data;

@Data
public class FlowContext {

    private Client client;
    private Integer connectionId;
    private InteractionService interactionService;

    private CookieStorage cookieStorage;
    private Map<String, Object> attributes = new ConcurrentHashMap<>();

    private CompletableFuture<FlowContext> future = new CompletableFuture<>();

    public CookieStorage getCookieStorage() {
        return cookieStorage;
    }

    public void putAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    public <T> T getAttribute(String key, Class<T> clazz) {
        return clazz.cast(attributes.get(key));
    }

    public <T> T removeAttribute(String key, Class<T> clazz) {
        return clazz.cast(attributes.remove(key));
    }

    public void done() {
        future.complete(this);
    }

    public void error(Throwable t) {
        if (future != null && !future.isDone()) {
            future.completeExceptionally(t);
        }
    }

    public FlowContext copyFrom() {
        return copyFrom(false);
    }

    public FlowContext copyFrom(boolean deep) {
        FlowContext context = new FlowContext();
        context.setClient(client);
        context.setConnectionId(connectionId);
        context.setInteractionService(interactionService);
        if (deep) {
            CookieStorage newCookieStorage = new CookieStorage();
            newCookieStorage.setCookies(cookieStorage.getCookies());
            context.setCookieStorage(newCookieStorage);
            context.setAttributes(new ConcurrentHashMap<>(attributes));
        } else {
            context.setCookieStorage(cookieStorage);
            context.setAttributes(attributes);
        }
        context.setFuture(new CompletableFuture<>());
        return context;
    }
}
