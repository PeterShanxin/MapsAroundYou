package mapsaroundyou.gui;

import mapsaroundyou.common.DataLoadException;
import mapsaroundyou.common.DestinationNotFoundException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GuiErrorTranslatorTest {
    @Test
    void toUserMessage_nullThrowable_returnsFallback() {
        assertEquals("Unknown error.", GuiErrorTranslator.toUserMessage(null));
    }

    @Test
    void toUserMessage_knownException_returnsOriginalMessage() {
        DestinationNotFoundException exception = new DestinationNotFoundException("Unknown destination: D99");

        assertEquals("Unknown destination: D99", GuiErrorTranslator.toUserMessage(exception));
    }

    @Test
    void toUserMessage_unknownExceptionWithBlankMessage_returnsClassNameFallback() {
        IllegalStateException exception = new IllegalStateException("   ");

        assertEquals("Unexpected error (IllegalStateException).", GuiErrorTranslator.toUserMessage(exception));
    }

    @Test
    void toUserMessage_unknownExceptionWithMessage_addsPrefix() {
        RuntimeException exception = new RuntimeException("Boom");

        assertEquals("Unexpected error: Boom", GuiErrorTranslator.toUserMessage(exception));
    }

    @Test
    void toUserMessage_knownDataLoadException_returnsOriginalMessage() {
        DataLoadException exception = new StubDataLoadException("Dataset failed");

        assertEquals("Dataset failed", GuiErrorTranslator.toUserMessage(exception));
    }

    private static final class StubDataLoadException extends DataLoadException {
        private static final long serialVersionUID = 1L;

        private StubDataLoadException(String message) {
            super(message);
        }
    }
}
