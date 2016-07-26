package com.lyl.progressview;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lyl.progressview.progress.ColorOrientation;
import com.lyl.progressview.progress.ProgressView;

public class MainActivity extends AppCompatActivity {
    private ProgressView mProgressView, mProgressView2, mProgressView3;
    private int i = 0, j = 0, z = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        initView();
    }

    private void initView() {
        LinearLayout bodyLayout = (LinearLayout) findViewById(R.id.body_layout);
        mProgressView = (ProgressView) findViewById(R.id.progress);
        mProgressView.setMaxCount(100);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (i < 75) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressView.setCurrentCount(i);
                        }
                    });
                    i++;
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        mProgressView2 = (ProgressView) findViewById(R.id.progress2);
        mProgressView2.setMaxCount(100);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (j < 100) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressView2.setCurrentCount(j);
                        }
                    });
                    j++;
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        mProgressView3 = new ProgressView(this);
        mProgressView3.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mProgressView3.setShaderColors(new int[]{
                ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, R.color.progress_start)});
        mProgressView3.setColorOrientation(ColorOrientation.LEFT_TOP_RIGHT);
        mProgressView3.setMaxCount(100);
        mProgressView3.setTextSize(20);
        mProgressView3.setEmptyStrokeWidth(20);
        mProgressView3.setFullStrokeWidth(18);
        bodyLayout.addView(mProgressView3);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (z < 75) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressView3.setCurrentCount(z);
                        }
                    });
                    z++;
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
