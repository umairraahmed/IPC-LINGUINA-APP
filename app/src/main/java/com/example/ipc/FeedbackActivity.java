package com.example.ipc;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ipc.Feedback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
public class FeedbackActivity extends AppCompatActivity {
    private EditText uxRatingEditText;
    private EditText communicationRatingEditText;
    private EditText recommendEditText;
    private EditText additionalCommentsEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);

        // Initialize your EditText fields
        uxRatingEditText = findViewById(R.id.tv_UX_detail);
        communicationRatingEditText = findViewById(R.id.tv_Comment_detail);
        recommendEditText = findViewById(R.id.tv_Recommend_detail);
        additionalCommentsEditText = findViewById(R.id.tv_Share_more_detail);

        // Handle the "Submit Feedback" button click
        findViewById(R.id.Submit_Feedback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitFeedback();
            }
        });
    }
    private void submitFeedback() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            String userUid = user.getUid();
            String uxRating = uxRatingEditText.getText().toString();
            String communicationRating = communicationRatingEditText.getText().toString();
            String recommend = recommendEditText.getText().toString();
            String additionalComments = additionalCommentsEditText.getText().toString();

            Feedback feedback = new Feedback(userUid, uxRating, communicationRating, recommend, additionalComments);

            // Get the Firestore instance
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Add the feedback to Firestore
            db.collection("feedback")
                    .add(feedback.toMap())
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                // Feedback added successfully
                                Toast.makeText(FeedbackActivity.this, "Feedback submitted successfully!", Toast.LENGTH_LONG).show();
                            } else {
                                // Handle the error
                                Exception exception = task.getException();
                                if (exception != null) {
                                    // Log or display the error message
                                    String errorMessage = exception.getMessage();
                                    Toast.makeText(FeedbackActivity.this, "Failed to submit feedback. Error: " + errorMessage, Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
        }
    }
}
