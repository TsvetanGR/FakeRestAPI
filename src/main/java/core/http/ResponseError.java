package core.http;

public class ResponseError {
    private final String rawBody;

    public ResponseError(String rawBody) {
        this.rawBody = rawBody;
    }

    public String getRawBody() {
        return rawBody;
    }
}
