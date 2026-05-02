package todo;

import todo.service.TaskManager;
import todo.ui.ConsoleUI;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();
        ConsoleUI ui = new ConsoleUI(manager);
        ui.start();
    }
}
