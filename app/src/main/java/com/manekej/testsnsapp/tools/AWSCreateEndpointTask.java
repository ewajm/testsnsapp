package com.manekej.testsnsapp.tools;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.CreatePlatformEndpointRequest;
import com.amazonaws.services.sns.model.CreatePlatformEndpointResult;

import static com.google.android.gms.internal.zzs.TAG;

/**
 * Created by Ewa on 3/6/2017.
 */

public class AWSCreateEndpointTask extends AsyncTask<String, Void,  CreatePlatformEndpointResult> {

    Context context;
    private final AmazonSNS snsClient;

    public AWSCreateEndpointTask(Context context, AmazonSNS client){

        super();

        this.context = context;
        this.snsClient = client;

    }

    @Override

    protected CreatePlatformEndpointResult doInBackground( String[] params ) {

        if(params.length < 3){

            return null;

        }

        String arn = params[0];

        String gcmToken = params[1];

        String userData = params[2];

        try {

            CreatePlatformEndpointRequest request = new CreatePlatformEndpointRequest();

            request.setCustomUserData(userData);

            request.setToken(gcmToken);

            request.setPlatformApplicationArn(arn);

            return snsClient.createPlatformEndpoint(request);

        }catch(Exception ex){

            return null;

        }

    }

    @Override

    protected void onPostExecute(CreatePlatformEndpointResult result) {

        if(result != null) {
            Log.i(TAG, "onPostExecute: " + result.getEndpointArn());

//            SharedPreferences prefs = context.getSharedPreferences( “my_prefs” , Context.MODE_PRIVATE );
//
//            String endpointArn = result.getEndpointArn();
//
//            prefs.edit().putString( context.getString(R.string.endpoint_arn), endpointArn ).apply();

        }

    }

}
