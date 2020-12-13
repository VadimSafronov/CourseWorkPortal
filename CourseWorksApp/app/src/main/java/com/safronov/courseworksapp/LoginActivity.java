package com.safronov.courseworksapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.safronov.courseworksapp.Helpers.DatabaseContext;
import com.safronov.courseworksapp.Helpers.DatabaseOperations;
import com.safronov.courseworksapp.Helpers.HashingHepler;
import com.safronov.courseworksapp.Helpers.KeyboardHepler;
import com.safronov.courseworksapp.Helpers.NetworkHelper;
import com.safronov.courseworksapp.Helpers.SnackbarHelper;
import com.safronov.courseworksapp.Models.Token;
import com.safronov.courseworksapp.Models.User;
import com.android.volley.ClientError;
import com.android.volley.Request;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button signInButton, signUpButton;
    private EditText usernameEditText, passEditText;
    private TextView textView;
    private TextInputLayout usernameTextInputLayout, passTextInputLayout;
    private ProgressBar progressBar;

    private String portal_url;
    private String url_get_token;
    private String url_post_user;

    private DatabaseOperations dbOp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);

        signInButton = findViewById(R.id.signInButton);
        signUpButton = findViewById(R.id.signUpButton);

        passEditText = findViewById(R.id.passEditText);
        usernameEditText = findViewById(R.id.usernameEditText);

        textView = findViewById(R.id.textView);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        passTextInputLayout = findViewById(R.id.passTextInputLayout);
        usernameTextInputLayout = findViewById(R.id.usernameTextInputLayout);

        signInButton.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        portal_url = getApplicationContext().getString(R.string.portal_url);
        url_get_token = portal_url + "/token";
        url_post_user = portal_url + "/api/Users/Post";

        dbOp = new DatabaseOperations(this);
    }

    @Override
    public void onClick(View v) {

        String username = usernameEditText.getText().toString();
        String pass = passEditText.getText().toString();
        String hash_pass = HashingHepler.getHash(username, pass);

        if (NetworkHelper.isNetConn(this)) {
            switch (v.getId()) {
                case R.id.signInButton:
                    KeyboardHepler.closeKeyboard(this);
                    new LoginAsync().execute("signIn", username, hash_pass);
                    break;
                case R.id.signUpButton:
                    KeyboardHepler.closeKeyboard(this);
                    new LoginAsync().execute("signUp", username, hash_pass);
                    break;
            }
        } else {

            // закрываем клавиатуру (если открыта)
            KeyboardHepler.closeKeyboard(this);

            // открываем снакбар с выбранным цветом
            SnackbarHelper.showStaticSnackbar(v, this);
        }
    }

    private class LoginAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
            passTextInputLayout.setError(null);
        }

        @Override
        protected String doInBackground(final String... par) {
            if (par[0].equals("signIn")) return signIn(par[1], par[2]);
            else return signUp(par[1], par[2]);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            passTextInputLayout.setError(values[0]);
        }

        @Override
        protected void onPostExecute(final String token) {
            if (token != null) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
            progressBar.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        }

        private String signIn(final String username, final String pass) {
            RequestFuture<String> requestFuture = RequestFuture.newFuture();
            StringRequest requestToken = new StringRequest(Request.Method.POST, url_get_token, requestFuture, requestFuture) {
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("grant_type", "password");
                    params.put("username", username);
                    params.put("password", pass);
                    return params;
                }
            };

            Volley.newRequestQueue(getApplicationContext()).add(requestToken);

            try {
                String response = requestFuture.get(10, TimeUnit.SECONDS);
                String token = new GsonBuilder().create().fromJson(response, Token.class).access_token;
                String id = new JSONObject(response).get("id").toString();

                // удаляем предыдущего юзера и сохраняем нового в базе вместе с токеном
                dbOp.dropTable(DatabaseContext.T_USERS);
                dbOp.insertUser(id, username, pass, token);
                return token;
            } catch (ExecutionException e) {
                if (e.getCause() instanceof ClientError) {
                    ClientError error = (ClientError) e.getCause();
                    switch (error.networkResponse.statusCode) {
                        case 400:
                            publishProgress("Wrong login or password");
                            break;
                        case 500:
                            publishProgress("Code 500. Internal Server Error");
                            break;
                        default:
                            publishProgress("Unexpected error. Try again");
                            break;
                    }
                }
            } catch (TimeoutException e) {
                publishProgress("Timeout exceeded");
            } catch (Exception e) {
            }
            return null;
        }

        private String signUp(final String username, final String pass) {
            RequestFuture<String> requestFuture = RequestFuture.newFuture();
            StringRequest requestToken = new StringRequest(Request.Method.POST, url_post_user, requestFuture, requestFuture) {

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() {
                    User newUser = new User(username, pass, "user");
                    String data = new GsonBuilder().create().toJson(newUser);
                    byte[] b = new byte[0];
                    try {
                        b = data.getBytes("utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    return b;
                }
            };

            Volley.newRequestQueue(getApplicationContext()).add(requestToken);

            try {
                requestFuture.get(10, TimeUnit.SECONDS);
                return signIn(username, pass);
            } catch (ExecutionException e) {
                if (e.getCause() instanceof ClientError) {
                    ClientError error = (ClientError) e.getCause();
                    switch (error.networkResponse.statusCode) {
                        case 404:
                            publishProgress("This username already exists");
                            break;
                        default:
                            publishProgress("Unexpected error. Try again");
                            break;
                    }
                }
            } catch (TimeoutException e) {
                publishProgress("Timeout exceeded");
            } catch (Exception e) {
            }
            return null;
        }
    }
}
