package androidlab.project;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidlab.project.models.Movie;

public class MovieJsonParser {
    public static ArrayList<Movie> getObjectFromJason(String jason) {
        ArrayList<Movie> movies;
        try {
            JSONArray jsonArray = new JSONArray(jason);
            movies = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject = (JSONObject) jsonArray.get(i);
                Movie movie = new Movie();
                movie.setId(jsonObject.getInt("id"));
                movie.setTitle(jsonObject.getString("title"));
                movie.setYear(jsonObject.getInt("year"));
                JSONArray genresObject = jsonObject.getJSONArray("genres");
                ArrayList<String> genres = new ArrayList<>();
                for (int k = 0; k < genresObject.length(); k++) {
                    genres.add(genresObject.getString(k));
                }
                movie.setGenres(genres);
                movie.setDuration(jsonObject.getString("duration"));
                movie.setReleaseDate(new SimpleDateFormat("yyyy-dd-MM").parse(jsonObject.getString("releaseDate")));

                movie.setStoryLine(jsonObject.getString("storyline"));

                JSONArray actorsObject = jsonObject.getJSONArray("actors");
                ArrayList<String> actors = new ArrayList<String>();
                for (int k = 0; k < actorsObject.length(); k++) {
                    actors.add(actorsObject.getString(k));
                }
                movie.setActors(actors);
                String rating = jsonObject.getString("imdbRating");
                if (rating.equals("")) {
                    movie.setImbdRating(0.0);
                } else {
                    movie.setImbdRating(Double.parseDouble(rating));
                }
                movie.setPosterurl(jsonObject.getString("posterurl"));
                movies.add(movie);
            }
        } catch (JSONException | ParseException e) {
            System.out.println("Hereee");
            e.printStackTrace();
            return null;
        }
        return movies;
    }
}
