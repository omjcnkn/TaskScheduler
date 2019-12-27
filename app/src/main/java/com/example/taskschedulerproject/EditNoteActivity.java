package com.example.taskschedulerproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditNoteActivity extends AppCompatActivity {
    private EditText titleEditText, descriptionEditText;
    private FloatingActionButton finishButton;

    private DatabaseHelper dbh;

    private String intentTag = "";
    private String intentListName = "";

    private SimpleDateFormat stf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        dbh = new DatabaseHelper(getApplicationContext());
        stf = new SimpleDateFormat("yyyy/MM/dd HH:mm");

        intentTag = getIntent().getStringExtra("MODE");
        intentListName = getIntent().getStringExtra("LIST");

        titleEditText = findViewById(R.id.NoteTitleEdittext);
        descriptionEditText = findViewById(R.id.NoteDiscriptionEdittext);
        finishButton = findViewById(R.id.NoteFAB);

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(titleEditText.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(),"No Title written, Please go set a titleEditText",Toast.LENGTH_SHORT).show();
                if(descriptionEditText.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(),"No Descrtption written, Please go set a descritpion",Toast.LENGTH_SHORT).show();
                if(!titleEditText.getText().toString().isEmpty() && !descriptionEditText.getText().toString().isEmpty())
                    finished();


            }
        });

        if(intentTag.equalsIgnoreCase("edit")) {
            loadInfo();
        }

    }

    private void loadInfo() {
        String title = getIntent().getStringExtra("OLD");
        Cursor c = dbh.getNoteItem(title);
        c.moveToFirst();

        titleEditText.setText(title);
        descriptionEditText.setText(c.getString(c.getColumnIndex("NoteDescription")));
    }

    public void finished() {
        String title = titleEditText.getText().toString();
        String description = descriptionEditText.getText().toString();

        try {
            if (intentTag.equalsIgnoreCase("create")) {
                dbh.insertNoteItem(intentListName, title, description, stf.format(new Date()));
            } else {
                dbh.updateNoteListItem(getIntent().getStringExtra("OLD"), intentListName,
                        title, description, stf.format(new Date()));
            }

            setResult(RESULT_OK);
        } catch(SQLException ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            setResult(RESULT_CANCELED);
        }

        finish();
    }
}
