package com.rupani.lwasriapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.amazon.identity.auth.device.AuthError;
import com.amazon.identity.auth.device.api.authorization.AuthCancellation;
import com.amazon.identity.auth.device.api.authorization.AuthorizationManager;
import com.amazon.identity.auth.device.api.authorization.AuthorizeListener;
import com.amazon.identity.auth.device.api.authorization.AuthorizeRequest;
import com.amazon.identity.auth.device.api.authorization.AuthorizeResult;
import com.amazon.identity.auth.device.api.authorization.Scope;
import com.amazon.identity.auth.device.api.authorization.ScopeFactory;
import com.amazon.identity.auth.device.api.workflow.RequestContext;
import com.rupani.lwasriapp.api.RetroClient;
import com.rupani.lwasriapp.model.TokenResponse;
import com.rupani.lwasriapp.model.TokensRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String PRODUCT_ID = "AVSShifu";
    private static final String PRODUCT_DSN = "ANJRSCKGHYM1O";
    private static final Scope ALEXA_ALL_SCOPE = ScopeFactory.scopeNamed("alexa:all");
    private static final String TAG = MainActivity.class.getSimpleName();
    private ImageButton mLoginButton;
    private RequestContext mRequestContext;
    private String codeChallenge, codeVerifier;

    @Override
    protected void onStart() {
        super.onStart();
        AuthorizationManager.getToken(this, new Scope[]{ALEXA_ALL_SCOPE}, new TokenListener());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestContext = RequestContext.create(this);
        mRequestContext.registerListener(new AuthorizeListener() {
            @Override
            public void onSuccess(final AuthorizeResult authorizeResult) {

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TokensRequest tokensRequest = new TokensRequest();
                        tokensRequest.setClientId(authorizeResult.getClientId());
                        tokensRequest.setCode(authorizeResult.getAuthorizationCode());
                        tokensRequest.setCodeVerifier(codeVerifier);
                        tokensRequest.setRedirectUri(authorizeResult.getRedirectURI());

                        RetroClient.getApiService().getTokensFromAlexa(tokensRequest).enqueue(new Callback<TokenResponse>() {
                            @Override
                            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                                Log.e(TAG,"success :\t" + response.body());
                            }

                            @Override
                            public void onFailure(Call<TokenResponse> call, Throwable t) {
                                Log.e(TAG,"fail :\t" + t);
                            }
                        });
                    }
                });
            }

            @Override
            public void onError(AuthError authError) {

            }

            @Override
            public void onCancel(AuthCancellation authCancellation) {

            }
        });
        codeVerifier = generateCodeVerifier();
        codeChallenge = generateCodeChallenge(codeVerifier, "S256");
        // Find the button with the login_with_amazon ID
        // and set up a click handler
        setContentView(R.layout.activity_main);
        mLoginButton = findViewById(R.id.login_with_amazon);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final JSONObject scopeData = new JSONObject();
                final JSONObject productInstanceAttributes = new JSONObject();

                try {
                    productInstanceAttributes.put("deviceSerialNumber", PRODUCT_DSN);
                    scopeData.put("productInstanceAttributes", productInstanceAttributes);
                    scopeData.put("productID", PRODUCT_ID);

                    AuthorizationManager.authorize(new AuthorizeRequest.Builder(mRequestContext)
                            .addScopes(ScopeFactory.scopeNamed("alexa:voice_service:pre_auth"),
                                    ScopeFactory.scopeNamed("alexa:all", scopeData))
                            .forGrantType(AuthorizeRequest.GrantType.AUTHORIZATION_CODE)
                            .withProofKeyParameters(codeChallenge,"S256")
                            .build());
                } catch (JSONException e) {
                    // handle exception here
                    Log.e("", "==>" + e);
                }
            }
        });
    }

    private String generateCodeVerifier() {
        String randomOctetSequence = generateRandomOctetSequence(100);
        String codeVerifier = base64UrlEncode(randomOctetSequence.getBytes());
        return codeVerifier;
    }

    private String generateRandomOctetSequence(int count) {
        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890-_.~".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }

    private String generateCodeChallenge(String codeVerifier, String codeChallengeMethod) {
        String codeChallenge = "";
        if ("S256".equalsIgnoreCase(codeChallengeMethod)) {
            try {
                byte[] digest = MessageDigest.getInstance("SHA-256").digest(
                        codeVerifier.getBytes());
                codeChallenge = base64UrlEncode(
                        digest);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        } else {
            // Fall back to code_challenge_method = "plain" codeChallenge = codeVerifier;
        }
        return codeChallenge;
    }

    private String base64UrlEncode(byte[] arg) {
        String s = new String(Base64.encode(arg, Base64.URL_SAFE | Base64.NO_PADDING | Base64.NO_WRAP));
        s = s.split("=")[0]; // Remove any trailing '='s
        s = s.replace('+', '-'); // 62nd char of encoding
        s = s.replace('/', '_'); // 63rd char of encoding
        return s;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRequestContext.onResume();
    }
}
