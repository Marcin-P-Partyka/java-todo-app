package todo.model;
import java.time.LocalDate;

public class Task {
    private final int id;
    private String nazwa;
    private String opis;
    private Priority waznosc;
    private boolean status = false;
    private int czasTrwania = 0;
    private LocalDate deadline;
    private final LocalDate dataUtworzenia;

    public Task(int id, String nazwa, Priority waznosc){
        this.id = id;
        this.nazwa = nazwa;
        this.waznosc = waznosc;
        this.dataUtworzenia = LocalDate.now();
    }

    public void setNazwa(String nazwa){this.nazwa = nazwa;}
    public void setOpis(String opis){this.opis = opis;}
    public void setWaznosc(Priority waznosc){this.waznosc = waznosc;}
    public void setStatus(boolean status){this.status = status;}
    public void setCzasTrwania(int czasTrwania){this.czasTrwania = czasTrwania;}
    public void setDeadline(LocalDate deadline){this.deadline = deadline;}

    public int getId(){return id;}
    public String getNazwa(){return nazwa;}
    public String getOpis(){return opis;}
    public Priority getWaznosc(){return waznosc;}
    public boolean isStatus(){return status;}
    public int getCzasTrwania(){return czasTrwania;}
    public LocalDate getDeadline(){return deadline;}
    public LocalDate getDataUtworzenia(){return dataUtworzenia;}
}
