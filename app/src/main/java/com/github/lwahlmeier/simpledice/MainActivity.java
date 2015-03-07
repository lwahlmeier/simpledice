package com.github.lwahlmeier.simpledice;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.SeekBar;
import android.widget.TextView;

import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


public class MainActivity extends ActionBarActivity {
    private static final SecureRandom rnd = new SecureRandom();
    private ImageView iv;
    private EditText et;
    private Bitmap bitmap;
    private Canvas canvas;
    private SeekBar seeker;
    private TextView tv;
    private SimpleExecutor executor = new SimpleExecutor();
    private AtomicInteger rollsRunning = new AtomicInteger(0);
    private RollRunner rollRunner = new RollRunner();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.simpledice.simpledice.R.layout.activity_main);
        iv = (ImageView) findViewById(com.simpledice.simpledice.R.id.imageView);
        seeker = (SeekBar) findViewById(com.simpledice.simpledice.R.id.seekBar);
        tv = (TextView) findViewById(com.simpledice.simpledice.R.id.textView);
        et = (EditText) findViewById(com.simpledice.simpledice.R.id.editText);
        et.setFocusable(false);
        et.setScroller(new Scroller(getApplicationContext()));
        et.setVerticalScrollBarEnabled(true);
        et.setMovementMethod(new ScrollingMovementMethod());
        et.setText("Welcome!");
        bitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        doRoll();
        iv.setOnClickListener(new Roller());
        seeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv.setText("  "+Integer.toString(progress+1));
                doRoll();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public byte[] doRoll() {
        final int dn = seeker.getProgress();
        final byte[] drs = new byte[dn+1];
        for(int i=0; i<dn+1; i++) {
            byte roll = (byte)(Math.abs((rnd.nextInt() % 6)) + 1);
            drs[i] = roll;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Paint paint = new Paint(Color.BLACK);
                canvas.drawPaint(paint);
                for(int i=0; i<dn+1; i++) {
                    canvas.drawBitmap(checkScale(drs[i], dn), DiceCache.dicemap[dn][i*2], DiceCache.dicemap[dn][(i*2)+1], null);
                }
                iv.setImageBitmap(bitmap);

            }
        });
        return drs;
    }

    private Bitmap checkScale(byte diceRoll, int dn) {
        if(dn<=3) {
            return DiceCache.getDice(diceRoll, 300);
        } else if (dn > 3  && dn < 6) {
            return DiceCache.getDice(diceRoll, 200);
        } else {
            return DiceCache.getDice(diceRoll, 100);
        }
    }

    private class Roller implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //Log.d("main", "click:"+rollsRunning.get());
            if(rollsRunning.incrementAndGet() < 3) {
                seeker.setEnabled(false);
                //We farm this off to another thread because we dont want to pause on the
                //UI thread ever
                executor.execute(rollRunner);
            } else {
                rollsRunning.decrementAndGet();
            }
        }
    }

    private class RollRunner implements Runnable {
        private int rolls = 0;
        @Override
        public void run() {
            byte[] rolld = new byte[0];
            for (int i = 0; i < 10; i++) {
                rolld = doRoll();
                try {
                    //We should probably do something better here
                    //But I didnt feel like writting a scheduler or bringing a good one in.
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //These are clicks for roll not times we call doRoll #confusing
            rolls++;
            final byte[] last = rolld;
            //TODO:change to string formatter
            final StringBuilder sb = new StringBuilder();
            sb.append("\nRoll:");
            sb.append(rolls);
            sb.append(" was:");
            for(int i=0; i<last.length; i++) {
                sb.append(last[i]);
                if(i != last.length-1) {
                    sb.append(",");
                }
            }
            if(rollsRunning.decrementAndGet() == 0) {
                runOnUiThread(new FinishRoll(sb.toString()));
            }
        }
    }

    private class FinishRoll implements Runnable {
        String out;

        FinishRoll(String out) {
            this.out = out;
        }
        @Override
        public void run() {
            seeker.setEnabled(true);
            et.append(out);
            //Is there an easier way to scroll down?
            int scrollAmount = (et.getLayout().getLineTop(et.getLineCount()) - et.getHeight()) + et.getLineHeight();
            et.scrollTo(0, scrollAmount);
        }
    }

}
