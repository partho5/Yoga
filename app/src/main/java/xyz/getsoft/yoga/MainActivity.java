package xyz.getsoft.yoga;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    TextView asanNameTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        asanNameTV = (TextView)findViewById(R.id.asanName);


        String AsanNames[] = {"শবাসন", "উজ্জীবনাসন" , "অর্ধচন্দ্রাসন" ,"ভদ্রাসন", "গোমুখাসন", "জানুশিরাসন", "পবন মুক্তাসন", "শবাসন"};

        int AsanStepTimes[][] = {
                {3*5},
                {20,20,20,20,20,20,30},
                {10,5,10},
                {30},
                {30,10,30},
                {15,10,15},
                {15,5,15,5,15},
                {5*60}
        };

        int asanInterval = 10;
        int asanFrequency[] = {1,2,2,1,3,3,3,1};


        int asanIndex = 0;
        int nextNotifyTime = 0;

        for(final String asan: AsanNames){
            Log.d("asan", "\n-----------------"+asan+"---------------\n");
            for (int f=0; f < asanFrequency[asanIndex]; f++ ){
                Log.d("frequency", "FREQUENCY : "+f);
                for (final int duration : AsanStepTimes[asanIndex]){
                    nextNotifyTime+= duration;
                    final Handler handler = new Handler();
                    final int finalF = f;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Calendar now = Calendar.getInstance();
                            int hour = now.get(Calendar.HOUR);
                            int minute = now.get(Calendar.MINUTE);
                            int sec = now.get(Calendar.SECOND);

                            Log.d("duration", asan+"("+ (finalF+1) +") > "+hour+" : "+minute+" : "+sec);
                            makeNotification(asan+", "+ (finalF+1),  hour+" : "+minute+" : "+sec);
                            asanNameTV.setText(asan);
                        }

                    }, nextNotifyTime*1000);

                }
            }
            ++asanIndex;
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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


    public void makeNotification(String tittle, String body){
        int intent_id = 22;
        Notification notification;
        NotificationManager notificationManager;

        Intent myIntent=new Intent(getApplicationContext(),MainActivity.class);
        PendingIntent pendingIntent= PendingIntent.getActivity(getApplicationContext(), (int) System.currentTimeMillis(),
                myIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        notification=new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle(tittle)
                .setContentText(body)
                .setTicker("Yoga")
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();

        notificationManager=(NotificationManager) getApplicationContext().
                getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
        notificationManager.notify(intent_id,notification);
    }

}
