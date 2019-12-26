package com.example.taskschedulerproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ItemsActivity extends AppCompatActivity {
    private RecyclerView rvItems;
    private FloatingActionButton addNewListItemFab;

    private ItemsAdapter adapter;
    ArrayList<Item> items;

    private DatabaseHelper dbh;

    private String currentListName;
    private String currentListType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        createDatabase();
        initLogic();
        initUI();
    }

    private void createDatabase(){
        dbh = new DatabaseHelper(getApplicationContext());
    }

    private void initLogic() {
        currentListName = getIntent().getStringExtra("ListName");
        currentListType = getIntent().getStringExtra("ListType");

        Log.e("List_Name ", currentListName);
        Log.e("List_Type ", currentListType);
    }

    private void initUI() {
        getSupportActionBar().setTitle(currentListName);

        rvItems = findViewById(R.id.itemsRecyclerView);
        addNewListItemFab = findViewById(R.id.addNewTaskActionButton);

        Cursor c;
        if(currentListType.equalsIgnoreCase(DatabaseHelper.CHECK_LIST_TYPE)) {
            c = dbh.getCheckListItems(currentListName);
        } else {
            c = dbh.getNoteListItems(currentListName);
        }
        adapter = new ItemsAdapter(this, c);
        rvItems.setAdapter(adapter);
        rvItems.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvItems.addItemDecoration(new DividerItemDecoration(rvItems.getContext(),
                DividerItemDecoration.VERTICAL));
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                deleteSelectedItem(((ItemsAdapter.ViewHolder)viewHolder).itemNameTextView.getText().toString());
            }
        }).attachToRecyclerView(rvItems);

        /* Setting the Add Fab button listener */
        addNewListItemFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                try {
//                    if (currentListType.equalsIgnoreCase(DatabaseHelper.CHECK_LIST_TYPE)) {
//                        dbh.insertTaskItem(currentListName, "New Task", "New Task", "25/12", 3, "30:00", "25/12");
//                        Cursor cursor = dbh.getCheckListItems(currentListName);
//                        adapter.swapCursor(cursor);
//                    } else if(currentListType.equalsIgnoreCase(DatabaseHelper.NOTES_LIST_TYPE)) {
//                        dbh.insertNoteItem(currentListName, "New Note", "new Note", "25/12");
//                        Cursor cursor = dbh.getNoteListItems(currentListName);
//                        adapter.swapCursor(cursor);
//                    }
//                } catch(SQLException ex) {
//                    Toast.makeText(ItemsActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
//                }

                if (currentListType.equalsIgnoreCase(DatabaseHelper.CHECK_LIST_TYPE)) {
                    Intent intent = new Intent(ItemsActivity.this, EditTaskActivity.class);
                    intent.putExtra("MODE", "create");
                    intent.putExtra("LIST", currentListName);
                    startActivityForResult(intent, 1);
                } else if (currentListType.equalsIgnoreCase(DatabaseHelper.NOTES_LIST_TYPE)) {
                }
            }
        });

    }

    public void deleteSelectedItem(String itemName) {
        if(currentListType.equalsIgnoreCase(DatabaseHelper.CHECK_LIST_TYPE)) {
            dbh.removeCheckListItem(itemName);
            Cursor c = dbh.getCheckListItems(currentListName);
            adapter.swapCursor(c);
        } else {
            dbh.removeNoteListItem(itemName);
            Cursor c = dbh.getNoteListItems(currentListName);
            adapter.swapCursor(c);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1) {
            if(resultCode == RESULT_OK) {
                if (currentListType.equalsIgnoreCase(DatabaseHelper.CHECK_LIST_TYPE)) {
                    Cursor c = dbh.getCheckListItems(currentListName);
                    adapter.swapCursor(c);
                } else if (currentListType.equalsIgnoreCase(DatabaseHelper.NOTES_LIST_TYPE)) {
                    Cursor c = dbh.getNoteListItems(currentListName);
                    adapter.swapCursor(c);
                }
            }
        }
    }

    /* Creating RecyclerView Adapter and ViewHolder */
    public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {
        private Context context;
        private Cursor cursor;

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public TextView itemNameTextView;
            public TextView itemDueDateTextView;
            public ImageView priorityImageView;
            public ImageView checkImageView;
            public ImageView deleteImageView;


            @SuppressLint("ResourceType")
            public ViewHolder(final View itemView) {
                super(itemView);

                itemNameTextView = itemView.findViewById(R.id.itemNameTextView);
                itemDueDateTextView = itemView.findViewById(R.id.itemDueDateTextView);
                checkImageView = itemView.findViewById(R.id.checkImageView);
                deleteImageView = itemView.findViewById(R.id.deleteImageView);
                priorityImageView =itemView.findViewById(R.id.priorityImageView);

                checkImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Cursor c = dbh.getTaskItem(itemNameTextView.getText().toString());
                            c.moveToFirst();
                            String listName = c.getString(c.getColumnIndex("List"));
                            String checkListName = c.getString(c.getColumnIndex("TaskTitle"));
                            String checkListDescription = c.getString(c.getColumnIndex("TaskDescription"));
                            String creationDate = c.getString(c.getColumnIndex("TaskDate"));
                            int priority = c.getInt(c.getColumnIndex("TaskPriority"));
                            String duration = c.getString(c.getColumnIndex("TaskDuration"));
                            String deadline = c.getString(c.getColumnIndex("TaskDeadline"));
                            int checked = c.getInt(c.getColumnIndex("TaskChecked"));

                            if(checked == 0) {
                                checked = 1;
                            } else {
                                checked = 0;
                            }

                            dbh.updateCheckListItem(checkListName, listName, checkListName, checkListDescription,
                                    creationDate, priority, duration, deadline, checked);

                            c = dbh.getCheckListItems(currentListName);
                            adapter.swapCursor(c);
                        } catch(SQLException ex) {
                            Toast.makeText(ItemsActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

                deleteImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteSelectedItem(itemNameTextView.getText().toString());
                    }
                });

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
//                lastAdapterPosition = getAdapterPosition();
//                Item item = userSelectedList.getItemByIndex(getAdapterPosition());
//
//                if(item instanceof TaskItem) {
//                    TaskItem taskItem = (TaskItem) item;
                    Intent editTaskIntent = new Intent(ItemsActivity.this, EditTaskActivity.class);
