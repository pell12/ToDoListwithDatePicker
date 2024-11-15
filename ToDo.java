@Entity
public class ToDo {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String date;
    public boolean isComplete;
}

