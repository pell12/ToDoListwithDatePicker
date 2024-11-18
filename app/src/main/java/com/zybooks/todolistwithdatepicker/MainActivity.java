package com.zybooks.todolistwithdatepicker;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private EditText taskNameEditText;
    private TextView dueDateTextView;
    private Button showDatePickerButton;
    private Button addTaskButton;
    private ListView taskListView;

    private ArrayList<Task> taskList;
    private ArrayAdapter<Task> taskAdapter;
    private Database database;
    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskNameEditText = findViewById(R.id.taskNameEditText);
        dueDateTextView = findViewById(R.id.dueDateTextView);
        showDatePickerButton = findViewById(R.id.showDatePickerButton);
        addTaskButton = findViewById(R.id.addTaskButton);
        taskListView = findViewById(R.id.taskListView);

        // Initialize task list and database
        taskList = new ArrayList<>();
        database = new Database(this);

        // Initialize ListView and adapter
        taskAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, taskList);
        taskListView.setAdapter(taskAdapter);

        // Load tasks from the database
        loadTasksFromDatabase();

        // Set up date picker
        showDatePickerButton.setOnClickListener(v -> showDatePickerDialog());

        // Add task button logic
        addTaskButton.setOnClickListener(v -> addTaskToDatabase());

        // Item click listener to mark task complete or remove
        taskListView.setOnItemClickListener((parent, view, position, id) -> {
            Task task = taskList.get(position);
            int taskId = task.getTaskId();

            if (task.isCompleted()) {
                removeTaskFromDatabase(taskId);
            } else {
                markTaskCompletedInDatabase(taskId);
            }
        });
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Display the selected date in the TextView
                    dueDateTextView.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void addTaskToDatabase() {
        String taskName = taskNameEditText.getText().toString();
        String dueDate = dueDateTextView.getText().toString();

        if (taskName.isEmpty() || dueDate.isEmpty()) {
            Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
        } else {
            // Insert task into the database
            boolean isInserted = database.addTask(taskName, dueDate);
            if (isInserted) {
                taskNameEditText.setText("");
                dueDateTextView.setText("");
                loadTasksFromDatabase();
            } else {
                Toast.makeText(this, "Failed to add task", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadTasksFromDatabase() {
        Cursor cursor = database.getAllTasks();
        taskList.clear();
        while (cursor.moveToNext()) {
            int taskId = cursor.getInt(0);
            String taskName = cursor.getString(1);
            String dueDate = cursor.getString(2);
            int completed = cursor.getInt(3);

            Task task = new Task(taskId, taskName, dueDate, completed == 1);
            taskList.add(task);
        }
        cursor.close();
        taskAdapter.notifyDataSetChanged();
    }

    private void markTaskCompletedInDatabase(int taskId) {
        boolean success = database.markTaskCompleted(taskId);
        if (success) {
            loadTasksFromDatabase();
        }
    }

    private void removeTaskFromDatabase(int taskId) {
        boolean success = database.removeTask(taskId);
        if (success) {
            loadTasksFromDatabase();
        }
    }
}
