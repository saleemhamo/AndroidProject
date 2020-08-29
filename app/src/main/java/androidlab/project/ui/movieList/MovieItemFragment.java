package androidlab.project.ui.movieList;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import androidlab.project.DataBaseHelper;
import androidlab.project.LoginActivity;
import androidlab.project.R;
import androidlab.project.SharedPrefManager;
import androidlab.project.models.Movie;
import androidlab.project.ui.profile.ProfileFragment;

public class MovieItemFragment extends DialogFragment {
    MovieListViewModel movieListViewModel;
    private int itemId;

    public MovieItemFragment() {
    }

    public MovieItemFragment(int itemId) {
        this.itemId = itemId;
    }

    public int getItemId() {
        return itemId;
    }

    public static void setItemId(int itemId) {
        itemId = itemId;
    }

    public static MovieItemFragment newInstance(String param1, String param2) {
        MovieItemFragment fragment = new MovieItemFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_movie_item, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();


        ImageView imageView = getActivity().findViewById(R.id.imageViewItem);
        TextView textViewTitle = getActivity().findViewById(R.id.textViewItemTitle);
        TextView textViewYear = getActivity().findViewById(R.id.textViewItemYear);
        TextView textViewDuration = getActivity().findViewById(R.id.textViewItemDuration);
        TextView textViewImdpRating = getActivity().findViewById(R.id.textViewItemImdpRating);
        TextView textViewReleaseDate = getActivity().findViewById(R.id.textViewItemeReleaseDate);
        TextView textViewActors = getActivity().findViewById(R.id.textViewItemActors);
        TextView textViewGeneres = getActivity().findViewById(R.id.textViewItemGenres);
        TextView textViewStoryLine = getActivity().findViewById(R.id.textViewItemStoryLine);
        RatingBar ratingBar = getActivity().findViewById(R.id.ratingBarItem);


        final DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity(), "PROJECT", null, 1);

        Cursor moviesCursor = dataBaseHelper.getMovieById(itemId);
        moviesCursor.moveToFirst();
        final Movie movie = new Movie();
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

        Picasso.get().load(movie.getPosterurl())
                .placeholder(R.drawable.placeholder)
                .into(imageView);
        textViewTitle.setText(movie.getTitle());
        textViewYear.setText(movie.getYear() + "");
        textViewDuration.setText(movie.getDuration());
        textViewImdpRating.setText(String.valueOf(movie.getImbdRating()));

        DateFormat destDf = new SimpleDateFormat("MM-dd-yyyy");
        String dateStr = destDf.format(movie.getReleaseDate());
        textViewReleaseDate.setText(dateStr);

        textViewActors.setText(movie.getActors().toString().replace("[", "").replace("]", ""));
        textViewGeneres.setText(movie.getGenres().toString().replace("[", "").replace("]", ""));
        textViewStoryLine.setText(movie.getStoryLine());

        SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(getActivity());
        Cursor ratingCursor = dataBaseHelper.getMovieRating(movie.getId(), sharedPrefManager.readString("userEmail", "Email"));
        if (ratingCursor.getCount() > 0) {
            ratingCursor.moveToFirst();
            ratingBar.setRating(ratingCursor.getFloat(3));
        } else {
            ratingBar.setRating(movie.getRating());
        }
        ratingBar.setEnabled(false);

        Button buttonAddComment = getActivity().findViewById(R.id.buttonComment);
        final EditText editTextComment = getActivity().findViewById(R.id.editTextComment);
        final LinearLayout commentsLayout = getActivity().findViewById(R.id.commentsLayout);

        Cursor commentsCursor = dataBaseHelper.getAllCommentsMovie(movie.getId());

        final ArrayList<String> comments = new ArrayList<>();
        while (commentsCursor.moveToNext()) {
            comments.add(commentsCursor.getString(3) + "\n" + commentsCursor.getString(4));
        }


        for (String comment : comments) {

            TextView textView = new TextView(getActivity());
            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            textView.setText(comment);
            textView.setTextSize(18);

            textView.setTextColor(getResources().getColor(R.color.colorText));
            textView.setBackgroundColor(getResources().getColor(R.color.colorBackground));

            commentsLayout.addView(textView);

        }
        sharedPrefManager = SharedPrefManager.getInstance(getActivity());
        final String email = sharedPrefManager.readString("userEmail", "nothing");
        final String name = sharedPrefManager.readString("userName", "nothing");
        buttonAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataBaseHelper.addToComments(movie.getId(), email, editTextComment.getText().toString(), name);
                for (String comment : comments) {

                    TextView textView = new TextView(getActivity());
                    textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    textView.setText(comment);
                    textView.setTextSize(18);

                    textView.setTextColor(getResources().getColor(R.color.colorText));
                    textView.setBackgroundColor(getResources().getColor(R.color.colorBackground));

                    commentsLayout.addView(textView);

                }
                editTextComment.setText("");
            }
        });

    }
}
