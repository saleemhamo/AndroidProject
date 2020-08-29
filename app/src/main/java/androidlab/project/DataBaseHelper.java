package androidlab.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;


import java.util.Date;

import androidlab.project.models.Movie;
import androidlab.project.models.User;

public class DataBaseHelper extends android.database.sqlite.SQLiteOpenHelper {


    public DataBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE USERS(EMAIL TEXT PRIMARY KEY,FIRSTNAME TEXT, PHONE TEXT,GENDER TEXT, PASSWORD TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE MOVIES(ID LONG PRIMARY KEY,TITLE TEXT, YEAR INTEGER,GENRES TEXT, DURATION TEXT, RELEASEDATE DATE, STORYLINE TEXT, ACTORS TEXT, IMDPRATING FLOAT, POSTERURL TEXT, RATE FLOAT, ADDEDTOWATCHLIST BOOLEAN)");
        sqLiteDatabase.execSQL("CREATE TABLE WATCHLIST(ID TEXT  PRIMARY KEY ,MOVIE_ID LONG, USER_EMAIL TEXT, ADD_DATE DATE)");
        sqLiteDatabase.execSQL("CREATE TABLE RATELIST(ID TEXT PRIMARY KEY,MOVIE_ID LONG, USER_EMAIL TEXT, RATE FLOAT, RATEDATE DATE)");
        sqLiteDatabase.execSQL("CREATE TABLE COMMENTS(ID TEXT PRIMARY KEY,MOVIE_ID LONG, USER_EMAIL TEXT, USER_NAME TEXT, COMMENT TEXT)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public Cursor getMovieRating(int movieId, String email) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM RATELIST WHERE MOVIE_ID = " + movieId + " AND USER_EMAIL = '" + email + "';", null);
    }

    public Cursor getMovieWatchList(int movieId, String email) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM WATCHLIST WHERE MOVIE_ID = " + movieId + " AND USER_EMAIL = '" + email + "';", null);
    }

    public Cursor getAllMovies() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM MOVIES", null);
    }

    public Cursor getMovieByTitle(String title) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM MOVIES WHERE TITLE = '" + title + "'", null);
    }

    public Cursor getAllComments() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM COMMENTS", null);
    }

    public Cursor getAllCommentsMovie(int id) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM COMMENTS WHERE MOVIE_ID = " + id, null);
    }

    public Cursor getFilteredMovies(String query) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery(query, null);
    }

    public Cursor getWatchListMovies(String email) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM WATCHLIST WHERE USER_EMAIL = '" + email + "'", null);
    }

    public Cursor getAllWatchListMovies() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM WATCHLIST", null);
    }

    public Cursor getMovieById(int id) {
        Cursor movie = null;
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String strSQL = "SELECT * FROM MOVIES WHERE ID = " + id;
        try {
            movie = sqLiteDatabase.rawQuery(strSQL, null);
        } catch (Exception e) {

        }
        return movie;
    }

    public Cursor getRatedListMovies(String email) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM RATELIST WHERE USER_EMAIL = '" + email + "'", null);

    }

    public Cursor getAllUsers() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM USERS", null);
    }

    public void addRatingToMovie(float rating, int position, String email) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", email+"_"+(position+1)+"");
        contentValues.put("MOVIE_ID", position + 1);
        contentValues.put("USER_EMAIL", email);
        contentValues.put("RATE", rating);
        contentValues.put("RATEDATE", (new Date().toString()));
        sqLiteDatabase.insert("RATELIST", null, contentValues);

    }

    public Cursor getUserByEmail(String email) {
        Cursor user = null;
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String strSQL = "SELECT * FROM USERS WHERE EMAIL = '" + email + "'";
        try {
            user = sqLiteDatabase.rawQuery(strSQL, null);
        } catch (Exception e) {

        }
        return user;
    }

    public void updateUserName(String email, String newName) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String strSQL = "UPDATE USERS SET FIRSTNAME = '" + newName + "' WHERE EMAIL = '" + email + "'";
        sqLiteDatabase.execSQL(strSQL);
    }

    public void updateUserPassword(String email, String password) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String strSQL = "UPDATE USERS SET PASSWORD = '" + password + "' WHERE EMAIL = '" + email + "'";
        sqLiteDatabase.execSQL(strSQL);
    }

    public void insertMovie(Movie movie) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", movie.getId());
        contentValues.put("TITLE", movie.getTitle());
        contentValues.put("YEAR", movie.getYear());
        contentValues.put("GENRES", movie.getGenres().toString());
        contentValues.put("DURATION", movie.getDuration());
        contentValues.put("RELEASEDATE", movie.getReleaseDate().toString());
        contentValues.put("ACTORS", movie.getActors().toString());
        contentValues.put("STORYLINE", movie.getStoryLine());
        contentValues.put("IMDPRATING", movie.getImbdRating());
        contentValues.put("POSTERURL", movie.getPosterurl());
        contentValues.put("RATE", "0");
        contentValues.put("ADDEDTOWATCHLIST", "FALSE");

        sqLiteDatabase.insert("MOVIES", null, contentValues);
    }

    public void insertUser(User user) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("EMAIL", user.getEmail());
        contentValues.put("FIRSTNAME", user.getFirstName());
        contentValues.put("PHONE", user.getPhoneNumber());
        contentValues.put("GENDER", user.getGender());
        contentValues.put("PASSWORD", user.getPassword());
        sqLiteDatabase.insert("USERS", null, contentValues);
    }

    public void addToWatchList(int position, String email) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", email+"_"+(position+1)+"");
        contentValues.put("MOVIE_ID", position + 1);
        contentValues.put("USER_EMAIL", email);
        contentValues.put("ADD_DATE",new Date().toString());
        sqLiteDatabase.insert("WATCHLIST", null, contentValues);

    }

    public void removeFromWatchList(int position, String email) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String strSQL = "DELETE FROM WATCHLIST WHERE MOVIE_ID = " + (position + 1) + " AND USER_EMAIL = '" + email + "'";
        sqLiteDatabase.execSQL(strSQL);
    }


    public void addToComments(int position, String email, String comment, String name) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", getAllComments().getCount());
        contentValues.put("MOVIE_ID", position);
        contentValues.put("USER_EMAIL", email);
        contentValues.put("USER_NAME", name);
        contentValues.put("COMMENT", comment);
        sqLiteDatabase.insert("COMMENTS", null, contentValues);

    }
}
