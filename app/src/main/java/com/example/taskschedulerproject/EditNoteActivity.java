package com.example.taskschedulerproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EditNoteActivity extends AppCompatActivity {
    EditText title,desc;
    FloatingActionButton finishButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        title = findViewById(R.id.NoteTitleEdittext);
        desc = findViewById(R.id.NoteDiscriptionEdittext);
        finishButton = findViewById(R.id.NoteFAB);


        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(title.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(),"No Title written, Please go set a title",Toast.LENGTH_SHORT).show();
                if(desc.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(),"No Descrtption written, Please go set a descritpion",Toast.LENGTH_SHORT).show();
                if(!title.getText().toString().isEmpty() && !desc.getText().toString().isEmpty())
                    finished();


            }
        });
    }
    public void finished(){
        finish();
    }
}
