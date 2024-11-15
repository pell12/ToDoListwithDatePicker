@Database(entities = {ToDo.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract ToDoDao toDoDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "todo_database").allowMainThreadQueries().build();
        }
        return instance;
    }
}

@Dao
public interface ToDoDao {
    @Insert
    void insert(ToDo toDo);

    @Update
    void update(ToDo toDo);

    @Delete
    void delete(ToDo toDo);

    @Query("SELECT * FROM ToDo")
    List<ToDo> getAllToDos();

    @Query("SELECT * FROM ToDo WHERE id = :id")
    ToDo getToDoById(int id);
}
