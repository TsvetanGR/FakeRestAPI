package httpclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import configs.ConfigLoader;
import core.http.Response;
import core.http.RestClient;
import model.BookModel;
import model.ErrorModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.BooksService;
import io.qameta.allure.*;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic("FakeREST API")
@Feature("Books API")

public class BookTests {

    private BooksService books;

    @BeforeEach
    void setup() {
        RestClient client = new RestClient(ConfigLoader.getBooksUrl());
        books = new BooksService(client);
    }

    private BookModel buildBook(int id, String title, String description, int pageCount, String excerpt, String publishDate) {
        return new BookModel(id, title, description, pageCount, excerpt, publishDate);
    }

    public BookTests() throws JsonProcessingException {
    }

    @Story("Get all books")
    @Test
    void getBooks() throws IOException, InterruptedException {
        Response<List<BookModel>> response = books.getAll();
        assertEquals(200, response.getStatusCode());

        List<BookModel> books = response.getBody();
        Assertions.assertFalse(books.isEmpty(), "Books list should not be empty");

        int totalBooks = books.size();
        int lastId = -1;
        if (totalBooks > 0) {
            BookModel lastBook = books.get(totalBooks - 1);
            lastId = lastBook.id;
        }
        assertEquals(200, totalBooks, "Total number of books should be " + totalBooks);
    }

    @Story("Get specific book")
    @Test
    void getSpecificBooksByID() throws IOException, InterruptedException {
        int bookToGet = 123;
        Response<BookModel> response = books.getAllByID(bookToGet);
        assertEquals(200, response.getStatusCode());

        BookModel books = response.getBody();
        assertEquals(bookToGet, books.id);
        assertEquals("Book 123", books.title);
    }

    @Story("Try to get Book by OutOfRangeID")
    @Test
    void getBooksByOutOfRangeID() throws IOException, InterruptedException {
        int nonExistingId = 9999;
        Response<ErrorModel> responseError = books.getErrorByID(nonExistingId);
        assertEquals(404, responseError.getStatusCode());

        ErrorModel error = responseError.getBody();

        assertEquals("Not Found", error.title);
    }

    @Story("Try to get Book by long ID")
    @Test
    void getBooksByLongID() throws IOException, InterruptedException {
        int nonExistingId = 2147483647;
        Response<ErrorModel> responseError = books.getErrorByID(nonExistingId);
        assertEquals(404, responseError.getStatusCode());

        ErrorModel error = responseError.getBody();

        assertEquals("Not Found", error.title);
    }

    @Story("Try to get Book by negative ID")
    @Test
    void getBooksByNegativeID() throws IOException, InterruptedException {
        int nonExistingId = (-6);
        Response<ErrorModel> responseError = books.getErrorByID(nonExistingId);
        assertEquals(404, responseError.getStatusCode());

        ErrorModel error = responseError.getBody();

        assertEquals("Not Found", error.title);
    }

    @Story("Try to get Book by other negative ID")
    @Test
    void getBooksByOtherNegativeID() throws IOException, InterruptedException {
        int nonExistingId = -50;
        Response<ErrorModel> responseError = books.getErrorByID(nonExistingId);
        assertEquals(404, responseError.getStatusCode());

        ErrorModel error = responseError.getBody();

        assertEquals("Not Found", error.title);
    }

    @Story("Try to get Book with 0 for ID")
    @Test
    void getBooksBy0ID() throws IOException, InterruptedException {
        int nonExistingId = 0;
        Response<ErrorModel> responseError = books.getErrorByID(nonExistingId);
        assertEquals(404, responseError.getStatusCode());

        ErrorModel error = responseError.getBody();

        assertEquals("Not Found", error.title);
    }

    @Story("Create book")
    @Test
    void postBook() throws IOException, InterruptedException {
        BookModel createdBook = buildBook(123, "My TEST Book", "How to create automation framework.",
                222, "Sample excerpt...", ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT));

        Response<BookModel> response = books.create(createdBook);
        assertEquals(200, response.getStatusCode());

        BookModel expected = createdBook;
        BookModel actual = response.getBody();

