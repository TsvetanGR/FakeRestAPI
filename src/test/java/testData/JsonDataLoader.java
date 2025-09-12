package testData;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Stream;

public class JsonDataLoader {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> Stream<T> loadFromJson(String fileName, TypeReference<List<T>> type) throws IOException {
        try (InputStream inputStream = BookTestData.class.getResourceAsStream(fileName)) {
            List<T> data = mapper.readValue(inputStream, type);
            return data.stream();
        }
    }
}
