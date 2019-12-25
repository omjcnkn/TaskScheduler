package com.example.taskschedulerproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvLists;
    private FloatingActionButton addNewListFab;
    private EditText listNameEditText;
    private ImageButton checkListImageButton;

    private ListsAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

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

    }

    private void initUI() {
        addNewListFab = findViewById(R.id.addNewListFab);
        rvLists = findViewById(R.id.listsContainer);
        listNameEditText = findViewById(R.id.listNameEditText);
        checkListImageButton = findViewById(R.id.checkListImageButton);

        checkListImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkListImageButton.getTag().toString().equalsIgnoreCase("0")) {
                    checkListImageButton.setImageResource(R.drawable.noteslist);
                    checkListImageButton.setTag("1");
                } else if(checkListImageButton.getTag().toString().equalsIgnoreCase("1")) {
                    checkListImageButton.setImageResource(R.drawable.checklist);
                    checkListImageButton.setTag("0");
                }
            }
        });

        /* Setting up the RecyclerView */
        adapter = new ListsAdapter(this, dbh.getAllLists());

        rvLists.setAdapter(adapter);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvLists.setLayoutManager(linearLayoutManager);
        rvLists.scrollToPosition(adapter.getItemCount() - 1);

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
                /* Inserting in the db */
                try {
                    if(checkListImageButton.getTag().toString().equalsIgnoreCase("0")) {
                        dbh.insertCheckList(listNameEditText.getText().toString(), "current date");
                    } else {
                        dbh.insertNotesList(listNameEditText.getText().toString(), "current date");
                    }
                    
                    Cursor c = dbh.getAllLists();
                    adapter.swapCursor(c);
                } catch(SQLException ex) {
                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                }

                /* Hiding keyboard */
                View view = MainActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                /* Showing last items */
                rvLists.scrollToPosition(adapter.getItemCount() - 1);
            }
        });
    }

    public class ListsAdapter extends RecyclerView.Adapter<ListsAdapter.ViewHolder> {
        private Context context;
        private Cursor cursor;

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
                    }
                });

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                Intent selectedListIntent = new Intent(MainActivity.this, ItemsActivity.class);
                cursor.moveToPosition(getAdapterPosition());
                String listName = cursor.getString(cursor.getColumnIndex("ListName"));
                String listType = cursor.getString(cursor.getColumnIndex("ListType"));
                selectedListIntent.putExtra("ListName", listName);
                selectedListIntent.putExtra("ListType", listType);
                startActivity(selectedListIntent);
            }
        }


        public ListsAdapter(Context context, Cursor cursor) {
            this.context = context;
            this.cursor = cursor;
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
            if(cursor.moveToPosition(position)) {
                holder.listNameTextView.setText(cursor.getString(cursor.getColumnIndex("ListName")));
                holder.creationDateTextView.setText(cursor.getString(cursor.getColumnIndex("CreationDate")));
            } else {
                return;
            }
        }

        @Override
        public int getItemCount() {
            return cursor.getCount();
        }

        public void swapCursor(Cursor newCursor) {
            if(this.cursor != null) {
                cursor.close();
            }

            this.cursor = newCursor;

            if(newCursor != null) {
                notifyDataSetChanged();
            }
        }
    }

}
