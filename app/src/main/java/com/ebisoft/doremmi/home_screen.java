package com.ebisoft.doremmi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class home_screen extends PianoBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
    }

    public void lesson_intent(View view){
        Intent intent = new Intent(this, LessonSelectorActivity.class);
        startActivity(intent);
    }

    public void free_Style_intent(View view){
        Intent intent = new Intent(this, FreeStyleActivity.class);
        startActivity(intent);
    }

    public void songs_intent(View view){

        Intent intent = new Intent(this, SongsActivity.class);
        startActivity(intent);
    }

    public void games_intent(View view){

        Intent intent = new Intent(this, GamesActivity.class);
        startActivity(intent);
    }

    public void scores_intent(View view){

        Intent intent = new Intent(this, ScoresActivity.class);
        startActivity(intent);
    }
}
