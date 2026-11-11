package com.ebisoft.doremmi;

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

public class NoteMathsActivity extends PianoBaseActivity {

    ViewFlipper viewFlipper;
    Button next;
    Button previous;
    int counter = 1;
    int score = 0;
    TextView scoreDisplay;

    RadioButton rb19;
    RadioButton rb24;
    RadioButton rb28;
    RadioButton rb29;
    RadioButton rb37;
    RadioButton rb33;

    TextView time;
    ImageView badge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_maths);

        viewFlipper = (ViewFlipper)findViewById(R.id.viewFlipper);
        next = (Button)findViewById(R.id.next);
        previous = (Button)findViewById(R.id.prev);

        scoreDisplay = (TextView)findViewById(R.id.scoreid);

        rb19 = (RadioButton)findViewById(R.id.radioButton19);
        rb24 = (RadioButton)findViewById(R.id.radioButton24);
        rb28 = (RadioButton)findViewById(R.id.radioButton28);
        rb29 = (RadioButton)findViewById(R.id.radioButton29);
        rb37 = (RadioButton)findViewById(R.id.radioButton37);
        rb33 = (RadioButton)findViewById(R.id.radioButton33);

        time = (TextView)findViewById(R.id.textView41);

        badge = (ImageView)findViewById(R.id.imageView);

        new CountDownTimer(20000, 1000) {

            public void onTick(long millisUntilFinished) {
                time.setText("Time: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                time.setText("done!");

                counter = 6;
                viewFlipper.setDisplayedChild( viewFlipper.indexOfChild(findViewById(R.id.question9)) );
                next_question(next);

            }
        }.start();


// holds the score for this quiz in sharedpref
        SharedPreferences sharedPref2 = getSharedPreferences("noteMathsPreference_score", Context.MODE_PRIVATE);

        String scoreText = sharedPref2.getString("noteMathsScore", "score unavailable" );

        //save the score to be read from games activity
        SharedPreferences sharedPref = getSharedPreferences("noteMathsPreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor20 = sharedPref.edit();

        String final_score = "Note maths last Score : "+ scoreText;
        editor20.putString("noteMaths", final_score);
        editor20.commit();


    }
    public void next_question (View view){
        counter++;
        if( counter > 7){
            counter = 7;
//            Intent intent = new Intent(this, GamesActivity.class);
//            startActivity(intent);

        }
        else
            viewFlipper.showNext();

        if (counter == 7){

            if(rb19.isChecked()){
                score += 5;
            }
            if(rb24.isChecked()){
                score += 5;
            }
            if(rb28.isChecked()){
                score += 5;
            }
            if(rb29.isChecked()){
                score += 5;
            }
            if(rb37.isChecked()){
                score += 5;
            }
            if(rb33.isChecked()){
                score += 5;
            }

            previous.setEnabled(false);
            next.setEnabled(false);

            if (score < 11){
                badge.setImageResource(R.drawable.game_over);
                scoreDisplay.setText("Your score is " + score + "/30 Please Try again");
            } else {
                scoreDisplay.setText("Your score is " + score + "/30");
                badge.setImageResource(R.drawable.award2);

            }
        }
        //scoreDisplay.setText("Your score is " + score + "/30");

        SharedPreferences sharedPref2 = getSharedPreferences("noteMathsPreference_score", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor12 = sharedPref2.edit();
        String scoreToText = "" +  score + "/30";
        editor12.putString("noteMathsScore",scoreToText);
        editor12.commit();


        if (counter != 7){

            if(rb19.isChecked()){
                score += 0;
            }
            if(rb24.isChecked()){
                score += 0;
            }
            if(rb28.isChecked()){
                score += 0;
            }
            if(rb29.isChecked()){
                score += 0;
            }
            if(rb37.isChecked()){
                score += 0;
            }
            if(rb33.isChecked()){
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
