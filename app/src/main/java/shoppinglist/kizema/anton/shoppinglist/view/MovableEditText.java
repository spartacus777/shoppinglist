package shoppinglist.kizema.anton.shoppinglist.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Color;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import shoppinglist.kizema.anton.shoppinglist.App;


public class MovableEditText extends EditText implements OnSwipeTouchListener.OnHorizontalMoveViewListener {

    private OnSwipeTouchListener touchListener;
    private boolean onLongClickIndicator = false;

    private float downX;
    private float downY;

    private float lastMoveX = 0;

    private int mShortAnimationDuration;
    private OnScreenStateChangeListener stateListener;

    private enum Direction{
        LEFT, RIGHT,
    }

    public interface OnScreenStateChangeListener{
        void onRemove(View v);
    }

    public MovableEditText(Context context) {
        super(context);
        init();
    }

    public MovableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MovableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mShortAnimationDuration = getContext().getResources().getInteger(
                android.R.integer.config_shortAnimTime);


        touchListener = new OnSwipeTouchListener(getContext());
        touchListener.setOnMoveViewListener(MovableEditText.this);
        touchListener.setOnSwipeListener(new OnSwipeTouchListener.OnSwipeListener() {
            @Override
            public void onSwipeRight() {
                Log.d("TAG", "onSwipeRight");
                disappearAnim(Direction.RIGHT);
            }

            @Override
            public void onSwipeLeft() {
                Log.d("TAG", "onSwipeLeft");
                disappearAnim(Direction.LEFT);
            }
        });

        setOnLongClickListener( new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setBackgroundColor(Color.RED);
                onLongClickIndicator = true;
                setOnTouchListener(touchListener);

                long downTime = SystemClock.uptimeMillis();
                long eventTime = SystemClock.uptimeMillis() + 100;
                MotionEvent mo = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, downX, downY, 0);

                touchListener.onTouch(MovableEditText.this, mo);
                mo.recycle();
                return false;
            }
        });
    }

    private void animateDisappearRight(){

    }

    private void disappearAnim(Direction dir){
        if (dir == Direction.RIGHT){
            animate()
                    .x(App.getW())
                    .setDuration(mShortAnimationDuration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            getOnScreenStateChangeListener().onRemove(MovableEditText.this);
                        }
                    });
        } else{
            animate()
                    .x(-App.getW())
                    .setDuration(mShortAnimationDuration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            getOnScreenStateChangeListener().onRemove(MovableEditText.this);
                        }
                    });
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        boolean res = super.dispatchTouchEvent(event);

        if ( MotionEvent.ACTION_DOWN == event.getAction()){
            Log.d("TAG", "ACTION_DOWN");

            downX = event.getX();
            downY = event.getY();
        }

        if ( MotionEvent.ACTION_UP == event.getAction() || MotionEvent.ACTION_CANCEL == event.getAction()){
            Log.d("TAG", "ACTION_UP");
            if (onLongClickIndicator){
                MovableEditText.this.setOnTouchListener(null);
                onLongClickIndicator = false;
            }
        }


        return res;
    }

    @Override
    public void onMove(float dx) {
        Log.d("TAG", "onMove : " + dx);

        if (Math.abs(dx - lastMoveX) > getRequredMinShift()) {
            if (dx - lastMoveX > 0) {
                setX(lastMoveX + getRequredMinShift());
                lastMoveX = lastMoveX + getRequredMinShift();
            } else{
                setX(lastMoveX - getRequredMinShift());
                lastMoveX = lastMoveX - getRequredMinShift();
            }
        }
    }

    @Override
    public void onCancelMove() {
        Log.i("TAG", "onCancelMove : ");
        lastMoveX = 0;
        clearFocus();

        animate()
                .x(0f)
                .setDuration(mShortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        setBackgroundColor(Color.TRANSPARENT);
                    }
                });
    }

    private float getRequredMinShift(){
        return App.getW()/100;
    }

    public void setOnScreenStateChangeListener(OnScreenStateChangeListener stateListener){
        this.stateListener = stateListener;
    }

    public OnScreenStateChangeListener getOnScreenStateChangeListener(){
        return stateListener;
    }

}
