package com.example.taskschedulerproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private UserBoard userBoard;

    private RecyclerView rvLists;
    private FloatingActionButton addNewListFab;

    private ListsAdapter adapter;
    ArrayList<ItemList> allCreatedLists;
    private DatabaseHelper dbh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createDataBase();
        initLogic();
        initUI();
    }

    private void createDataBase(){
        dbh = new DatabaseHelper(getApplicationContext());

    }
    private void initLogic() {
        userBoard = new UserBoard("Mahmoud Ahmed Khalil");
    }

    private void initUI() {
        addNewListFab = findViewById(R.id.addNewListFab);
        rvLists = findViewById(R.id.listsContainer);

        /* Setting up the RecyclerView */
        allCreatedLists = userBoard.getLists();

        adapter = new ListsAdapter(allCreatedLists);

        rvLists.setAdapter(adapter);
        rvLists.setLayoutManager(new LinearLayoutManager(getApplicationContext()));



        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        });



        /* Setting up the Add Fab Listener */
        addNewListFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userBoard.createNewCheckList("Notes List "+ allCreatedLists.size() );
                adapter.notifyItemInserted(userBoard.getLists().size() - 1);
            }
        });
    }

    public class ListsAdapter extends RecyclerView.Adapter<ListsAdapter.ViewHolder> {
        private ArrayList<ItemList> createdLists;

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public TextView listNameTextView, creationDateTextView;
            public ImageView deleteImageButton;

            public ViewHolder(View itemView) {
                super(itemView);

                listNameTextView = itemView.findViewById(R.id.listNameTextView);
                creationDateTextView = itemView.findViewById(R.id.creationDateTextView);

                deleteImageButton = itemView.findViewById(R.id.deleteImageButton);

                deleteImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        userBoard.removeListByIndex(getAdapterPosition());
                        adapter.notifyItemRemoved(getAdapterPosition());
                    }
                });

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                Intent selectedListIntent = new Intent(MainActivity.this, ItemsActivity.class);
                selectedListIntent.putExtra("SELECTEDLIST", userBoard.getListByIndex(getAdapterPosition()));
                startActivity(selectedListIntent);
            }
        }


        public ListsAdapter(ArrayList<ItemList> createdLists) {
            this.createdLists = createdLists;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the custom layout
            View cardListView = inflater.inflate(R.layout.card_view_layout, parent, false);

            // Return a new holder instance
            ViewHolder viewHolder = new ViewHolder(cardListView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.listNameTextView.setText(createdLists.get(position).getListTitle());
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy/MM/dd 'at' HH:mm:ss");
            Date date = new Date(System.currentTimeMillis());
            holder.creationDateTextView.setText("Created in "+formatter.format(date));
            holder.deleteImageButton.setImageResource(R.drawable.delete_button);
        }

        @Override
        public int getItemCount() {
            return createdLists.size();
        }
    }

}
