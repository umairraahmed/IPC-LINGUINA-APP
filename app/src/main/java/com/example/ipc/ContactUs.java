package com.example.ipc;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ContactUs extends AppCompatActivity {

    private TextView Info1;
    private Button azfarInfo;
    private TextView Info2;
    private Button umairInfo;
    private TextView Info3;
    private Button osamaInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_us);

        Info1 = findViewById(R.id.Info1);
        azfarInfo = findViewById(R.id.azfarInfo);

        Info2 = findViewById(R.id.Info2);
        umairInfo = findViewById(R.id.umairInfo);

        Info3 = findViewById(R.id.Info3);
        osamaInfo = findViewById(R.id.osamaInfo);
    }

    public void toggleAnswerVisibility1(View view) {
        if (Info1.getVisibility() == View.VISIBLE) {
            Info1.setVisibility(View.GONE);
            azfarInfo.setText("Azfar Nasir");
        } else {
            Info1.setVisibility(View.VISIBLE);
            azfarInfo.setText("Azfar Nasir");
        }
    }

        public void toggleAnswerVisibility2 (View view){
            if (Info2.getVisibility() == View.VISIBLE) {
                Info2.setVisibility(View.GONE);
                umairInfo.setText("Umair Ahmed");
            } else {
                Info2.setVisibility(View.VISIBLE);
                umairInfo.setText("Umair Ahmed");
            }
        }

        public void toggleAnswerVisibility3 (View view){
            if (Info3.getVisibility() == View.VISIBLE) {
                Info3.setVisibility(View.GONE);
                osamaInfo.setText("Osama Waheed");
            } else {
                Info3.setVisibility(View.VISIBLE);
                osamaInfo.setText("Osama Waheed");
            }
        }
    }
