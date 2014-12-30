package shoppinglist.kizema.anton.shoppinglist.view;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import shoppinglist.kizema.anton.shoppinglist.App;

public class OnSwipeTouchListener implements View.OnTouchListener {

    private static final int SWIPE_THRESHOLD = (int) (0.1f ) * App.getW();

    private float previousMoveX = 0;
    private float onDownX = 0;

    private final GestureDetector gestureDetector;
    private OnSwipeListener onSwipeListener;
    private OnHorizontalMoveViewListener onMoveViewListener;

    public interface OnSwipeListener{
        public void onSwipeRight();
        public void onSwipeLeft();
    }

    public interface OnHorizontalMoveViewListener {
        void onMove(float dx, int sign);
        void onCancelMove();
    }

    public OnSwipeTouchListener (Context ctx){
        gestureDetector = new GestureDetector(ctx, new GestureListener());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        boolean res = gestureDetector.onTouchEvent(event);
        Log.d ("TAG", "onTouch: gestureDetector.onTouchEvent(event) RES = "+res + " ; event = "+event.toString());


        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            previousMoveX = event.getX();
            onDownX = event.getX();
        }

        if (res == false && event.getAction() == MotionEvent.ACTION_MOVE){
            if (getOnMoveViewListener() != null){
                getOnMoveViewListener().onMove(event.getX() - previousMoveX, ( Math.abs(event.getX() - onDownX) > Math.abs(previousMoveX - onDownX)) ? 1 : -1 );
                previousMoveX = event.getX();
            }
        }

        if (res == false && event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL){
            if (getOnMoveViewListener() != null){
                getOnMoveViewListener().onCancelMove();
            }
        }

        return res;
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_VELOCITY_THRESHOLD = 1000;

        @Override
        public boolean onDown(MotionEvent e) {
            Log.d("TAG", "GestureListener :: onDown");
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            if (getOnSwipeListener() != null) {
                                getOnSwipeListener().onSwipeRight();
                            }
                        } else {
                            if (getOnSwipeListener() != null) {
                                getOnSwipeListener().onSwipeLeft();
                            }
                        }
                        result = true;
                    }
                }

            } catch (Exception exception) {
                exception.printStackTrace();
            }

            Log.i("TAG", "onFling RESULT = "+ result);
            return result;
        }
    }

    public void setOnSwipeListener(OnSwipeListener onSwipeListener){
        this.onSwipeListener = onSwipeListener;
    }

    public OnSwipeListener getOnSwipeListener(){
        return onSwipeListener;
    }


    public void setOnMoveViewListener(OnHorizontalMoveViewListener onMoveViewListener){
        this.onMoveViewListener = onMoveViewListener;
    }

    public OnHorizontalMoveViewListener getOnMoveViewListener(){
        return onMoveViewListener;
    }

}