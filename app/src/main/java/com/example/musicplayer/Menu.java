package com.example.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Menu extends AppCompatActivity implements View.OnClickListener  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_author);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                Intent intent2 = new Intent(this, MainActivity.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.logout:
                Intent intent1 = new Intent(this, LoginActivity.class);
                startActivity(intent1);
                finish();
                break;
        }
    }
}
