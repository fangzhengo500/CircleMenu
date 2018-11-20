package com.loosu.circlemenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private CircleMenu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMenu = (CircleMenu) findViewById(R.id.menu);
        mMenu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mMenu.isOpen()) {
            mMenu.close(true);
        }else {
            mMenu.open(true);
        }
    }
}
