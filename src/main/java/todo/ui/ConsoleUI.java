package todo.ui;

import todo.model.Priority;
import todo.service.TaskManager;

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

    public void start(){
        System.out.println(BLUE + "╔══════════╗" + RESET);
        System.out.println(BLUE + "║  MENU    ║" + RESET);
        System.out.println(BLUE + "╚══════════╝" + RESET);

        System.out.println(BLUE + "┌─── ZADANIA ─────┐" + RESET);

        System.out.println(BLUE + "┌─── MENU ────────┐" + RESET);
        System.out.println(BLUE + "│  [1] Dodaj zadanie      [2] Ukończ zadanie │" + RESET);
        System.out.println(BLUE + "│  [3] Usuń zadanie     │" + RESET);

        while (true){
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
                    break;
                }
                case 2: {
                    System.out.println("Podaj id zadania: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    taskManager.completeTask(id);
                    break;
                }
                case 3: {
                    System.out.println("Podaj id zadania: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    taskManager.deleteTask(id);
                    break;
                }
            }
        }
    }
}
