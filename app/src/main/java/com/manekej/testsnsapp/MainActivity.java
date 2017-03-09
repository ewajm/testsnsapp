package com.manekej.testsnsapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.google.firebase.iid.FirebaseInstanceId;
import com.manekej.testsnsapp.tools.AWSCreateApplicationTask;
import com.manekej.testsnsapp.tools.AWSCreateEndpointTask;
import com.manekej.testsnsapp.tools.AWSPublishNotificationTask;
import com.manekej.testsnsapp.tools.AWSRemoveEndpointTask;

//TODO: abstract to any endpoint

public class MainActivity extends AppCompatActivity {
    AmazonSNS mSns;
    private static final String TAG = MainActivity.class.getSimpleName();
    private String mEndpointArn;

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
        Button createEndpointButton = (Button) findViewById(R.id.createEndpointButton);
        Button removeEndpointButton = (Button) findViewById(R.id.removeEndpointButton);
        mSns = new AmazonSNSClient(credentialsProvider);
        new AWSCreateApplicationTask(this, mSns).execute("testsnsapp", "", "apiKey");
        final SharedPreferences prefs = this.getSharedPreferences("my_prefs",  Context.MODE_PRIVATE);
        mEndpointArn = prefs.getString(this.getString(R.string.endpoint_arn), "");
        Log.i(TAG, "onCreate: " + mEndpointArn);
        createEndpointButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mEndpointArn == null || mEndpointArn.length() == 0) {
                    new AWSCreateEndpointTask(getApplicationContext(), mSns).execute("registration id", "Me");
                }
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AWSPublishNotificationTask(getApplicationContext(), mSns).execute(mEndpointArn);
            }
        });
        removeEndpointButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: " + new AWSRemoveEndpointTask(getApplicationContext(), mSns).execute(mEndpointArn));
                mEndpointArn = "";
            }
        });

    }

}