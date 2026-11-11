package com.emerson.piano;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class ScoresActivity extends PianoBaseActivity {


    TextView name_note_score;
    TextView identify_note_score;
    TextView note_maths_score;
    TextView word_match_score;

    public static final String mypreference = "mypref";
    public static final String nameNote = "nameKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);


        name_note_score = (TextView)findViewById(R.id.textView48);
        identify_note_score = (TextView)findViewById(R.id.textView49);
        note_maths_score = (TextView)findViewById(R.id.textView50);
        word_match_score = (TextView)findViewById(R.id.textView51);

        //name note game data
        SharedPreferences sharedPref = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        String score = sharedPref.getString(nameNote, "No score yet" );
        name_note_score.setText(score);

        //identify note game data
        SharedPreferences sharedPref10 = getSharedPreferences("identifyNotePreference", Context.MODE_PRIVATE);
        String score10 = sharedPref10.getString("identifyNote", "No score yet" );
        identify_note_score.setText(score10);

        //word match game data
        SharedPreferences sharedPref20 = getSharedPreferences("wordMatchPreference", Context.MODE_PRIVATE);
        String score20 = sharedPref20.getString("wordMatch", "No score yet" );
        word_match_score.setText(score20);

        //note math game data
        SharedPreferences sharedPref30 = getSharedPreferences("noteMathsPreference", Context.MODE_PRIVATE);
        String score30 = sharedPref30.getString("noteMaths", "No score yet" );
        note_maths_score.setText(score30);
    }
}
