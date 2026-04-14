package mapsaroundyou.storage;

import java.io.IOException;
import java.io.Reader;

/**
 * Opens a {@link Reader} for CSV ingestion, abstracting classpath versus filesystem sources.
 */
@FunctionalInterface
interface ReaderSupplier {
    /**
     * @return a new reader positioned after any required preamble (callers close the stream)
     * @throws IOException if the source cannot be opened
     */
    Reader open() throws IOException;
}
