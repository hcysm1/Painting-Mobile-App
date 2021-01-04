

//Name: Sonia Mubasher
//Student ID: 20129528

package com.example.painting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class ClrActivity extends AppCompatActivity {
    int color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clr);

    }

    public void setColor(View view) { //set color function called when the user taps on any of the color
        this.color = ((ColorDrawable) view.getBackground()).getColor(); //saves the background color to color variable
        TextView txtView1 = findViewById(R.id.crnt_clr);
        txtView1.setText("Current Color: " + Integer.toHexString(color)); //display it on screen
    }

    public void onClick1(View view) {
        finish();
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        int returnString3 = this.color;
        data.putExtra("returnData3", returnString3); //pass the color to main activity
        setResult(RESULT_OK, data);
        super.finish();
    }
}