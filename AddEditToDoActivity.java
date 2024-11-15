
public class AddEditToDoActivity extends AppCompatActivity {

    private EditText nameEditText;
    private Button dateButton;
    private boolean isEditing = false;
    private ToDo toDo;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_todo);

        nameEditText = findViewById(R.id.nameEditText);
        dateButton = findViewById(R.id.dateButton);
        db = AppDatabase.getInstance(this);

        int toDoId = getIntent().getIntExtra("todo_id", -1);
        if (toDoId != -1) {
            isEditing = true;
            toDo = db.toDoDao().getToDoById(toDoId);
            nameEditText.setText(toDo.name);
            dateButton.setText(toDo.date);
        } else {
            toDo = new ToDo();
        }

        dateButton.setOnClickListener(v -> showDatePicker());

        findViewById(R.id.saveButton).setOnClickListener(v -> saveToDo());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String date = dayOfMonth + "/" + (month + 1) + "/" + year;
            dateButton.setText(date);
            toDo.date = date;
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void saveToDo() {
        toDo.name = nameEditText.getText().toString();
        if (isEditing) {
            db.toDoDao().update(toDo);
        } else {
            db.toDoDao().insert(toDo);
        }
        finish();
    }
}

}
