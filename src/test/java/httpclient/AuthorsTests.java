package httpclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import configs.ConfigLoader;
import core.http.Response;
import core.http.RestClient;
import model.AuthorsModel;
import model.ErrorModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.AuthorsService;

import java.io.IOException;
import java.util.List;

import io.qameta.allure.*;

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

    @Story("Get specific author")
    @Test
    void getAuthorsById() throws IOException, InterruptedException {
        int idToGet = 150;
        Response<AuthorsModel> response = authors.getAllByID(idToGet);
        assertEquals(200, response.getStatusCode());

        AuthorsModel authors = response.getBody();

        assertEquals(150, authors.id);
        assertEquals("First Name 150", authors.firstName);
        assertEquals("Last Name 150", authors.lastName);
    }

    @Story("Try to get Author by OutOfRangeID")
    @Test
    void getAuthorsByOutOfRangeId() throws IOException, InterruptedException {
        int nonExistingId = 9999;
        Response<ErrorModel> responseError = authors.getErrorByID(nonExistingId);
        assertEquals(404, responseError.getStatusCode());

        ErrorModel error = responseError.getBody();

        assertEquals("Not Found", error.title);
    }

    @Story("Try to get Author by long ID")
    @Test
    void getAuthorsBylongId() throws IOException, InterruptedException {
        int longId  = 2147483647;
        Response<ErrorModel> responseError = authors.getErrorByID(longId);
        assertEquals(404, responseError.getStatusCode());

        ErrorModel error = responseError.getBody();

        assertEquals("Not Found", error.title);
    }

    @Story("Try to get Author by negative ID")
    @Test
    void getAuthorsByNegativeId() throws IOException, InterruptedException {
        int negaviteId  = (-5);
        Response<ErrorModel> responseError = authors.getErrorByID(negaviteId);
        assertEquals(404, responseError.getStatusCode());

        ErrorModel error = responseError.getBody();

        assertEquals("Not Found", error.title);
    }

    @Story("Try to get Author by other negative ID")
    @Test
    void getAuthorsByOtherNegativeId() throws IOException, InterruptedException {
        int otherNegaviteId  = -5;
        Response<ErrorModel> responseError = authors.getErrorByID(otherNegaviteId);
        assertEquals(404, responseError.getStatusCode());

        ErrorModel error = responseError.getBody();

        assertEquals("Not Found", error.title);
    }

    @Story("Try to get Author with 0 for ID")
    @Test
    void getAuthorsBy0Id() throws IOException, InterruptedException {
        int zeroId  = 0;
        Response<ErrorModel> responseError = authors.getErrorByID(zeroId);
        assertEquals(404, responseError.getStatusCode());

        ErrorModel error = responseError.getBody();

        assertEquals("Not Found", error.title);
    }

    @Story("Create author")
    @Test
    void postAuthors() throws IOException, InterruptedException {
        AuthorsModel createdAuthors = buildAuthors(322, 1010, "Joana", "Faber");
        Response<AuthorsModel> response = authors.create(createdAuthors);

        assertEquals(200, response.getStatusCode());

        AuthorsModel expected = createdAuthors;
        AuthorsModel actual = response.getBody();

        assertEquals(expected.id, actual.id);
        assertEquals(expected.idBook, actual.idBook);
        assertEquals(expected.firstName, actual.firstName);
        assertEquals(expected.lastName, actual.lastName);
    }

    @Story("Try to create author with invalid data")
    @Test
    void postAuthorsInvalidData() throws IOException, InterruptedException {
        AuthorsModel createdAuthors = buildAuthors(2147483647, 2147483647, "Too", "LargeID");
        Response<AuthorsModel> response = authors.create(createdAuthors);

        assertEquals(200, response.getStatusCode());

        AuthorsModel expected = createdAuthors;
        AuthorsModel actual = response.getBody();

        assertEquals(expected.id, actual.id);
        assertEquals(expected.idBook, actual.idBook);
        assertEquals(expected.firstName, actual.firstName);
        assertEquals(expected.lastName, actual.lastName);
    }

    @Story("Try to create author with long name")
    @Test
    void postAuthorsLongName() throws IOException, InterruptedException {
        AuthorsModel createdAuthors = buildAuthors(603,
                60525,
                "LongNameLongNameLongNameLongNameLongNameLongNameLongNameLongNameLongNameLongNameLongNameLongNameLongNameLongNameLongNameLongName",
                "LongLastNameLongLastNameLongLastNameLongLastNameLongLastNameLongLastNameLongLastNameLongLastNameLongLastNameLongLastNameLongLastNameLongLastNameLongLastName");
        Response<AuthorsModel> response = authors.create(createdAuthors);

        assertEquals(200, response.getStatusCode());

        AuthorsModel expected = createdAuthors;
        AuthorsModel actual = response.getBody();

        assertEquals(expected.id, actual.id);
        assertEquals(expected.idBook, actual.idBook);
        assertEquals(expected.firstName, actual.firstName);
        assertEquals(expected.lastName, actual.lastName);
    }

    @Story("Try to create author with empty names")
    @Test
    void postAuthorsEmptyNames() throws IOException, InterruptedException {
        AuthorsModel createdAuthors = buildAuthors(608, 60526, "       ", "          ");
        Response<AuthorsModel> response = authors.create(createdAuthors);

        assertEquals(200, response.getStatusCode());

        AuthorsModel expected = createdAuthors;
        AuthorsModel actual = response.getBody();

        assertEquals(expected.id, actual.id);
        assertEquals(expected.idBook, actual.idBook);
        assertEquals(expected.firstName, actual.firstName);
        assertEquals(expected.lastName, actual.lastName);
    }

    @Story("Try to create author with null for last name")
    @Test
    void postAuthorsNullLastName() throws IOException, InterruptedException {
        AuthorsModel createdAuthors = buildAuthors(609, 60527, "Harry", null);
        Response<AuthorsModel> response = authors.create(createdAuthors);

        assertEquals(200, response.getStatusCode());

        AuthorsModel expected = createdAuthors;
        AuthorsModel actual = response.getBody();

        assertEquals(expected.id, actual.id);
        assertEquals(expected.idBook, actual.idBook);
        assertEquals(expected.firstName, actual.firstName);
        assertEquals(expected.lastName, actual.lastName);
    }

    @Story("Try to create author with null for first name")
    @Test
    void postAuthorsNullFirstName() throws IOException, InterruptedException {
        AuthorsModel createdAuthors = buildAuthors(615, 60529, null, "Johnson");
        Response<AuthorsModel> response = authors.create(createdAuthors);

        assertEquals(200, response.getStatusCode());

        AuthorsModel expected = createdAuthors;
        AuthorsModel actual = response.getBody();

        assertEquals(expected.id, actual.id);
        assertEquals(expected.idBook, actual.idBook);
        assertEquals(expected.firstName, actual.firstName);
        assertEquals(expected.lastName, actual.lastName);
    }

    @Story("Try to create author with null for both names")
    @Test
    void postAuthorsNullBothNames() throws IOException, InterruptedException {
        AuthorsModel createdAuthors = buildAuthors(616, 60530, null, null);
        Response<AuthorsModel> response = authors.create(createdAuthors);

        assertEquals(200, response.getStatusCode());

        AuthorsModel expected = createdAuthors;
        AuthorsModel actual = response.getBody();

        assertEquals(expected.id, actual.id);
        assertEquals(expected.idBook, actual.idBook);
        assertEquals(expected.firstName, actual.firstName);
        assertEquals(expected.lastName, actual.lastName);
    }

    @Story("Update author")
    @Test
    void updateAuthors() throws IOException, InterruptedException {
        AuthorsModel updatedAuthors = buildAuthors(323, 1011, "First Name 511323", "Last Name 511323");
        Response<AuthorsModel> response = authors.update(updatedAuthors);

        assertEquals(200, response.getStatusCode());

        AuthorsModel expected = updatedAuthors;
        AuthorsModel actual = response.getBody();

        assertEquals(expected.id, actual.id);
        assertEquals(expected.idBook, actual.idBook);
        assertEquals(expected.firstName, actual.firstName);
        assertEquals(expected.lastName, actual.lastName);
    }

    @Story("Get and then update author")
    @Test
    void getUpdateAuthors() throws IOException, InterruptedException {
        int idToGet = 200;
        Response<AuthorsModel> response = authors.getAllByID(idToGet);
        assertEquals(200, response.getStatusCode());

        AuthorsModel authorToCompare = response.getBody();

        AuthorsModel updatedAuthors = buildAuthors(200, 66, "First Name 500200", "Last Name 500200");
        Response<AuthorsModel> responseUpdate = authors.update(updatedAuthors);

        assertEquals(200, responseUpdate.getStatusCode());

        AuthorsModel actual = responseUpdate.getBody();

        assertEquals(authorToCompare.id, actual.id);
        assertEquals(authorToCompare.idBook, actual.idBook);
        assertEquals(authorToCompare.firstName, actual.firstName);
        assertEquals(authorToCompare.lastName, actual.lastName);
    }

    @Story("Delete author")
    @Test
    void deleteAuthor() throws IOException, InterruptedException {
        int idToDelete = 201;
        Response<AuthorsModel> response = authors.delete(idToDelete);
        assertEquals(200, response.getStatusCode());
        Response<AuthorsModel> responseGet = authors.getAllByID(idToDelete);
        ;
        assertEquals(404, responseGet.getStatusCode());
    }

    @Story("Try to delete author by providing invalid ID")
    @Test
    void invalidDelete() throws IOException, InterruptedException {
        int idToDelete = -80;
        Response<AuthorsModel> response = authors.delete(idToDelete);
        assertEquals(404, response.getStatusCode());
    }

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
