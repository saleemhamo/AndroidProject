package androidlab.project.ui.ratedList;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
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
import androidlab.project.R;
import androidlab.project.SharedPrefManager;
import androidlab.project.models.Movie;
import androidlab.project.ui.movieList.MovieListAdapter;
import androidlab.project.ui.movieList.MovieListFragment;
import androidlab.project.ui.movieList.MovieListViewModel;

public class RatedListFragment extends Fragment {
    RecyclerView recyclerView;
    RatedListViewModel ratedListViewModel;

    public RatedListFragment() {
    }

    public static RatedListFragment newInstance(String param1, String param2) {
        RatedListFragment fragment = new RatedListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ratedListViewModel =
                ViewModelProviders.of(this).get(RatedListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_rated_list, container, false);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        recyclerView = getActivity().findViewById(R.id.recyclerViewRatedList);
        recyclerView.setHasFixedSize(true);

        ArrayList<Movie> moviesArrayList = new ArrayList<>();
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity(), "PROJECT", null, 1);
        SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(getActivity());
        Cursor moviesRatedCursor = dataBaseHelper.getRatedListMovies(sharedPrefManager.readString("userEmail", "Email"));
        while (moviesRatedCursor.moveToNext()) {
            Cursor moviesCursor = dataBaseHelper.getMovieById(Integer.parseInt(moviesRatedCursor.getString(1)));
            if (moviesCursor.getCount() > 0) {
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
                try {
                    Date date = new SimpleDateFormat("E MMM dd HH:mm:ss zzz yyyy").parse(moviesCursor.getString(5));
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


                sharedPrefManager = SharedPrefManager.getInstance(getActivity());
                Cursor ratingCursor = dataBaseHelper.getMovieRating(movie.getId(), sharedPrefManager.readString("userEmail", "Email"));
                if (ratingCursor.getCount() > 0) {
                    ratingCursor.moveToFirst();
                    movie.setRating(ratingCursor.getFloat(3));
                } else {
                    movie.setRating(moviesCursor.getFloat(10));
                }

                moviesArrayList.add(movie);
            }
        }


        RatedListAdapter ratedListAdapter = new RatedListAdapter(getContext(), moviesArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(ratedListAdapter);

    }


}
