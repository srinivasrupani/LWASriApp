package com.rupani.lwasriapp;

import android.content.Context;
import android.util.Log;

import com.amazon.identity.auth.device.AuthError;
import com.amazon.identity.auth.device.api.authorization.AuthCancellation;
import com.amazon.identity.auth.device.api.authorization.AuthorizationManager;
import com.amazon.identity.auth.device.api.authorization.AuthorizeListener;
import com.amazon.identity.auth.device.api.authorization.AuthorizeResult;
import com.amazon.identity.auth.device.api.authorization.Scope;
import com.amazon.identity.auth.device.api.authorization.ScopeFactory;

/**
 * Created by Srinivas Rupani on 13/12/18.
 * Copyright (c) 2018 Smartron, All rights reserved.
 */
public class AuthorizeListenerImpl  extends AuthorizeListener {
    private Context mContext;
    public AuthorizeListenerImpl(Context _context) {
        this.mContext=_context;
    }

    /* Authorization was completed successfully. */
    @Override
    public void onSuccess(final AuthorizeResult authorizeResult) {
        AuthorizationManager.getToken(mContext, new Scope[] { ScopeFactory.scopeNamed("alexa:all") }, new TokenListener());
    }

    /* There was an error during the attempt to authorize the application. */
    @Override
    public void onError(final AuthError authError) {
        Log.e("","==>"+authError);
    }

    /* Authorization was cancelled before it could be completed. */
    @Override
    public void onCancel(final AuthCancellation authCancellation) {
        Log.e("","==>"+authCancellation);
    }
}