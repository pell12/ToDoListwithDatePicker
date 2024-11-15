public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder> {

    private List<ToDo> toDoList;
    private Consumer<ToDo> editCallback;
    private Consumer<ToDo> deleteCallback;

    public ToDoAdapter(List<ToDo> toDoList, Consumer<ToDo> editCallback, Consumer<ToDo> deleteCallback) {
        this.toDoList = toDoList;
        this.editCallback = editCallback;
        this.deleteCallback = deleteCallback;
    }

    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item, parent, false);
        return new ToDoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder holder, int position) {
        ToDo toDo = toDoList.get(position);
        holder.bind(toDo);
    }

    @Override
    public int getItemCount() {
        return toDoList.size();
    }

    class ToDoViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView, dateTextView;
        CheckBox completeCheckBox;

        public ToDoViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            completeCheckBox = itemView.findViewById(R.id.completeCheckBox);
        }

        public void bind(ToDo toDo) {
            nameTextView.setText(toDo.name);
            dateTextView.setText(toDo.date);
            completeCheckBox.setChecked(toDo.isComplete);

            completeCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                toDo.isComplete = isChecked;
                db.toDoDao().update(toDo);
            });

            itemView.setOnClickListener(v -> editCallback.accept(toDo));
            itemView.findViewById(R.id.deleteButton).setOnClickListener(v -> deleteCallback.accept(toDo));
        }
    }
}

}
