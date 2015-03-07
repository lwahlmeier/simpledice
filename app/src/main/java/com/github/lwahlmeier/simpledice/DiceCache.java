package com.github.lwahlmeier.simpledice;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by lwahlmeier on 2/21/15.
 */
public class DiceCache {
    public static final int[][] dicemap = new int[20][];
    private static final Map<Byte, DiceResCache> dice;
    static {
        HashMap<Byte, DiceResCache> tmpMap = new HashMap<>();
        Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint.setColor(Color.RED);
        canvas.drawPaint(paint);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(50, 50, 10, paint);
        tmpMap.put((byte)1, new DiceResCache(bitmap, 1));

        bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.RGB_565);
        canvas = new Canvas(bitmap);
        paint.setColor(Color.RED);
        canvas.drawPaint(paint);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(20, 80, 10, paint);
        canvas.drawCircle(80, 20, 10, paint);
        tmpMap.put((byte)2, new DiceResCache(bitmap, 2));

        bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.RGB_565);
        canvas = new Canvas(bitmap);
        paint.setColor(Color.RED);
        canvas.drawPaint(paint);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(80, 80, 10, paint);
        canvas.drawCircle(20, 20, 10, paint);
        canvas.drawCircle(50, 50, 10, paint);
        tmpMap.put((byte)3, new DiceResCache(bitmap, 3));

        bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.RGB_565);
        canvas = new Canvas(bitmap);
        paint.setColor(Color.RED);
        canvas.drawPaint(paint);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(80, 80, 10, paint);
        canvas.drawCircle(20, 20, 10, paint);
        canvas.drawCircle(20, 80, 10, paint);
        canvas.drawCircle(80, 20, 10, paint);
        tmpMap.put((byte)4, new DiceResCache(bitmap, 4));

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
        tmpMap.put((byte)5, new DiceResCache(bitmap, 5));

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
        tmpMap.put((byte)6, new DiceResCache(bitmap, 6));

        dice = Collections.unmodifiableMap(tmpMap);

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

    public static Bitmap getDice(int dn, int scale) {
        //Log.d("DC", dn+":"+scale+":"+dice.toString());
        return dice.get((byte)(dn)).getSize(scale);
    }


    public static class DiceResCache {
        public final Bitmap origDice;
        public final int number;
        public final HashMap<Integer, Bitmap> cachedSizes = new HashMap<>();

        public DiceResCache(Bitmap dice, int number) {
            origDice = dice;
            this.number = number;
            cachedSizes.put(dice.getWidth(), dice);
        }

        public Bitmap getSize(int size) {
            if(!cachedSizes.containsKey(size)) {
                synchronized (cachedSizes) {
                    if(!cachedSizes.containsKey(size)) {
                        Log.d("DC", "NewDice:"+number+":"+size);
                        cachedSizes.put(size, Bitmap.createScaledBitmap(origDice, size, size, false));
                    }
                }
            }
            return cachedSizes.get(size);
        }
    }
}
