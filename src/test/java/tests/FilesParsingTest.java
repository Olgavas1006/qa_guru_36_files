import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import static com.codeborne.xlstest.XLS.containsText;
import static org.hamcrest.MatcherAssert.assertThat;


public class FilesParsingTest {

    private ClassLoader cl = FilesParsingTest.class.getClassLoader();

    @DisplayName("Проверка содержимого zip")
    @Test
    void zipFileParsingTest() throws Exception {
        try (ZipInputStream zipInput = new ZipInputStream(
                cl.getResourceAsStream("files.zip")
        )) {
            ZipEntry entry;

            while ((entry = zipInput.getNextEntry()) != null) {
                System.out.println(entry.getName());
            }
            zipInput.closeEntry();
        }
    }

    @DisplayName("Чтение и проверка содержимого pdf файла из архива")
    @Test
    void pdfCheckFileTest() throws Exception {
        try (ZipInputStream zipInput = new ZipInputStream(
                cl.getResourceAsStream("files.zip")
        )) {
            ZipEntry entry;

            while ((entry = zipInput.getNextEntry()) != null) {
                if (entry.getName().equals("50quickideas.pdf")) {
                    // Читаем PDF и проверяем, что он содержит текст "Quick Ideas"
                    PDF pdf = new PDF(zipInput);
                    Assertions.assertEquals("Gojko Adzic", pdf.author);
                    Assertions.assertEquals("50 Quick Ideas to Improve your User Stories", pdf.title);
                    return;
                }
                zipInput.closeEntry();
            }
        }
    }

    @DisplayName("Чтение и проверка содержимого csv файла из архива")
    @Test
    void csvCheckFileTest() throws Exception {
        try (ZipInputStream zipInput = new ZipInputStream(
                cl.getResourceAsStream("files.zip")
        )) {
            ZipEntry entry;

            while ((entry = zipInput.getNextEntry()) != null) {
                if (entry.getName().equals("файл.csv")) {
                    try (CSVReader csvReader = new CSVReader(new InputStreamReader(zipInput))) {
                        List<String[]> data = csvReader.readAll();
                        Assertions.assertEquals(3, data.size());
                        Assertions.assertArrayEquals(
                                new String[]{"Имя", "Возраст", "Город"},
                                data.get(0)
                        );
                        Assertions.assertArrayEquals(
                                new String[]{"Анна", "25", "Москва"},
                                data.get(1)
                        );
                        Assertions.assertArrayEquals(
                                new String[]{"Иван", "30", "Санкт-Петербург"},
                                data.get(2)
                        );
                    }
                    return;
                }
            }
        }
    }

    @DisplayName("Чтение и проверка содержимого xls файла из архива")
    @Test
    void xlsCheckFileTest() throws Exception {
        try (ZipInputStream zipInput = new ZipInputStream(
                cl.getResourceAsStream("files.zip")
        )) {
            ZipEntry entry;
            while ((entry = zipInput.getNextEntry()) != null) {
                if (entry.getName().equals("statement.xlsx")) {
                    XLS xls = new XLS(zipInput);
                    String actualValue = xls.excel.getSheetAt(0).getRow(0).getCell(0).getStringCellValue();
                    Assertions.assertTrue(actualValue.contains("Выписка"));
                            assertThat(xls, containsText("Период"));
                            assertThat(xls, containsText("Solntsev Andrei"));
                }
            }
        }
    }
}