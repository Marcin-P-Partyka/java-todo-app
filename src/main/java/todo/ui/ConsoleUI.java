package todo.ui;

import todo.model.Priority;
import todo.model.Task;
import todo.service.TaskManager;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class ConsoleUI {

    private TaskManager taskManager;
    private Scanner scanner = new Scanner(System.in);
    public static final String BLUE = "\u001B[34m";
    public static final String RESET = "\u001B[0m";

    public ConsoleUI(TaskManager taskManager){
        this.taskManager = taskManager;
    }

    public void printTasks(){
        List<Task> listaZadan = taskManager.getAllTasks();
        for (Task zadanie : listaZadan) {
            // tutaj masz dostęp do obiektu zadanie
            String status = zadanie.isStatus() ? "✓ DONE" : "○ TODO";
            String formated = String.format("%-5d %-20s %-10s %-6s", zadanie.getId(), zadanie.getNazwa(), zadanie.getWaznosc(), status);
            System.out.println(formated);
        }
        if (listaZadan.isEmpty()) {
            System.out.println(" Brak zadań. ");
        }
    }

    public void start(){
        while (true){
            System.out.println(BLUE + "╔══════════╗" + RESET);
            System.out.println(BLUE + "║  MENU    ║" + RESET);
            System.out.println(BLUE + "╚══════════╝" + RESET);

            System.out.println(BLUE + "┌─── ZADANIA ─────┐" + RESET);
            printTasks();
            System.out.println(BLUE + "└─────────────────┘" + RESET);

            System.out.println(BLUE + "┌─── MENU ────────┐" + RESET);
            System.out.println(BLUE + "│  [1] Dodaj zadanie      [2] Ukończ zadanie │" + RESET);
            System.out.println(BLUE + "│  [3] Usuń zadanie     │" + RESET);

            System.out.println("Wybierz opcje 0-3: ");
            int wybor = scanner.nextInt();
            scanner.nextLine();

            switch (wybor){
                case 0: {
                    return;
                }
                case 1: {
                    System.out.println("Podaj nazwę zadania:");
                    String nazwa = scanner.nextLine();
                    System.out.println("Podaj ważność (LOW/MEDIUM/HIGH):");
                    Priority waznosc = Priority.valueOf(scanner.nextLine().toUpperCase());
                    taskManager.addTask(nazwa, waznosc);
                    System.out.println("Zadanie dodane!");
                    break;
                }
                case 2: {
                    System.out.println("Podaj id zadania: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    taskManager.completeTask(id);
                    System.out.println("Zadanie ukończone!");
                    break;
                }
                case 3: {
                    System.out.println("Podaj id zadania: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    taskManager.deleteTask(id);
                    System.out.println("Zadanie usuniete!");
                    break;
                }
            }
        }
    }
}
