package com.manekej.testsnsapp.tools;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.DeleteEndpointRequest;
import com.manekej.testsnsapp.R;

import static com.google.android.gms.internal.zzs.TAG;

/**
 * Created by Ewa on 3/8/2017.
 */

public class AWSRemoveEndpointTask extends AsyncTask<String, Void, Boolean> {

    Context context;
    AmazonSNS client;

    public AWSRemoveEndpointTask(Context context, AmazonSNS client ){

        super();

        this.context = context;
        this.client = client;

    }

    @Override

    protected Boolean doInBackground( String[] params ) {

        if(params.length < 1){

            return false;

        }

        String arn = params[0];

        if(TextUtils.isEmpty(arn)){

            return false;

        }

        try {

            DeleteEndpointRequest request = new DeleteEndpointRequest();

            request.setEndpointArn(arn);

            client.deleteEndpoint(request);

            context.getSharedPreferences( "my_prefs" , Context.MODE_PRIVATE ).edit().remove( context.getString(R.string.endpoint_arn)).apply();

            return true;

        }catch(Exception ex){
            Log.i(TAG, "doInBackground: " + ex.getMessage());
            return false;

        }

    }

}