package services;

import com.fasterxml.jackson.core.type.TypeReference;
import core.http.Response;
import core.http.RestClient;
import io.qameta.allure.Step;
import model.BookModel;
import model.ErrorModel;

import java.io.IOException;
import java.util.List;

public class BooksService {

    private final RestClient rest;

    public BooksService(RestClient rest) {
        this.rest = rest;
    }

    @Step("Create Book")
    public Response<BookModel> create(BookModel author) {
        return rest.post("/api/v1/Books", author, BookModel.class);
    }

    @Step("Update Book")
    public Response<BookModel> update(BookModel author) {
        return rest.put("/api/v1/Books", 511, author, BookModel.class);
    }

    @Step("Delete Book")
    public Response<BookModel> delete(int id) {
        return rest.delete("/api/v1/Books", id, BookModel.class);
    }


    @Step("Get all Books")
    public Response<List<BookModel>> getAll() throws IOException, InterruptedException {
        return rest.get("/api/v1/Books", new TypeReference<List<BookModel>>() {
        });
    }

    @Step("Get All Books by ID")
    public Response<BookModel> getAllByID(int id) throws IOException, InterruptedException {
        return rest.getByID("/api/v1/Books", id, new TypeReference<BookModel>() {
        });
    }

    @Step("Get Book with error")
    public Response<ErrorModel> getErrorByID(int id) throws IOException, InterruptedException {
        return rest.getErrorByID("/api/v1/Books", id, ErrorModel.class);
    }
}