        assertEquals(expected.id, actual.id);
        assertEquals(expected.title, actual.title);
    }

    @Story("Try to create book with invalid data")
    @Test
    void postBookInvalidData() throws IOException, InterruptedException {
        BookModel createdBook = buildBook(2147483647, "My Invalid Book", "How to create invalid book",
                550, "Invalid", ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT));

        Response<BookModel> response = books.create(createdBook);
        assertEquals(200, response.getStatusCode());

        BookModel expected = createdBook;
        BookModel actual = response.getBody();

        assertEquals(expected.id, actual.id);
        assertEquals(expected.title, actual.title);
    }

    @Story("Try to create book with long title")
    @Test
    void postBookLongTitle() throws IOException, InterruptedException {
        BookModel createdBook = buildBook(100, "My Book Long Title My Book Long Title My Book Long Title My Book Long Title My Book Long Title My Book Long Title My Book Long Title My Book Long Title",
                "Book with long title",
                530, "Title", ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT));

        Response<BookModel> response = books.create(createdBook);
        assertEquals(200, response.getStatusCode());

        BookModel expected = createdBook;
        BookModel actual = response.getBody();

        assertEquals(expected.id, actual.id);
        assertEquals(expected.title, actual.title);
    }

    @Story("Try to create book with empty title")
    @Test
    void postBookEmptyTitle() throws IOException, InterruptedException {
        BookModel createdBook = buildBook(133, "",
                "Book with empty title",
                510, "Title", ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT));

        Response<BookModel> response = books.create(createdBook);
        assertEquals(200, response.getStatusCode());

        BookModel expected = createdBook;
        BookModel actual = response.getBody();

        assertEquals(expected.id, actual.id);
        assertEquals(expected.title, actual.title);
    }

    @Story("Try to create book with empty description")
    @Test
    void postBookEmptyDescription() throws IOException, InterruptedException {
        BookModel createdBook = buildBook(138, "MyBookTitle",
                "",
                514, "MyBookTitle", ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT));

        Response<BookModel> response = books.create(createdBook);
        assertEquals(200, response.getStatusCode());

        BookModel expected = createdBook;
        BookModel actual = response.getBody();

        assertEquals(expected.id, actual.id);
        assertEquals(expected.title, actual.title);
    }

    @Story("Try to create book with null data")
    @Test
    void postBookNullData() throws IOException, InterruptedException {
        BookModel createdBook = buildBook(190, null,
                null,
                190, "MyBookTitle", ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT));

        Response<BookModel> response = books.create(createdBook);
        assertEquals(200, response.getStatusCode());

        BookModel expected = createdBook;
        BookModel actual = response.getBody();

        assertEquals(expected.id, actual.id);
        assertEquals(expected.title, actual.title);
    }

    @Story("Update author")
    @Test
    void updateBook() throws IOException, InterruptedException {
        BookModel updatedBook = buildBook(140, "My Updated TEST Book", "How to update", 220, "Sample Update",
                ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT));

        Response<BookModel> response = books.update(updatedBook);
        assertEquals(200, response.getStatusCode());

        BookModel expected = updatedBook;
        BookModel actual = response.getBody();

        assertEquals(expected.id, actual.id);
        assertEquals(expected.title, actual.title);
        assertEquals(expected.description, actual.description);
    }

    @Story("Get author and update author")
    @Test
    void getUpdateBook() throws IOException, InterruptedException {
        int bookToGet = 50;
        Response<BookModel> response = books.getAllByID(bookToGet);
        assertEquals(200, response.getStatusCode());

        BookModel bookToCompare = response.getBody();

        BookModel updatedBook = buildBook(300, "Get Update TEST Book", "Get Update", 258, "Sample Get Update",
                ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT));

        Response<BookModel> responseUpdated = books.update(updatedBook);
        assertEquals(200, responseUpdated.getStatusCode());

        BookModel bookUpdated = responseUpdated.getBody();

        assertEquals(bookToCompare.id, bookUpdated.id);
        assertEquals(bookToCompare.title, bookUpdated.title);
        assertEquals(bookToCompare.description, bookUpdated.description);
    }


    @Story("Delete author")
    @Test
    void deleteBook() throws IOException, InterruptedException {
        int idToDelete = 100;
        Response<BookModel> response = books.delete(idToDelete);
        assertEquals(200, response.getStatusCode());
        Response<BookModel> responseGet = books.getAllByID(idToDelete);
        assertEquals(404, responseGet.getStatusCode());
    }

    @Story("Try to delete book by providing invalid ID")
    @Test
    void invalidDelete() throws IOException, InterruptedException {
        int idToDelete = -100;
        Response<BookModel> response = books.delete(idToDelete);
        assertEquals(404, response.getStatusCode());
    }

    @Story("Try to create book with invalid ID")
    @Test
    void createInvalidPOSTBook() throws IOException, InterruptedException {
        BookModel invalidBook = buildBook(-9, "InvalidTestBook", "Invalid", 221, "Invalid",
                ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT));
        Response<BookModel> response = books.create(invalidBook);

        assertEquals(400, response.getStatusCode());

        BookModel expected = invalidBook;
        BookModel actual = response.getBody();

        assertEquals(expected.title, actual.title);
    }
}