//                    editTaskIntent.putExtra("SELECTEDTASK", taskItem);
                    startActivity(editTaskIntent);
//                }
            }


        }

        public ItemsAdapter(Context context, Cursor cursor) {
            this.context = context;
            this.cursor = cursor;
        }

        @Override
        public ItemsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();

            LayoutInflater inflater = LayoutInflater.from(context);

            View itemView = inflater.inflate(R.layout.items_layout, parent, false);

            ViewHolder holder = new ViewHolder(itemView);

            return holder;
        }

        @Override
        public void onBindViewHolder(ItemsAdapter.ViewHolder holder, int position) {
            if(cursor.moveToPosition(position)) {
                if(currentListType.equalsIgnoreCase(DatabaseHelper.CHECK_LIST_TYPE)) {
                    createNewTask(holder);
                } else {
                    createNewNote(holder);
                }
            }
        }

        @Override
        public int getItemCount() {
            return cursor.getCount();
        }

        public void swapCursor(Cursor newCursor) {
            if(cursor != null) {
                cursor.close();
            }

            cursor = newCursor;

            if(newCursor != null) {
                notifyDataSetChanged();
            }
        }

        public void createNewTask(ItemsAdapter.ViewHolder holder) {
            holder.itemNameTextView.setText(cursor.getString(cursor.getColumnIndex("TaskTitle")));
            holder.itemDueDateTextView.setText(cursor.getString(cursor.getColumnIndex("TaskDeadline")));
            int priority = Integer.parseInt(cursor.getString(cursor.getColumnIndex("TaskPriority")));

            switch(priority) {
                case 1:
                    holder.priorityImageView.setImageResource(R.drawable.high_priority);
                    break;
                case 2:
                    holder.priorityImageView.setImageResource(R.drawable.medium_priority);
                    break;
                case 3:
                    holder.priorityImageView.setImageResource(R.drawable.low_priority);
                    break;
            }

            holder.checkImageView.setImageResource(R.drawable.check_mark);
            holder.deleteImageView.setImageResource(R.drawable.delete_button);

            if(cursor.getString(cursor.getColumnIndex("TaskChecked")).equalsIgnoreCase("1")) {
                holder.itemNameTextView.setPaintFlags(holder.itemNameTextView.getPaintFlags() |
                        Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                holder.itemNameTextView.setPaintFlags(holder.itemNameTextView.getPaintFlags() &
                        (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
        }

        public void createNewNote(ItemsAdapter.ViewHolder holder) {
            holder.itemDueDateTextView.setVisibility(View.INVISIBLE);
            holder.priorityImageView.setVisibility(View.INVISIBLE);
            holder.checkImageView.setVisibility(View.INVISIBLE);

            holder.itemDueDateTextView.setEnabled(false);
            holder.priorityImageView.setEnabled(false);
            holder.checkImageView.setEnabled(false);

            holder.itemNameTextView.setText(cursor.getString(cursor.getColumnIndex("NoteTitle")));
            holder.deleteImageView.setImageResource(R.drawable.delete_button);
        }
    }
}
