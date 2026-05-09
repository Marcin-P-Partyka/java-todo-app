package todo.ui;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import todo.model.Priority;
import todo.model.Task;
import todo.service.TaskManager;
import todo.storage.FileStorage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConsoleUITest {

    private ConsoleUI ui;
    private ByteArrayOutputStream out;
    private PrintStream originalOut;

    /** FileStorage that lives in memory — no disk access, no side effects. */
    static class FakeFileStorage extends FileStorage {
        private final List<Task> tasks;

        FakeFileStorage(List<Task> tasks) {
            super("/dev/null");
            this.tasks = new ArrayList<>(tasks);
        }

        @Override public List<Task> load() { return new ArrayList<>(tasks); }
        @Override public void save(List<Task> zadania) { }
    }

    private ConsoleUI makeUI(List<Task> tasks) throws IOException {
        TaskManager tm = new TaskManager(new FakeFileStorage(tasks));
        Terminal terminal = TerminalBuilder.builder()
                .system(false)
                .streams(System.in, System.out)
                .dumb(true)
                .build();
        return new ConsoleUI(tm, terminal);
    }

    @BeforeEach
    void setUp() throws IOException {
        originalOut = System.out;
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ui = makeUI(List.of());
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    // --- wrapText ---

    @Test
    void wrapText_shortText_returnsOneLine() {
        List<String> result = ui.wrapText("Hello", 20);
        assertEquals(1, result.size());
        assertEquals("Hello", result.get(0));
    }

    @Test
    void wrapText_textFitsExactly_returnsOneLine() {
        List<String> result = ui.wrapText("ab cd", 5);
        assertEquals(1, result.size());
        assertEquals("ab cd", result.get(0));
    }

    @Test
    void wrapText_longText_wrapsAtWordBoundary() {
        List<String> result = ui.wrapText("jeden dwa trzy cztery", 10);
        assertTrue(result.size() > 1, "Powinno byc wiecej niz jedna linia");
        for (String linia : result) {
            assertTrue(linia.length() < 10, "Linia za dluga: '" + linia + "'");
        }
    }

    @Test
    void wrapText_singleWord_returnsOneLine() {
        List<String> result = ui.wrapText("jednoslowo", 5);
        assertEquals(1, result.size());
    }

    // --- printTasks ---

    @Test
    void printTasks_emptyList_showsBrakZadan() throws IOException {
        ui = makeUI(List.of());
        ui.printTasks();
        assertTrue(out.toString().contains("Brak zadan!"));
    }

    @Test
    void printTasks_withTask_showsTaskName() throws IOException {
        ui = makeUI(List.of(new Task(1, "Kupic mleko", Priority.LOW)));
        ui.printTasks();
        assertTrue(out.toString().contains("Kupic mleko"));
    }

    @Test
    void printTasks_completedTask_showsDone() throws IOException {
        Task z = new Task(2, "Zrobic pranie", Priority.HIGH);
        z.setStatus(true);
        ui = makeUI(List.of(z));
        ui.printTasks();
        assertTrue(out.toString().contains("DONE"));
    }

    @Test
    void printTasks_pendingTask_showsTodo() throws IOException {
        ui = makeUI(List.of(new Task(3, "Zrobic zakupy", Priority.MEDIUM)));
        ui.printTasks();
        assertTrue(out.toString().contains("TODO"));
    }

    @Test
    void printTasks_longName_truncatesToThreeDots() throws IOException {
        ui = makeUI(List.of(new Task(4, "Bardzo dlugie zadanie do zrobienia", Priority.LOW)));
        ui.printTasks();
        String output = out.toString();
        assertTrue(output.contains("..."), "Oczekiwano '...' dla skroconej nazwy");
        assertFalse(output.contains("Bardzo dlugie zadanie do zrobienia"),
                "Pelna nazwa nie powinna byc wyswietlana");
    }

    @Test
    void printTasks_multipleTasks_showsAllTasks() throws IOException {
        ui = makeUI(List.of(
                new Task(1, "Zadanie pierwsze", Priority.LOW),
                new Task(2, "Zadanie drugie", Priority.HIGH)
        ));
        ui.printTasks();
        String output = out.toString();
        assertTrue(output.contains("Zadanie pierwsze"));
        assertTrue(output.contains("Zadanie drugie"));
    }

    @Test
    void printTasks_highPriority_showsHigh() throws IOException {
        ui = makeUI(List.of(new Task(5, "Pilne", Priority.HIGH)));
        ui.printTasks();
        assertTrue(out.toString().contains("HIGH"));
    }
}
