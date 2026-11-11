package com.emerson.piano;

import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;


import java.io.IOException;
import java.util.HashMap;


public class Lesson_2Activity extends PianoBaseActivity implements OnTouchListener, View.OnClickListener {

    ViewFlipper viewFlipper;
    ViewFlipper keyboardFlipper;
    Button next;
    Button previous;

    Button higher_octave;
    Button lower_octave;
    int counter = 1;
    int keyboard_Counter = 1;

    Button recordButton;
    private String FILE;

    private MediaPlayer mediaPlayer;
    private MediaRecorder mediaRecorder;
    SoundPool soundPool;
    SoundPool.Builder soundPoolBuilder;

    AudioAttributes attributes;
    AudioAttributes.Builder attributesBuilder;

    Button C4_lightable;
    Button D4_lightable;
    Button E4_lightable;
    Button F4_lightable;
    Button G4_lightable;
    Button A4_lightable;
    Button B4_lightable;
    Button C5_lightable;

    Button C3_lightable;
    Button D3_lightable;
    Button E3_lightable;
    Button F3_lightable;
    Button G3_lightable;
    Button A3_lightable;
    Button B3_lightable;
    Button C41_lightable;

    Button ib1;
    Button ib2;
    Button ib3;
    Button ib4;
    Button ib5;
    Button ib6;
    Button ib7;
    Button ib8;
    Button ib9;
    Button ib10;
    Button ib11;
    Button ib12;
    Button ib13;
    Button ib14;
    Button ib15;
    Button ib16;
    Button ib17;
    Button ib18;
    Button ib19;
    Button ib20;
    Button ib21;
    Button ib22;
    Button ib23;
    Button ib24;
    Button ib25;
    Button ib26;
    Button ib27;
    Button ib28;
    Button ib29;
    Button ib30;
    Button ib31;
    Button ib32;
    Button ib33;
    Button ib34;
    Button ib35;
    Button ib36;
    Button ib37;
    Button ib38;
    Button ib39;
    Button ib40;
    Button ib41;

    int soundID_C4;
    int soundID_Db4;
    int soundID_D4;
    int soundID_Eb4;
    int soundID_E4;
    int soundID_F4;
    int soundID_Gb4;
    int soundID_G4;
    int soundID_Ab4;
    int soundID_A4;
    int soundID_Bb4;
    int soundID_B4;
    int soundID_C5;
    int soundID_Db51;
    int soundID_D51;

    int soundID_C51;
    int soundID_Db5;
    int soundID_D5;
    int soundID_Eb5;
    int soundID_E5;
    int soundID_F5;
    int soundID_Gb5;
    int soundID_G5;
    int soundID_Ab5;
    int soundID_A5;
    int soundID_Bb5;
    int soundID_B5;
    int soundID_C6;

