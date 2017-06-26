package com.meetvr.share.view;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

/**
 * Created by wzm-pc on 2016/8/22.
 */
public class FixedEditText  extends EditText {
    private String fixedText;
    private View.OnClickListener mListener;

    public FixedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public void setFixedText(String text) {
        fixedText = text;
        int left = (int) getPaint().measureText(fixedText)+ getPaddingLeft();
        setPadding(left, getPaddingTop(), getPaddingBottom(), getPaddingRight());
        invalidate();
    }

    public void setDrawableClk(View.OnClickListener listener) {
        mListener = listener;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        fixedText = getText().toString();


       // canvas.drawText("xxx",Float.valueOf(getPaddingStart()),10,new Paint());
        super.onDraw(canvas);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (mListener != null && getCompoundDrawables()[2] != null) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    int i = getMeasuredWidth() - getCompoundDrawables()[2].getIntrinsicWidth();
                    if (event.getX() > i) {
                        mListener.onClick(this);
                        return true;
                    }
                    break;
                case MotionEvent.ACTION_UP:

                    break;
                default:
                    break;
            }

        }

        return super.onTouchEvent(event);
    }

}
