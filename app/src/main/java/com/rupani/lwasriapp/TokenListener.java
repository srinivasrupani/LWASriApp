package com.rupani.lwasriapp;

import android.util.Log;

import com.amazon.identity.auth.device.AuthError;
import com.amazon.identity.auth.device.api.Listener;
import com.amazon.identity.auth.device.api.authorization.AuthorizeResult;

/**
 * Created by Srinivas Rupani on 13/12/18.
 * Copyright (c) 2018 Smartron, All rights reserved.
 */
public class TokenListener implements Listener<AuthorizeResult, AuthError> {
    /* getToken completed successfully. */
    @Override
    public void onSuccess(AuthorizeResult authorizeResult) {
        String accessToken = authorizeResult.getAccessToken();
    }

    /* There was an error during the attempt to get the token. */
    @Override
    public void onError(AuthError authError) {
        Log.e("","==>"+authError);
    }
}