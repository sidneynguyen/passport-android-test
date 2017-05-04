package com.mobewash.passportandroidtest;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject body = new JSONObject();
                            body.put("username", "bob");
                            body.put("password", "asdf");
                            URL url = new URL("http://10.0.2.2:3000/login");
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setDoOutput(true);
                            conn.setDoInput(true);
                            CookieManager cookieManager = CookieManager.getInstance();
                            String cookie = cookieManager.getCookie(conn.getURL().toString());
                            if (cookie != null) {
                                conn.setRequestProperty("Cookie", cookie);
                            }
                            conn.setRequestProperty("Content-Type", "application/json");
                            conn.setRequestProperty("Accept", "application/json; charset=UTF-8");
                            conn.setRequestMethod("POST");
                            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                            writer.write(body.toString());
                            writer.flush();
                            List<String> cookieList = conn.getHeaderFields().get("Set-Cookie");
                            if (cookieList != null) {
                                for (String cookieTemp : cookieList) {
                                    cookieManager.setCookie(conn.getURL().toString(), cookieTemp);
                                }
                            }
                            if (conn.getResponseCode() == 200) {
                                InputStream responseBody = conn.getInputStream();
                                InputStreamReader responseBodyReader =
                                        new InputStreamReader(responseBody, "UTF-8");
                                JsonReader jsonReader = new JsonReader(responseBodyReader);
                                jsonReader.beginObject();
                                while (jsonReader.hasNext()) {
                                    String key = jsonReader.nextName();
                                    if (key.equals("username")) {
                                        Log.d(TAG, "username: " + jsonReader.nextString());
                                    } else if (key.equals("password")) {
                                        Log.d(TAG, "password: " + jsonReader.nextString());
                                    } else {
                                        jsonReader.skipValue();
                                    }
                                }
                                jsonReader.close();
                                conn.disconnect();
                            } else {
                                Log.d(TAG, "NOT 200");
                            }
                        } catch (JSONException | IOException e) {
                            Log.e(TAG, "ERROR", e);
                        }
                    }
                });
            }
        });

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL("http://10.0.2.2:3000/me");
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            CookieManager cookieManager = CookieManager.getInstance();
                            String cookie = cookieManager.getCookie(conn.getURL().toString());
                            if (cookie != null) {
                                conn.setRequestProperty("Cookie", cookie);
                            }
                            List<String> cookieList = conn.getHeaderFields().get("Set-Cookie");
                            if (cookieList != null) {
                                for (String cookieTemp : cookieList) {
                                    cookieManager.setCookie(conn.getURL().toString(), cookieTemp);
                                }
                            }
                            if (conn.getResponseCode() == 200) {
                                InputStream responseBody = conn.getInputStream();
                                InputStreamReader responseBodyReader =
                                        new InputStreamReader(responseBody, "UTF-8");
                                JsonReader jsonReader = new JsonReader(responseBodyReader);
                                jsonReader.beginObject();
                                while (jsonReader.hasNext()) {
                                    String key = jsonReader.nextName();
                                    if (key.equals("isAuthenticated")) {
                                        Log.d(TAG, "isAuthenticated: " + jsonReader.nextBoolean());
                                    } else {
                                        jsonReader.skipValue();
                                    }
                                }
                                jsonReader.close();
                                conn.disconnect();
                            } else {
                                Log.d(TAG, "NOT 200 - " + conn.getResponseCode());
                            }
                        } catch (IOException e) {
                            Log.e(TAG, "ERROR", e);
                        }
                    }
                });
            }
        });

        Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL("http://10.0.2.2:3000/logout");
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            CookieManager cookieManager = CookieManager.getInstance();
                            String cookie = cookieManager.getCookie(conn.getURL().toString());
                            if (cookie != null) {
                                conn.setRequestProperty("Cookie", cookie);
                            }
                            List<String> cookieList = conn.getHeaderFields().get("Set-Cookie");
                            if (cookieList != null) {
                                for (String cookieTemp : cookieList) {
                                    cookieManager.setCookie(conn.getURL().toString(), cookieTemp);
                                }
                            }
                            if (conn.getResponseCode() == 200) {
                                InputStream responseBody = conn.getInputStream();
                                InputStreamReader responseBodyReader =
                                        new InputStreamReader(responseBody, "UTF-8");
                                JsonReader jsonReader = new JsonReader(responseBodyReader);
                                jsonReader.beginObject();
                                while (jsonReader.hasNext()) {
                                    String key = jsonReader.nextName();
                                    if (key.equals("isAuthenticated")) {
                                        Log.d(TAG, "isAuthenticated: " + jsonReader.nextBoolean());
                                    } else {
                                        jsonReader.skipValue();
                                    }
                                }
                                jsonReader.close();
                                conn.disconnect();
                            } else {
                                Log.d(TAG, "NOT 200 - " + conn.getResponseCode());
                            }
                        } catch (IOException e) {
                            Log.e(TAG, "ERROR", e);
                        }
                    }
                });
            }
        });

        callbackManager = CallbackManager.Factory.create();

        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "Logged in to Facebook");
                final String token = loginResult.getAccessToken().getToken();
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject body = new JSONObject();
                            body.put("access_token", token);
                            URL url = new URL("http://10.0.2.2:3000/facebook/token");
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setDoOutput(true);
                            conn.setDoInput(true);
                            CookieManager cookieManager = CookieManager.getInstance();
                            String cookie = cookieManager.getCookie(conn.getURL().toString());
                            if (cookie != null) {
                                conn.setRequestProperty("Cookie", cookie);
                            }
                            conn.setRequestProperty("Content-Type", "application/json");
                            conn.setRequestProperty("Accept", "application/json; charset=UTF-8");
                            conn.setRequestMethod("POST");
                            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                            writer.write(body.toString());
                            writer.flush();
                            List<String> cookieList = conn.getHeaderFields().get("Set-Cookie");
                            if (cookieList != null) {
                                for (String cookieTemp : cookieList) {
                                    cookieManager.setCookie(conn.getURL().toString(), cookieTemp);
                                }
                            }
                            if (conn.getResponseCode() == 200) {
                                InputStream responseBody = conn.getInputStream();
                                InputStreamReader responseBodyReader =
                                        new InputStreamReader(responseBody, "UTF-8");
                                JsonReader jsonReader = new JsonReader(responseBodyReader);
                                jsonReader.beginObject();
                                while (jsonReader.hasNext()) {
                                    String key = jsonReader.nextName();
                                    if (key.equals("isAuthenticated")) {
                                        Log.d(TAG, "isAuthenticated: " + jsonReader.nextBoolean());
                                    } else {
                                        jsonReader.skipValue();
                                    }
                                }
                                jsonReader.close();
                                conn.disconnect();
                            } else {
                                Log.d(TAG, "NOT 200 - " + conn.getResponseCode());
                            }
                        } catch (JSONException | IOException e) {
                            Log.e(TAG, "ERROR", e);
                        }
                    }
                });
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
