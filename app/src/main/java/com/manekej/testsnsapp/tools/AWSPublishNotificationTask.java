package com.manekej.testsnsapp.tools;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

import java.util.HashMap;
import java.util.Map;

import static com.google.android.gms.internal.zzs.TAG;

/**
 * Created by Ewa on 3/8/2017.
 */

public class AWSPublishNotificationTask extends AsyncTask<String, Void, PublishResult> {
    Context context;
    private final AmazonSNS snsClient;

    public AWSPublishNotificationTask(Context context, AmazonSNS client){

        super();

        this.context = context;
        this.snsClient = client;

    }

    @Override
    protected PublishResult doInBackground(String[] params) {
        if(params.length < 1){

            return null;

        }

        String endpoint = params[0];
        try {
            PublishRequest publishRequest = new PublishRequest();
            publishRequest.setMessageStructure("json");
            // If the message attributes are not set in the requisite method,
            // notification is sent with default attributes
            String message = SampleMessageGenerator.getSampleAndroidMessage();
            Map<String, String> messageMap = new HashMap<String, String>();
            messageMap.put("GCM", message);
            message = SampleMessageGenerator.jsonify(messageMap);
            // For direct publish to mobile end points, topicArn is not relevant.
            publishRequest.setTargetArn(endpoint);

            // Display the message that will be sent to the endpoint/
            Log.i(TAG, "{Message Body: " + message + "}");

            publishRequest.setMessage(message);
            return snsClient.publish(publishRequest);
        } catch(Exception ex){
            Log.i(TAG, "doInBackground: " + ex.getMessage());
            return null;

        }
    }

    @Override

    protected void onPostExecute(PublishResult result) {

        if(result != null) {
            Log.i(TAG, "onPostExecute: " + result.getMessageId());
        }
    }
}
