package ru.netology;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

public class Request {

    private final String method;

    private final URI uri;

    private final List<NameValuePair> queryParams;

    public URI getUri() {
        return uri;
    }

    public String getMethod() {
        return method;
    }

    public Request(String method, URI uri) {
        this.method = method;
        this.uri = uri;
        this.queryParams = URLEncodedUtils.parse(uri, StandardCharsets.UTF_8);
    }

    public List<NameValuePair> getQueryParams() {
        return queryParams;
    }

    public String getQueryParam(String name) {
        Optional<NameValuePair> foundedPair = queryParams.stream()
                .filter(nameValuePair -> nameValuePair.getName().equals(name))
                .findFirst();
        return foundedPair.map(NameValuePair::getValue).orElse(null);
    }
}
