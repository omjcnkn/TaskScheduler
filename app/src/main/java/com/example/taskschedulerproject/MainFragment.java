package com.example.taskschedulerproject;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainFragment extends Fragment {

    private RecyclerView rvLists;
    private FloatingActionButton addNewListFab;
    private EditText listNameEditText;
    private ImageButton checkListImageButton;

    private ListsAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    private DatabaseHelper dbh;

    private SimpleDateFormat stf;

    private boolean newList = true;
    private String selectedList = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_main, container , false);
        createDataBase();
        initLogic();
        initUI(view);

        return view;


    }


    private void createDataBase(){
        dbh = new DatabaseHelper(getActivity().getApplicationContext());
    }

    private void initLogic() {
        stf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    }

    private void initUI(View view) {
        getActivity().setTitle("Task Scheduler");

        addNewListFab = view.findViewById(R.id.addNewListFab);
        rvLists = view.findViewById(R.id.listsContainer);
        listNameEditText = view.findViewById(R.id.listNameEditText);
        checkListImageButton = view.findViewById(R.id.checkListImageButton);

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
        adapter = new ListsAdapter(getContext(), dbh.getAllLists());

        rvLists.setAdapter(adapter);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rvLists.setLayoutManager(linearLayoutManager);
        rvLists.scrollToPosition(adapter.getItemCount() - 1);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                deleteSelectedList(((ListsAdapter.ViewHolder)viewHolder).listNameTextView.getText().toString());
            }
        }).attachToRecyclerView(rvLists);



        /* Setting up the Add Fab Listener */
        addNewListFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listNameEditText.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "You must enter your list name", Toast.LENGTH_LONG).show();
                    return;
                }
                
                try {
                    /* Editting selected list */
                    if(newList == false) {
                        if(checkListImageButton.getTag().toString().equalsIgnoreCase("0")) {
                            dbh.updateCheckList(selectedList, listNameEditText.getText().toString(), stf.format(new Date()));
                        } else {
                            dbh.updateNoteList(selectedList, listNameEditText.getText().toString(), stf.format(new Date()));
                        }

                        newList = true;
                        selectedList = "";
                    } else {

                        /* Inserting in the db */
                        if (checkListImageButton.getTag().toString().equalsIgnoreCase("0")) {
                            dbh.insertCheckList(listNameEditText.getText().toString(), stf.format(new Date()));
                        } else {
                            dbh.insertNotesList(listNameEditText.getText().toString(), stf.format(new Date()));
                        }
                    }

                    Cursor c = dbh.getAllLists();
                    adapter.swapCursor(c);
                } catch(SQLException ex) {
                    Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                }

                /* Hiding keyboard */
                View view = getActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                /* Showing last items */
                rvLists.scrollToPosition(adapter.getItemCount() - 1);

                listNameEditText.setText("");
            }
        });
    }

    public void updateIsNewList(String listName) {
        newList = false;
        selectedList = listName;
    }

    public void deleteSelectedList(String listName) {
        dbh.removeList(listName);
        Cursor c = dbh.getAllLists();
        adapter.swapCursor(c);
    }


    public class ListsAdapter extends RecyclerView.Adapter<ListsAdapter.ViewHolder> {
        private Context context;
        private Cursor cursor;

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
            public TextView listNameTextView, creationDateTextView;
            public ImageView deleteImageButton, listTypeImageView;

            public ViewHolder(View itemView) {
                super(itemView);

                listNameTextView = itemView.findViewById(R.id.listNameTextView);
                creationDateTextView = itemView.findViewById(R.id.creationDateTextView);

                deleteImageButton = itemView.findViewById(R.id.deleteImageButton);
                listTypeImageView = itemView.findViewById(R.id.listTypeImageView);

                deleteImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteSelectedList(listNameTextView.getText().toString());
                    }
                });

                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
            }

            @Override
            public void onClick(View v) {
                Intent selectedListIntent = new Intent(getContext(), ItemsActivity.class);
                cursor.moveToPosition(getAdapterPosition());
                String listName = cursor.getString(cursor.getColumnIndex("ListName"));
                String listType = cursor.getString(cursor.getColumnIndex("ListType"));
                selectedListIntent.putExtra("ListName", listName);
                selectedListIntent.putExtra("ListType", listType);
                startActivity(selectedListIntent);
            }

            @Override
            public boolean onLongClick(View v) {
                String selected = listNameTextView.getText().toString();
                Cursor c = dbh.getList(selected);
                c.moveToFirst();

                listNameEditText.setText(c.getString(c.getColumnIndex("ListName")));
                if(c.getString(c.getColumnIndex("ListType")).equalsIgnoreCase(DatabaseHelper.CHECK_LIST_TYPE)) {
                    checkListImageButton.setImageResource(R.drawable.checklist);
                    checkListImageButton.setTag("0");
                } else {
                    checkListImageButton.setImageResource(R.drawable.noteslist);
                    checkListImageButton.setTag("1");
                }

                updateIsNewList(selected);
                return true;
            }
        }


        public ListsAdapter(Context context, Cursor cursor) {
            this.context = context;
            this.cursor = cursor;
        }

        @NonNull
        @Override
        public ListsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the custom layout
            View cardListView = inflater.inflate(R.layout.card_view_layout, parent, false);

            // Return a new holder instance
            ListsAdapter.ViewHolder viewHolder = new ListsAdapter.ViewHolder(cardListView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ListsAdapter.ViewHolder holder, int position) {
            if(cursor.moveToPosition(position)) {
                holder.listNameTextView.setText(cursor.getString(cursor.getColumnIndex("ListName")));
                holder.creationDateTextView.setText(cursor.getString(cursor.getColumnIndex("CreationDate")));
                holder.itemView.setTag(cursor.getString(cursor.getColumnIndex("Id")));
                holder.deleteImageButton.setImageResource(R.drawable.delete_button);

                if(cursor.getString(cursor.getColumnIndex("ListType")).equalsIgnoreCase(DatabaseHelper.CHECK_LIST_TYPE)) {
                    holder.listTypeImageView.setImageResource(R.drawable.checklist);
                } else {
                    holder.listTypeImageView.setImageResource(R.drawable.noteslist);
                }
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
