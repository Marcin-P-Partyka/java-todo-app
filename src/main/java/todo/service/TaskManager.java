package todo.service;
import todo.model.Priority;
import todo.model.Task;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskManager {

    private List<Task> listaZadan;
    private int ostatnieId;

    public TaskManager(){
        listaZadan = new ArrayList<Task>();
        ostatnieId = 0;
    }

    public int getOstatnieId(){return ostatnieId;}
    public void addTask(String nazwa, Priority waznosc){
        Task zadanie = new Task(getOstatnieId(), nazwa, waznosc);
        listaZadan.add(zadanie);
        ostatnieId++;
    }
    public List<Task> getAllTasks(){
        return new ArrayList<>(listaZadan);
    }
    public Optional<Task> getTaskById(int id){
        for (int i = 0; i < listaZadan.size(); i++){
            if (id == listaZadan.get(i).getId()){
                return Optional.of(listaZadan.get(i));
            }
        }
        return Optional.empty();
    }
    public void completeTask(int id){
        Optional<Task> zadanie = getTaskById(id);
        zadanie.ifPresent(value -> {
            value.setStatus(true);});
    }
    public void deleteTask(int id){
        Optional<Task> zadanie = getTaskById(id);
        zadanie.ifPresent(value -> {
            listaZadan.remove(value);});
    }
}