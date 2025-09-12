package services;

import com.fasterxml.jackson.core.type.TypeReference;
import core.http.Response;
import core.http.RestClient;
import io.qameta.allure.Step;
import model.AuthorsModel;
import model.ErrorModel;

import java.io.IOException;
import java.util.List;

public class AuthorsService {
    private final RestClient rest;

    public AuthorsService(RestClient rest) {
        this.rest = rest;
    }

    @Step("Create Author")
    public Response<AuthorsModel> create(AuthorsModel author) {
        return rest.post("/api/v1/Authors", author, AuthorsModel.class);
    }

    @Step("Update Author")
    public Response<AuthorsModel> update(AuthorsModel author) {
        return rest.put("/api/v1/Authors", 511, author, AuthorsModel.class);
    }

    @Step("Delete Author")
    public Response<AuthorsModel> delete(int id) {
        return rest.delete("/Authors", id, AuthorsModel.class);
    }


    @Step("Get all authors")
    public Response<List<AuthorsModel>> getAll() throws IOException, InterruptedException {
        return rest.get("/api/v1/Authors", new TypeReference<List<AuthorsModel>>() {
        });
    }

    @Step("Get All authors by ID")
    public Response<AuthorsModel> getAllByID(int id) throws IOException, InterruptedException {
        return rest.getByID("/api/v1/Authors", id, new TypeReference<AuthorsModel>() {
        });
    }

    @Step("Get Author with error")
    public Response<ErrorModel> getErrorByID(int id) throws IOException, InterruptedException {
        return rest.getErrorByID("/api/v1/Authors", id, ErrorModel.class);
    }
}

