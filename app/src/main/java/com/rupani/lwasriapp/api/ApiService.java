package com.rupani.lwasriapp.api;

import com.rupani.lwasriapp.model.TokenResponse;
import com.rupani.lwasriapp.model.TokensRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Srinivas Rupani on 19/12/18.
 * Copyright (c) 2018 Smartron, All rights reserved.
 */
public interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("auth/O2/token")
    Call<TokenResponse> getTokensFromAlexa(@Body TokensRequest tokensRequest);
}
