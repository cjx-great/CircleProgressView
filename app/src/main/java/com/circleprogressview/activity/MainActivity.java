package com.circleprogressview.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.circleprogressview.R;
import com.circleprogressview.view.CircleProgressBar;

public class MainActivity extends AppCompatActivity {

    private Button start = null;

    private CircleProgressBar circleProgressBar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start = (Button) findViewById(R.id.start);
        circleProgressBar = (CircleProgressBar) findViewById(R.id.progress);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int m = 0;
                        while (m < 80){
                            circleProgressBar.setProgress(m);

                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            m++;
                        }

                    }
                }).start();
                for (int i = 0; i < 80; i++) {
                    circleProgressBar.setProgress(i);
                }
            }
        });
    }
}