    int soundID_C3;
    int soundID_Db3;
    int soundID_D3;
    int soundID_Eb3;
    int soundID_E3;
    int soundID_F3;
    int soundID_Gb3;
    int soundID_G3;
    int soundID_Ab3;
    int soundID_A3;
    int soundID_Bb3;
    int soundID_B3;
    int soundID_C41;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_2);

        ib1 = (Button)findViewById(R.id.C4);
        ib2 = (Button)findViewById(R.id.D4);
        ib3 = (Button)findViewById(R.id.Db4);
        ib4 = (Button)findViewById(R.id.Eb4);
        ib5 = (Button)findViewById(R.id.E4);
        ib6 = (Button)findViewById(R.id.F4);
        ib7 = (Button)findViewById(R.id.Gb4);
        ib8 = (Button)findViewById(R.id.G4);
        ib9 = (Button)findViewById(R.id.Ab4);
        ib10 = (Button)findViewById(R.id.A4);
        ib11 = (Button)findViewById(R.id.Bb4);
        ib12 = (Button)findViewById(R.id.B4);
        ib13 = (Button)findViewById(R.id.C5);
        ib40 = (Button)findViewById(R.id.Db51);
        ib41 = (Button)findViewById(R.id.D51);

        //higher_octave

        ib14 = (Button)findViewById(R.id.C51);
        ib15 = (Button)findViewById(R.id.D5);
        ib16 = (Button)findViewById(R.id.Db5);
        ib17 = (Button)findViewById(R.id.Eb5);
        ib18 = (Button)findViewById(R.id.E5);
        ib19 = (Button)findViewById(R.id.F5);
        ib20 = (Button)findViewById(R.id.Gb5);
        ib21 = (Button)findViewById(R.id.G5);
        ib22 = (Button)findViewById(R.id.Ab5);
        ib23 = (Button)findViewById(R.id.A5);
        ib24 = (Button)findViewById(R.id.Bb5);
        ib25 = (Button)findViewById(R.id.B5);
        ib26 = (Button)findViewById(R.id.C6);

        //lower_octave

        ib27 = (Button)findViewById(R.id.C3);
        ib28 = (Button)findViewById(R.id.D3);
        ib29 = (Button)findViewById(R.id.Db3);
        ib30 = (Button)findViewById(R.id.Eb3);
        ib31 = (Button)findViewById(R.id.E3);
        ib32 = (Button)findViewById(R.id.F3);
        ib33 = (Button)findViewById(R.id.Gb3);
        ib34 = (Button)findViewById(R.id.G3);
        ib35 = (Button)findViewById(R.id.Ab3);
        ib36 = (Button)findViewById(R.id.A3);
        ib37 = (Button)findViewById(R.id.Bb3);
        ib38 = (Button)findViewById(R.id.B3);
        ib39 = (Button)findViewById(R.id.C41);

        soundPool = PianoSoundPool.create();

        soundID_C4 = soundPool.load(this, R.raw.c4, 1);
        soundID_Db4 = soundPool.load(this, R.raw.db4, 1);
        soundID_D4 = soundPool.load(this, R.raw.d4, 1);
        soundID_Eb4 = soundPool.load(this, R.raw.eb4, 1);
        soundID_E4 = soundPool.load(this, R.raw.e4, 1);
        soundID_F4 = soundPool.load(this, R.raw.f4, 1);
        soundID_Gb4 = soundPool.load(this, R.raw.gb4, 1);
        soundID_G4 = soundPool.load(this, R.raw.g4, 1);
        soundID_Ab4 = soundPool.load(this, R.raw.ab4, 1);
        soundID_A4 = soundPool.load(this, R.raw.a4, 1);
        soundID_Bb4 = soundPool.load(this, R.raw.bb4, 1);
        soundID_B4 = soundPool.load(this, R.raw.b4, 1);
        soundID_C5 = soundPool.load(this, R.raw.c5, 1);
        soundID_Db51 = soundPool.load(this, R.raw.db5, 1);
        soundID_D51 = soundPool.load(this, R.raw.d5, 1);

        //higher octave soundPool

        soundID_C51 = soundPool.load(this, R.raw.c5, 1);
        soundID_Db5 = soundPool.load(this, R.raw.db5, 1);
        soundID_D5 = soundPool.load(this, R.raw.d5, 1);
        soundID_Eb5 = soundPool.load(this, R.raw.eb5, 1);
        soundID_E5 = soundPool.load(this, R.raw.e5, 1);
        soundID_F5 = soundPool.load(this, R.raw.f5, 1);
        soundID_Gb5 = soundPool.load(this, R.raw.gb5, 1);
        soundID_G5 = soundPool.load(this, R.raw.g5, 1);
        soundID_Ab5 = soundPool.load(this, R.raw.ab5, 1);
        soundID_A5 = soundPool.load(this, R.raw.a5, 1);
        soundID_Bb5 = soundPool.load(this, R.raw.bb5, 1);
        soundID_B5 = soundPool.load(this, R.raw.b5, 1);
        soundID_C6 = soundPool.load(this, R.raw.c6, 1);

        //lower octave soundPool

        soundID_C3 = soundPool.load(this, R.raw.c3, 1);
        soundID_Db3 = soundPool.load(this, R.raw.db3, 1);
        soundID_D3 = soundPool.load(this, R.raw.d3, 1);
        soundID_Eb3 = soundPool.load(this, R.raw.eb3, 1);
        soundID_E3 = soundPool.load(this, R.raw.e3, 1);
        soundID_F3 = soundPool.load(this, R.raw.f3, 1);
        soundID_Gb3 = soundPool.load(this, R.raw.gb3, 1);
        soundID_G3 = soundPool.load(this, R.raw.g3, 1);
        soundID_Ab3 = soundPool.load(this, R.raw.ab3, 1);
        soundID_A3 = soundPool.load(this, R.raw.a3, 1);
        soundID_Bb3 = soundPool.load(this, R.raw.bb3, 1);
        soundID_B3 = soundPool.load(this, R.raw.b3, 1);
        soundID_C41 = soundPool.load(this, R.raw.c5, 1);

        ib1.setOnTouchListener(this);
        ib2.setOnTouchListener(this);
        ib3.setOnTouchListener(this);
        ib4.setOnTouchListener(this);
        ib5.setOnTouchListener(this);
        ib6.setOnTouchListener(this);
        ib7.setOnTouchListener(this);
        ib8.setOnTouchListener(this);
        ib9.setOnTouchListener(this);
        ib10.setOnTouchListener(this);
        ib11.setOnTouchListener(this);
        ib12.setOnTouchListener(this);
        ib13.setOnTouchListener(this);
        ib40.setOnTouchListener(this);
        ib41.setOnTouchListener(this);


        //higher octave ontouch
        ib14.setOnTouchListener(this);
        ib15.setOnTouchListener(this);
        ib16.setOnTouchListener(this);
        ib17.setOnTouchListener(this);
        ib18.setOnTouchListener(this);
        ib19.setOnTouchListener(this);
        ib20.setOnTouchListener(this);
        ib21.setOnTouchListener(this);
        ib22.setOnTouchListener(this);
        ib23.setOnTouchListener(this);
        ib24.setOnTouchListener(this);
        ib25.setOnTouchListener(this);
        ib26.setOnTouchListener(this);

        //lower octave ontouch
        ib27.setOnTouchListener(this);
        ib28.setOnTouchListener(this);
        ib29.setOnTouchListener(this);
        ib30.setOnTouchListener(this);
        ib31.setOnTouchListener(this);
        ib32.setOnTouchListener(this);
        ib33.setOnTouchListener(this);
        ib34.setOnTouchListener(this);
        ib35.setOnTouchListener(this);
        ib36.setOnTouchListener(this);
        ib37.setOnTouchListener(this);
        ib38.setOnTouchListener(this);
        ib39.setOnTouchListener(this);

        viewFlipper = (ViewFlipper)findViewById(R.id.viewFlipper);
        next = (Button)findViewById(R.id.next);
        previous = (Button)findViewById(R.id.prev);
        next.setOnClickListener(this);
        previous.setOnClickListener(this);

        keyboardFlipper = (ViewFlipper)findViewById(R.id.viewFlipper_piano);
        higher_octave = (Button)findViewById(R.id.high_octave);
        lower_octave = (Button)findViewById(R.id.low_octave);

        recordButton = (Button)findViewById(R.id.record);
        

        //lightable keys
        C4_lightable = (Button)findViewById(R.id.C4);
        D4_lightable = (Button)findViewById(R.id.D4);
        E4_lightable = (Button)findViewById(R.id.E4);
        F4_lightable = (Button)findViewById(R.id.F4);
        G4_lightable = (Button)findViewById(R.id.G4);
        A4_lightable = (Button)findViewById(R.id.A4);
        B4_lightable = (Button)findViewById(R.id.B4);
        C5_lightable = (Button)findViewById(R.id.C5);

        C3_lightable = (Button)findViewById(R.id.C3);
        D3_lightable = (Button)findViewById(R.id.D3);
        E3_lightable = (Button)findViewById(R.id.E3);
        F3_lightable = (Button)findViewById(R.id.F3);
        G3_lightable = (Button)findViewById(R.id.G3);
        A3_lightable = (Button)findViewById(R.id.A3);
        B3_lightable = (Button)findViewById(R.id.B3);
        C41_lightable = (Button)findViewById(R.id.C41);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        int action1 = event.getActionMasked();
        switch(v.getId()){
            case R.id.C4:
                if(action == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                    soundPool.play(soundID_C4, 1, 1, 0, 0, 1);


//                    if (counter == 5){
//                        ib1.setBackgroundResource(R.drawable.white_selector);
//                        ib2.setBackgroundResource(R.drawable.red_key);
//                    }

                }
                else if (action == MotionEvent.ACTION_UP){
                    v.setPressed(false);
                }
                else if (action1 == MotionEvent.ACTION_POINTER_UP){
                    //mediaPlayer.release();
                }

                break;
            case R.id.Db4:
                if(action == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                    soundPool.play(soundID_Db4, 1, 1, 0, 0, 1);
                }
                else if (action == MotionEvent.ACTION_UP){
                    v.setPressed(false);
                }
                else if (action1 == MotionEvent.ACTION_POINTER_UP){
                    v.setPressed(false);
                }
                break;
            case R.id.D4:
                if(action == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                    soundPool.play(soundID_D4, 1, 1, 0, 0, 1);
                }
                else if (action == MotionEvent.ACTION_UP){
                    v.setPressed(false);
                }
                else if (action == MotionEvent.ACTION_POINTER_UP){
                    v.setPressed(false);
                }
                break;
            case R.id.Eb4:
                if(action == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                    soundPool.play(soundID_Eb4, 1, 1, 0, 0, 1);
                }
                else if (action == MotionEvent.ACTION_UP){
                    v.setPressed(false);
                }

                else if (action1 == MotionEvent.ACTION_POINTER_UP){
                    //mediaPlayer.release();
                }
                break;
            case R.id.E4:
                if(action == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                    soundPool.play(soundID_E4, 1, 1, 0, 0, 1);
                }
                else if (action == MotionEvent.ACTION_UP){
                    v.setPressed(false);
                }
                else if (action1 == MotionEvent.ACTION_POINTER_UP){
                    v.setPressed(false);
                }
                break;
            case R.id.F4:
                if(action == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                    soundPool.play(soundID_F4, 1, 1, 0, 0, 1);
                }
                else if (action == MotionEvent.ACTION_UP){
                    v.setPressed(false);
                }
                else if (action == MotionEvent.ACTION_POINTER_UP){
                    //mediaPlayer.release();
                }
                break;
            case R.id.Gb4:
                if(action == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                    soundPool.play(soundID_Gb4, 1, 1, 0, 0, 1);
                }
                else if (action == MotionEvent.ACTION_UP){
                    v.setPressed(false);
                }
                else if (action == MotionEvent.ACTION_POINTER_UP){
                    //mediaPlayer.release();
                }
                break;
            case R.id.G4:
                if(action == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                    soundPool.play(soundID_G4, 1, 1, 0, 0, 1);
                }
                else if (action == MotionEvent.ACTION_UP){
                    v.setPressed(false);
                }
                break;
            case R.id.Ab4:
                if(action == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                    soundPool.play(soundID_Ab4, 1, 1, 0, 0, 1);
                }
                else if (action == MotionEvent.ACTION_UP){
                    v.setPressed(false);
                }
                break;
            case R.id.A4:
                if(action == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                    soundPool.play(soundID_A4, 1, 1, 0, 0, 1);
                }
                else if (action == MotionEvent.ACTION_UP){
                    //mediaPlayer.release();
                    v.setPressed(false);
                }
                break;
            case R.id.Bb4:
                if(action == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                    soundPool.play(soundID_Bb4, 1, 1, 0, 0, 1);

                }
                else if (action == MotionEvent.ACTION_UP){
                    v.setPressed(false);
                }
                else if (action1 == MotionEvent.ACTION_POINTER_UP){
                    mediaPlayer.release();
                }

                break;
            case R.id.B4:
                if(action == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                    soundPool.play(soundID_B4, 1, 1, 0, 0, 1);

                }
                else if (action == MotionEvent.ACTION_UP){
                    v.setPressed(false);
                }
                else if (action1 == MotionEvent.ACTION_POINTER_UP){
                    //mediaPlayer.release();
                }

                break;
            case R.id.C5:
                if(action == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                    soundPool.play(soundID_C5, 1, 1, 0, 0, 1);
                }
                else if (action == MotionEvent.ACTION_UP){
                    v.setPressed(false);
                }
                else if (action1 == MotionEvent.ACTION_POINTER_UP){
                    mediaPlayer.release();
                }

                break;

            case R.id.Db51:
                if(action == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                    soundPool.play(soundID_Db51, 1, 1, 0, 0, 1);
                }
                else if (action == MotionEvent.ACTION_UP){
                    //mediaPlayer.release();
                    v.setPressed(false);
                }
                else if (action1 == MotionEvent.ACTION_POINTER_UP){
                    v.setPressed(false);
                    mediaPlayer.release();
                }
                break;
            case R.id.D51:
                if(action == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                    soundPool.play(soundID_D51, 1, 1, 0, 0, 1);
                }
                else if (action == MotionEvent.ACTION_UP){
                    //mediaPlayer.release();
                    v.setPressed(false);
                }
                else if (action1 == MotionEvent.ACTION_POINTER_UP){
                    v.setPressed(false);
                    mediaPlayer.release();
                }
                break;




            //upper octave
            case R.id.C51:
                if(action == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                    soundPool.play(soundID_C51, 1, 1, 0, 0, 1);
                }
                else if (action == MotionEvent.ACTION_UP){
                    // mediaPlayer.release();
                    v.setPressed(false);
                }
                else if (action1 == MotionEvent.ACTION_POINTER_UP){
                    //mediaPlayer.release();
                }

                break;
            case R.id.Db5:
                if(action == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                    soundPool.play(soundID_Db5, 1, 1, 0, 0, 1);
                }
                else if (action == MotionEvent.ACTION_UP){
                    //mediaPlayer.release();
                    v.setPressed(false);
                }
                else if (action1 == MotionEvent.ACTION_POINTER_UP){
                    v.setPressed(false);
                    mediaPlayer.release();
                }
                break;
            case R.id.D5:
                if(action == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                    soundPool.play(soundID_D5, 1, 1, 0, 0, 1);
//                    mediaPlayer = MediaPlayer.create(this, R.raw.d4);
//                    mediaPlayer.seekTo(0);
//                    mediaPlayer.start();
                }
                else if (action == MotionEvent.ACTION_UP){
                    //mediaPlayer.release();
                    v.setPressed(false);
                }
                else if (action == MotionEvent.ACTION_POINTER_UP){
                    //mediaPlayer.release();
                    v.setPressed(false);
                }
                break;
            case R.id.Eb5:
                if(action == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                    soundPool.play(soundID_Eb5, 1, 1, 0, 0, 1);
                }
                else if (action == MotionEvent.ACTION_UP){
                    v.setPressed(false);
                }

                else if (action1 == MotionEvent.ACTION_POINTER_UP){
                }
                break;
            case R.id.E5:
                if(action == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                    soundPool.play(soundID_E5, 1, 1, 0, 0, 1);
                }
                else if (action == MotionEvent.ACTION_UP){
                    //mediaPlayer.release();
                    v.setPressed(false);
                }
                else if (action1 == MotionEvent.ACTION_POINTER_UP){
                    //mediaPlayer.release();
                    v.setPressed(false);
                }
                break;
            case R.id.F5:
                if(action == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                    soundPool.play(soundID_F5, 1, 1, 0, 0, 1);
                }
                else if (action == MotionEvent.ACTION_UP){
                    // mediaPlayer.release();
                    v.setPressed(false);
                }
                else if (action == MotionEvent.ACTION_POINTER_UP){
                    //mediaPlayer.release();
                }
                break;
            case R.id.Gb5:
                if(action == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                    soundPool.play(soundID_Gb5, 1, 1, 0, 0, 1);
                }
                else if (action == MotionEvent.ACTION_UP){
                    v.setPressed(false);
                }
                else if (action == MotionEvent.ACTION_POINTER_UP){
                    //mediaPlayer.release();
                }
                break;
            case R.id.G5:
                if(action == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                    soundPool.play(soundID_G5, 1, 1, 0, 0, 1);
                }
                else if (action == MotionEvent.ACTION_UP){
                    v.setPressed(false);
                }
                break;
            case R.id.Ab5:
                if(action == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                    soundPool.play(soundID_Ab5, 1, 1, 0, 0, 1);
                }
                else if (action == MotionEvent.ACTION_UP){
                    v.setPressed(false);
                }
                break;
            case R.id.A5:
                if(action == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                    soundPool.play(soundID_A5, 1, 1, 0, 0, 1);
                }
                else if (action == MotionEvent.ACTION_UP){
                    v.setPressed(false);
                }
                break;
            case R.id.Bb5:
                if(action == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                    soundPool.play(soundID_Bb5, 1, 1, 0, 0, 1);
                }
                else if (action == MotionEvent.ACTION_UP){
                    v.setPressed(false);
                }
                else if (action1 == MotionEvent.ACTION_POINTER_UP){
                    mediaPlayer.release();
                }

                break;
            case R.id.B5:
                if(action == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                    soundPool.play(soundID_B5, 1, 1, 0, 0, 1);
                }
                else if (action == MotionEvent.ACTION_UP){
                    v.setPressed(false);
                }
                else if (action1 == MotionEvent.ACTION_POINTER_UP){
                    //mediaPlayer.release();
                }

                break;
            case R.id.C6:
                if(action == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                    soundPool.play(soundID_C6, 1, 1, 0, 0, 1);
                }
                else if (action == MotionEvent.ACTION_UP){
                    v.setPressed(false);
                }
                else if (action1 == MotionEvent.ACTION_POINTER_UP){
                    mediaPlayer.release();
                }

                break;

            //upper octave

            case R.id.C3:
                if(action == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                    soundPool.play(soundID_C3, 1, 1, 0, 0, 1);

                }
                else if (action == MotionEvent.ACTION_UP){
                    v.setPressed(false);
                }
                else if (action1 == MotionEvent.ACTION_POINTER_UP){
                    //mediaPlayer.release();
                }

                break;
            case R.id.Db3:
                if(action == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                    soundPool.play(soundID_Db3, 1, 1, 0, 0, 1);
                }
                else if (action == MotionEvent.ACTION_UP){
                    v.setPressed(false);
                }
                else if (action1 == MotionEvent.ACTION_POINTER_UP){
                    v.setPressed(false);
                }
                break;
            case R.id.D3:
                if(action == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                    soundPool.play(soundID_D3, 1, 1, 0, 0, 1);
                }
                else if (action == MotionEvent.ACTION_UP){
                    v.setPressed(false);
                }
                else if (action == MotionEvent.ACTION_POINTER_UP){
                    v.setPressed(false);
                }
                break;
            case R.id.Eb3:
                if(action == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                    soundPool.play(soundID_Eb3, 1, 1, 0, 0, 1);
                }
                else if (action == MotionEvent.ACTION_UP){
                    v.setPressed(false);
                }

                else if (action1 == MotionEvent.ACTION_POINTER_UP){
                    //mediaPlayer.release();
                }
                break;
            case R.id.E3:
                if(action == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                    soundPool.play(soundID_E3, 1, 1, 0, 0, 1);
                }
                else if (action == MotionEvent.ACTION_UP){
                    v.setPressed(false);
                }
                else if (action1 == MotionEvent.ACTION_POINTER_UP){
                    v.setPressed(false);
                }
                break;
            case R.id.F3:
                if(action == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                    soundPool.play(soundID_F3, 1, 1, 0, 0, 1);
                }
                else if (action == MotionEvent.ACTION_UP){
                    v.setPressed(false);
                }
                else if (action == MotionEvent.ACTION_POINTER_UP){
                }
                break;
            case R.id.Gb3:
                if(action == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                    soundPool.play(soundID_Gb3, 1, 1, 0, 0, 1);
                }
                else if (action == MotionEvent.ACTION_UP){
                    v.setPressed(false);
                }
                else if (action == MotionEvent.ACTION_POINTER_UP){

                }
                break;
            case R.id.G3:
                if(action == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                    soundPool.play(soundID_G3, 1, 1, 0, 0, 1);
                }
                else if (action == MotionEvent.ACTION_UP){
                    v.setPressed(false);
                }
                break;
            case R.id.Ab3:
                if(action == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                    soundPool.play(soundID_Ab3, 1, 1, 0, 0, 1);
                }
                else if (action == MotionEvent.ACTION_UP){
                    v.setPressed(false);
                }
                break;
            case R.id.A3:
                if(action == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                    soundPool.play(soundID_A3, 1, 1, 0, 0, 1);
                }
                else if (action == MotionEvent.ACTION_UP){
                    v.setPressed(false);
                }
                break;
            case R.id.Bb3:
                if(action == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                    soundPool.play(soundID_Bb3, 1, 1, 0, 0, 1);
                }
                else if (action == MotionEvent.ACTION_UP){
                    v.setPressed(false);
                }
                else if (action1 == MotionEvent.ACTION_POINTER_UP){
                    mediaPlayer.release();
                }

                break;
            case R.id.B3:
                if(action == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                    soundPool.play(soundID_B3, 1, 1, 0, 0, 1);

                }
                else if (action == MotionEvent.ACTION_UP){
                    // mediaPlayer.release();
                    v.setPressed(false);
                }
                else if (action1 == MotionEvent.ACTION_POINTER_UP){
                    //mediaPlayer.release();
                }

                break;
            case R.id.C41:
                if(action == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                    soundPool.play(soundID_C4, 1, 1, 0, 0, 1);

                }
                else if (action == MotionEvent.ACTION_UP){
                    // mediaPlayer.release();
                    v.setPressed(false);
                }
                else if (action1 == MotionEvent.ACTION_POINTER_UP){
                    mediaPlayer.release();
                }

                break;
        }

                return true;

    }
    @Override
    public void onClick(View v) {
        if (v == next){
            counter++;
            if( counter > 40){
                //open next intent for next lesson
                counter = 40;
                Intent intent = new Intent(this, Lesson_3Activity.class);
                startActivity(intent);
            }
            else
                viewFlipper.showNext();

            //fingering exercise
//            switch (counter){
//                case 5:
            if(counter == 5){
                    ib1.setBackgroundResource(R.drawable.red_key);

                    ib1.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int action = event.getAction();
                        switch(action) {
                            case MotionEvent.ACTION_DOWN:
                                v.setPressed(true);
                                soundPool.play(soundID_C4, 1, 1, 0, 0, 1);
                                ib1.setBackgroundResource(R.drawable.white_selector);
                                ib2.setBackgroundResource(R.drawable.red_key);
                                break;
                            case MotionEvent.ACTION_UP:
                                v.setPressed(false);
                                ib1.setBackgroundResource(R.drawable.white_selector);
                                break;
                        }
                        return true;
                    }

                    });
                    ib2.setOnTouchListener(new OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            int action = event.getAction();
                            switch(action) {
                                case MotionEvent.ACTION_DOWN:
                                    soundPool.play(soundID_D4, 1, 1, 0, 0, 1);
                                    ib2.setBackgroundResource(R.drawable.white_selector);
                                    ib5.setBackgroundResource(R.drawable.red_key);
                                    break;
                                case MotionEvent.ACTION_UP:
                                    v.setPressed(false);
                                    ib2.setBackgroundResource(R.drawable.white_selector);
                                    break;
                            }
                            return true;
                        }

                    });

                    ib5.setOnTouchListener(new OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            int action = event.getAction();
                            switch(action) {
                                case MotionEvent.ACTION_DOWN:
                                    v.setPressed(true);
                                    soundPool.play(soundID_E4, 1, 1, 0, 0, 1);
                                    ib5.setBackgroundResource(R.drawable.white_selector);
                                    ib6.setBackgroundResource(R.drawable.red_key);
                                    break;
                                case MotionEvent.ACTION_UP:
                                    v.setPressed(false);
                                    ib5.setBackgroundResource(R.drawable.white_selector);
                                    break;
                            }
                            return true;
                        }

                    });
                    ib6.setOnTouchListener(new OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            int action = event.getAction();
                            switch(action) {
                                case MotionEvent.ACTION_DOWN:
                                    v.setPressed(true);
                                    soundPool.play(soundID_F4, 1, 1, 0, 0, 1);
                                    ib6.setBackgroundResource(R.drawable.white_selector);
                                    ib8.setBackgroundResource(R.drawable.red_key);
                                    break;
                                case MotionEvent.ACTION_UP:
                                    v.setPressed(false);
                                    ib6.setBackgroundResource(R.drawable.white_selector);
                                    break;
                            }
                            return true;
                        }

                    });

                    ib8.setOnTouchListener(new OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            int action = event.getAction();
                            switch(action) {
                                case MotionEvent.ACTION_DOWN:
                                    v.setPressed(true);
                                    soundPool.play(soundID_G4, 1, 1, 0, 0, 1);
                                    ib8.setBackgroundResource(R.drawable.white_selector);
                                    break;
                                case MotionEvent.ACTION_UP:
                                    v.setPressed(false);
                                    ib8.setBackgroundResource(R.drawable.white_selector);
                                    break;
                            }
                            return true;
                        }

                    });}

            if(counter != 5){
                ib1.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int action = event.getAction();
                        switch(action) {
                            case MotionEvent.ACTION_DOWN:
                                v.setPressed(true);
                                soundPool.play(soundID_C4, 1, 1, 0, 0, 1);
                                ib1.setBackgroundResource(R.drawable.white_selector);
                                break;
                            case MotionEvent.ACTION_UP:
                                v.setPressed(false);
                                ib1.setBackgroundResource(R.drawable.white_selector);
                                break;
                        }
                        return true;
                    }

                });
                ib2.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int action = event.getAction();
                        switch(action) {
                            case MotionEvent.ACTION_DOWN:
                                soundPool.play(soundID_D4, 1, 1, 0, 0, 1);
                                ib2.setBackgroundResource(R.drawable.white_selector);
                                break;
                            case MotionEvent.ACTION_UP:
                                v.setPressed(false);
                                ib2.setBackgroundResource(R.drawable.white_selector);
                                break;
                        }
                        return true;
                    }

                });

                ib5.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int action = event.getAction();
                        switch(action) {
                            case MotionEvent.ACTION_DOWN:
                                v.setPressed(true);
                                soundPool.play(soundID_E4, 1, 1, 0, 0, 1);
                                ib5.setBackgroundResource(R.drawable.white_selector);
                                break;
                            case MotionEvent.ACTION_UP:
                                v.setPressed(false);
                                ib5.setBackgroundResource(R.drawable.white_selector);
                                break;
                        }
                        return true;
                    }

                });
                ib6.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int action = event.getAction();
                        switch(action) {
                            case MotionEvent.ACTION_DOWN:
                                v.setPressed(true);
                                soundPool.play(soundID_F4, 1, 1, 0, 0, 1);
                                ib6.setBackgroundResource(R.drawable.white_selector);
                                break;
                            case MotionEvent.ACTION_UP:
                                v.setPressed(false);
                                ib6.setBackgroundResource(R.drawable.white_selector);
                                break;
                        }
                        return true;
                    }

                });}
            if(counter == 7){
                ib27.setBackgroundResource(R.drawable.red_key);

                ib27.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int action = event.getAction();
                        switch(action) {
                            case MotionEvent.ACTION_DOWN:
                                v.setPressed(true);
                                soundPool.play(soundID_C3, 1, 1, 0, 0, 1);
                                ib27.setBackgroundResource(R.drawable.white_selector);
                                ib28.setBackgroundResource(R.drawable.red_key);
                                break;
                            case MotionEvent.ACTION_UP:
                                v.setPressed(false);
                                ib27.setBackgroundResource(R.drawable.white_selector);
                                break;
                        }
                        return true;
                    }

                });
                ib28.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int action = event.getAction();
                        switch(action) {
                            case MotionEvent.ACTION_DOWN:
                                soundPool.play(soundID_D3, 1, 1, 0, 0, 1);
                                ib28.setBackgroundResource(R.drawable.white_selector);
                                ib31.setBackgroundResource(R.drawable.red_key);
                                break;
                            case MotionEvent.ACTION_UP:
                                v.setPressed(false);
                                ib28.setBackgroundResource(R.drawable.white_selector);
                                break;
                        }
                        return true;
                    }

                });

                ib31.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int action = event.getAction();
                        switch(action) {
                            case MotionEvent.ACTION_DOWN:
                                v.setPressed(true);
                                soundPool.play(soundID_E3, 1, 1, 0, 0, 1);
                                ib31.setBackgroundResource(R.drawable.white_selector);
                                ib32.setBackgroundResource(R.drawable.red_key);
                                break;
                            case MotionEvent.ACTION_UP:
                                v.setPressed(false);
                                ib31.setBackgroundResource(R.drawable.white_selector);
                                break;
                        }
                        return true;
                    }

                });
                ib32.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int action = event.getAction();
                        switch(action) {
                            case MotionEvent.ACTION_DOWN:
                                v.setPressed(true);
                                soundPool.play(soundID_F3, 1, 1, 0, 0, 1);
                                ib32.setBackgroundResource(R.drawable.white_selector);
                                ib34.setBackgroundResource(R.drawable.red_key);
                                break;
                            case MotionEvent.ACTION_UP:
                                v.setPressed(false);
                                ib32.setBackgroundResource(R.drawable.white_selector);
                                break;
                        }
                        return true;
                    }

                });

                ib34.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int action = event.getAction();
                        switch(action) {
                            case MotionEvent.ACTION_DOWN:
                                v.setPressed(true);
                                soundPool.play(soundID_G3, 1, 1, 0, 0, 1);
                                ib34.setBackgroundResource(R.drawable.white_selector);
                                break;
                            case MotionEvent.ACTION_UP:
                                v.setPressed(false);
                                ib34.setBackgroundResource(R.drawable.white_selector);
                                break;
                        }
                        return true;
                    }

                });}

            if(counter != 7){
                ib27.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int action = event.getAction();
                        switch(action) {
                            case MotionEvent.ACTION_DOWN:
                                v.setPressed(true);
                                soundPool.play(soundID_C3, 1, 1, 0, 0, 1);
                                ib27.setBackgroundResource(R.drawable.white_selector);
                                break;
                            case MotionEvent.ACTION_UP:
                                v.setPressed(false);
                                ib27.setBackgroundResource(R.drawable.white_selector);
                                break;
                        }
                        return true;
                    }

                });
                ib28.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int action = event.getAction();
                        switch(action) {
                            case MotionEvent.ACTION_DOWN:
                                soundPool.play(soundID_D3, 1, 1, 0, 0, 1);
                                ib28.setBackgroundResource(R.drawable.white_selector);
                                break;
                            case MotionEvent.ACTION_UP:
                                v.setPressed(false);
                                ib28.setBackgroundResource(R.drawable.white_selector);
                                break;
                        }
                        return true;
                    }

                });

                ib31.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int action = event.getAction();
                        switch(action) {
                            case MotionEvent.ACTION_DOWN:
                                v.setPressed(true);
                                soundPool.play(soundID_E3, 1, 1, 0, 0, 1);
                                ib31.setBackgroundResource(R.drawable.white_selector);
                                break;
                            case MotionEvent.ACTION_UP:
                                v.setPressed(false);
                                ib31.setBackgroundResource(R.drawable.white_selector);
                                break;
                        }
                        return true;
                    }

                });
                ib32.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int action = event.getAction();
                        switch(action) {
                            case MotionEvent.ACTION_DOWN:
                                v.setPressed(true);
                                soundPool.play(soundID_F3, 1, 1, 0, 0, 1);
                                ib32.setBackgroundResource(R.drawable.white_selector);
                                break;
                            case MotionEvent.ACTION_UP:
                                v.setPressed(false);
                                ib32.setBackgroundResource(R.drawable.white_selector);
                                break;
                        }
                        return true;
                    }

                });}

            if(counter == 33){
                ib1.setBackgroundResource(R.drawable.red_key);

                ib1.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int action = event.getAction();
                        switch(action) {
                            case MotionEvent.ACTION_DOWN:
                                v.setPressed(true);
                                soundPool.play(soundID_C4, 1, 1, 0, 0, 1);
                                ib1.setBackgroundResource(R.drawable.white_selector);
                                ib2.setBackgroundResource(R.drawable.red_key);
                                break;
                            case MotionEvent.ACTION_UP:
                                v.setPressed(false);
                                ib1.setBackgroundResource(R.drawable.white_selector);
                                break;
                        }
                        return true;
                    }

                });
                ib2.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int action = event.getAction();
                        switch(action) {
                            case MotionEvent.ACTION_DOWN:
                                v.setPressed(true);
                                soundPool.play(soundID_D4, 1, 1, 0, 0, 1);
                                ib2.setBackgroundResource(R.drawable.white_selector);
                                ib5.setBackgroundResource(R.drawable.red_key);
                                break;
                            case MotionEvent.ACTION_UP:
                                v.setPressed(false);
                                ib2.setBackgroundResource(R.drawable.white_selector);
                                break;
                        }
                        return true;
                    }

                });

                ib5.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int action = event.getAction();
                        switch(action) {
                            case MotionEvent.ACTION_DOWN:
                                v.setPressed(true);
                                soundPool.play(soundID_E4, 1, 1, 0, 0, 1);
                                ib5.setBackgroundResource(R.drawable.white_selector);
                                ib1.setBackgroundResource(R.drawable.red_key);
                                break;
                            case MotionEvent.ACTION_UP:
                                v.setPressed(false);
                                ib5.setBackgroundResource(R.drawable.white_selector);
                                break;
                        }
                        return true;
                    }

                });
                ib1.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int action = event.getAction();
                        switch(action) {
                            case MotionEvent.ACTION_DOWN:
                                v.setPressed(true);
                                soundPool.play(soundID_C4, 1, 1, 0, 0, 1);
                                ib1.setBackgroundResource(R.drawable.white_selector);
                                ib2.setBackgroundResource(R.drawable.red_key);
                                break;
                            case MotionEvent.ACTION_UP:
                                v.setPressed(false);
                                ib1.setBackgroundResource(R.drawable.white_selector);
                                break;
                        }
                        return true;
                    }

                });
                ib2.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int action = event.getAction();
                        switch(action) {
                            case MotionEvent.ACTION_DOWN:
                                soundPool.play(soundID_D4, 1, 1, 0, 0, 1);
                                ib2.setBackgroundResource(R.drawable.white_selector);
                                ib5.setBackgroundResource(R.drawable.red_key);
                                break;
                            case MotionEvent.ACTION_UP:
                                v.setPressed(false);
                                ib2.setBackgroundResource(R.drawable.white_selector);
                                break;
                        }
                        return true;
                    }

                });

                ib5.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int action = event.getAction();
                        switch (action) {
                            case MotionEvent.ACTION_DOWN:
                                v.setPressed(true);
                                soundPool.play(soundID_E4, 1, 1, 0, 0, 1);
                                ib5.setBackgroundResource(R.drawable.white_selector);
                                ib1.setBackgroundResource(R.drawable.red_key);
                                break;
                            case MotionEvent.ACTION_UP:
                                v.setPressed(false);
                                ib5.setBackgroundResource(R.drawable.white_selector);
                                break;
                        }
                        return true;
                           }

                });
            }
            if (counter == 40){
                mediaPlayer = MediaPlayer.create(this, R.raw.crowd_applause);
                mediaPlayer.start();
            }
        }
        else if (v == previous){
            counter--;
            if (counter <= 0){
                //reopen current intent
                counter = 1;

            }else if ( counter >= 0)
                viewFlipper.showPrevious();
            if(counter == 5){
                ib1.setBackgroundResource(R.drawable.red_key);
            }if (counter != 5){
                ib1.setBackgroundResource(R.drawable.white_selector);
            }
            if (counter == 7){
                ib27.setBackgroundResource(R.drawable.red_key);
            }
            if (counter != 7){
                ib27.setBackgroundResource(R.drawable.white_selector);
            }
        }

    }

    public void stop(){
        if(mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void high_octave(View view){
        keyboard_Counter++;
        if(keyboard_Counter > 2){
            keyboard_Counter = 2;
        }else
            keyboardFlipper.showNext();
    }

    public void low_octave(View view){
        keyboard_Counter--;
        if (keyboard_Counter < 0){
            keyboard_Counter = 0;
        }
        else
            keyboardFlipper.showPrevious();
    }

    public void record(View view){

        if (recordButton.getText().toString().equals("Record")){
            if (!beginRecordingIfPermitted(recordButton)) {
                return;
            }
            recordButton.setText("Stop");
            Toast.makeText(this, "Recording Audio", Toast.LENGTH_SHORT).show();
        }
        else if (recordButton.getText().toString().equals("Stop")){
            stopRecord();
            recordButton.setText("Play");
        }
        else if (recordButton.getText().toString().equals("Play")){
            try {
                playRecord();
                recordButton.setText("Record");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            recordButton.setText("Record");

        }
    }

    public void startRecord(){
        recordingHelper.startRecording();
    }

    public void stopRecord(){
        recordingHelper.stopRecording();
    }

    public void playRecord() throws IOException {
        recordingHelper.playRecording();
    }


//    public void C4(View view){
//        //stop();
//        mediaPlayer = MediaPlayer.create(this, R.raw.c4);
//        mediaPlayer.start();
//    }

    @Override
    protected void onDestroy() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
        stop();
        super.onDestroy();
    }
}
