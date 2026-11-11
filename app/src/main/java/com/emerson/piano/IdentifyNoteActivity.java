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

public class IdentifyNoteActivity extends PianoBaseActivity {

    ViewFlipper viewFlipper;
    Button next;
    Button previous;
    int counter = 1;
    int score = 0;
    TextView scoreDisplay;

    RadioButton rb19;
    RadioButton rb21;
    RadioButton rb27;
    RadioButton rb31;
    RadioButton rb38;
    RadioButton rb33;
    RadioButton rb44;
    RadioButton rb45;

    TextView time;
    public static final String mypreference = "mypref";
    public static final String TAG_NAME = "nameKey";
    public static final String TAG_AGE = "emailKey";

    ImageView badge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify_note);

        viewFlipper = (ViewFlipper)findViewById(R.id.viewFlipper);
        next = (Button)findViewById(R.id.next);
        previous = (Button)findViewById(R.id.prev);

        scoreDisplay = (TextView)findViewById(R.id.scoreid);

        rb19 = (RadioButton)findViewById(R.id.radioButton19);
        rb21 = (RadioButton)findViewById(R.id.radioButton21);
        rb27 = (RadioButton)findViewById(R.id.radioButton27);
        rb31 = (RadioButton)findViewById(R.id.radioButton31);
        rb38 = (RadioButton)findViewById(R.id.radioButton38);
        rb33 = (RadioButton)findViewById(R.id.radioButton33);
        rb44 = (RadioButton)findViewById(R.id.radioButto44);
        rb45 = (RadioButton)findViewById(R.id.radioButton45);

        time = (TextView)findViewById(R.id.textView41);
        badge = (ImageView)findViewById(R.id.imageView);


        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                time.setText("Time: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                time.setText("done!");

                counter = 8;
                viewFlipper.setDisplayedChild( viewFlipper.indexOfChild(findViewById(R.id.question12)) );
                next_question(next);

            }
        }.start();
        SharedPreferences sharedPref1 = getSharedPreferences("identifyNotePreference_score", Context.MODE_PRIVATE);

        String scoreText = sharedPref1.getString("identifyscore", "No score yet" );

        SharedPreferences sharedPref10 = getSharedPreferences("identifyNotePreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor10 = sharedPref10.edit();

        String final_score = "Identify that note Last Score : "+ scoreText;
        editor10.putString("identifyNote", final_score);
        editor10.commit();


    }
    public void next_question (View view){
        counter++;
        if( counter > 9){

            Intent intent = new Intent(this, GamesActivity.class);
            startActivity(intent);
        }
        else
            viewFlipper.showNext();


        if (counter == 9){

            if(rb19.isChecked()){
                score += 5;
            }
            if(rb21.isChecked()){
                score += 5;
            }
            if(rb27.isChecked()){
                score += 5;
            }
            if(rb31.isChecked()){
                score += 5;
            }
            if(rb38.isChecked()){
                score += 5;
            }
            if(rb33.isChecked()){
                score += 5;
            }
            if(rb44.isChecked()){
                score += 5;
            }
            if(rb45.isChecked()){
                score += 5;
            }
            previous.setEnabled(false);
            next.setEnabled(false);

            if (score < 20){
                badge.setImageResource(R.drawable.game_over);
                scoreDisplay.setText("Your score is " + score + "/40 Please Try again");
            } else {
                scoreDisplay.setText("Your score is " + score + "/40");
                badge.setImageResource(R.drawable.gold_medal);

            }

        }
//        scoreDisplay.setText("Your score is " + score + "/40");

        int real_score = score;

        SharedPreferences sharedPref1 = getSharedPreferences("identifyNotePreference_score", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPref1.edit();
        String scoreToText = "" +  real_score + "/40";
        editor1.putString("identifyscore",scoreToText);
        editor1.commit();



        if (counter != 9){

            if(rb19.isChecked()){
                score += 0;
            }
            if(rb21.isChecked()){
                score += 0;
            }
            if(rb27.isChecked()){
                score += 0;
            }
            if(rb31.isChecked()){
                score += 0;
            }
            if(rb38.isChecked()){
                score += 0;
            }
            if(rb33.isChecked()){
                score += 0;
            }
            if(rb44.isChecked()){
                score += 0;
            }
            if(rb45.isChecked()){
                score += 0;
            }
        }

        //go back to Games page and update the score there


    }

    public void previous_question (View view) {
        counter--;
        if (counter <= 0) {
            counter = 1;

        } else if (counter >= 0)
            viewFlipper.showPrevious();
    }
}
