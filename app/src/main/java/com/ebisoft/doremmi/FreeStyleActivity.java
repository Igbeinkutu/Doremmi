package com.ebisoft.doremmi;

import android.os.Bundle;

public class FreeStyleActivity extends PianoBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_style);
        setupToolbar(R.string.section_free_play);
        setupPiano();
    }
}
