package httpclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import configs.ConfigLoader;
import core.http.Response;
import core.http.RestClient;
import model.AuthorsModel;
import model.ErrorModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import services.AuthorsService;

import java.io.IOException;
import java.util.List;

import io.qameta.allure.*;
import testData.AuthorTestData;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic("FakeREST API")
@Feature("Authors API")

public class AuthorsTests {

    private AuthorsService authors;

    @BeforeEach
    void setup() {
        RestClient client = new RestClient(ConfigLoader.getAuthorsUrl());
        authors = new AuthorsService(client);
    }

    private AuthorsModel buildAuthors(int id, int idBook, String firstName, String lastName) {
        return new AuthorsModel(id, idBook, firstName, lastName);
    }

    public AuthorsTests() throws JsonProcessingException {
    }

    @DisplayName("Get all authors")
    @Story("Get all authors")
    @Test
    void getAuthors() throws IOException, InterruptedException {
        Response<List<AuthorsModel>> response = authors.getAll();
        assertEquals(200, response.getStatusCode());

        List<AuthorsModel> authors = response.getBody();
        Assertions.assertFalse(authors.isEmpty(), "Authors list should not be empty");

        int totalAuthors = authors.size();

        int lastId = -1;
        if (totalAuthors > 0) {
            AuthorsModel lastAuthor = authors.get(totalAuthors - 1);
            lastId = lastAuthor.id;
        }

        assertEquals(lastId, totalAuthors, "Total number of authors should be " + totalAuthors);
    }

    @DisplayName("Get specific author by ID")
    @Story("Get specific author")
    @ParameterizedTest
    @Step("Testing get author by ID: {testData.id}")
    @MethodSource("testData.AuthorTestData#positiveGetData")
    void getAuthorById(AuthorTestData.GetAuthorPositiveNegativeCase testData) throws IOException, InterruptedException {
        Response<AuthorsModel> response = authors.getByID(testData.id);
        assertEquals(testData.expectedStatus, response.getStatusCode());

        AuthorsModel authors = response.getBody();

        assertEquals(testData.id, authors.id);
        assertEquals(testData.firstName, authors.firstName);
        assertEquals(testData.lastName, authors.lastName);
    }

    @DisplayName("Try to get Author by providing negative data")
    @Story("Try to get Author by providing negative data")
    @ParameterizedTest
    @Step("Testing negative case with author ID: {testData.id}")
    @MethodSource("testData.AuthorTestData#negativeGetData")
    void getAuthorsByNegativeData(AuthorTestData.GetAuthorPositiveNegativeCase testData) throws IOException, InterruptedException {
        Response<ErrorModel> responseError = authors.getErrorByID(testData.id);
        assertEquals(testData.expectedStatus, responseError.getStatusCode());

        ErrorModel error = responseError.getBody();

        assertEquals(testData.expectedTitle, error.title);
    }

    @DisplayName("Try to get Author by providing negative ID")
    @Story("Try to get Author by negative ID")
    @Test
    void getAuthorsByNegativeId() throws IOException, InterruptedException {
        int negaviteId  = (-5);
        Response<ErrorModel> responseError = authors.getErrorByID(negaviteId);
        assertEquals(404, responseError.getStatusCode());

        ErrorModel error = responseError.getBody();

        assertEquals("Not Found", error.title);
    }

    @DisplayName("Create Author with valid data")
    @Story("Create author")
    @ParameterizedTest
    @Step("Testing author creation with author ID: {author.id}")
    @MethodSource("testData.AuthorTestData#positivePostData")
    void postAuthors(AuthorsModel author) throws IOException, InterruptedException {
        Response<AuthorsModel> response = authors.create(author);

        assertEquals(200, response.getStatusCode());

        AuthorsModel expected = author;
        AuthorsModel actual = response.getBody();

        assertEquals(expected.id, actual.id);
        assertEquals(expected.idBook, actual.idBook);
        assertEquals(expected.firstName, actual.firstName);
        assertEquals(expected.lastName, actual.lastName);
    }

