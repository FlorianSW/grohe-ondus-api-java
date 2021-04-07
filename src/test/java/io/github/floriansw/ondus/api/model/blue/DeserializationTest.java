package io.github.floriansw.ondus.api.model.blue;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static org.junit.Assume.assumeNotNull;

public class DeserializationTest {
    protected ObjectMapper mapper = new ObjectMapper();

    protected StringBuilder jsonStringFrom(String path) throws IOException {
        StringBuilder json = new StringBuilder();
        InputStream inputStream = ApplianceTest.class.getClassLoader().getResourceAsStream(path);
        assumeNotNull(inputStream);
        InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(streamReader);
        for (String line; (line = reader.readLine()) != null; ) {
            json.append(line);
        }
        return json;
    }
}
