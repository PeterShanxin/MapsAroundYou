package mapsaroundyou.storage;

import mapsaroundyou.common.DatasetIntegrityException;
import mapsaroundyou.common.DatasetIOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Shared CSV helpers: resource suppliers, header validation, typed parsing, and safe cleanup.
 */
final class CsvSupport {
    private static final Logger LOGGER = Logger.getLogger(CsvSupport.class.getName());

    private CsvSupport() {
    }

    /**
     * Supplies readers for classpath resources (typically under {@code commute_data/}).
     */
    static ReaderSupplier classpathReader(String resourcePath) {
        return () -> {
            InputStream inputStream = CsvSupport.class.getClassLoader().getResourceAsStream(resourcePath);
            if (inputStream == null) {
                throw new DatasetIntegrityException("Missing resource: " + resourcePath);
            }
            return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        };
    }

    /** Supplies readers for arbitrary filesystem paths (tests and tooling). */
    static ReaderSupplier fileReader(Path filePath) {
        return () -> Files.newBufferedReader(filePath, StandardCharsets.UTF_8);
    }

    /**
     * Opens a {@link CSVParser} with default formatting, validating required headers up front.
     *
     * @throws DatasetIOException on read failures (reader closed on error)
     * @throws DatasetIntegrityException when headers are missing or the resource is absent
     */
    static CSVParser openParser(ReaderSupplier readerSupplier, String sourceName, String... requiredHeaders) {
        Reader reader = null;
        CSVParser parser = null;
        try {
            reader = readerSupplier.open();
            CSVFormat format = CSVFormat.DEFAULT.builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .setTrim(true)
                    .get();
            parser = format.parse(reader);
            try {
                validateHeaders(parser.getHeaderMap(), sourceName, requiredHeaders);
            } catch (RuntimeException exception) {
                closeQuietly(parser, sourceName);
                throw exception;
            }
            return parser;
        } catch (IOException exception) {
            closeQuietly(parser, sourceName);
            closeQuietly(reader, sourceName);
            throw new DatasetIOException("Failed to read dataset: " + sourceName, exception);
        }
    }

    /** Returns a trimmed, non-blank column value or throws a row-aware integrity error. */
    static String requireValue(CSVRecord record, String header, String sourceName) {
        String value = record.get(header).trim();
        if (value.isEmpty()) {
            throw new DatasetIntegrityException("Blank value for '" + header + "' in " + sourceName
                    + " at row " + record.getRecordNumber());
        }
        return value;
    }

    /** Parses a required integer column, preserving row numbers in error messages. */
    static int parseRequiredInt(CSVRecord record, String header, String sourceName) {
        String value = requireValue(record, header, sourceName);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            throw new DatasetIntegrityException("Invalid integer for '" + header + "' in " + sourceName
                    + " at row " + record.getRecordNumber() + ": " + value, exception);
        }
    }

    /** Parses a required floating-point column. */
    static double parseRequiredDouble(CSVRecord record, String header, String sourceName) {
        String value = requireValue(record, header, sourceName);
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException exception) {
            throw new DatasetIntegrityException("Invalid decimal for '" + header + "' in " + sourceName
                    + " at row " + record.getRecordNumber() + ": " + value, exception);
        }
    }

    /** Parses {@code true}/{@code false} (case-insensitive) boolean columns. */
    static boolean parseRequiredBoolean(CSVRecord record, String header, String sourceName) {
        String value = requireValue(record, header, sourceName);
        if ("true".equalsIgnoreCase(value)) {
            return true;
        }
        if ("false".equalsIgnoreCase(value)) {
            return false;
        }
        throw new DatasetIntegrityException("Invalid boolean for '" + header + "' in " + sourceName
                + " at row " + record.getRecordNumber() + ": " + value);
    }

    /** Ensures every required header exists in the parsed header map. */
    private static void validateHeaders(Map<String, Integer> headerMap, String sourceName, String... requiredHeaders) {
        for (String header : requiredHeaders) {
            if (!headerMap.containsKey(header)) {
                throw new DatasetIntegrityException("Missing required column '" + header + "' in " + sourceName
                        + ". Present columns: " + Arrays.toString(headerMap.keySet().toArray()));
            }
        }
    }

    /** Closes readers after failures without masking the original parsing error. */
    private static void closeQuietly(Reader reader, String sourceName) {
        if (reader == null) {
            return;
        }
        try {
            reader.close();
        } catch (IOException exception) {
            LOGGER.log(Level.WARNING, "Failed to close reader after error opening dataset: " + sourceName, exception);
        }
    }

    /** Closes parsers after failures without masking the original error. */
    private static void closeQuietly(CSVParser parser, String sourceName) {
        if (parser == null) {
            return;
        }
        try {
            parser.close();
        } catch (IOException exception) {
            LOGGER.log(Level.WARNING, "Failed to close parser after error opening dataset: " + sourceName, exception);
        }
    }
}
