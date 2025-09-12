package model;

public class ErrorModel {

    public String type;
    public String title;
    public int status;
    public String traceId;

    public ErrorModel() {};

    public ErrorModel (String type, String title, int status, String traceId) {
        this.type = type;
        this.title = title;
        this.status = status;
        this.traceId = traceId;
    }
}
