package com.example.diana.booksrecommender;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class BookDetailsActivity extends MainActivity {


    private static final String LOG_TAG = BookDetailsActivity.class.getName();

    private ImageView bookImageView;
    private TextView authorTextView;
    private TextView titleTextView;
    private TextView rateTextView;
    private TextView pagesTextView;
    private TextView tagsTextView;
    private TextView descriptionTextView;
    private Button favoriteBooksButton;
    private DatabaseHelper myDb;

    //the current book id from db is stored here, in case the book does not exist in db this will be -1
    private int bookId;
    //true if the current user has the book in favorites table already
    private boolean existsInFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        final Book currentBook = (Book) getIntent().getSerializableExtra("Book");
        myDb = new DatabaseHelper(this);
        bookImageView = (ImageView)findViewById(R.id.book_cover);
        authorTextView = (TextView)findViewById(R.id.author);
        titleTextView =  (TextView)findViewById(R.id.title);
        rateTextView =  (TextView)findViewById(R.id.rate);
        pagesTextView = (TextView)findViewById(R.id.pages);
        tagsTextView = (TextView)findViewById(R.id.tags);
        descriptionTextView = (TextView)findViewById(R.id.description);
        favoriteBooksButton = (Button)findViewById(R.id.fav);

        Glide.with(this).load(currentBook.getImgUrl()).into(bookImageView);
        authorTextView.setText("by " + currentBook.getAuthor());
        titleTextView.setText(currentBook.getTitle());
        rateTextView.setText("Avg. " + currentBook.getAverageRating());
        pagesTextView.setText(currentBook.getPageCount() + " pages");
        if (currentBook.getCategories() == null) {
            tagsTextView.setText("No Category");
        }else {
            String[] aux = currentBook.getCategories();
            for(int i = 0;i < aux.length;i++){
                tagsTextView.append(aux[i] + "\n");
            }
        }
        descriptionTextView.setText(currentBook.getDescription());
        bookId = myDb.bookExists(currentBook.getId());
        existsInFavorites = false;
        if (bookId != -1) {
            existsInFavorites = myDb.favoriteExists(MainActivity.sUserId, bookId);
            if (existsInFavorites == true) favoriteBooksButton.setText("Remove from FAVORITES");
        }

        favoriteBooksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (bookId == -1) {
                        bookId = myDb.insertBook(currentBook.getId(), currentBook.getTitle(), currentBook.getAuthor(), currentBook.getCategories()[0],currentBook.getImgUrl());
                    }
                    //in case the book is in favorites list remove it and redirect to favorites books activity
                    if (myDb.favoriteExists(MainActivity.sUserId, bookId)) {
                        myDb.deleteFavorite(bookId, MainActivity.sUserId);
                        Toast.makeText(BookDetailsActivity.this, "Book removed from favorites", Toast.LENGTH_LONG).show();
                        if (currentBook.getSelfLink() == null) {
                            Intent intent = new Intent(BookDetailsActivity.this,FavoritesBooksActivity.class);
                            startActivity(intent);
                        }
                    } else {
                        boolean isInserted = myDb.insertFavorite(MainActivity.sUserId, bookId);
                        if (isInserted == true) {
                            Toast.makeText(BookDetailsActivity.this, "Book added to favorites", Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(BookDetailsActivity.this, "ERROR try again", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Log.e(LOG_TAG, "Problem insering ro removind book from favorites", e);
                }
            }
        });

    }
}
