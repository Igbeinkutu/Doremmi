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
import android.widget.ViewFlipper;

public class WordMatchActivity extends PianoBaseActivity {

    ViewFlipper viewFlipper;
    Button next;
    Button previous;
    int counter = 1;

    ImageView badge;

    int score = 0;
    TextView scoreDisplay;

    RadioButton rb4;
    RadioButton rb6;
    RadioButton rb11;
    RadioButton rb13;
    RadioButton rb19;
    RadioButton rb22;
    RadioButton rb25;
    RadioButton rb32;
    RadioButton rb35;
    RadioButton rb39;
    RadioButton rb44;
    RadioButton rb47;


    TextView time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_match);

    viewFlipper = (ViewFlipper)findViewById(R.id.viewFlipper);
    next = (Button)findViewById(R.id.next);
    previous = (Button)findViewById(R.id.prev);

        scoreDisplay = (TextView)findViewById(R.id.scoreid);

        rb4 = (RadioButton)findViewById(R.id.radioButton4);
        rb6 = (RadioButton)findViewById(R.id.radioButton6);
        rb11 = (RadioButton)findViewById(R.id.radioButton11);
        rb13 = (RadioButton)findViewById(R.id.radioButton13);
        rb19 = (RadioButton)findViewById(R.id.radioButton19);
        rb22 = (RadioButton)findViewById(R.id.radioButton22);
        rb25 = (RadioButton)findViewById(R.id.radioButton25);
        rb32 = (RadioButton)findViewById(R.id.radioButton32);
        rb35 = (RadioButton)findViewById(R.id.radioButton35);
        rb39 = (RadioButton)findViewById(R.id.radioButton39);
        rb44 = (RadioButton)findViewById(R.id.radioButton44);
        rb47 = (RadioButton)findViewById(R.id.radioButton47);

        badge = (ImageView)findViewById(R.id.imageView);


        time = (TextView)findViewById(R.id.textView41);

        new CountDownTimer(50000, 1000) {

            public void onTick(long millisUntilFinished) {
                time.setText("Time: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                time.setText("done!");

                counter = 12;
                viewFlipper.setDisplayedChild( viewFlipper.indexOfChild(findViewById(R.id.question12)) );
                next_question(next);


            }
        }.start();


// holds the score for this quiz in sharedpref
        SharedPreferences sharedPref4 = getSharedPreferences("wordMatchPreference_score", Context.MODE_PRIVATE);

        String scoreText = sharedPref4.getString("wordMatchScore", "No score yet" );

        //save the score to be read from games activity
        SharedPreferences sharedPref40 = getSharedPreferences("wordMatchPreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor40 = sharedPref40.edit();

        String final_score = "Find the matching word last Score : "+ scoreText;
        editor40.putString("wordMatch", final_score);
        editor40.commit();
}

    public void next_question (View view){
        counter++;
        if( counter > 13){
            counter = 13;
//            Intent intent = new Intent(this, GamesActivity.class);
//            startActivity(intent);
        }
        else
            viewFlipper.showNext();

        if (counter == 13) {

            if (rb4.isChecked()) {
                score += 5;
            }
            if (rb6.isChecked()) {
                score += 5;
            }
            if (rb11.isChecked()) {
                score += 5;
            }
            if (rb13.isChecked()) {
                score += 5;
            }
            if (rb19.isChecked()) {
                score += 5;
            }
            if (rb22.isChecked()) {
                score += 5;
            }
            if (rb25.isChecked()) {
                score += 5;
            }
            if (rb32.isChecked()) {
                score += 5;
            }
            if (rb35.isChecked()) {
                score += 5;
            }
            if (rb39.isChecked()) {
                score += 5;
            }
            if (rb44.isChecked()) {
                score += 5;
            }
            if (rb47.isChecked()) {
                score += 5;
            }

            previous.setEnabled(false);
            next.setEnabled(false);

            if (score < 20){
                badge.setImageResource(R.drawable.game_over);
                scoreDisplay.setText("Your score is " + score + "/60 Please Try again");
            } else {
                scoreDisplay.setText("Your score is " + score + "/60");
                badge.setImageResource(R.drawable.badge_game1);

            }
        }
        //scoreDisplay.setText("Your score is " + score + "/60");

        //save score to shared pref

        SharedPreferences sharedPref4 = getSharedPreferences("wordMatchPreference_score", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor14 = sharedPref4.edit();
        String scoreToText = "" +  score + "/60";
        editor14.putString("wordMatchScore",scoreToText);
        editor14.commit();


            if (counter != 13){

                if(rb4.isChecked()){
                    score += 0;
                }
                if(rb6.isChecked()){
                    score += 0;
                }
                if(rb11.isChecked()){
                    score += 0;
                }
                if(rb13.isChecked()){
                    score += 0;
                }
                if(rb19.isChecked()){
                    score += 0;
                }
                if(rb22.isChecked()){
                    score += 0;
                }
                if(rb25.isChecked()){
                    score += 0;
                }
                if(rb32.isChecked()){
                    score += 0;
                }
                if(rb35.isChecked()){
                    score += 0;
                }
                if(rb39.isChecked()){
                    score += 0;
                }
                if(rb44.isChecked()){
                    score += 0;
                }
                if(rb47.isChecked()){
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
