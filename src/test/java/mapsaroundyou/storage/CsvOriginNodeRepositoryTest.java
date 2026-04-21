package mapsaroundyou.storage;

import mapsaroundyou.common.DataLoadException;
import mapsaroundyou.model.OriginNode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CsvOriginNodeRepositoryTest {
    @TempDir
    Path tempDir;

    @Test
    void findAll_validCsv_returnsOriginNodes() throws Exception {
        Path csvPath = tempDir.resolve("origin_nodes.csv");
        Files.writeString(csvPath, """
                Flat_ID,Postal_Code,Region,Area_Name
                R01,111111,West,Clementi
                R02,222222,Central,Bukit Timah
                """);

        CsvOriginNodeRepository repository = new CsvOriginNodeRepository(csvPath);

        List<OriginNode> originNodes = repository.findAll();
        assertEquals(2, originNodes.size());
        assertEquals("Clementi", originNodes.getFirst().areaName());
        assertEquals("222222", repository.findById("R02").orElseThrow().postalCode());
    }

    @Test
    void constructor_missingColumn_throwsDataLoadException() throws Exception {
        Path csvPath = tempDir.resolve("broken-origin-nodes.csv");
        Files.writeString(csvPath, """
                Flat_ID,Postal_Code,Region
                R01,111111,West
                """);

        DataLoadException exception = assertThrows(
                DataLoadException.class,
                () -> new CsvOriginNodeRepository(csvPath)
        );

        assertTrue(exception.getMessage().contains("Area_Name"));
    }

    @Test
    void constructor_duplicateOriginNodeId_throwsDataLoadException() throws Exception {
        Path csvPath = tempDir.resolve("duplicate-origin-nodes.csv");
        Files.writeString(csvPath, """
                Flat_ID,Postal_Code,Region,Area_Name
                R01,111111,West,Clementi
                R01,222222,Central,Bukit Timah
                """);

        DataLoadException exception = assertThrows(
                DataLoadException.class,
                () -> new CsvOriginNodeRepository(csvPath)
        );

        assertEquals("Duplicate origin node id in " + csvPath + ": R01", exception.getMessage());
    }

    @Test
    void constructor_blankRegion_throwsDataLoadException() throws Exception {
        Path csvPath = tempDir.resolve("blank-origin-nodes.csv");
        Files.writeString(csvPath, """
                Flat_ID,Postal_Code,Region,Area_Name
                R01,111111,   ,Clementi
                """);

        DataLoadException exception = assertThrows(
                DataLoadException.class,
                () -> new CsvOriginNodeRepository(csvPath)
        );

        assertTrue(exception.getMessage().contains("Blank value for 'Region'"));
    }
}
