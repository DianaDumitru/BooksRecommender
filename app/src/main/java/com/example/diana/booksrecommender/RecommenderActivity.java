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

public class RecommenderActivity extends MainActivity {


    private BookAdapter adapter;

    private ListView bookListView;
    private LinearLayout recommenderEmptyLiniarLayout;
    DatabaseHelper myDb;
    Button recommenderSeachButton;
    public static final String GOOGLE_BOOKS_REQUEST_URL =
            "https://www.googleapis.com/books/v1/volumes?q=intitle:";
    //put you key here
    private static final String GOOGLE_BOOKS_APP_KEY = "&key=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommender);

        recommenderEmptyLiniarLayout = (LinearLayout) findViewById(R.id.rec_case_empty);        
        bookListView = (ListView)findViewById(R.id.rec_books_list_view);
        recommenderSeachButton = (Button)findViewById(R.id.rec_search);
        recommenderSeachButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecommenderActivity.this,BooksActivity.class);
                startActivity(intent);
            }
        });
        recommenderEmptyLiniarLayout.setVisibility(View.GONE);
        myDb = new DatabaseHelper(this);
        adapter = new BookAdapter(this, new ArrayList< Book >());
        bookListView.setAdapter(adapter);
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView < ? > adapterView, View view, int position, long l) {
                Book currentBook = adapter.getItem(position);
                Intent intent = new Intent(RecommenderActivity.this,BookDetailsActivity.class);
                intent.putExtra("Book",currentBook);
                startActivity(intent);
            }
        });

        adapter.clear();
        BookRecAsync mt = new BookRecAsync();
        mt.execute(MainActivity.sUserId);
    }

    public class BookRecAsync extends AsyncTask<Integer, Void, List<Book>> {
        BookAdapter bookAdapter;

        @Override
        protected void onPreExecute() {
            bookAdapter = (BookAdapter)bookListView.getAdapter();
        }

        @Override
        protected List<Book> doInBackground(Integer... params) {
            int id = params[0];
            List<Book> list = myDb.getBooks(id);
            if (list == null) return list;
            //the recommender uses users current favorite books and provides more with same words in title or from the same category
            //it randomly peeks one book from favorites
            Book analysedBook = list.get((int)Math.random()*list.size());
            String title = analysedBook.getTitle().split(" ")[0];
            String url;
            String cateory = analysedBook.getCategories()[0];
            if (cateory.startsWith("NO"))
                url = GOOGLE_BOOKS_REQUEST_URL + title  + GOOGLE_BOOKS_APP_KEY;
            else
                url = GOOGLE_BOOKS_REQUEST_URL + cateory  + GOOGLE_BOOKS_APP_KEY;
            list = Helper.fetchBookData(url);
            if (list == null)
                return Helper.fetchBookData(GOOGLE_BOOKS_REQUEST_URL + title  + GOOGLE_BOOKS_APP_KEY);
            return list;
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            if (books == null)
            {
                recommenderEmptyLiniarLayout.setVisibility(View.VISIBLE);
                bookListView.setVisibility(View.GONE);
            }
            else{
                adapter.addAll(books);
            }

        }
    }
}
