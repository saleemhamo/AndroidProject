package androidlab.project;

import android.app.Activity;
import android.media.Image;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.SortedMap;

import androidlab.project.models.Movie;

public class ConnectionAsyncTask extends AsyncTask<String, String, String> {
    Activity activity;

    public ConnectionAsyncTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ((MainActivity) activity).setProgress(true);
    }

    @Override
    protected String doInBackground(String... params) {
        String data = HttpManager.getData(params[0]);
        return data;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        ((MainActivity) activity).setProgress(false);
        try {
            ArrayList<Movie> movies = MovieJsonParser.getObjectFromJason(s);
            ((MainActivity) activity).setMovieArrayList(movies);
            ((MainActivity) activity).goToLogin();
            ((MainActivity) activity).addMoviesToDB();


        } catch (Exception e) {

            ((MainActivity) activity).throwError();
        }


    }

}

