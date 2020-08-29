package androidlab.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;

import androidlab.project.R;
import androidlab.project.models.Movie;
import androidlab.project.models.User;

public class MainActivity extends AppCompatActivity {

    ArrayList<Movie> movieArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttonLoadData = (Button) findViewById(R.id.buttonLoadData);
        Button buttonContinue = (Button) findViewById(R.id.buttonContinue);
        setProgress(false);

//        final SwitchCompat switchCompat = findViewById(R.id.themeSwitch);
        final SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(this);
        String mode = sharedPrefManager.readString("mode", "light");
//        if (mode.equals("night"))
//            switchCompat.setChecked(true);
//        else
//            switchCompat.setChecked(false);

        if (mode.equals("night")) {

            sharedPrefManager.writeString("mode", "night");
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//            Intent intent = new Intent(MainActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
        } else {
            sharedPrefManager.writeString("mode", "light");

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//            Intent intent = new Intent(MainActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
        }


        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLogin();
//                User user = new User();
//                user.setEmail("saleem@gmail.com");
//                user.setFirstName("Saleem");
//                user.setPhoneNumber("00970597153799");
//                user.setGender("Male");
//                user.setPassword("Saleem@123");
//                DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this, "PROJECT", null, 1);
//                dataBaseHelper.insertUser(user);
            }
        });

        buttonLoadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ConnectionAsyncTask connectionAsyncTask = new ConnectionAsyncTask(MainActivity.this);
//                  connectionAsyncTask.execute("https://firebasestorage.googleapis.com/v0/b/advance-proj1.appspot.com/o/movies-in.json?alt=media&token=80-99d8-c1d0ad2eaaf");
                    connectionAsyncTask.execute("https://firebasestorage.googleapis.com/v0/b/advance-proj1.appspot.com/o/movies-in-theaters.json?alt=media&token=e3121ae7-be1b-4480-99d8-c1d0ad2eaa2f");

                } catch (Exception e) {

                }
            }
        });

    }

    public void setProgress(boolean progress) {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        if (progress) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    public void throwError() {
        Toast toast = Toast.makeText(MainActivity.this, "Something Went Wrong!", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void setMovieArrayList(ArrayList<Movie> movieArrayList) {
        this.movieArrayList = movieArrayList;
    }

    public void addMoviesToDB() {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this, "PROJECT", null, 1);
        for (Movie movie : movieArrayList) {
            dataBaseHelper.insertMovie(movie);
        }
    }

    public void goToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        MainActivity.this.startActivity(intent);
        finish();
    }


}

