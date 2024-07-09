
package com.scrambleticket.client;

import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

public class HttpClient implements ApplicationContextAware {

    private static RestTemplate restTemplate;

    private static RestTemplate getRestTemplate() {
        if (restTemplate == null) {
            restTemplate = restTemplateHttps();
        }
        return restTemplate;
    }

    public static <T> T get(String url, Class<T> type) {
        return getRestTemplate().getForObject(url, type);
    }

    public static <T> ResponseEntity<T> get(String url, String header, Class<T> type) {
        HttpEntity<?> entity = getEntity(header, null);
        // get 添加 header
        RequestCallback requestCallback = com.scrambleticket.v1.HttpClient.getRestTemplate().httpEntityCallback(entity, type);
        ResponseExtractor<ResponseEntity<T>> responseExtractor =
            com.scrambleticket.v1.HttpClient.getRestTemplate().responseEntityExtractor(type);
        return com.scrambleticket.v1.HttpClient.getRestTemplate().execute(url, HttpMethod.GET, requestCallback, responseExtractor);
    }

    public static <T> ResponseEntity<T> post(String url, String header, Class<T> type) {
        return post(url, header, null, type);
    }

    public static <T> ResponseEntity<T> post(String url, String header, Object body, Class<T> type) {
        Limiter.limit();
        HttpEntity<?> entity = getEntity(header, body);
        return com.scrambleticket.v1.HttpClient.getRestTemplate().postForEntity(url, entity, type);
    }

    private static HttpEntity<?> getEntity(String header, Object body) {
        MultiValueMap<String, String> headers = parseHeader(header);
        return new HttpEntity<>(body, headers);
    }

    private static MultiValueMap<String, String> parseHeader(String header) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        for (String s : header.split("\n")) {
            int i = s.indexOf(":");
            if (i == -1)
                continue;
            headers.add(s.substring(0, i), s.substring(i + 2));
        }
        return headers;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.restTemplate = applicationContext.getBean(RestTemplate.class);
    }

    private static CloseableHttpClient createClient() {

        SSLContext sslContext;
        try {
            sslContext = SSLContexts.custom()
                    .loadTrustMaterial(null, (chain, authType) -> true)
                    .build();
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            throw new RuntimeException("Get SSLContext instance failed");
        }

        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, null, null, NoopHostnameVerifier.INSTANCE);

        Registry registry = RegistryBuilder.create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", csf)
                .build();

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        connectionManager.setMaxTotal(3000); // 最大连接数3000
        connectionManager.setDefaultMaxPerRoute(400); // 路由链接数400

        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(60000)
                .setConnectTimeout(60000)
                .setConnectionRequestTimeout(10000)
                // TODO 非重定向
                .setRedirectsEnabled(false)
                .build();

        return HttpClients.custom().setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connectionManager)
                .evictExpiredConnections()
                .evictIdleConnections(30, TimeUnit.SECONDS)
                .setProxy(new HttpHost("localhost", 8888, "http"))
                .build();
    }

    private static RestTemplate restTemplateHttps() {
        RestTemplate restTemplate = null;
        try {
            CloseableHttpClient client = createClient();
            HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
            httpRequestFactory.setConnectionRequestTimeout(30000);
            httpRequestFactory.setConnectTimeout(30000);
            httpRequestFactory.setReadTimeout(30000);
            httpRequestFactory.setHttpClient(client);

            restTemplate = new RestTemplate(httpRequestFactory);
            // 解决乱码
            List<HttpMessageConverter<?>> httpMessageConverters = restTemplate.getMessageConverters();
            httpMessageConverters.stream().forEach(httpMessageConverter -> {
                if (httpMessageConverter instanceof StringHttpMessageConverter) {
                    StringHttpMessageConverter messageConverter = (StringHttpMessageConverter)httpMessageConverter;
                    messageConverter.setDefaultCharset(Charset.forName("UTF-8"));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return restTemplate;
    }
}
