package testData;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.AuthorsModel;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

public class AuthorTestData {
    public static class GetAuthorPositiveNegativeCase {
        public int id;
        public int expectedStatus;
        public String firstName;
        public String lastName;
        public String expectedTitle;
    }

    public static Stream<GetAuthorPositiveNegativeCase> positiveGetData() throws IOException {
        return JsonDataLoader.loadFromJson("/author_get_positive_data.json", new TypeReference<List<GetAuthorPositiveNegativeCase>>() {
        });
    }

    public static Stream<GetAuthorPositiveNegativeCase> negativeGetData() throws IOException {
        return JsonDataLoader.loadFromJson("/author_get_negative_data.json", new TypeReference<List<GetAuthorPositiveNegativeCase>>() {
        });
    }

    public static Stream<AuthorsModel> positivePostData() throws IOException {
        return JsonDataLoader.loadFromJson("/author_post_positive_data.json", new TypeReference<List<AuthorsModel>>() {
        });
    }

    public static Stream<AuthorsModel> negativePostData() throws IOException {
        return JsonDataLoader.loadFromJson("/author_post_negative_data.json", new TypeReference<List<AuthorsModel>>() {
        });
    }

    public static Stream<AuthorsModel> positivePutData() throws IOException {
        return JsonDataLoader.loadFromJson("/author_put_positive_data.json", new TypeReference<List<AuthorsModel>>() {
        });
    }

    public static Stream<GetAuthorPositiveNegativeCase> positiveDeleteData() throws IOException {
        return JsonDataLoader.loadFromJson("/author_delete_positive_data.json", new TypeReference<List<GetAuthorPositiveNegativeCase>>() {
        });
    }

    public static Stream<GetAuthorPositiveNegativeCase> negativeDeleteData() throws IOException {
        return JsonDataLoader.loadFromJson("/author_delete_negative_data.json", new TypeReference<List<GetAuthorPositiveNegativeCase>>() {
        });
    }
}
