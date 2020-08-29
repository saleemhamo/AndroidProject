package androidlab.project.ui.movieList;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import androidlab.project.DataBaseHelper;
import androidlab.project.R;
import androidlab.project.SharedPrefManager;
import androidlab.project.models.Movie;


public class MovieListFragment extends Fragment {


    RecyclerView recyclerView;
    MovieListViewModel movieListViewModel;

    public MovieListFragment() {
    }

    public static MovieListFragment newInstance(String param1, String param2) {
        MovieListFragment fragment = new MovieListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        movieListViewModel =
                ViewModelProviders.of(this).get(MovieListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_movie_list, container, false);


        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        recyclerView = getActivity().findViewById(R.id.recyclerViewMovieList);
//        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


        final Button buttonOpenFilter = getActivity().findViewById(R.id.buttonOpenFilter);
        final ConstraintLayout filterLayout = getActivity().findViewById(R.id.layoutFilter);


        filterLayout.setVisibility(View.GONE);
        buttonOpenFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (buttonOpenFilter.getText().equals("OPEN FILTER")) {
                    filterLayout.setVisibility(View.VISIBLE);
                    buttonOpenFilter.setText("CLOSE FILTER");
                    recyclerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                } else {
                    filterLayout.setVisibility(View.GONE);
                    buttonOpenFilter.setText("OPEN FILTER");
                    recyclerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


                }

            }
        });
        final TextView textViewDateFrom = getActivity().findViewById(R.id.editTextDateFilterFrom);
        final TextView textViewDateTo = getActivity().findViewById(R.id.editTextDateFilterTo);
        final TextView textViewDurationFrom = getActivity().findViewById(R.id.editTextDutationFilterFrom);
        final TextView textViewDurationTo = getActivity().findViewById(R.id.editTextDurationFilterTo);
        final TextView textViewRatingFrom = getActivity().findViewById(R.id.editTextRatingFilterFrom);
        final TextView textViewRatingTo = getActivity().findViewById(R.id.editTextRatingFilterTo);

        final Button buttonSearchFilter = getActivity().findViewById(R.id.buttonSearchFilter);
        buttonSearchFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = "SELECT * FROM MOVIES";
                ArrayList<String> constraints = new ArrayList<>();
                String dateStr = "";
                if (textViewDateFrom.getText().length() > 0) {
                    try {
                        Date date = new SimpleDateFormat("dd-MM-yyyy").parse(textViewDateFrom.getText().toString());
                        DateFormat destDf = new SimpleDateFormat("E MMM dd HH:mm:ss zzz yyyy");
                        dateStr = destDf.format(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    constraints.add(" RELEASEDATE >= '" + dateStr +"'");
                }
                if (textViewDateTo.getText().length() > 0) {
                    try {
                        Date date = new SimpleDateFormat("dd-MM-yyyy").parse(textViewDateTo.getText().toString());
                        DateFormat destDf = new SimpleDateFormat("E MMM dd HH:mm:ss zzz yyyy");
                        dateStr = destDf.format(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    constraints.add(" RELEASEDATE <= '" + dateStr +"'");
                }
                if (textViewDurationFrom.getText().length() > 0) {

                    constraints.add(" DURATION >= 'PT" + textViewDurationFrom.getText() + "M'");
                }
                if (textViewDurationTo.getText().length() > 0) {
                    constraints.add(" DURATION <= 'PT" + textViewDurationTo.getText() + "M'");

                }
                if (textViewRatingFrom.getText().length() > 0) {
                    constraints.add(" IMDPRATING >= " + textViewRatingFrom.getText());

                }
                if (textViewRatingTo.getText().length() > 0) {
                    constraints.add(" IMDPRATING <= " + textViewRatingTo.getText());
                }

                if (constraints.size() != 0) {
                    query += " WHERE";
                }
                for (int i = 0; i < constraints.size(); i++) {
                    if (i != 0)
                        query += " AND";
                    query += constraints.get(i);

                }
                query += ";";

                System.out.println(query);


                ArrayList<Movie> moviesArrayList = new ArrayList<>();
                DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity(), "PROJECT", null, 1);
                Cursor moviesCursor = dataBaseHelper.getFilteredMovies(query);

                while (moviesCursor.moveToNext()) {
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
//            System.out.println(moviesCursor.getString(5));
                    try {
                        Date date = new SimpleDateFormat("E MMM dd HH:mm:ss zzz yyyy").parse(moviesCursor.getString(5));
                        movie.setReleaseDate(date);
//                System.out.println(movie.getReleaseDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    movie.setStoryLine(moviesCursor.getString(6));
                    String actors = moviesCursor.getString(7);
                    actors = actors.replace("[", "");
                    actors = actors.replace("]", "");
                    String actorsContent[] = actors.split(",");
                    ArrayList<String> list2 = new ArrayList<String>();
                    Collections.addAll(list2, actorsContent);
                    movie.setActors(list2);
                    try {
                        movie.setImbdRating(Double.parseDouble(moviesCursor.getString(8)));
                    } catch (Exception e) {
                    }
                    movie.setPosterurl(moviesCursor.getString(9));
                    movie.setRating(moviesCursor.getFloat(10));


                    SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(getActivity());
                    Cursor watchListCursor = dataBaseHelper.getMovieWatchList(movie.getId(), sharedPrefManager.readString("userEmail", "Email"));
                    if (watchListCursor.getCount() > 0) {
                        watchListCursor.moveToFirst();
                        movie.setAddedToWatchList(true);
                    } else {
                        movie.setAddedToWatchList(false);
                    }


                    moviesArrayList.add(movie);
                }


                MovieListAdapter movieListAdapter = new MovieListAdapter(getContext(), moviesArrayList);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(movieListAdapter);
            }
        });


        ArrayList<Movie> moviesArrayList = new ArrayList<>();
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity(), "PROJECT", null, 1);
        Cursor moviesCursor = dataBaseHelper.getAllMovies();

        while (moviesCursor.moveToNext()) {
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
//            System.out.println(moviesCursor.getString(5));
            try {
                Date date = new SimpleDateFormat("E MMM dd HH:mm:ss zzz yyyy").parse(moviesCursor.getString(5));
                movie.setReleaseDate(date);
//                System.out.println(movie.getReleaseDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            movie.setStoryLine(moviesCursor.getString(6));
            String actors = moviesCursor.getString(7);
            actors = actors.replace("[", "");
            actors = actors.replace("]", "");
            String actorsContent[] = actors.split(",");
            ArrayList<String> list2 = new ArrayList<String>();
            Collections.addAll(list2, actorsContent);
            movie.setActors(list2);
            try {
                movie.setImbdRating(Double.parseDouble(moviesCursor.getString(8)));
            } catch (Exception e) {
            }
            movie.setPosterurl(moviesCursor.getString(9));

            SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(getActivity());
            Cursor ratingCursor = dataBaseHelper.getMovieRating(movie.getId(), sharedPrefManager.readString("userEmail", "Email"));
            if (ratingCursor.getCount() > 0) {
                ratingCursor.moveToFirst();
                movie.setRating(ratingCursor.getFloat(3));
            } else {
                movie.setRating(moviesCursor.getFloat(10));
            }


            sharedPrefManager = SharedPrefManager.getInstance(getActivity());
            Cursor watchListCursor = dataBaseHelper.getMovieWatchList(movie.getId(), sharedPrefManager.readString("userEmail", "Email"));
            if (watchListCursor.getCount() > 0) {
                watchListCursor.moveToFirst();
                movie.setAddedToWatchList(true);
            } else {
                movie.setAddedToWatchList(false);
            }


            moviesArrayList.add(movie);
        }


        MovieListAdapter movieListAdapter = new MovieListAdapter(getContext(), moviesArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(movieListAdapter);
        movieListAdapter.setOnItemWatchListClickListener(new MovieListAdapter.OnItemWatchListClickListener() {
            @Override
            public void addToWatchListClicked(int position) {

                SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(getActivity());
                DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity(), "PROJECT", null, 1);
                dataBaseHelper.addToWatchList(position, sharedPrefManager.readString("userEmail", "email"));


            }

            @Override
            public void removeFromWatchListClicked(int position) {
                SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(getActivity());
                DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity(), "PROJECT", null, 1);
                dataBaseHelper.removeFromWatchList(position, sharedPrefManager.readString("userEmail", "email"));
            }


            @Override
            public void rateClicked(int position, float rating) {
//                RatingBar ratingBar = getActivity().findViewById(R.id.ratingBar);
//                float rating = ratingBar.getRating();
//                System.out.println(rating);
                SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(getActivity());
                DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity(), "PROJECT", null, 1);
                dataBaseHelper.addRatingToMovie(rating, position, sharedPrefManager.readString("userEmail", "Email"));
            }

            @Override
            public void viewMovieDetails(int position, String title) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity(), "PROJECT", null, 1);
                Cursor movie = dataBaseHelper.getMovieByTitle(title);
                movie.moveToFirst();
                int id = Integer.parseInt(movie.getString(0));

                fr.replace(R.id.nav_host_fragment, new MovieItemFragment(id));
                fr.commit();
            }
        });
    }


    public interface MovieListCommunicator {
        void showItem(int position);

    }
}