    @DisplayName("Try to Create Author with invalid data")
    @Story("Try to create author with invalid data")
    @ParameterizedTest
    @Step("Testing author creation with invalid ID: {author.id}")
    @MethodSource("testData.AuthorTestData#negativePostData")
    void postAuthorsInvalidData(AuthorsModel author) throws IOException, InterruptedException {
        Response<AuthorsModel> response = authors.create(author);

        assertEquals(200, response.getStatusCode());

        AuthorsModel expected = author;
        AuthorsModel actual = response.getBody();

        assertEquals(expected.id, actual.id);
        assertEquals(expected.idBook, actual.idBook);
        assertEquals(expected.firstName, actual.firstName);
        assertEquals(expected.lastName, actual.lastName);
    }

    @DisplayName("Update author and check response")
    @Story("Update author")
    @ParameterizedTest
    @Step("Testing author update and check response with author ID: {author.id}")
    @MethodSource("testData.AuthorTestData#positivePutData")
    void updateAuthors(AuthorsModel author) throws IOException, InterruptedException {
        Response<AuthorsModel> response = authors.update(author, author.id);

        assertEquals(200, response.getStatusCode());

        AuthorsModel expected = author;
        AuthorsModel actual = response.getBody();

        assertEquals(expected.id, actual.id);
        assertEquals(expected.idBook, actual.idBook);
        assertEquals(expected.firstName, actual.firstName);
        assertEquals(expected.lastName, actual.lastName);
    }

    @DisplayName("Get author, update and check update is made")
    @Story("Get and then update author")
    @ParameterizedTest
    @Step("Testing get author, update and check update is made for author ID: {author.id}")
    @MethodSource("testData.AuthorTestData#positivePutData")
    void getUpdateAuthors(AuthorsModel author) throws IOException, InterruptedException {
//        int idToGet = 200;
        Response<AuthorsModel> response = authors.getByID(author.id);
        assertEquals(200, response.getStatusCode());

        AuthorsModel authorToCompare = response.getBody();

        Response<AuthorsModel> responseUpdated = authors.update(author, author.id);

        assertEquals(200, responseUpdated.getStatusCode());

        AuthorsModel authorUpdated = responseUpdated.getBody();

        assertEquals(authorUpdated.id, authorToCompare.id);
        assertEquals(authorUpdated.idBook, authorToCompare.idBook);
        assertEquals(authorUpdated.firstName, authorToCompare.firstName);
        assertEquals(authorUpdated.lastName, authorToCompare.lastName);
    }

    @DisplayName("Delete author with valid data")
    @Story("Delete author")
    @ParameterizedTest
    @Step("Testing delete author with ID: {testData.id}")
    @MethodSource("testData.AuthorTestData#positiveDeleteData")
    void deleteAuthor(AuthorTestData.GetAuthorPositiveNegativeCase testData) throws IOException, InterruptedException {
        Response<AuthorsModel> response = authors.delete(testData.id);
        assertEquals(200, response.getStatusCode());
        Response<AuthorsModel> responseGet = authors.getByID(testData.id);
        assertEquals(testData.expectedStatus, responseGet.getStatusCode());
    }

    @DisplayName("Try to delete author by providing invalid data")
    @Story("Try to delete author by providing invalid ID")
    @ParameterizedTest
    @Step("Testing delete author with invalid ID: {testData.id}")
    @MethodSource("testData.AuthorTestData#negativeDeleteData")
    void invalidDelete(AuthorTestData.GetAuthorPositiveNegativeCase testData) throws IOException, InterruptedException {
        Response<AuthorsModel> response = authors.delete(testData.id);
        assertEquals(testData.expectedStatus, response.getStatusCode());
    }

    @DisplayName("Try to create author with invalid ID")
    @Story("Try to create author with invalid ID")
    @Test
    void createInvalidPOSTAuthors() throws IOException, InterruptedException {
        AuthorsModel invalidAuthors = buildAuthors(-8, 1012, "Joana", "Faber");
        Response<AuthorsModel> response = authors.create(invalidAuthors);

        assertEquals(400, response.getStatusCode());

        AuthorsModel expected = invalidAuthors;
        AuthorsModel actual = response.getBody();

        assertEquals(expected.firstName, actual.firstName);
    }
}
