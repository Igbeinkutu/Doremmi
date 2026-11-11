package com.emerson.piano;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class NameThatNoteActivity extends PianoBaseActivity {

    ViewFlipper viewFlipper;
    Button next;
    Button previous;
    int counter = 1;
    int score = 0;
    TextView scoreDisplay;

    RadioButton rb1;
    RadioButton rb7;
    RadioButton rb9;
    RadioButton rb15;
    RadioButton rb20;


    public static final String mypreference = "mypref";
    public static final String nameNote = "nameKey";

    TextView time;
    ImageView badge;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_that_note);

        viewFlipper = (ViewFlipper)findViewById(R.id.viewFlipper);
        next = (Button)findViewById(R.id.next);
        previous = (Button)findViewById(R.id.prev);

        scoreDisplay = (TextView)findViewById(R.id.scoreid);

        rb1 = (RadioButton)findViewById(R.id.radioButton1);
        rb7 = (RadioButton)findViewById(R.id.radioButton7);
        rb9 = (RadioButton)findViewById(R.id.radioButton9);
        rb15 = (RadioButton)findViewById(R.id.radioButton15);
        rb20 = (RadioButton)findViewById(R.id.radioButton20);

        badge = (ImageView)findViewById(R.id.imageView);

        time = (TextView)findViewById(R.id.textView41);

        new CountDownTimer(20000, 1000) {

            public void onTick(long millisUntilFinished) {
                time.setText("Time: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                time.setText("done!");

                counter = 5;
                viewFlipper.setDisplayedChild( viewFlipper.indexOfChild(findViewById(R.id.question5)) );
                next_question(next);

            }
        }.start();

        SharedPreferences sharedPref3 = getSharedPreferences("nameNotePreference", Context.MODE_PRIVATE);

        String scoreText = sharedPref3.getString("namescore", "No score yet" );

        SharedPreferences sharedPref = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        String final_score = "Name that note last Score : "+ scoreText;
        editor.putString(nameNote, final_score);
        editor.commit();





    }

    public void next_question (View view){
        counter++;
        if( counter > 6){
            counter = 6;
            Intent intent = new Intent(this, home_screen.class);
            startActivity(intent);
        }
        else
            viewFlipper.showNext();

        if (counter == 6){

            if(rb1.isChecked()){
                score += 5;
            }
            if(rb7.isChecked()){
                score += 5;
            }
            if(rb9.isChecked()){
                score += 5;
            }
            if(rb15.isChecked()){
                score += 5;
            }
            if(rb20.isChecked()){
                score += 5;
            }

            previous.setEnabled(false);
            next.setEnabled(false);
            if (score < 11){
                badge.setImageResource(R.drawable.game_over);
                scoreDisplay.setText("Your score is " + score + "/25 Please Try again");
            } else {
                scoreDisplay.setText("Your score is " + score + "/25");
                badge.setImageResource(R.drawable.award1);

            }
        }
        int real_score = score;

        //scoreDisplay.setText("Your score is " + score + "/25");
        SharedPreferences sharedPref3 = getSharedPreferences("nameNotePreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor13 = sharedPref3.edit();
        String scoreToText = "" +  real_score + "/25";
        editor13.putString("namescore",scoreToText);
        editor13.commit();
;

        if (counter != 6){

            if(rb1.isChecked()){
                score += 0;
            }
            if(rb7.isChecked()){
                score += 0;
            }
            if(rb9.isChecked()){
                score += 0;
            }
            if(rb15.isChecked()){
                score += 0;
            }
            if(rb20.isChecked()){
                score += 0;
            }


        }


        //go back to Games page and update the score there


    }

    public void previous_question (View view){
        counter--;
        if (counter <= 0){
            counter = 1;

        }else if ( counter >= 0)
            viewFlipper.showPrevious();
    }


}
