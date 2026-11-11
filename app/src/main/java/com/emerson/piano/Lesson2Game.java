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

public class Lesson2Game extends PianoBaseActivity {

    ViewFlipper viewFlipper;
    Button next;
    Button previous;
    int counter = 1;

    int score = 0;
    TextView scoreDisplay;

    ImageView badge;

    RadioButton rb13;
    RadioButton rb19;
    RadioButton rb21;
    RadioButton rb25;
    RadioButton rb32;
    RadioButton rb35;
    RadioButton rb38;
    RadioButton rb33;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson2_game);

        viewFlipper = (ViewFlipper)findViewById(R.id.viewFlipper);
        next = (Button)findViewById(R.id.next);
        previous = (Button)findViewById(R.id.prev);

        scoreDisplay = (TextView)findViewById(R.id.scoreid);

        rb19 = (RadioButton)findViewById(R.id.radioButton19);
        rb21 = (RadioButton)findViewById(R.id.radioButton21);
        rb25 = (RadioButton)findViewById(R.id.radioButton25);
        rb38 = (RadioButton)findViewById(R.id.radioButton38);
        rb33 = (RadioButton)findViewById(R.id.radioButton33);

        badge = (ImageView)findViewById(R.id.imageView);
    }

    public void next_question (View view){
        counter++;
        if( counter > 7){
            counter = 7;
           Intent intent = new Intent(this, Lesson_2Activity.class);
           startActivity(intent);
        }
        else
            viewFlipper.showNext();

        if (counter == 7) {

            //q1
            if (rb19.isChecked()) {
                score += 5;
            }
            //q2
            if (rb21.isChecked()) {
                score += 5;
            }
            //q3
            if (rb25.isChecked()) {
                score += 5;
            }
            //q4
            if (rb38.isChecked()) {
                score += 5;
            }
            //q5
            if (rb33.isChecked()) {
                score += 5;
            }

            previous.setEnabled(false);
            next.setText("Lesson 2");
        }
        if (score < 11){
            badge.setImageResource(R.drawable.game_over);
            scoreDisplay.setText("Your score is " + score + "/25 Please Try again");
        } else {
            scoreDisplay.setText("Your score is " + score + "/25");
            badge.setImageResource(R.drawable.award2);

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