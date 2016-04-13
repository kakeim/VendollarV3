package com.example.kevin.vendollarv3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

public class page3 extends AppCompatActivity {

    private static Context context;
    private View b;


    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page3);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        page3.context = getApplicationContext();
        b = findViewById(R.id.imageButton);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(page3.this, b);

                // Inflating the popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                // Registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        LayoutInflater inflater = getLayoutInflater();
                        final View view;
                        Intent intent;
                        switch (item.getItemId()) {
                            case R.id.home:
                                intent = new Intent(context, MainActivity.class);
                                startActivity(intent);
                                return true;
                            case R.id.recipes:
                                intent = new Intent(context, page1.class);
                                startActivity(intent);
                                return true;
                            case R.id.fotw:
                                intent = new Intent(context, page2.class);
                                startActivity(intent);
                                return true;
                            case R.id.checkout:
                                intent = new Intent(context, page3.class);
                                startActivity(intent);
                                return true;
                            default:
                                return false;
                        }

                    }
                });

                // Showing the popup menu
                popup.show();
            }
        });

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
    }

}
