package todo.ui;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.NonBlockingReader;
import todo.model.Priority;
import todo.model.Task;
import todo.service.TaskManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class ConsoleUI {

    private TaskManager taskManager;
    private Scanner scanner = new Scanner(System.in);
    private Terminal terminal;
    private NonBlockingReader reader;
    private int aktywnyIndeks = 0;
    public static final String BLUE = "\u001B[34m";
    public static final String RESET = "\u001B[0m";
    private static final int LEFT_WIDTH = 27;
    private static final int RIGHT_WIDTH = 40;
    private static final int WIDTH = LEFT_WIDTH + RIGHT_WIDTH + 7;

    public ConsoleUI(TaskManager taskManager){
        this(taskManager, buildTerminal());
    }

    ConsoleUI(TaskManager taskManager, Terminal terminal) {
        this.taskManager = taskManager;
        this.terminal = terminal;
        this.reader = terminal.reader();
    }

    private static Terminal buildTerminal() {
        try {
            return TerminalBuilder.terminal();
        } catch (IOException e) {
            throw new RuntimeException("Nie mozna otworzyc terminala", e);
        }
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
            System.out.println(BLUE + "║ " + RESET
                    + String.format("%-" + (WIDTH-4) + "s", linia)
                    + BLUE + " ║" + RESET);
        }
    }

    private List<String> buildTaskList(){
        List<String> lewa = new ArrayList<>();
        List<Task> listaZadan = taskManager.getAllTasks();
        for (Task zadanie : listaZadan) {
            String nazwaSkrocona = zadanie.getNazwa().length() > 15
                    ? zadanie.getNazwa().substring(0, 15) + "..."
                    : zadanie.getNazwa();
            lewa.add(String.format("%-20s %-6s", nazwaSkrocona, zadanie.getWaznosc()));
        }
        return lewa;
    }

    private List<String> buildDetails(Task zadanie){
        List<String> prawa = new ArrayList<>();
        prawa.add(String.format("%-10s %s", "ID:", zadanie.getId()));
        prawa.add(String.format("%-10s %s", "Nazwa:", zadanie.getNazwa()));
        prawa.add(String.format("%-10s %s", "Waznosc:", zadanie.getWaznosc()));
        String status = zadanie.isStatus() ? "✓ DONE" : "○ TODO";
        prawa.add(String.format("%-10s %s", "Status:", status));
        prawa.add(String.format("%-10s %s", "Deadline:", zadanie.getDeadline()));
        prawa.add(String.format("%-10s %s", "Utworzono:", zadanie.getDataUtworzenia()));
        if (zadanie.getOpis() != null){
            List<String> linie = wrapText(zadanie.getOpis(), RIGHT_WIDTH -2);
            for (int i = 0; i < linie.size(); i++){
                if (i == 0) {
                    prawa.add(String.format("%-10s %s", "Opis:", linie.get(i)));
                } else {
                    prawa.add("           " + linie.get(i));
                }
            }
        }
        return prawa;
    }

    private void printTwoColumns(List<String> lewa, List<String> prawa){
        int maxLinii = Math.max(lewa.size(), prawa.size());
        for (int i = 0; i < maxLinii; i++){
            String lewaLinia = i < lewa.size() ? lewa.get(i) : "";
            String prawaLinia = i < prawa.size() ? prawa.get(i) : "";
            System.out.println(
                    BLUE + "║ " + RESET +
                            String.format("%-" + LEFT_WIDTH + "s", lewaLinia) +
                            BLUE + " ║ " + RESET +
                            String.format("%-" + RIGHT_WIDTH + "s", prawaLinia) +
                            BLUE + " ║" + RESET
            );
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

    List<String> wrapText(String text, int width){
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
                if (!aktualnaLinia.isEmpty()) {
                    linie.add(aktualnaLinia);
                }
                aktualnaLinia = slowo;
            }
        }
        linie.add(aktualnaLinia);
        return linie;
    }

    private void cleanScreen(){
        System.out.println("\033[H\033[2J");
        System.out.flush();
    }
    public void start() throws IOException, InterruptedException {
        terminal.enterRawMode();
        System.out.println("Terminal type: " + terminal.getType());
        System.out.flush();
        Thread.sleep(2000);
        while (true){
            cleanScreen();
            printTopBorder();
            printRow("MENADZER ZADAN");
            printSeparator();

            List<Task> lista = taskManager.getAllTasks();
            Task aktywne = lista.isEmpty() ? null : lista.get(aktywnyIndeks);
            printTwoColumns(buildTaskList(), buildDetails(aktywne));
            printSeparator();

            printRow("[1] Dodaj [2] Ukoncz [3] Usun [0] Wyjscie");

            printBottomBorder();
            int c = reader.read();
            if (c == 27) {        // strzałka
                reader.read();    // [
                int arrow = reader.read();
                if (arrow == 'A') aktywnyIndeks--;      // góra
                if (arrow == 'B') aktywnyIndeks++;      // dół
                System.out.println(aktywnyIndeks);
            } else if (c == 'a') {  // dodaj
                // showPopup(...)
            } else if (c == 'q') {  // wyjście
                return;
            }

            // zabezpieczenie przed wyjściem poza listę
            int rozmiar = taskManager.getAllTasks().size();
            aktywnyIndeks = Math.clamp(aktywnyIndeks, 0, rozmiar - 1);
            /*
            System.out.print("Wybierz opcje 0-3: ");
            int wybor = scanner.nextInt();
            scanner.nextLine();

            switch (wybor){
                case 0: {
                    return;
                }
                case 1: {
                    System.out.print("Podaj nazwę zadania: ");
                    String nazwa = scanner.nextLine();
                    System.out.print("Podaj ważność (LOW/MEDIUM/HIGH): ");
                    Priority waznosc = Priority.valueOf(scanner.nextLine().toUpperCase());
                    taskManager.addTask(nazwa, waznosc);
                    System.out.println("Zadanie dodane!");
                    break;
                }
                case 2: {
                    System.out.print("Podaj id zadania: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    taskManager.completeTask(id);
                    System.out.println("Zadanie ukończone!");
                    break;
                }
                case 3: {
                    System.out.print("Podaj id zadania: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    taskManager.deleteTask(id);
                    System.out.println("Zadanie usuniete!");
                    break;
                }
            } */
        }
    }
}
