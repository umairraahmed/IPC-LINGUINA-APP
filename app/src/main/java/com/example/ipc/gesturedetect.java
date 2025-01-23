package com.example.ipc;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ipc.ml.Detect;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class gesturedetect extends AppCompatActivity {

    Button capturebtn, PredictBtn, loadimagebtn;
    ImageView imageV;
    TextView resultT;
    Bitmap bitmap;

    private String[] readLabelsFromAsset(String fileName) {
        try {
            InputStream inputStream = getAssets().open(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            List<String> labelsList = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                labelsList.add(line.trim());
            }
            reader.close();
            return labelsList.toArray(new String[0]);
        } catch (IOException e) {
            e.printStackTrace();
            return new String[0];
        }
    }

    private int getMax(float[] array) {
        int maxIndex = 0;
        float max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((R.layout.activity_gesturedetect));

        int cnt = 0;

        capturebtn = findViewById(R.id.capturebtn);
        PredictBtn = findViewById(R.id.PredictBtn);
        loadimagebtn = findViewById(R.id.loadimagebtn);
        imageV = findViewById(R.id.imageV);
        resultT = findViewById(R.id.resultT);

        String[] labels = {"Hello", "Yes", "No", "Thanks", "I Love You"};

        loadimagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 10);

            }
        });

        capturebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,12);
            }
        });


        PredictBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Detect model = null;
                TensorBuffer inputFeature0 = null;

                try {
                    model = Detect.newInstance(gesturedetect.this);

                    // Expected input size for your model
                    int expectedInputSize = 320 * 320 * 3 * 4; // Adjust this size based on your model's input size

                    // Creates inputs for reference.
                    inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 320, 320, 3}, DataType.FLOAT32);

                    bitmap = Bitmap.createScaledBitmap(bitmap, 320, 320, true);

                    // Initialize a new TensorImage object
                    TensorImage tensorImage = new TensorImage(DataType.FLOAT32);

                    // Ensure that the input data has the correct size
                    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(expectedInputSize);
                    byteBuffer.order(ByteOrder.nativeOrder());

                    // Flatten the bitmap data and put it into the input tensor
                    tensorImage.load(bitmap);  // Load the bitmap directly
                    tensorImage.getBuffer().rewind();  // Rewind the buffer
                    tensorImage.getBuffer().put(byteBuffer);  // Copy the data from ByteBuffer

                    // Log input data for debugging
                    Log.d("InputData", Arrays.toString(tensorImage.getBuffer().array()));

                    // Set the input tensor for the model
                    inputFeature0.loadBuffer(tensorImage.getBuffer());

                    // Runs model inference and gets result.
                    Detect.Outputs outputs = model.process(inputFeature0);

                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                    // Log raw model output for debugging
                    Log.d("ModelOutput", Arrays.toString(outputFeature0.getFloatArray()));

                    float[] outputArray = outputFeature0.getFloatArray();

                    if (outputArray != null && outputArray.length > 0) {
                        // Debug statement to print the maxClassIndex and corresponding label
                        int maxClassIndex = getMax(outputArray);

                        // Read labels from the file
                        String[] labels = readLabelsFromAsset("labels.txt");

                        if (maxClassIndex >= 0 && maxClassIndex < labels.length) {
                            String result = labels[maxClassIndex];
                            Log.d("Debug", "Result: " + result);
                            resultT.setText(result);
                        } else {
                            resultT.setText("Invalid class index");
                        }
                    } else {
                        resultT.setText("Result is null or empty.");
                    }

                } catch (IOException e) {
                    e.printStackTrace(); // Log the error for debugging
                    resultT.setText("Error: Model initialization failed.");
                    // Handle the exception (e.g., show an error message to the user)
                } finally {
                    if (model != null) {
                        model.close();
                    }
                }
            }
        });

    }


    void getPermission(){
        if (checkSelfPermission(Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(gesturedetect.this, new String[]{Manifest.permission.CAMERA}, 11);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==11){
            if(grantResults.length>0){
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    this.getPermission();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==10){
            if(data!=null){
                Uri uri =data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    imageV.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        else if(requestCode==12){
            bitmap = (Bitmap) data.getExtras().get("data");
            imageV.setImageBitmap(bitmap);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}