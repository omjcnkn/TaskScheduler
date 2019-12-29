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
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ItemNoteActivity extends AppCompatActivity {
    private RecyclerView rvItemsNotes;
    private FloatingActionButton addNewNotesItemFab;

    private DatabaseHelper dbh;
    private ItemsAdapter adapter;

    private String currentListName;
    private String currentListType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_note);

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
    }

    private void initUI() {
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#232931")));

        getSupportActionBar().setTitle(currentListName);

        rvItemsNotes = findViewById(R.id.itemsNoteRecyclerView);
        addNewNotesItemFab = findViewById(R.id.addNewNoteActionButton);

        Cursor c = dbh.getNoteListItems(currentListName);
        adapter = new ItemsAdapter(this, c);
        rvItemsNotes.setAdapter(adapter);
        rvItemsNotes.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvItemsNotes.addItemDecoration(new DividerItemDecoration(rvItemsNotes.getContext(),
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
        }).attachToRecyclerView(rvItemsNotes);

        /* Setting the Add Fab button listener */
        addNewNotesItemFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemNoteActivity.this, EditNoteActivity.class);
                intent.putExtra("MODE", "create");
                intent.putExtra("LIST", currentListName);
                startActivityForResult(intent, 1);
            }
        });

    }


    public void deleteSelectedItem(String itemName) {
        try {
            dbh.removeNoteListItem(itemName);
            Cursor c = dbh.getNoteListItems(currentListName);
            adapter.swapCursor(c);
        } catch(SQLException ex) {
            Toast.makeText(getApplicationContext(), "Couldn't delete item", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1) {
            if(resultCode == RESULT_OK) {
                try {
                    Cursor c = dbh.getNoteListItems(currentListName);
                    adapter.swapCursor(c);
                } catch(SQLException ex) {
                    Toast.makeText(getApplicationContext(), "Something went wrong, try entering different Note Title", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {
        private Context context;
        private Cursor cursor;

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
            public TextView itemNameTextView;
            public ImageView deleteImageView;


            @SuppressLint("ResourceType")
            public ViewHolder(final View itemView) {
                super(itemView);

                itemNameTextView = itemView.findViewById(R.id.itemNoteTitleTextView);
                deleteImageView = itemView.findViewById(R.id.deleteNoteImageView);

                deleteImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteSelectedItem(itemNameTextView.getText().toString());
                    }
                });

                itemView.setOnLongClickListener(this);
            }

            @Override
            public boolean onLongClick(View view) {
                Intent editNoteIntent = new Intent(ItemNoteActivity.this, EditNoteActivity.class);
                editNoteIntent.putExtra("MODE", "edit");
                editNoteIntent.putExtra("LIST", currentListName);
                editNoteIntent.putExtra("OLD", itemNameTextView.getText().toString());
                startActivityForResult(editNoteIntent, 1);

                return true;
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

            View itemView = inflater.inflate(R.layout.items_note_layout, parent, false);

            ViewHolder holder = new ViewHolder(itemView);

            return holder;
        }

        @Override
        public void onBindViewHolder(ItemsAdapter.ViewHolder holder, int position) {
            if(cursor.moveToPosition(position)) {
                createNewNote(holder);
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

        public void createNewNote(ItemsAdapter.ViewHolder holder) {
            holder.itemNameTextView.setText(cursor.getString(cursor.getColumnIndex("NoteTitle")));
            holder.deleteImageView.setImageResource(R.drawable.delete_button);
        }
    }
}
