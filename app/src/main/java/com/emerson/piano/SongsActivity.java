package com.emerson.piano;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class SongsActivity extends PianoBaseActivity {

    private MediaPlayer mediaPlayer;
    private String FILE;
    //Button mediaPlayerControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs);

        //final Button mediaControl = (Button)mediaPlayerControl.findViewById(R.id.mediaControl);

        String[] songs = {"alla_turca", "arabesque1", "autumn", "autumn_piano_arrangement", "beethoven_minuet_in_g",
        "beethoven_symphony9_4_ode_to_joy_piano_solo", "book1_prelude01", "brahms_lullaby_wiegenlied_piano", "fur_elise",
        "grieg_lyric_piece_op12_no1_arietta", "handel_water_music_hornpipe_piano", "haydn_piano_sonata_31_1",
        "mozart_minuet_k2", "mozart_piano_sonata_k333_movement1", "mozart_symphony40_1_piano_solo",
        "notebook2_4_minuet_by_bach", "notebook2_7_minuet", "notebook2_16_march_beethoven", "notebook2_16_march_bach",
        "pachelbels_canon_arranged", "reverie_traumerei", "scott_joplin_peacherine_rag", "sonata_in_c","sugar_plum_fairy_piano",
        "the_blue_danube_piano", "wagner_bridal_chorus"};


        ListAdapter songsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, songs)

        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
            /// Get the Item from ListView
            View view = super.getView(position, convertView, parent);

            TextView tv = (TextView) view.findViewById(android.R.id.text1);

            // Set the text size 30 dip for ListView each item
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,30);

                tv.setTextColor(Color.BLACK);

            // Return the view
            return view;
        }
        };

        ListView songsListView = (ListView)findViewById(R.id.songListView);
        songsListView.setAdapter(songsAdapter);

        songsListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        String song = String.valueOf(parent.getItemAtPosition(position));
                        Toast.makeText(SongsActivity.this, "Playing "+ song, Toast.LENGTH_LONG).show();


                        //mediaPlayer = MediaPlayer.create(getApplicationContext(), getResources().getIdentifier(song,"raw",getPackageName()));

                        FILE = "android.resource://" +getPackageName() + "/raw/"+song;
                        stop();
                        mediaPlayer = new MediaPlayer();
                        try {
                            mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(FILE));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            mediaPlayer.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mediaPlayer.start();
                        //mediaPlayerControl.setEnabled(true);

                    }
                }
        );

    }

    public void stopSong(View view){

        //if (mediaPlayer.isPlaying()){
            stop();
            //mediaPlayerControl.setEnabled(false);
        //}

    }

    public void stop(){
        if(mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        stop();
        super.onDestroy();
    }
}
