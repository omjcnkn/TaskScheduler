package com.example.taskschedulerproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import java.util.Date;

public class ItemsActivity extends AppCompatActivity {
    private ItemList userSelectedList;

    private RecyclerView rvItems;
    private FloatingActionButton addNewListItemFab;
    DatabaseHelper dph;
    private ItemsAdapter adapter;
    ArrayList<Item> items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        createDatabase();
        initLogic();
        initUI();
    }
    private void createDatabase(){
        dph = new DatabaseHelper(getApplicationContext());

    }
    private void initLogic() {
        userSelectedList = (ItemList)getIntent().getSerializableExtra("SELECTEDLIST");
    }

    private void initUI() {
        rvItems = findViewById(R.id.itemsRecyclerView);
        addNewListItemFab = findViewById(R.id.addNewTaskActionButton);

        items = userSelectedList.getItems();

        adapter = new ItemsAdapter(items);
        rvItems.setAdapter(adapter);
        rvItems.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvItems.addItemDecoration(new DividerItemDecoration(rvItems.getContext(),
                DividerItemDecoration.VERTICAL));

        /* Setting the Add Fab button listener */
        addNewListItemFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userSelectedList instanceof CheckList) {
                    CheckList taskList = (CheckList) userSelectedList;
                    taskList.addItem("New Task", "task", null, 1, 2, null);
                    adapter.notifyDataSetChanged();
                    adapter.notifyItemInserted(taskList.getItems().size() - 1);
                    dph.insertTaskItem();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            if(requestCode == 1) {
                TaskItem editedTask = (TaskItem)data.getSerializableExtra("Edited Task");
                items.set(adapter.getLastAdapterPosition(),editedTask);
                adapter.notifyItemChanged(adapter.getLastAdapterPosition());
            }
        }
    }

    /* Creating RecyclerView Adapter and ViewHolder */
    public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {
        private ArrayList<Item> userSelectedListItems;
        private int lastAdapterPosition;

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public TextView itemNameTextView;
            public TextView itemDueDateTextView;
            public ImageView priorityImageView;
            public ImageView checkImageView;
            public ImageView deleteImageView;


            @SuppressLint("ResourceType")
            public ViewHolder(View itemView) {
                super(itemView);

                itemNameTextView = itemView.findViewById(R.id.itemNameTextView);
                itemDueDateTextView = itemView.findViewById(R.id.itemDueDateTextView);
                checkImageView = itemView.findViewById(R.id.checkImageView);
                deleteImageView = itemView.findViewById(R.id.deleteImageView);
                priorityImageView =itemView.findViewById(R.id.priorityImageView);

                checkImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TaskItem taskItem = (TaskItem) userSelectedList.getItemByIndex(getAdapterPosition());
                        taskItem.setChecked(!taskItem.isChecked());
                        adapter.notifyItemChanged(getAdapterPosition());
                    }
                });

                deleteImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        userSelectedList.removeItemByIndex(getAdapterPosition());
                        adapter.notifyItemRemoved(getAdapterPosition());
                    }
                });

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                lastAdapterPosition = getAdapterPosition();
                Item item = userSelectedList.getItemByIndex(getAdapterPosition());

                if(item instanceof TaskItem) {
                    TaskItem taskItem = (TaskItem) item;
                    Intent editTaskIntent = new Intent(ItemsActivity.this, EditTaskActivity.class);
                    editTaskIntent.putExtra("SELECTEDTASK", taskItem);
                    startActivityForResult(editTaskIntent, 1);
                }
            }


        }

        public ItemsAdapter(ArrayList<Item> userSelectedListItems) {
            this.userSelectedListItems = userSelectedListItems;
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
            Item item = userSelectedListItems.get(position);

            holder.itemNameTextView.setText(item.getTitle());
            holder.itemDueDateTextView.setText(item.getTitle());
//            holder.priorityImageView.setImageResource(R.drawable.medium_priority);
            if(item instanceof TaskItem) {
                TaskItem taskItem = (TaskItem) item;
                if(taskItem.isChecked()) {
                    holder.itemNameTextView.setPaintFlags(holder.itemNameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
            }
        }

        @Override
        public int getItemCount() {
            return userSelectedListItems.size();
        }

        public int getLastAdapterPosition() {
            return lastAdapterPosition;
        }
    }
}
