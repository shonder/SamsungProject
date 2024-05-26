package com.example.snake;



import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;

import com.example.snake.db.MyDbManager;


class SnakeEngine extends SurfaceView implements Runnable {
    private Thread thread = null;

    private Context context;

    private SoundPool soundPool;
    private int eat_bob = -1;
    private int snake_crash = -1;

    public enum Heading {UP, RIGHT, DOWN, LEFT, STAY}

    private Heading heading;
    private Heading previousHeading;

    private int screenX;
    private int screenY;

    private int snakeLength;

    private int bobX;
    private int bobY;

    private int blockSize;

    private int NUM_BLOCKS_WIDE;
    private int numBlocksHigh;

    private long nextFrameTime;

    static long FPS = 10;

    static long MILLIS_PER_SECOND = 1000;

    public int score;
    public int maxScore;

    class SnakePoint {
        int x, y;
        public SnakePoint(int x, int y) {
            this.x = x;
            this.y = y;
        }
        public SnakePoint() {
        }
    }
    SnakePoint snakePoint = new SnakePoint();
    LinkedList<SnakePoint> snakeXY;
    private int[] snakeXs;
    private int[] snakeYs;

    private volatile boolean isPlaying;
    // A canvas for our paint
    private Canvas canvas;
    // Required to use canvas
    private SurfaceHolder surfaceHolder;
    // Some paint for our canvas
    private Paint paint;

