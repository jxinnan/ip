package janet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
}
