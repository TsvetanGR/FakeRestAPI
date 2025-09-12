package core.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import configs.ConfigLoader;
import model.ErrorModel;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class RestClient {

    private final HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
    private final ObjectMapper mapper = new ObjectMapper();
    private final String baseUrl;

    public RestClient(String baseUrl) {
        this.baseUrl = ConfigLoader.getProperty("api.url");
    }

    public <T> Response<T> get(String path, TypeReference<T> typeRef) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUrl + path)).GET().build();
        java.net.http.HttpResponse<String> httpResponse = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

        T body = null;
        String responseBody = httpResponse.body();
        if (responseBody != null && !responseBody.isBlank()) {
            body = mapper.readValue(responseBody, typeRef);
        }
        return new Response<>(httpResponse.statusCode(), body);
    }

    public Response<ErrorModel> getError (String path, Class<ErrorModel> type) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUrl + path)).GET().build();
        java.net.http.HttpResponse<String> httpResponse = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

        ErrorModel body = mapper.readValue(httpResponse.body(), ErrorModel.class);
        return new Response<>(httpResponse.statusCode(), body);
    }

    public <T> Response<T> getByID(String path, Object id, TypeReference<T> typeRef)
            throws IOException, InterruptedException {
        return get(path + "/" + id, typeRef);
    }

    public Response<ErrorModel> getErrorByID (String path, Object id, Class<ErrorModel> type) throws IOException, InterruptedException {
        return getError(path + "/" + id, type);
    }

    public <T> Response<T> post(String path, Object requestBody, Class<T> type) {
        try {
            String json = mapper.writeValueAsString(requestBody);

            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUrl + path))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

            T body = null;
            if (httpResponse.body() != null && !httpResponse.body().isBlank()) {
                body = mapper.readValue(httpResponse.body(), type);
            }
            return new Response<>(httpResponse.statusCode(), body);
        } catch (Exception e) {
            throw new RuntimeException("POST request failed for " + path, e);
        }
    }

    public <T> Response<T> put(String path, Object id, Object requestBody, Class<T> type) {
        try {
            String json = mapper.writeValueAsString(requestBody);

            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUrl + path + "/" + id))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

            T body = null;
            if (httpResponse.body() != null && !httpResponse.body().isBlank()) {
                body = mapper.readValue(httpResponse.body(), type);
            }
            return new Response<>(httpResponse.statusCode(), body);
        } catch (Exception e) {
            throw new RuntimeException("PUT request failed for " + path, e);
        }
    }

    public <T> Response<T> delete(String path, Object id, Class<T> type) {
        try {
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUrl + path + "/" + id)).DELETE().build();
            HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

            return new Response<>(httpResponse.statusCode(), null);
        } catch (Exception e) {
            throw new RuntimeException("DELETE request failed for " + path, e);
        }
    }
}
