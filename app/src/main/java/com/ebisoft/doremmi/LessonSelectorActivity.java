package com.ebisoft.doremmi;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LessonSelectorActivity extends PianoBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_selector);

        String[] lessons = {"Lesson 1", "Lesson 2", "Lesson 3", "Lesson 4", "Lesson 5"};

        ListAdapter lessonsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, lessons)

        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                /// Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                // Set the text size 25 dip for ListView each item
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,30);

                tv.setTextColor(Color.BLACK);

                // Return the view
                return view;
            }
        };

        ListView lessonsListView = (ListView)findViewById(R.id.lessonsListView);
        lessonsListView.setAdapter(lessonsAdapter);

        lessonsListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String lesson = String.valueOf(parent.getItemAtPosition(position));

                        if( lesson == "Lesson 1"){
                            Intent intent = new Intent(LessonSelectorActivity.this, Lesson_1Activity.class);
                            startActivity(intent);
                            Toast.makeText(LessonSelectorActivity.this,lesson, Toast.LENGTH_SHORT).show();
                        }
                        else if( lesson == "Lesson 2"){
                            Intent intent = new Intent(LessonSelectorActivity.this, Lesson_2Activity.class);
                            startActivity(intent);
                            Toast.makeText(LessonSelectorActivity.this,lesson, Toast.LENGTH_LONG).show();
                        }
                        else if( lesson == "Lesson 3"){
                            Intent intent = new Intent(LessonSelectorActivity.this, Lesson_3Activity.class);
                            startActivity(intent);
                            Toast.makeText(LessonSelectorActivity.this,lesson, Toast.LENGTH_LONG).show();
                        }
                        else if( lesson == "Lesson 4"){
                            Intent intent = new Intent(LessonSelectorActivity.this, Lesson_4Activity.class);
                            startActivity(intent);
                            Toast.makeText(LessonSelectorActivity.this,lesson, Toast.LENGTH_LONG).show();
                        }
                        else if( lesson == "Lesson 5"){
                            Intent intent = new Intent(LessonSelectorActivity.this, Lesson_5Activity.class);
                            startActivity(intent);
                            Toast.makeText(LessonSelectorActivity.this,lesson, Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );

    }
}
