package tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import tests.model.Address;
import tests.model.Students;
import java.io.InputStream;
import static org.junit.jupiter.api.Assertions.*;

public class FileJsonParsingTest {

    private final ClassLoader cl = FileJsonParsingTest.class.getClassLoader();
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void testParseJsonFile() throws Exception {
        try (InputStream inputStream = cl.getResourceAsStream("students.json")) {
            Students[] students = mapper.readValue(inputStream, Students[].class);

            Students firstStudent  = students[0];
            assertEquals(1, firstStudent .getId());
            assertEquals("Иван Петров", firstStudent .getName());
            assertEquals("Java", firstStudent .getSkills().get(0));
            assertEquals("SQL", firstStudent .getSkills().get(1));
            Address address = firstStudent .getAddress();
            assertEquals("Москва", address.getCity());
            assertEquals("Ленина", address.getStreet());


            Students secondStudent = students[1];
            assertEquals(2, secondStudent.getId());
            assertEquals("Анна Сидорова", secondStudent.getName());
            assertNull(secondStudent.getAddress());
        }
    }
}