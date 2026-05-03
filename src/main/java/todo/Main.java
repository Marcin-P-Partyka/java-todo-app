package todo;

import todo.service.TaskManager;
import todo.storage.FileStorage;
import todo.ui.ConsoleUI;

public class Main {
    static void main(String[] args) {
        FileStorage storage = new FileStorage("tasks.json");
        TaskManager manager = new TaskManager(storage);
        ConsoleUI ui = new ConsoleUI(manager);
        ui.start();
    }
}
