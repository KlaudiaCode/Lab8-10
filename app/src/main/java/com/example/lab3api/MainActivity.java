package com.example.lab3api;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private final String API_KEY = "edd5fa3dad29451fc55d0159d3a54260";
    private MovieApplication app;
    RecyclerView recyclerView;
    MovieAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        app = (MovieApplication) getApplication();

        recyclerView = findViewById(R.id.recycler);
        adapter = new MovieAdapter();
        recyclerView.setAdapter(adapter);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        Spinner spinner = findViewById(R.id.category);
        spinner.setAdapter(ArrayAdapter.createFromResource(this, R.array.criteria, android.R.layout.simple_spinner_dropdown_item));

        Button searchButton = findViewById(R.id.search_button);
        EditText queryField = findViewById(R.id.query_field);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (spinner.getSelectedItem().toString()) {
                    case "Szukaj po aktorach":
                        getMoviesByActors(queryField.getText().toString());
                        break;
                    case "Szukaj po tytule":
                        getMovies(queryField.getText().toString());
                        break;
                    case "Szukaj po roku":
                        getMoviesByYear(Integer.parseInt(queryField.getText().toString()));
                        break;
                    default:
                        adapter.setData(Collections.emptyList());
                        break;
                }
            }
        });
    }

    private void getMoviesByYear(int year) {
        app.service.getMoviesByYear(API_KEY, year).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.body() != null) {
                    adapter.setData(response.body().results);
                } else {
                    adapter.setData(Collections.emptyList());
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getMoviesByActors(String query) {
        String[] actors = query.split(",");
        List<Integer> ids = new ArrayList();
        StringBuilder strbul = new StringBuilder();
        for (String actor : actors) {
            ids.add(getActor(actor));
        }
        for(int str : ids)
        {
            strbul.append(str);
            strbul.append("|");
        }
        strbul.setLength(strbul.length()-1);
        if (ids.size() != 0) {
            app.service.getMoviesByActors(API_KEY, strbul.toString()).enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    if (response.body() != null) {
                        adapter.setData(response.body().results);
                    } else {
                        adapter.setData(Collections.emptyList());
                    }
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {
                    Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private int getActor(String query) {
        int firstActorId = 0;
        Response<ActorResponse> response = null;
        try {
            response = app.service.getActors(API_KEY, query).execute();
        } catch (IOException error){
            Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
        }
        if (response != null) {
            if (response.body() != null) {
                firstActorId = response.body().results.get(0).id;
            }
        }
        return firstActorId;
    }

    private void getMovies(String query) {
        app.service.getMovies(API_KEY, query).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.body() != null) {
                    adapter.setData(response.body().results);
                } else {
                    adapter.setData(Collections.emptyList());
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}