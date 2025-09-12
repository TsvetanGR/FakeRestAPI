package model;

public class BookModel {

    public int id;
    public String title;
    public String description;
    public int pageCount;
    public String excerpt;
    public String publishDate;

    public BookModel() {}


    public BookModel(int id, String title, String description, int pageCount, String excerpt, String publishDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.pageCount = pageCount;
        this.excerpt = excerpt;
        this.publishDate = publishDate;
    }
}
