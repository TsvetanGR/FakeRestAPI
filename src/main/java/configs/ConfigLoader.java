package configs;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static final Properties properties = new Properties();
    private static final String BASE_URL = getProperty("api.url");

    static {
        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new RuntimeException("Unable to find application.properties in classpath");
            }
            properties.load(input);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load application.properties", ex);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getBooksUrl() {
        return BASE_URL + "/api/v1/Books";
    }

    public static String getAuthorsUrl() {
        return BASE_URL + "/api/v1/Authors";
    }
}
