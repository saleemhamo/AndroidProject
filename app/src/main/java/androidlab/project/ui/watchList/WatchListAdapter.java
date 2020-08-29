package androidlab.project.ui.watchList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import androidlab.project.R;
import androidlab.project.models.Movie;

public class WatchListAdapter extends RecyclerView.Adapter<WatchListAdapter.MyViewHolder> {
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void addToWatchListClicked(int position);
        void rateClicked(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    Context context;
    ArrayList<Movie> watchList;


    public WatchListAdapter(Context context, ArrayList<Movie> watchList) {
        this.context = context;
        this.watchList = watchList;
    }

    public void addToWatchList(Movie addedMovie) {
        this.watchList.add(addedMovie);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_item_1, parent, false);
        return new WatchListAdapter.MyViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textViewTitle.setText(watchList.get(position).getTitle());
        DateFormat destDf = new SimpleDateFormat("MM-dd-yyyy");
        String dateStr = destDf.format(watchList.get(position).getReleaseDate());
        holder.textViewDate.setText(dateStr);
        Picasso.get().load(watchList.get(position).getPosterurl())
                .placeholder(R.drawable.placeholder)
                .into(holder.imageView);


    }


    @Override
    public int getItemCount() {
        return watchList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewDate;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewWatchTitle);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            imageView = itemView.findViewById(R.id.imageView2);

        }
    }
}
