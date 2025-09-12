package testData;

import com.fasterxml.jackson.core.type.TypeReference;
import model.BookModel;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

public class BookTestData {

    public static class GetBookPositiveNegativeCase {
        public int id;
        public int expectedStatus;
        public String expectedTitle;
    }

    public static Stream<BookModel> negativePostData() throws IOException {
        return JsonDataLoader.loadFromJson("/book_post_negative_data.json", new TypeReference<List<BookModel>>() {
        }).peek(book -> book.publishDate = ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT));
    }

    public static Stream<BookModel> positivePostData() throws IOException {
        return JsonDataLoader.loadFromJson("/book_post_positive_data.json", new TypeReference<List<BookModel>>() {
        }).peek(book -> book.publishDate = ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT));
    }

    public static Stream<BookModel> positivePutData() throws IOException {
        return JsonDataLoader.loadFromJson("/book_put_positive_data.json", new TypeReference<List<BookModel>>() {
        }).peek(book -> book.publishDate = ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT));
    }

    public static Stream<GetBookPositiveNegativeCase> negativeGetData() throws IOException {
        return JsonDataLoader.loadFromJson("/book_get_negative_data.json", new TypeReference<List<GetBookPositiveNegativeCase>>() {
        });
    }

    public static Stream<GetBookPositiveNegativeCase> positiveGetData() throws IOException {
        return JsonDataLoader.loadFromJson("/book_get_positive_data.json", new TypeReference<List<GetBookPositiveNegativeCase>>() {
        });
    }

    public static Stream<GetBookPositiveNegativeCase> positiveDeleteData() throws IOException {
        return JsonDataLoader.loadFromJson("/book_delete_positive_data.json", new TypeReference<List<GetBookPositiveNegativeCase>>() {
        });
    }

    public static Stream<GetBookPositiveNegativeCase> negativeDeleteData() throws IOException {
        return JsonDataLoader.loadFromJson("/book_delete_negative_data.json", new TypeReference<List<GetBookPositiveNegativeCase>>() {
        });
    }
}
