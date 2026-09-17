package janet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class JanetTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void getResponse_validAndInvalidCommands_returnsCleanResponsesAndPreservesState() {
        Janet janet = new Janet(temporaryDirectory.resolve("data/janet.txt").toString());

        String addResponse = janet.getResponse("todo read book");
        String invalidResponse = janet.getResponse("mark 2");
        String listResponse = janet.getResponse("list");

        assertEquals("""
                Got it. I've added this task:
                [T][ ] read book
                Now you have 1 tasks in the list.""", addResponse);
        assertEquals("Sorry, that task number does not exist.", invalidResponse);
        assertTrue(listResponse.contains("1.[T][ ] read book"));
    }

    @Test
    void getResponse_commonInputMistakes_returnsSpecificErrorsAndPreservesState() {
        Janet janet = new Janet(temporaryDirectory.resolve("data/janet.txt").toString());

        assertEquals("Please enter a command.", janet.getResponse("   "));
        assertEquals("Sorry, list does not take any arguments.", janet.getResponse("list extra"));
        assertEquals("Sorry, bye does not take any arguments.", janet.getResponse("bye now"));
        assertEquals("Sorry, task details cannot contain tabs or line breaks.",
                janet.getResponse("todo read\tbook"));

        String addResponse = janet.getResponse("  todo   read book  ");
        String listResponse = janet.getResponse("  list  ");

        assertTrue(addResponse.contains("[T][ ] read book"));
        assertTrue(listResponse.contains("1.[T][ ] read book"));
    }

    @Test
    void getResponse_saveFails_reversesAllTaskChanges() throws IOException {
        Path dataFile = temporaryDirectory.resolve("data/janet.txt");
        Janet janet = new Janet(dataFile.toString());
        janet.getResponse("todo first");
        janet.getResponse("todo second");

        Files.delete(dataFile);
        Files.createDirectory(dataFile);
        Files.writeString(dataFile.resolve("blocking-file.txt"), "keep directory non-empty");

        String markResponse = janet.getResponse("mark 1");
        String deleteResponse = janet.getResponse("delete 2");
        String addResponse = janet.getResponse("todo third");
        String listResponse = janet.getResponse("list");

        assertTrue(markResponse.contains("that change was reversed"));
        assertTrue(deleteResponse.contains("that change was reversed"));
        assertTrue(addResponse.contains("that change was reversed"));
        assertTrue(listResponse.contains("1.[T][ ] first"));
        assertTrue(listResponse.contains("2.[T][ ] second"));
        assertFalse(listResponse.contains("third"));
    }

    @Test
    void getResponse_malformedSavedData_warnsAndProtectsOriginalFile() throws IOException {
        Path dataFile = temporaryDirectory.resolve("data/janet.txt");
        Files.createDirectories(dataFile.getParent());
        String originalData = "T\t0\tvalid task\ninvalid row\n";
        Files.writeString(dataFile, originalData);

        Janet janet = new Janet(dataFile.toString());
        String addResponse = janet.getResponse("todo another task");
        String listResponse = janet.getResponse("list");

        assertEquals(1, janet.getStartupWarnings().size());
        assertTrue(janet.getStartupWarnings().get(0).contains("line 2"));
        assertTrue(addResponse.contains("saved-data warning is unresolved"));
        assertTrue(listResponse.contains("1.[T][ ] valid task"));
        assertFalse(listResponse.contains("another task"));
        assertEquals(originalData, Files.readString(dataFile));
    }
}
