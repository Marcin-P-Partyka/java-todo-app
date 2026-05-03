package todo.ui;

import todo.model.Priority;
import todo.model.Task;
import todo.service.TaskManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class ConsoleUI {

    private TaskManager taskManager;
    private Scanner scanner = new Scanner(System.in);
    public static final String BLUE = "\u001B[34m";
    public static final String RESET = "\u001B[0m";
    private static final int WIDTH = 60;

    public ConsoleUI(TaskManager taskManager){
        this.taskManager = taskManager;
    }

    public void printTasks(){
        List<Task> listaZadan = taskManager.getAllTasks();
        for (Task zadanie : listaZadan) {
            // tutaj masz dostęp do obiektu zadanie
            String status = zadanie.isStatus() ? "✓ DONE" : "○ TODO";
            String nazwaSkrocona = zadanie.getNazwa().length() > 20
                    ? zadanie.getNazwa().substring(0, 17) + "..."
                    : zadanie.getNazwa();
            String formated = String.format("%-5d %-20s %-10s %-6s", zadanie.getId(), nazwaSkrocona, zadanie.getWaznosc(), status);
            printRow(formated);
        }
        if (listaZadan.isEmpty()) {
            printRow("Brak zadan!");
        }
    }

    private void printRow(String content){
        List<String> newContent = wrapText(content, WIDTH - 4);
        for (String linia : newContent){
            System.out.println(BLUE + "│ " + RESET
                    + String.format("%-56s", linia)
                    + BLUE + " │" + RESET);
        }
    }

    private void printTopBorder(){
        System.out.println(BLUE + "╔" + "═".repeat(WIDTH - 2) + "╗" + RESET);
    }

    private void printBottomBorder(){
        System.out.println(BLUE + "╚" + "═".repeat(WIDTH - 2) + "╝" + RESET);
    }

    private void printSeparator(){
        System.out.println(BLUE + "╠" + "═".repeat(WIDTH - 2) + "╣" + RESET);
    }

    private List<String> wrapText(String text, int width){
        List<String> linie = new ArrayList<>();
        String[] slowa = text.split(" ");
        String aktualnaLinia = "";
        for (String slowo : slowa ){
            if ((aktualnaLinia + slowo).length() < width){
                if (aktualnaLinia.isEmpty()){
                    aktualnaLinia = slowo;
                } else {
                    aktualnaLinia = aktualnaLinia + " " + slowo;
                }
            } else {
                linie.add(aktualnaLinia);
                aktualnaLinia = slowo;
            }
        }
        linie.add(aktualnaLinia);
        return linie;
    }

    public void start(){
        while (true){
            printTopBorder();
            printRow("MENADZER ZADAN");
            printSeparator();

            printTasks();
            printSeparator();

            printRow("[1] Dodaj [2] Ukoncz [3] Usun [0] Wyjscie");

            printBottomBorder();
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
