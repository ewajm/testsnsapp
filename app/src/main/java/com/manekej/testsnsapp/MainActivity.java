package com.manekej.testsnsapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.google.firebase.iid.FirebaseInstanceId;
import com.manekej.testsnsapp.tools.AWSCreateApplicationTask;
import com.manekej.testsnsapp.tools.AmazonSNSClientWrapper;

public class MainActivity extends AppCompatActivity {
    AmazonSNS mSns;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "us-west-2:7457534f-8671-41e3-abad-81ecc85b91db", // Identity Pool ID
                Regions.US_WEST_2 // Region
        );
        Log.i(TAG, "registration: " + FirebaseInstanceId.getInstance().getToken());
        Button button = (Button) findViewById(R.id.logTokenButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get token
                String token = FirebaseInstanceId.getInstance().getToken();

                Log.d(TAG, token);
                Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
            }
        });
        Button sendButton = (Button) findViewById(R.id.sendButton);
        mSns = new AmazonSNSClient(new BasicAWSCredentials("", ""));
        final AmazonSNSClientWrapper wrapper = new AmazonSNSClientWrapper(mSns);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AWSCreateApplicationTask(getApplicationContext(), mSns).execute("testsnsapp", "", "");
            }
        });

    }

    public void demoAndroidAppNotification(AmazonSNSClientWrapper snsClientWrapper) {
        // TODO: Please fill in following values for your application. You can
        // also change the notification payload as per your preferences using
        // the method
        // com.amazonaws.sns.samples.tools.SampleMessageGenerator.getSampleAndroidMessage()
        String serverAPIKey = "";
        String applicationName = "testsnsapp";
        String registrationId = "";
        snsClientWrapper.demoNotification("", serverAPIKey,
                registrationId, applicationName);
    }
    //
}