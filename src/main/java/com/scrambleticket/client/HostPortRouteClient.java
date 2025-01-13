
package com.scrambleticket.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

import com.scrambleticket.exception.ScrambleTicketClientException;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public class HostPortRouteClient implements Client {

    private final Map<String, Client> clientMap = new ConcurrentHashMap<>();
    private final BiFunction<String, Integer, Client> clientCreator;

    public HostPortRouteClient(BiFunction<String, Integer, Client> clientCreator) {
        this.clientCreator = clientCreator;
    }

    @Override
    public FullHttpResponse sync(Integer connectionId, FullHttpRequest request, long timeoutMillis)
        throws ScrambleTicketClientException {
        String uri = request.uri();
        Client delegate = getClientByURI(uri);
        return delegate.sync(connectionId, request, timeoutMillis);
    }

    @Override
    public void async(Integer connectionId, FullHttpRequest request, Callback callback) {
        String uri = request.uri();
        Client delegate = getClientByURI(uri);
        delegate.async(connectionId, request, callback);
    }

    @Override
    public void close() {
        for (Map.Entry<String, Client> entry : clientMap.entrySet()) {
            entry.getValue().close();
        }
        clientMap.clear();
    }

    private Client getClientByURI(String uri) {
        int schemeIdx = uri.indexOf("://");
        int hostAndPortIdx = uri.indexOf("/", schemeIdx + 3);
        String clientKey = uri.substring(0, hostAndPortIdx);
        clientMap.computeIfAbsent(clientKey, k -> {
            String scheme = uri.substring(0, schemeIdx);
            String hostAndPort = uri.substring(schemeIdx + 3, hostAndPortIdx);
            int portIdx = hostAndPort.indexOf(":");
            String host;
            int port;
            if (portIdx == -1) {
                host = hostAndPort;
                if ("https".equals(scheme)) {
                    port = 443;
                } else if ("http".equals(scheme)) {
                    port = 80;
                } else {
                    throw new UnsupportedOperationException(
                        "port not defined because scheme is not support: " + scheme);
                }
            } else {
                host = hostAndPort.substring(0, portIdx);
                String portString = hostAndPort.substring(portIdx + 1);
                port = Integer.parseInt(portString);
            }
            return clientCreator.apply(host, port);
        });
        return clientMap.get(clientKey);
    }
}
