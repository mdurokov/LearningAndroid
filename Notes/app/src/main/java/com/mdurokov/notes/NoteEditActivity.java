package com.mdurokov.notes;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class NoteEditActivity extends AppCompatActivity {

    EditText editText;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);

        editText = findViewById(R.id.editNote);
        position = getIntent().getIntExtra("position", MainActivity.notes.size());

        if(position != MainActivity.notes.size()){
            String note = MainActivity.notes.get(position);
            editText.setText(note);
        }
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(position == MainActivity.notes.size()){
                    MainActivity.notes.add(editText.getText().toString());
                } else {
                    MainActivity.notes.set(position, editText.getText().toString());
                }
                try {
                    MainActivity.sharedPreferences.edit().putString("notes", ObjectSerializer.serialize(MainActivity.notes)).apply();
                    MainActivity.adapter.notifyDataSetChanged();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}
