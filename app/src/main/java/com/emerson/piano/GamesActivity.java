package com.emerson.piano;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class GamesActivity extends PianoBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);

    }
    public void name_that_note_intent(View view){
        Intent intent = new Intent(this, NameThatNoteActivity.class);
        startActivity(intent);
    }

    public void word_match_intent(View view){
        Intent intent = new Intent(this, WordMatchActivity.class);
        startActivity(intent);
    }

    public void note_maths_intent(View view){
        Intent intent = new Intent(this, NoteMathsActivity.class);
        startActivity(intent);
    }

    public void identify_note_intent(View view) {
        Intent intent = new Intent(this, IdentifyNoteActivity.class);
        startActivity(intent);
    }

    }
