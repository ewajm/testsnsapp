package com.manekej.testsnsapp.tools;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.CreatePlatformApplicationRequest;
import com.amazonaws.services.sns.model.CreatePlatformApplicationResult;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ewa on 3/6/2017.
 */

public class AWSCreateApplicationTask extends AsyncTask<String, Void,  CreatePlatformApplicationResult> {
    Context context;
    private final AmazonSNS snsClient;
    public static final String TAG = "Create Application Task";

    public AWSCreateApplicationTask(Context context, AmazonSNS client){

        super();

        this.context = context;
        this.snsClient = client;

    }

    @Override

    protected CreatePlatformApplicationResult doInBackground(String[] params ) {
        if(params.length < 3){

            return null;

        }

        String applicationName = params[0];

        String principal = params[1];

        String credential = params[2];
        try {

            CreatePlatformApplicationRequest request = new CreatePlatformApplicationRequest();
            Log.i(TAG, "doInBackground: inside task" + applicationName + " " + credential);
            Map<String, String> attributes = new HashMap<String, String>();
            attributes.put("PlatformPrincipal", principal);
            attributes.put("PlatformCredential", credential);
            request.setAttributes(attributes);
            request.setName(applicationName);
            request.setPlatform("GCM");
            return snsClient.createPlatformApplication(request);

        }catch(Exception ex){
            Log.i(TAG, "doInBackground: " + ex.getMessage());
            return null;

        }

    }

    @Override

    protected void onPostExecute(CreatePlatformApplicationResult result) {

        if(result != null) {
            Log.i(TAG, "onPostExecute: " + result.getPlatformApplicationArn());

//            SharedPreferences prefs = context.getSharedPreferences( “my_prefs” , Context.MODE_PRIVATE );
//
//            String endpointArn = result.getEndpointArn();
//
//            prefs.edit().putString( context.getString(R.string.endpoint_arn), endpointArn ).apply();

        } else {
            Log.i(TAG, "onPostExecute: failed");
        }

    }

}
