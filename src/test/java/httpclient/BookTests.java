package httpclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import configs.ConfigLoader;
import core.http.Response;
import core.http.RestClient;
import model.BookModel;
import model.ErrorModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import services.BooksService;
import io.qameta.allure.*;
import testData.BookTestData;

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

    @DisplayName("Get all books")
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

    @DisplayName("Get specific book by ID")
    @Story("Get specific book")
    @ParameterizedTest
    @Step("Testing get book with ID: {testData.id}")
    @MethodSource("testData.BookTestData#positiveGetData")
    void getSpecificBookByID(BookTestData.GetBookPositiveNegativeCase testData) throws IOException, InterruptedException {
        Response<BookModel> response = books.getByID(testData.id);
        assertEquals(testData.expectedStatus, response.getStatusCode());

        BookModel books = response.getBody();
        assertEquals(testData.id, books.id);
        assertEquals(testData.expectedTitle, books.title);
    }

    @DisplayName("Try to get Book by providing negative data")
    @Story("Try to get Book with negative data")
    @ParameterizedTest
    @Step("Testing negative case with book ID: {testData.id}")
    @MethodSource("testData.BookTestData#negativeGetData")
    void getBooksByNegativeData(BookTestData.GetBookPositiveNegativeCase testData) throws IOException, InterruptedException {
        Response<ErrorModel> responseError = books.getErrorByID(testData.id);
        assertEquals(testData.expectedStatus, responseError.getStatusCode());

        ErrorModel error = responseError.getBody();

        assertEquals(testData.expectedTitle, error.title);
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

    @DisplayName("Create Book with valid data")
    @Story("Create book")
    @ParameterizedTest
    @Step("Testing book creation with book ID: {book.id}")
    @MethodSource("testData.BookTestData#positivePostData")
    void postBook(BookModel book) throws IOException, InterruptedException {
        Response<BookModel> response = books.create(book);
        assertEquals(200, response.getStatusCode());

        BookModel expected = book;
        BookModel actual = response.getBody();

        assertEquals(expected.id, actual.id);
        assertEquals(expected.title, actual.title);
    }

    @DisplayName("Try to create Book with invalid data")
    @Story("Try to create book with invalid data")
    @ParameterizedTest
    @Step("Testing book creation with invalid ID: {book.id}")
    @MethodSource("testData.BookTestData#negativePostData")
    void postBookInvalidData(BookModel book) throws IOException, InterruptedException {
        Response<BookModel> response = books.create(book);
        assertEquals(200, response.getStatusCode());

        BookModel expected = book;
        BookModel actual = response.getBody();

        assertEquals(expected.id, actual.id);
        assertEquals(expected.title, actual.title);
    }

    @DisplayName("Update book and check response")
    @Story("Update book")
    @ParameterizedTest
    @Step("Testing book update and check response with book ID: {book.id}")
    @MethodSource("testData.BookTestData#positivePutData")
    void updateBook(BookModel book) throws IOException, InterruptedException {

        Response<BookModel> response = books.update(book, book.id);
        assertEquals(200, response.getStatusCode());

        BookModel expected = book;
        BookModel actual = response.getBody();

        assertEquals(expected.id, actual.id);
        assertEquals(expected.title, actual.title);
        assertEquals(expected.description, actual.description);
    }

    @DisplayName("Get book, update and check update is made")
    @Story("Get book and update")
    @ParameterizedTest
    @Step("Testing get book, update and check update is made for book ID: {book.id}")
    @MethodSource("testData.BookTestData#positivePutData")
    void getUpdateBook(BookModel book) throws IOException, InterruptedException {
        Response<BookModel> response = books.getByID(book.id);
        assertEquals(200, response.getStatusCode());

        BookModel bookToCompare = response.getBody();

        Response<BookModel> responseUpdated = books.update(book, book.id);
        assertEquals(200, responseUpdated.getStatusCode());

        BookModel bookUpdated = responseUpdated.getBody();

        assertEquals(bookUpdated.id, bookToCompare.id);
        assertEquals(bookUpdated.title, bookToCompare.title);
        assertEquals(bookUpdated.description, bookToCompare.description);
    }


    @DisplayName("Delete book with valid data")
    @Story("Delete book")
    @ParameterizedTest
    @Step("Testing delete book with ID: {testData.id}")
    @MethodSource("testData.BookTestData#positiveDeleteData")
    void deleteBook(BookTestData.GetBookPositiveNegativeCase testData) throws IOException, InterruptedException {
        Response<BookModel> response = books.delete(testData.id);
        assertEquals(200, response.getStatusCode());
        Response<BookModel> responseGet = books.getByID(testData.id);
        assertEquals(testData.expectedStatus, responseGet.getStatusCode());
    }

    @DisplayName("Try to delete book by providing invalid data")
    @Story("Try to delete book by providing invalid ID")
    @ParameterizedTest
    @Step("Testing delete book with invalid ID: {testData.id}")
    @MethodSource("testData.BookTestData#negativeDeleteData")
    void invalidDelete(BookTestData.GetBookPositiveNegativeCase testData) throws IOException, InterruptedException {
        Response<BookModel> response = books.delete(testData.id);
        assertEquals(testData.expectedStatus, response.getStatusCode());
    }

    @DisplayName("Try to create book with invalid ID")
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
