package com.ebisoft.doremmi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class GamesActivity extends PianoBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);
    }

    public void name_that_note_intent(View view) {
        startActivity(new Intent(this, NameThatNoteActivity.class));
    }

    public void word_match_intent(View view) {
        startActivity(new Intent(this, WordMatchActivity.class));
    }

    public void note_maths_intent(View view) {
        startActivity(new Intent(this, NoteMathsActivity.class));
    }

    public void identify_note_intent(View view) {
        startActivity(new Intent(this, IdentifyNoteActivity.class));
    }
}
