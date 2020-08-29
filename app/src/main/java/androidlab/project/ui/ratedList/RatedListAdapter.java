package androidlab.project.ui.ratedList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import androidlab.project.R;
import androidlab.project.models.Movie;

public class RatedListAdapter extends RecyclerView.Adapter<RatedListAdapter.MyViewHolder> {

    Context context;
    ArrayList<Movie> ratedList;


    public RatedListAdapter(Context context, ArrayList<Movie> ratedList) {
        this.context = context;
        this.ratedList = ratedList;
    }


    @NonNull
    @Override
    public RatedListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_item_2, parent, false);
        return new RatedListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textViewTitle.setText(ratedList.get(position).getTitle());
        holder.textViewDuration.setText(ratedList.get(position).getDuration());
        holder.textViewGenres.setText(ratedList.get(position).getGenres().toString().replace("]","").replace("[",""));
        holder.textViewRating.setText(ratedList.get(position).getImbdRating() + "");
        DateFormat destDf = new SimpleDateFormat("MM-dd-yyyy");
        String dateStr = destDf.format(ratedList.get(position).getReleaseDate());
        holder.textViewReleaseDate.setText(dateStr);


        Picasso.get().load(ratedList.get(position).getPosterurl())
                .placeholder(R.drawable.placeholder)
                .into(holder.imageView);
        holder.ratingBar.setRating(ratedList.get(position).getRating());
        holder.ratingBar.setEnabled(false);


//        holder.textViewReleaseDate.setText(ratedList.get(position).getReleaseDate().toString());
    }



    @Override
    public int getItemCount() {
        return ratedList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewReleaseDate, textViewDuration, textViewRating, textViewGenres;
        ImageView imageView;
        RatingBar ratingBar;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitleRate);
            textViewReleaseDate = itemView.findViewById(R.id.textViewReleaseDateRate);
            textViewDuration = itemView.findViewById(R.id.textViewDurationRate);
            textViewRating = itemView.findViewById(R.id.textViewRatingRate);
            textViewGenres = itemView.findViewById(R.id.textViewGenresRate);
            imageView = itemView.findViewById(R.id.imageViewRate);
            ratingBar = itemView.findViewById(R.id.ratingBar3);



        }
    }
}
