package androidlab.project.ui.movieList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import androidlab.project.DataBaseHelper;
import androidlab.project.HomeActivity;
import androidlab.project.R;
import androidlab.project.models.Movie;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MyViewHolder> {
    private OnItemWatchListClickListener onItemWatchListClickListener;

    public interface OnItemWatchListClickListener {
        void addToWatchListClicked(int position);

        void removeFromWatchListClicked(int position);

        void rateClicked(int position, float rate);

        void viewMovieDetails(int position, String title);
    }

    public void setOnItemWatchListClickListener(OnItemWatchListClickListener listener) {
        onItemWatchListClickListener = listener;
    }


    Context context;
    ArrayList<Movie> movieArrayList;


    public MovieListAdapter(Context context, ArrayList<Movie> movieArrayList) {
        this.context = context;
        this.movieArrayList = movieArrayList;
    }


    @NonNull
    @Override
    public MovieListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_item, parent, false);
        return new MovieListAdapter.MyViewHolder(view, onItemWatchListClickListener);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textViewTitle.setText(movieArrayList.get(position).getTitle());
        holder.textViewDuration.setText(movieArrayList.get(position).getDuration());
        holder.textViewGenres.setText(movieArrayList.get(position).getGenres().toString().replace("[", "").replace("]", ""));
        holder.textViewRating.setText(movieArrayList.get(position).getImbdRating() + "");

        DateFormat destDf = new SimpleDateFormat("MM-dd-yyyy");
        String dateStr = destDf.format(movieArrayList.get(position).getReleaseDate());
        holder.textViewReleaseDate.setText(dateStr);
        Picasso.get().load(movieArrayList.get(position).getPosterurl())
                .placeholder(R.drawable.placeholder)
                .into(holder.imageView);


        holder.ratingBar.setRating(movieArrayList.get(position).getRating());
        if (movieArrayList.get(position).getRating() > 0) {
            holder.ratingBar.setEnabled(false);
            holder.ratingBar.setRating(movieArrayList.get(position).getRating());
            holder.buttonRate.setEnabled(false);
        }
        Button buttonAddToWatchList = holder.buttonAddToWatchList;

        if (movieArrayList.get(position).isAddedToWatchList()) {
            buttonAddToWatchList.setText("Remove From Watch List");
        }

    }

    @Override
    public int getItemCount() {
        return movieArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewReleaseDate, textViewDuration, textViewRating, textViewGenres;
        Button buttonAddToWatchList, buttonRate;
        ImageView imageView;
        RatingBar ratingBar;


        public MyViewHolder(@NonNull View itemView, final OnItemWatchListClickListener listener) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewReleaseDate = itemView.findViewById(R.id.textViewReleaseDate);
            textViewDuration = itemView.findViewById(R.id.textViewDuration);
            textViewRating = itemView.findViewById(R.id.textViewRating);
            textViewGenres = itemView.findViewById(R.id.textViewGenres);
            buttonAddToWatchList = itemView.findViewById(R.id.buttonAddToWatchList);
            buttonRate = itemView.findViewById(R.id.buttonRate);
            imageView = itemView.findViewById(R.id.imageView3);
            ratingBar = itemView.findViewById(R.id.ratingBar);

            ConstraintLayout card = itemView.findViewById(R.id.constraintLayoutMovie);

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    String title = textViewTitle.getText().toString();
                    listener.viewMovieDetails(position, title);
                }
            });


            buttonAddToWatchList.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            if (buttonAddToWatchList.getText().equals("Remove From Watch List")) {
                                buttonAddToWatchList.setText("Add To Watch List");
                                listener.removeFromWatchListClicked(position);
                            } else {
                                buttonAddToWatchList.setText("Remove From Watch List");
                                listener.addToWatchListClicked(position);

                            }
                        } else {
                            System.out.println("Errooorrorororororor");
                        }
                    }
                }
            });
            buttonRate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            float rate = ratingBar.getRating();
                            buttonRate.setEnabled(false);
                            listener.rateClicked(position, rate);
                        }
                    }
                }
            });

        }
    }
}
