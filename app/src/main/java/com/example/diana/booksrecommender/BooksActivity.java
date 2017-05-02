package com.example.diana.booksrecommender;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;
import java.util.List;

public class BooksActivity extends AppCompatActivity {

    private static final String LOG_TAG = BooksActivity.class.getName();

    private static final String GOOGLE_BOOKS_REQUEST_URL =
            "https://www.googleapis.com/books/v1/volumes?q=intitle:";
    //put yout key here
    private static final String GOOGLE_BOOKS_APP_KEY = "&key=AIzaSyANSxW0TSBfHpNpMeVM5Gt-ClM8cUzns5M";
    private String searchText;

    private static final int BOOK_LOADER_ID = 1;

    private BookAdapter adapter;

    private TextView emptyStateTextView;
    private ListView bookListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        bookListView = (ListView) findViewById(R.id.listview_book);
        emptyStateTextView = (TextView) findViewById(R.id.empty_view_book);
        bookListView.setEmptyView(emptyStateTextView);

        adapter = new BookAdapter(this, new ArrayList < Book > ());
        bookListView.setAdapter(adapter);
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView < ? > adapterView, View view, int position, long l) {
                Book currentBook = adapter.getItem(position);
                Intent intent = new Intent(BooksActivity.this,BookDetailsActivity.class);
                intent.putExtra("Book",currentBook);
                startActivity(intent);
            }
        });


        Button button = (Button) findViewById(R.id.start_search_button);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText searchEditText = (EditText) findViewById(R.id.search_text);
                searchText = searchEditText.getText().toString();
                adapter.clear();
                BookSearchAsync mt = new BookSearchAsync();
                mt.execute(GOOGLE_BOOKS_REQUEST_URL + searchText + GOOGLE_BOOKS_APP_KEY);
                Log.i(LOG_TAG, "Search button pressed, now searching: " + searchText);

            }
        });


    }

    public class BookSearchAsync extends AsyncTask<String, Void, List<Book>> {
        BookAdapter bookAdapter;

        @Override
        protected void onPreExecute() {
            bookAdapter = (BookAdapter)bookListView.getAdapter();
        }

        @Override
        protected List<Book> doInBackground(String... params) {
            String Url = params[0];
            if (Url == null) {
                return null;
            }
            return Helper.fetchBookData(Url);
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            adapter.addAll(books);
        }
    }


}
