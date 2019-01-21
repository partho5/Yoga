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
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    TextView asanNameTV;
    TextView asanCountTV;
    ImageView currentStateImageIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        asanNameTV = (TextView)findViewById(R.id.asanName);
        asanCountTV = (TextView)findViewById(R.id.asanCount);
        currentStateImageIV = (ImageView) findViewById(R.id.currentStateImage);


        String AsanNames[] = {"শবাসন", "উজ্জীবনাসন" , "অর্ধচন্দ্রাসন" ,"ভদ্রাসন", "গোমুখাসন", "জানুশিরাসন", "পবন মুক্তাসন", "শবাসন"};


        int AsanStepTimes[][] = {
                {3*60},
                {20,20,20,20,20,20,30},
                {20,10,20},
                {30},
                {30,30},
                {30,30},
                {20,20,20},
                {5*60}
        };
        int endShabasanTime = 5*60;



//        int AsanStepTimes[][] = {
//                {3},
//                {2,2,2,2,2,2,3},
//                {2,2,2},
//                {3},
//                {3,3},
//                {2,2},
//                {2,2,2},
//                {5}
//        };
//        int endShabasanTime = 5;

        final String asanImages[][] = {
                {"shobasan"},
                {"ujjibon_back_bend", "ujjibon_front_bend", "ujjibon_parallel_to_ground", "ujjibon_lying_bend",
                        "ujjibon_flat_lying", "ujjibon_lying_bend", "ujjibon_front_bend"},
                {"ordho_bam", "ordho_straight", "ordho_dan"},
                {"vodrasan"},
                {"gomukh_dan", "gomukh_bam"},
                {"janu_bam", "janu_dan"},
                {"pobon_bam", "pobon_dan", "pobon_both"},
                {"shobasan"}
        };

        int asanInterval = 10;
        final int asanFrequency[] = {1,2,2,1,3,3,3,1};


        int asanIndex = 0;
        int nextNotifyTime = 0;

        for(final String asan: AsanNames){
            for (int f=0; f < asanFrequency[asanIndex]; f++ ){
                Log.d("frequency", "FREQUENCY : "+f);
                final int[] j = {-1};
                for (final int duration : AsanStepTimes[asanIndex]){
                    final Handler handler = new Handler();
                    final int finalF = f;
                    final int finalAsanIndex = asanIndex;
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
                            asanCountTV.setText( (finalF+1)+" / "+asanFrequency[finalAsanIndex] );
                            currentStateImageIV.setImageResource(getResources().
                                    getIdentifier(asanImages[finalAsanIndex][++j[0]], "drawable", getPackageName()));
                        }

                    }, nextNotifyTime*1000);
                    nextNotifyTime+= duration+asanInterval;
                }
            }
            ++asanIndex;

            if(asanIndex == asanFrequency.length-1){
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        makeNotification("Completed", "for today");
                        asanNameTV.setText("Completed");
                        asanCountTV.setText("");
                    }
                }, nextNotifyTime*1000+(endShabasanTime*1000));
            }
        }

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
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
