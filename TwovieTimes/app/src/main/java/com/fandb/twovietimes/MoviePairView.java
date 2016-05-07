package com.fandb.twovietimes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by Alex on 4/26/16.
 */
public class MoviePairView extends View {
    private MoviePair mMoviePair;
    private int mWidth, mHeight;
    private static String TAG = "MoviePairView";

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public MoviePairView(Context context) {
        super(context);
    }

    public MoviePairView(Context context, MoviePair moviePair, int width){
        super(context);
        mMoviePair = moviePair;
        mWidth = width;
    }

    public void setMoviePair(MoviePair moviePair) {
        mMoviePair = moviePair;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mHeight = canvas.getHeight();
        mPaint.setColor(0xFFFFFFFF);
        canvas.drawRect(0,0,mWidth, mHeight - 5, mPaint);


        if (mMoviePair.movieOneStartBeforeMovieTwo() && !mMoviePair.movieOneEndsBeforeMovieTwo()
                || !mMoviePair.movieOneStartBeforeMovieTwo() && mMoviePair.movieOneEndsBeforeMovieTwo()){

            drawInnerPair(canvas);
            return;
        }

        mPaint.setColor(0xFF000000);
        mPaint.setTextSize(30.0f);
        mPaint.setTextAlign(Paint.Align.LEFT);

        if (mMoviePair.getMovieOneStart().before(mMoviePair.getMovieTwoStart())){
            canvas.drawText(mMoviePair.getMovieOneTitle() + ": " + mMoviePair.getMovieOneTimesString(), 40, 40, mPaint);
            mPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(mMoviePair.getMovieTwoTitle() + ": " + mMoviePair.getMovieTwoTimesString(), mWidth - 40, mHeight - 20, mPaint);
        }
        else{
            canvas.drawText(mMoviePair.getMovieTwoTitle() + ": " + mMoviePair.getMovieTwoTimesString(), 40, 40, mPaint);
            mPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(mMoviePair.getMovieOneTitle() + ": " + mMoviePair.getMovieOneTimesString(), mWidth - 40, mHeight - 20, mPaint);
        }

        //color stolen from Twitter ;)
        mPaint.setColor(0xFF469BEB);
        canvas.drawCircle(100, (mHeight/2 - 50), 20, mPaint);
        canvas.drawRect(100, (mHeight/2 - 50) - 5, mWidth - 250, (mHeight/2 - 50) + 5, mPaint);
        canvas.drawCircle(mWidth - 250, (mHeight/2 - 50), 20, mPaint);

        canvas.drawCircle(250, (mHeight/2 + 50), 20, mPaint);
        canvas.drawRect(250, (mHeight/2 + 50) - 5, mWidth - 100, (mHeight/2 + 50) + 5, mPaint);
        canvas.drawCircle(mWidth - 100, (mHeight/2 + 50), 20, mPaint);




        mPaint.setColor(0xFF000000);
        mPaint.setTextSize(30.0f);
        mPaint.setTextAlign(Paint.Align.LEFT);
    }

    public void drawInnerPair(Canvas canvas){
        mPaint.setColor(0xFF000000);
        mPaint.setTextSize(30.0f);
        mPaint.setTextAlign(Paint.Align.LEFT);

        if (mMoviePair.getMovieOneStart().before(mMoviePair.getMovieTwoStart())){
            canvas.drawText(mMoviePair.getMovieOneTitle() + ": " + mMoviePair.getMovieOneTimesString(), 0, 40, mPaint);
            canvas.drawText(mMoviePair.getMovieTwoTitle() + ": " + mMoviePair.getMovieTwoTimesString(), 0, mHeight - 20, mPaint);
        }
        else{
            canvas.drawText(mMoviePair.getMovieTwoTitle() + ": " + mMoviePair.getMovieTwoTimesString(), 0, 40, mPaint);
            canvas.drawText(mMoviePair.getMovieOneTitle() + ": " + mMoviePair.getMovieOneTimesString(), 0, mHeight - 20, mPaint);
        }

        //color stolen from Twitter ;)
        /*mPaint.setColor(0xFF469BEB);
        canvas.drawCircle(100, (mHeight/2 - 50), 20, mPaint);
        canvas.drawRect(100, (mHeight/2 - 50) - 5, mWidth - 250, (mHeight/2 - 50) + 5, mPaint);
        canvas.drawCircle(mWidth - 250, (mHeight/2 - 50), 20, mPaint);

        canvas.drawCircle(250, (mHeight/2 + 50), 20, mPaint);
        canvas.drawRect(250, (mHeight/2 + 50) - 5, mWidth - 100, (mHeight/2 + 50) + 5, mPaint);
        canvas.drawCircle(mWidth - 100, (mHeight/2 + 50), 20, mPaint);*/

        //color stolen from Twitter ;)
        mPaint.setColor(0xFF469BEB);
        canvas.drawCircle(100, (mHeight/2 - 50), 20, mPaint);
        canvas.drawRect(100, (mHeight/2 - 50) - 5, mWidth - 100, (mHeight/2 - 50) + 5, mPaint);
        canvas.drawCircle(mWidth - 100, (mHeight/2 - 50), 20, mPaint);

        canvas.drawCircle(250, (mHeight/2 + 50), 20, mPaint);
        canvas.drawRect(250, (mHeight/2 + 50) - 5, mWidth - 250, (mHeight/2 + 50) + 5, mPaint);
        canvas.drawCircle(mWidth - 250, (mHeight/2 + 50), 20, mPaint);

    }
}
