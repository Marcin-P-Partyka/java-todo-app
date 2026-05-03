package todo.storage;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import todo.model.Task;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FileStorage {
    private String sciezka;
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>)
                    (date, type, ctx) -> new JsonPrimitive(date.toString()))
            .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>)
                    (json, type, ctx) -> LocalDate.parse(json.getAsString()))
            .create();

    public FileStorage(String sciezka){
        this.sciezka = sciezka;
    }

    public void save(List<Task> zadania){
        String json = gson.toJson(zadania);
        try (FileWriter writer = new FileWriter(sciezka)){
            writer.write(json);
            System.out.println("Zapisano plik " + sciezka);
        } catch (IOException e){
            System.out.println("Blad zapisu! ");
        }
    }
    public List<Task> load(){
        try (FileReader reader = new FileReader(sciezka)){
            Type type = new TypeToken<List<Task>>(){}.getType();
            System.out.println("Odczytano plik " + sciezka);
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            System.out.println("Brak pliku z danymi! ");
            return new ArrayList<>();
        }
    }
}
