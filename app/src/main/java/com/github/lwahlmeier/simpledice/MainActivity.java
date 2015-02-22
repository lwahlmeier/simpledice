package com.github.lwahlmeier.simpledice;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.SeekBar;
import android.widget.TextView;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;


public class MainActivity extends ActionBarActivity {
    static final int[][] dicemap = new int[20][];
    static final HashMap<Byte, Bitmap> dice = new HashMap<Byte, Bitmap>();
    static final SecureRandom rnd = new SecureRandom();
    static {
        HashSet<int[]> pos = new HashSet<int[]>();
        Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint.setColor(Color.RED);
        canvas.drawPaint(paint);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(50, 50, 10, paint);
        dice.put((byte)1, bitmap);

        bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.RGB_565);
        canvas = new Canvas(bitmap);
        paint.setColor(Color.RED);
        canvas.drawPaint(paint);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(20, 80, 10, paint);
        canvas.drawCircle(80, 20, 10, paint);
        dice.put((byte)2, bitmap);

        bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.RGB_565);
        canvas = new Canvas(bitmap);
        paint.setColor(Color.RED);
        canvas.drawPaint(paint);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(80, 80, 10, paint);
        canvas.drawCircle(20, 20, 10, paint);
        canvas.drawCircle(50, 50, 10, paint);
        dice.put((byte)3, bitmap);

        bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.RGB_565);
        canvas = new Canvas(bitmap);
        paint.setColor(Color.RED);
        canvas.drawPaint(paint);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(80, 80, 10, paint);
        canvas.drawCircle(20, 20, 10, paint);
        canvas.drawCircle(20, 80, 10, paint);
        canvas.drawCircle(80, 20, 10, paint);
        dice.put((byte)4, bitmap);

        bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.RGB_565);
        canvas = new Canvas(bitmap);
        paint.setColor(Color.RED);
        canvas.drawPaint(paint);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(80, 80, 10, paint);
        canvas.drawCircle(20, 20, 10, paint);
        canvas.drawCircle(20, 80, 10, paint);
        canvas.drawCircle(80, 20, 10, paint);
        canvas.drawCircle(50, 50, 10, paint);
        dice.put((byte)5, bitmap);

        bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.RGB_565);
        canvas = new Canvas(bitmap);
        paint.setColor(Color.RED);
        canvas.drawPaint(paint);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(80, 80, 10, paint);
        canvas.drawCircle(20, 20, 10, paint);
        canvas.drawCircle(20, 80, 10, paint);
        canvas.drawCircle(80, 20, 10, paint);
        canvas.drawCircle(80, 50, 10, paint);
        canvas.drawCircle(20, 50, 10, paint);
        dice.put((byte)6, bitmap);

        dicemap[0] = new int[] {350, 350};

        dicemap[1] = new int[] {100, 300, 600, 300};

        dicemap[2] = new int[] {100, 100, 600, 100, 350, 600};

        dicemap[3] = new int[] {100, 100, 600, 100, 100, 600, 600, 600};

        dicemap[4] = new int[] {100, 100, 700, 100, 400, 400, 100, 700, 700, 700};

        dicemap[5] = new int[] {
                200, 200,
                600, 200,
                200, 450,
                600, 450,
                200, 700,
                600, 700};

        dicemap[6] = new int[] {
                300, 200,
                600, 200,
                200, 450,
                450, 450,
                700, 450,
                300, 700,
                600, 700};

        dicemap[7] = new int[] {
                300, 200,
                600, 200,
                200, 450,
                450, 450,
                700, 450,
                200, 700,
                450, 700,
                700, 700};

        dicemap[8] = new int[] {
                200, 200,
                450, 200,
                700, 200,
                200, 450,
                450, 450,
                700, 450,
                200, 700,
                450, 700,
                700, 700};
    }
    ImageView iv;
    EditText et;
    Bitmap bitmap;
    Canvas canvas;
    SeekBar seeker;
    TextView tv;
    SimpleExecutor executor = new SimpleExecutor();
    AtomicBoolean isRunning = new AtomicBoolean(false);
    int rolls = 0;

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
                    canvas.drawBitmap(checkScale(dice.get(drs[i]), dn), dicemap[dn][i*2], dicemap[dn][(i*2)+1], null);
                }
                iv.setImageBitmap(bitmap);
            }
        });
        return drs;
    }

    private Bitmap checkScale(Bitmap b, int dice) {
        if(dice<=3) {
            return Bitmap.createScaledBitmap(b, 300, 300, false);
        } else if (dice > 3  && dice < 6) {
            return Bitmap.createScaledBitmap(b, 200, 200, false);
        }
        return b;
    }

    private class Roller implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(isRunning.compareAndSet(false, true)) {
                seeker.setEnabled(false);
                executor.execute(new Runnable() {
                    public void run() {
                        byte[] rolld = new byte[0];
                        for (int i = 0; i < 10; i++) {
                            rolld = doRoll();
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        rolls++;
                        final byte[] last = rolld;
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                seeker.setEnabled(true);
                                isRunning.set(false);
                                et.append(sb.toString());
                                int scrollAmount = (et.getLayout().getLineTop(et.getLineCount()) - et.getHeight()) + et.getLineHeight();
                                et.scrollTo(0, scrollAmount);
                            }
                        });

                    }
                });
            }
        }
    }

    private class SimpleExecutor extends Thread {
        LinkedBlockingDeque<Runnable> lbq = new LinkedBlockingDeque<Runnable>();

        public SimpleExecutor() {
            start();
        }

        @Override
        public void run() {
            while(true) {
                try {
                    lbq.take().run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void execute(Runnable run) {
            lbq.add(run);
        }
    }
}
