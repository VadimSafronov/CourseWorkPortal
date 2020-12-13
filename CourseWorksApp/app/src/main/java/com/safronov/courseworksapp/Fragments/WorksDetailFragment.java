package com.safronov.courseworksapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.safronov.courseworksapp.Helpers.DatabaseOperations;
import com.safronov.courseworksapp.Helpers.NetworkHelper;
import com.safronov.courseworksapp.Helpers.NotificationHelper;
import com.safronov.courseworksapp.Helpers.SnackbarHelper;
import com.safronov.courseworksapp.Helpers.ToolTipHelper;
import com.safronov.courseworksapp.MainActivity;
import com.safronov.courseworksapp.Models.CourseWork;
import com.safronov.courseworksapp.Models.FavoriteWork;
import com.safronov.courseworksapp.R;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class WorksDetailFragment extends Fragment implements View.OnClickListener {

    private View view;
    private TextView work_name;
    private TextView work_description;
    private FloatingActionButton fab;

    private CourseWork courseWork;
    private DatabaseOperations dbOp;
    private boolean isFavorite = false;

    private Context app_context;
    private NotificationHelper notificationHelper;

    private String portal_url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        app_context = MainActivity.app_context;
        notificationHelper = new NotificationHelper(getContext());
        portal_url = getResources().getString(R.string.portal_url);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_works_detail, container, false);

        work_name = view.findViewById(R.id.work_name);
        work_description = view.findViewById(R.id.work_description);
        fab = view.findViewById(R.id.fab);

        dbOp = new DatabaseOperations(app_context);

        fab.setOnClickListener(this);

        ToolTipHelper.show(getContext(), fab, "Also you can tap to download!");

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        courseWork = dbOp.getCourseWork(getArguments().getInt("id"));
        work_name.setText(courseWork.name);
        work_description.setText(courseWork.description);

        // если работа уже присутствует в базе
        if (dbOp.isFavWorkExist(courseWork.id)) {
            fab.setImageResource(R.drawable.baseline_favorite_white_48);
            isFavorite = true;
        }
    }

    @Override
    public void onClick(View v) {

        if (isFavorite) {
            // изменение иконки
            fab.setImageResource(R.drawable.baseline_favorite_border_white_48);

            // удаление из локальной базы
            dbOp.deleteFavWork(courseWork.id);

            // удаление из удаленной базы
            if (NetworkHelper.isNetConn(app_context)) deleteFavWork(courseWork.id);
            else SnackbarHelper.showStaticSnackbar(v, getContext());

            isFavorite = false;
        } else {
            // изменение иконки
            fab.setImageResource(R.drawable.baseline_favorite_white_48);

            // добавление в локальную базу
            dbOp.insertFavWork(courseWork.id);

            if (NetworkHelper.isNetConn(app_context)) {
                // добавление в удаленную базу
                postFavWork(courseWork.id);
                // скачивание файла
                getFile(courseWork.id);
            } else SnackbarHelper.showStaticSnackbar(v, getContext());

            isFavorite = true;
        }
    }

    private void postFavWork(final int work_id) {
        String url = portal_url + "/api/FavoriteWorks/Add";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            Toast.makeText(app_context, "Synchronization error. Please, try again", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                FavoriteWork favoriteWork = new FavoriteWork(dbOp.getUserId(), work_id);
                String token = new GsonBuilder().create().toJson(favoriteWork);

                byte[] b = new byte[0];
                try {
                    b = token.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return b;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + dbOp.getToken());
                return params;
            }
        };
        Volley.newRequestQueue(getContext()).add(request);
    }

    private void deleteFavWork(final int work_id) {
        String url = portal_url + "/api/FavoriteWorks/Delete/" + work_id;
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            Toast.makeText(app_context, "Synchronization error. Please, try again", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + dbOp.getToken());
                return params;
            }
        };
        Volley.newRequestQueue(app_context).add(request);
    }

    private void getFile(int work_id) {

        // уведомление о начале загрузки
        notificationHelper.createNotifiication();

        String url = portal_url + "/api/CourseWorks/GetFile/" + work_id;

        StringRequest postRequest1 = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject params = new JSONObject(response);

                            String base64 = params.get("file").toString();
                            String name = params.get("file_name").toString();

                            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), name);

                            FileOutputStream fileOutputStream = new FileOutputStream(file);
                            byte[] bytes = Base64.decode(base64, Base64.DEFAULT);
                            fileOutputStream.write(bytes);
                            fileOutputStream.close();

                            // уведомление об окончании загрузки
                            notificationHelper.updateNotification("File(s) downloaded!");

                        } catch (JSONException e) {
                            Log.d("JSON error", e.getMessage());
                        } catch (IOException e) {
                            Log.d("IOException error", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + dbOp.getToken());
                return params;
            }
        };
        Volley.newRequestQueue(app_context).add(postRequest1);
    }
}
