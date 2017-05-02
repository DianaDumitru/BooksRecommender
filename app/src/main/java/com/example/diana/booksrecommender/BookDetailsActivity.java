package com.example.diana.booksrecommender;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class BookDetailsActivity extends AppCompatActivity {


    ImageView imageView;
    TextView author;
    TextView title;
    TextView rate;
    TextView pages;
    TextView tags;
    TextView description;
    Button fav;
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        final Book currentBook = (Book) getIntent().getSerializableExtra("Book");
        myDb = new DatabaseHelper(this);
        imageView = (ImageView)findViewById(R.id.book_cover);
        author = (TextView)findViewById(R.id.author);
        title =  (TextView)findViewById(R.id.title);
        rate =  (TextView)findViewById(R.id.rate);
        pages = (TextView)findViewById(R.id.pages);
        tags = (TextView)findViewById(R.id.tags);

        Glide.with(this).load(currentBook.getImgUrl()).into(imageView);
        author.setText("by " + currentBook.getAuthors());
        title.setText(currentBook.getTitle());
        rate.setText("Avg. " + currentBook.getAverageRating());
        pages.setText(currentBook.getPageCount() + " pages");
        if (currentBook.getCategories() == null)
        {
            tags.setText("No Category");
        }else
        {
            String[] aux = currentBook.getCategories();
            for(int i = 0;i < aux.length;i++){
                tags.append(aux[i] + "\n");
            }
        }
        description = (TextView)findViewById(R.id.description);
        description.setText(currentBook.getDescription());
        fav = (Button)findViewById(R.id.fav);
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (myDb.bookExists(MainActivity.email,currentBook.getId()))
                {
                    myDb.deleteFavorite(currentBook.getId(),MainActivity.email);
                    Toast.makeText(BookDetailsActivity.this,"Book removed from favorites",Toast.LENGTH_LONG).show();
                }else
                {
                    boolean isInserted = myDb.insertData(MainActivity.email,currentBook.getId(),currentBook.getTitle(),currentBook.getAuthors(),currentBook.getCategories()[0]);
                    if (isInserted == true)
                    {
                        Toast.makeText(BookDetailsActivity.this,"Book added to favorites",Toast.LENGTH_LONG).show();
                    }else
                        Toast.makeText(BookDetailsActivity.this,"ERROR try again",Toast.LENGTH_LONG).show();
                }


            }
        });

    }
}
