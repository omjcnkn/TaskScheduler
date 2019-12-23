package com.example.taskschedulerproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;

public class ItemsActivity extends AppCompatActivity {
    private ItemList userSelectedList;

    private RecyclerView rvItems;
    private FloatingActionButton addNewListItemFab;

    private ItemsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        initLogic();
        initUI();
    }

    private void initLogic() {
        userSelectedList = (ItemList)getIntent().getSerializableExtra("SELECTEDLIST");
    }

    private void initUI() {
        rvItems = findViewById(R.id.itemsRecyclerView);
        addNewListItemFab = findViewById(R.id.addNewTaskActionButton);

        ArrayList<Item> items = userSelectedList.getItems();

        adapter = new ItemsAdapter(items);
        rvItems.setAdapter(adapter);
        rvItems.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvItems.addItemDecoration(new DividerItemDecoration(rvItems.getContext(),
                DividerItemDecoration.VERTICAL));

        Log.e("BEFORE", "HENA");
        /* Setting the Add Fab button listener */
        addNewListItemFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userSelectedList instanceof CheckList) {
                    Log.e("FAB", "Button Pressed");
                    CheckList taskList = (CheckList) userSelectedList;
                    taskList.addItem("Task", "task", null, 1, 2, null);
                    adapter.notifyItemInserted(taskList.getItems().size() - 1);
                }
            }
        });
    }

    /* Creating RecyclerView Adapter and ViewHolder */
    public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {
        private ArrayList<Item> userSelectedListItems;

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public TextView itemNameTextView;
            public TextView itemDueDateTextView;
            public ImageView priorityImageView;
            public ImageView checkImageView;
            public ImageView deleteImageView;


            public ViewHolder(View itemView) {
                super(itemView);

                itemNameTextView = itemView.findViewById(R.id.itemNameTextView);
                itemDueDateTextView = itemView.findViewById(R.id.itemDueDateTextView);
                checkImageView = itemView.findViewById(R.id.checkImageView);
                deleteImageView = itemView.findViewById(R.id.deleteImageView);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
//                Toast.makeText(getContext(), "Item" + getAdapterPosition(), Toast.LENGTH_LONG).show();
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
    }
}
