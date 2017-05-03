package com.example.diana.booksrecommender;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diana on 4/30/2017.
 */

public class Helper {
    private static final String LOG_TAG = Helper.class.getSimpleName();

    private Helper() {
    }

    public static List<Book> fetchBookData(String Url) {

        URL url = createUrl(Url);
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(BooksActivity.BookSearchAsync.class.getSimpleName(), "Problem making the HTTP request.", e);
        }

        List<Book> books = extractFeatureFromJson(jsonResponse);

        return books;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(BooksActivity.BookSearchAsync.class.getSimpleName(), "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(BooksActivity.BookSearchAsync.class.getSimpleName(), "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(BooksActivity.BookSearchAsync.class.getSimpleName(), "Problem retrieving the book JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Book> extractFeatureFromJson(String bookJSON) {
        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }

        List<Book> books = new ArrayList<>();

        try {

            JSONObject baseJsonResponse = new JSONObject(bookJSON);

            JSONArray bookArray = baseJsonResponse.getJSONArray("items");

            for (int i = 0; i < bookArray.length(); i++)
            {

                JSONObject currentBook = bookArray.getJSONObject(i);

                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");
                Log.i("Title from volumeInfo", volumeInfo.getString("title"));
                Log.i("Date from volumeInfo", volumeInfo.getString("publishedDate"));

                // Set Author to missing author. This will be loaded in case there is no author available
                String author = "No author";
                try {
                    JSONArray authors = volumeInfo.getJSONArray("authors");
                    author = authors.getString(0);
                    Log.i("Author: ", author);
                } catch (JSONException e) {
                    Log.e("JSON Exception", "No author available for this book", e);
                }

                Log.i("InfoLink da volumeInfo", volumeInfo.getString("infoLink"));

                String title = volumeInfo.getString("title");


                String selfLink = currentBook.getString("volumeInfo");
                Double averageRating = 0.0;
                int pageCount = 0;
                String[] categories = null;
                String description = "";
                try {

                    if (volumeInfo.has("averageRating"))
                        averageRating = volumeInfo.getDouble("averageRating");
                    if (volumeInfo.has("pageCount"))
                        pageCount = volumeInfo.getInt("pageCount");
                    if (volumeInfo.has("description"))
                        description = volumeInfo.getString("description");
                    if (volumeInfo.has("categories")) {
                        JSONArray cat = volumeInfo.getJSONArray("categories");
                        categories = new String[cat.length()];
                        for (int j = 0; j < cat.length(); j++) {
                            categories[j] = cat.getString(j);
                        }
                    }
                }catch (Exception e)
                {

                }

                JSONObject images = volumeInfo.getJSONObject("imageLinks");
                String imgUrl = images.getString("smallThumbnail");
                String id = currentBook.getString("id");

                Book book = new Book(title, author,imgUrl,averageRating,pageCount,description,categories,selfLink,id);

                books.add(book);
            }

        } catch (JSONException e) {
            Log.e("Helper", "Problem parsing the Google Books API JSON results", e);
        }

        return books;
    }

}
