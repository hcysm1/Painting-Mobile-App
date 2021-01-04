package com.example.painting;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

//Name: Sonia Mubasher
//Student ID: 20129528

import android.widget.EditText;


public class BrushActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brush);

    }

    public void onClick(View view) {
        finish(); //calls this function when the activity is destroyed
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        EditText editText1 = (EditText) findViewById(R.id.enterBrush);
        String returnString1 = editText1.getText().toString(); //get the brush size from user
        data.putExtra("returnData1", returnString1); //pass it to main activity
        EditText editText2 = (EditText) findViewById(R.id.entershape);
        String returnString2 = editText2.getText().toString(); //get the brush shape from user
        data.putExtra("returnData2", returnString2); //pass it to main activity
        setResult(RESULT_OK, data);
        super.finish();
    }
}
