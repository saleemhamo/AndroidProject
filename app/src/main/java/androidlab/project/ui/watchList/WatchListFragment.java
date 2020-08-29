package androidlab.project.ui.watchList;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import androidlab.project.DataBaseHelper;
import androidlab.project.HomeActivity;
import androidlab.project.R;
import androidlab.project.SharedPrefManager;
import androidlab.project.models.Movie;
import androidlab.project.ui.movieList.MovieListAdapter;
import androidlab.project.ui.movieList.MovieListFragment;
import androidlab.project.ui.movieList.MovieListViewModel;


public class WatchListFragment extends Fragment {

    RecyclerView recyclerView;
    WatchListViewModel watchListViewModel;

    public WatchListFragment() {
    }

    public static WatchListFragment newInstance(String param1, String param2) {
        WatchListFragment fragment = new WatchListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        watchListViewModel =
                ViewModelProviders.of(this).get(WatchListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_watch_list, container, false);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        ArrayList<Movie> watchList = new ArrayList<>();
        SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(getActivity());
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity(), "PROJECT", null, 1);


        Cursor moviesWatchCursor = dataBaseHelper.getWatchListMovies(sharedPrefManager.readString("userEmail", "Email"));
        while (moviesWatchCursor.moveToNext()) {
            Cursor moviesCursor = dataBaseHelper.getMovieById(Integer.parseInt(moviesWatchCursor.getString(1)));
            if (moviesCursor.getCount() > 0) {

//        while (moviesCursor.moveToNext()) {
                moviesCursor.moveToFirst();
                Movie movie = new Movie();
                movie.setId(Integer.parseInt(moviesCursor.getString(0)));
                movie.setTitle(moviesCursor.getString(1));
                movie.setYear(Integer.parseInt(moviesCursor.getString(2)));
                String genres = moviesCursor.getString(3);
                genres = genres.replace("[", "");
                genres = genres.replace("]", "");
                String genresContent[] = genres.split(",");
                ArrayList<String> list = new ArrayList<String>();
                Collections.addAll(list, genresContent);
                movie.setGenres(list);
                movie.setDuration(moviesCursor.getString(4));

//                System.out.println(moviesWatchCursor.getString(3));
                try {
                    Date date = new SimpleDateFormat("E MMM dd HH:mm:ss zzz yyyy").parse(moviesWatchCursor.getString(3));
                    movie.setReleaseDate(date);
//                    System.out.println(movie.getReleaseDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                movie.setStoryLine(moviesCursor.getString(6));
                String actors = moviesCursor.getString(7);
                actors = actors.replace("[", "");
                actors = actors.replace("]", "");
                String actorsContent[] = genres.split(",");
                ArrayList<String> list2 = new ArrayList<String>();
                Collections.addAll(list2, actorsContent);
                movie.setActors(list2);
                try {
                    movie.setImbdRating(Double.parseDouble(moviesCursor.getString(8)));
                } catch (Exception e) {
                }
                movie.setPosterurl(moviesCursor.getString(9));
                movie.setRating(moviesCursor.getFloat(10));

                watchList.add(movie);
            } else {
                System.out.println("Here is error");
            }
        }

        recyclerView = getActivity().findViewById(R.id.recyclerViewWatchList);
//        recyclerView.setHasFixedSize(true);
        WatchListAdapter watchListAdapter = new WatchListAdapter(getContext(), watchList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(watchListAdapter);


    }


}


