package com.example.ipc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class TandC extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tandc);

        TextView tncsTextView = findViewById(R.id.TandC_Text);
        tncsTextView.setText(Html.fromHtml(getString(R.string.tncs_content)));
    }
}