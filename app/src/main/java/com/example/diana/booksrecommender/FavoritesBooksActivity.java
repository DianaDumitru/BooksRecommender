package com.example.diana.booksrecommender;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class FavoritesBooksActivity extends MainActivity {


    private BookAdapter adapter;

    private ListView bookListView;
    private LinearLayout favoritesEmptyLiniarLayout;
    DatabaseHelper myDb;
    Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_books);
        favoritesEmptyLiniarLayout = (LinearLayout) findViewById(R.id.fav_case_empty);
        favoritesEmptyLiniarLayout.setVisibility(View.GONE);
        bookListView = (ListView)findViewById(R.id.fav_books_list_view);
        searchButton = (Button)findViewById(R.id.fav_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FavoritesBooksActivity.this,BooksActivity.class);
                startActivity(intent);
            }
        });
        myDb = new DatabaseHelper(this);
        adapter = new BookAdapter(this, new ArrayList< Book >());
        bookListView.setAdapter(adapter);
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView < ? > adapterView, View view, int position, long l) {
                Book currentBook = adapter.getItem(position);
                Intent intent = new Intent(FavoritesBooksActivity.this,BookDetailsActivity.class);
                intent.putExtra("Book",currentBook);
                startActivity(intent);
            }
        });

        adapter.clear();
        BookFavAsync mt = new BookFavAsync();
        mt.execute(MainActivity.sUserId);

    }

    public class BookFavAsync extends AsyncTask<Integer, Void, List<Book>> {
        BookAdapter bookAdapter;

        @Override
        protected void onPreExecute() {
            bookAdapter = (BookAdapter)bookListView.getAdapter();
        }

        @Override
        protected List<Book> doInBackground(Integer... params) {
            int id = params[0];
            return myDb.getBooks(id);
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            if (books == null) {
                favoritesEmptyLiniarLayout.setVisibility(View.VISIBLE);
                bookListView.setVisibility(View.GONE);
            }
            else{
                adapter.addAll(books);
            }

        }
    }
}
