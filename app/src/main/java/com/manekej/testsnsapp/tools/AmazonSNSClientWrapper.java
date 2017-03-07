package com.manekej.testsnsapp.tools;

/*
 * Copyright 2014 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

import android.util.Log;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.CreatePlatformApplicationRequest;
import com.amazonaws.services.sns.model.CreatePlatformApplicationResult;
import com.amazonaws.services.sns.model.CreatePlatformEndpointRequest;
import com.amazonaws.services.sns.model.CreatePlatformEndpointResult;
import com.amazonaws.services.sns.model.DeletePlatformApplicationRequest;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

import java.util.HashMap;
import java.util.Map;

import static com.google.android.gms.internal.zzs.TAG;

public class AmazonSNSClientWrapper {

	private final AmazonSNS snsClient;

	public AmazonSNSClientWrapper(AmazonSNS client) {
		this.snsClient = client;
	}

	private CreatePlatformApplicationResult createPlatformApplication(
			String applicationName, String principal,
			String credential) {
		CreatePlatformApplicationRequest platformApplicationRequest = new CreatePlatformApplicationRequest();
		Map<String, String> attributes = new HashMap<String, String>();
		attributes.put("PlatformPrincipal", principal);
		attributes.put("PlatformCredential", credential);
		platformApplicationRequest.setAttributes(attributes);
		platformApplicationRequest.setName(applicationName);
		platformApplicationRequest.setPlatform("GCM");
		return snsClient.createPlatformApplication(platformApplicationRequest);
	}

	private CreatePlatformEndpointResult createPlatformEndpoint(
			String customData, String platformToken,
			String applicationArn) {
		CreatePlatformEndpointRequest platformEndpointRequest = new CreatePlatformEndpointRequest();
		platformEndpointRequest.setCustomUserData(customData);
		String token = platformToken;
		String userId = null;
		platformEndpointRequest.setToken(token);
		platformEndpointRequest.setPlatformApplicationArn(applicationArn);
		return snsClient.createPlatformEndpoint(platformEndpointRequest);
	}

	private void deletePlatformApplication(String applicationArn) {
		DeletePlatformApplicationRequest request = new DeletePlatformApplicationRequest();
		request.setPlatformApplicationArn(applicationArn);
		snsClient.deletePlatformApplication(request);
	}

	private PublishResult publish(String endpointArn) {
		PublishRequest publishRequest = new PublishRequest();
		publishRequest.setMessageStructure("json");
		// If the message attributes are not set in the requisite method,
		// notification is sent with default attributes
		String message = SampleMessageGenerator.getSampleAndroidMessage();
		Map<String, String> messageMap = new HashMap<String, String>();
		messageMap.put("GCM", message);
		message = SampleMessageGenerator.jsonify(messageMap);
		// For direct publish to mobile end points, topicArn is not relevant.
		publishRequest.setTargetArn(endpointArn);

		// Display the message that will be sent to the endpoint/
		Log.i(TAG, "{Message Body: " + message + "}");

		publishRequest.setMessage(message);
		return snsClient.publish(publishRequest);
	}

	public void demoNotification(final String principal,
								 final String credential, final String platformToken, final String applicationName) {

                // Create Platform Application. This corresponds to an app on a
                // platform.
				CreatePlatformApplicationResult platformApplicationResult = createPlatformApplication(
						applicationName, principal, credential);
				Log.i(TAG, platformApplicationResult.toString());

				// The Platform Application Arn can be used to uniquely identify the
				// Platform Application.
				String platformApplicationArn = platformApplicationResult
						.getPlatformApplicationArn();

				// Create an Endpoint. This corresponds to an app on a device.
				CreatePlatformEndpointResult platformEndpointResult = createPlatformEndpoint(
						"CustomData - Useful to store endpoint specific data",
						platformToken, platformApplicationArn);
				Log.i(TAG, platformEndpointResult.toString());

				// Publish a push notification to an Endpoint.
				PublishResult publishResult = publish(
						platformEndpointResult.getEndpointArn());
				Log.i(TAG, "Published! \n{MessageId="
						+ publishResult.getMessageId() + "}");
				// Delete the Platform Application since we will no longer be using it.
				deletePlatformApplication(platformApplicationArn);
	}

}