    private GestureDetector gestureDetectorCompat;
    private GestureListener gestureListener = new GestureListener();
    Bitmap snake;
    Bitmap point;
    Bitmap resizedPoint;
    Bitmap resizedSnake;
    public SnakeEngine(Context context, Point size) {
        super(context);

        gestureDetectorCompat = new GestureDetector(context, gestureListener);

        context = context;

        screenX = size.x;
        screenY = size.y;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            NUM_BLOCKS_WIDE = 40;
        else
            NUM_BLOCKS_WIDE = 20;

        blockSize = screenX / NUM_BLOCKS_WIDE;

        numBlocksHigh = screenY / blockSize;



        soundPool = new SoundPool.Builder().setMaxStreams(10).build();
        try {

            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;


            descriptor = assetManager.openFd("eat_bob.mp3");
            eat_bob = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("snake_crash.mp3");
            snake_crash = soundPool.load(descriptor, 0);

        } catch (IOException e) {

        }



        surfaceHolder = getHolder();
        paint = new Paint();



        snakeXY = new LinkedList<>();


        maxScore = 0;


        newGame();
    }
    @Override
    public void run() {
        while (isPlaying) {

            if(updateRequired()) {
                update();
                draw();
            }

        }
    }
    public void pause() {
        isPlaying = false;
        try {
            thread.join();
        } catch (InterruptedException e) {

        }
    }
    public void resume() {
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    public void newGame() {

        snakeXY.clear();
        snakeLength = 1;
        int xs = NUM_BLOCKS_WIDE / 2;
        int ys = numBlocksHigh / 2;
        snakePoint.x = xs;
        snakePoint.y = ys;
        snakeXY.addFirst(snakePoint);

        heading = Heading.STAY;


        spawnBob();


        score = 0;


        nextFrameTime = System.currentTimeMillis();
    }

    public void spawnBob() {
        Random random = new Random();
        bobX = random.nextInt(NUM_BLOCKS_WIDE - 1) + 1;
        bobY = random.nextInt(numBlocksHigh - 1) + 1;
    }
    private void eatBob(){

        //snakeLength++;
        SnakePoint sn = new SnakePoint(snakeXY.getLast().x,snakeXY.getLast().y);
        snakeXY.addLast(sn);

        spawnBob();

        score = score + 1;
        soundPool.play(eat_bob, 1, 1, 0, 0, 1);
    }
    private void moveSnake(){


        int xs = snakeXY.getFirst().x;
        int ys = snakeXY.getFirst().y;
        switch (heading) {
            case UP:
                ys--;

                break;

            case RIGHT:
                xs++;

                break;

            case DOWN:
                ys++;

                break;

            case LEFT:
                xs--;

                break;
        }
        SnakePoint head = new SnakePoint(xs, ys);
        snakeXY.addFirst(head);
        snakeXY.removeLast();

    }

    private boolean detectDeath(){

        boolean dead = false;

        if (snakeXY.getFirst().x == -1) dead = true;
        if (snakeXY.getFirst().x >= NUM_BLOCKS_WIDE) dead = true;
        if (snakeXY.getFirst().y == -1) dead = true;
        if (snakeXY.getFirst().y == numBlocksHigh) dead = true;



        if (snakeXY.size() > 4) {
            for (int i = snakeXY.size() - 1; i > 0; i--) {
                if ((snakeXY.getFirst().x == snakeXY.get(i).x) && (snakeXY.getFirst().y == snakeXY.get(i).y)) {
                    dead = true;
                }
            }
        }



        return dead;
    }

    public void update() {


        if (snakeXY.getFirst().x == bobX && snakeXY.getFirst().y == bobY) eatBob();

        moveSnake();

        if (detectDeath()) {

            if (score > maxScore) maxScore = score;
            soundPool.play(snake_crash, 1, 1, 0, 0, 1);
            newGame();
        }
    }

    public void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();


            canvas.drawColor(Color.rgb(55, 55, 55));


            paint.setColor(Color.argb(255, 255, 255, 255));


            paint.setTextSize(90);
            canvas.drawText("Score:" + score, 10, 70, paint);

            for (SnakePoint sn : snakeXY) {
                canvas.drawRect(sn.x * blockSize,
                        sn.y * blockSize,
                        sn.x * blockSize + blockSize,
                        sn.y * blockSize + blockSize, paint);
            }



            paint.setColor(Color.argb(255, 255, 0, 0));


            canvas.drawRect(bobX * blockSize,
                    (bobY * blockSize),
                    (bobX * blockSize) + blockSize,
                    (bobY * blockSize) + blockSize,
                    paint);

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    public boolean updateRequired() {


        if(nextFrameTime <= System.currentTimeMillis()){

            nextFrameTime =System.currentTimeMillis() + MILLIS_PER_SECOND / FPS;

            return true;
        }

        return false;
    }

    public class GestureListener implements GestureDetector.OnGestureListener {

        @Override
        public boolean onDown(@NonNull MotionEvent motionEvent) {
            return false;
        }

        @Override
        public void onShowPress(@NonNull MotionEvent motionEvent) {

        }

        @Override
        public boolean onSingleTapUp(@NonNull MotionEvent motionEvent) {
            return false;
        }

        @Override
        public boolean onScroll(@Nullable MotionEvent motionEvent, @NonNull MotionEvent motionEvent1, float v, float v1) {
            return false;
        }

        @Override
        public void onLongPress(@NonNull MotionEvent motionEvent) {

        }

        @Override
        public boolean onFling(@Nullable MotionEvent motionEvent, @NonNull MotionEvent motionEvent1, float v, float v1) {
            float deltaX = motionEvent1.getX() - motionEvent.getX();
            float deltaY = motionEvent1.getY() - motionEvent.getY();

            if (Math.abs(deltaX) > Math.abs(deltaY)) {
                if (deltaX > 0) {
                    //RIGHT
                    if (snakeXY.size() > 1) {
                        if (previousHeading != Heading.LEFT) heading = Heading.RIGHT;
                    } else heading = Heading.RIGHT;
                } else {
                    //LEFT
                    if (snakeXY.size() > 1) {
                        if (previousHeading != Heading.RIGHT) heading = Heading.LEFT;
                    } else heading = Heading.LEFT;
                }
            } else {
                if (deltaY > 0) {
                    //DOWN
                    if (snakeXY.size() > 1) {
                        if (previousHeading != Heading.UP) heading = Heading.DOWN;
                    } else heading = Heading.DOWN;
                } else {
                    //UP
                    if (snakeXY.size() > 1) {
                        if (previousHeading != Heading.DOWN) heading = Heading.UP;
                    } else heading = Heading.UP;
                }
            }
            previousHeading = heading;
            return true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        gestureDetectorCompat.onTouchEvent(motionEvent);

        return true;
    }
}
