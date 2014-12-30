package shoppinglist.kizema.anton.shoppinglist.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import shoppinglist.kizema.anton.shoppinglist.App;
import shoppinglist.kizema.anton.shoppinglist.color.FillBackgroundPolocy;
import shoppinglist.kizema.anton.shoppinglist.util.Vector2f;


public class MovableView implements OnSwipeTouchListener.OnHorizontalMoveViewListener {

    private static final float FULL_ALPHA = 1f;
    private static final float PERCENT = 0.01f;

    private View viewHolder;

    private OnSwipeTouchListener swipeGestureListener;
    private OnScreenStateChangeListener stateListener;

    private FillBackgroundPolocy fillBackgroundPolocy;

    private Vector2f downCoords;

    boolean dispatchTouchToSwipeDetector = false;

    private int mShortAnimationDuration;

    private enum Direction{
        LEFT, RIGHT,
    }

    public interface OnScreenStateChangeListener{
        void onRemove(View v);
    }

    public MovableView(View view) {
        viewHolder = view;
        init();
    }

    private void init(){
        mShortAnimationDuration = viewHolder.getContext().getResources().getInteger(
                android.R.integer.config_shortAnimTime);

        initTouchListener();
        initSwipeGestureListener();
        initLongClickListener();
    }

    private void initTouchListener(){
        viewHolder.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean res = false;

                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    Log.d("TAG", "ACTION_DOWN");

                    downCoords = new Vector2f(event.getX(), event.getY());
                }

                if (dispatchTouchToSwipeDetector) {
                    res = swipeGestureListener.onTouch(v, event);
                }

                if (MotionEvent.ACTION_UP == event.getAction() || MotionEvent.ACTION_CANCEL == event.getAction()) {
                    Log.d("TAG", "ACTION_UP");
                    if (dispatchTouchToSwipeDetector) {
                        dispatchTouchToSwipeDetector = false;
                    }
                }
                return res;
            }
        });
    }

    private void initSwipeGestureListener(){
        swipeGestureListener = new OnSwipeTouchListener(viewHolder.getContext());
        swipeGestureListener.setOnMoveViewListener(MovableView.this);
        swipeGestureListener.setOnSwipeListener(new OnSwipeTouchListener.OnSwipeListener() {
            @Override
            public void onSwipeRight() {
                Log.d("TAG", "onSwipeRight");
                runDisappearAnim(Direction.RIGHT);
            }

            @Override
            public void onSwipeLeft() {
                Log.d("TAG", "onSwipeLeft");
                runDisappearAnim(Direction.LEFT);
            }
        });
    }

    private void initLongClickListener(){
        viewHolder.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (getFillBackgroundPolocy() != null){
                    getFillBackgroundPolocy().setHooveredBackground(viewHolder);
                }
                dispatchTouchToSwipeDetector = true;

                long downTime = SystemClock.uptimeMillis();
                long eventTime = SystemClock.uptimeMillis() + 100;
                MotionEvent motionEvent = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, downCoords.getX(), downCoords.getY(), 0);

                swipeGestureListener.onTouch(viewHolder, motionEvent);
                motionEvent.recycle();
                return false;
            }
        });
    }

    private void runDisappearAnim(Direction dir){
        if (dir == Direction.RIGHT){
            viewHolder.animate()
                    .x(App.getW())
                    .setDuration(mShortAnimationDuration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            getOnScreenStateChangeListener().onRemove(viewHolder);
                        }
                    });
        } else{
            viewHolder.animate()
                    .x(-App.getW())
                    .setDuration(mShortAnimationDuration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            getOnScreenStateChangeListener().onRemove(viewHolder);
                        }
                    });
        }
    }

    @Override
    public void onMove(float dx, int sign) {
        Log.d("TAG", "onMove : " + dx);

        if (Math.abs(dx) > getRequredMinShift()) {
            if (dx > 0) {
                viewHolder.setX( viewHolder.getX() + getRequredMinShift());
            } else{
                viewHolder.setX( viewHolder.getX() - getRequredMinShift());
            }

            viewHolder.setAlpha(viewHolder.getAlpha()*(1 - sign *10 * PERCENT));
        }
    }

    @Override
    public void onCancelMove() {
        Log.i("TAG", "onCancelMove : ");
        viewHolder.clearFocus();

        viewHolder.animate()
                .x(0f)
                .alpha(FULL_ALPHA)
                .setDuration(mShortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (getFillBackgroundPolocy() != null){
                            getFillBackgroundPolocy().setNormalBackground(viewHolder);
                        }
                    }
                });
    }

    private float getRequredMinShift(){
        return App.getW() * PERCENT;
    }

    public void setOnScreenStateChangeListener(OnScreenStateChangeListener stateListener){
        this.stateListener = stateListener;
    }

    public OnScreenStateChangeListener getOnScreenStateChangeListener(){
        return stateListener;
    }

    public void setFillBackgroundPolocy(FillBackgroundPolocy fillBackgroundPolocy){
        this.fillBackgroundPolocy = fillBackgroundPolocy;
    }

    public FillBackgroundPolocy getFillBackgroundPolocy(){
        return fillBackgroundPolocy;
    }

}
