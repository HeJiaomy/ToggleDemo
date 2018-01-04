package com.example.toggledemo.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 自定义开关
 * Created by 12191 on 2018/1/3.
 * <p>
 * Android的界面绘制流程
 * 测量           摆放      绘制
 * measure  ->  layout  —>  draw
 * |            |          |
 * onMeasure -> onLayout -> onDraw  重写这些方法，实现自定义控件
 * <p>
 * View
 * onMeasure()（这个方法指定自己的宽高） -> onDraw（绘制自己的内容）
 * ViewGroup
 * onMeasure()（指定自己的宽高，所有子View的宽高） -> onLayout()（摆放所有子View） -> onDraw()（绘制内容）
 */

public class ToggleView extends View {

    Bitmap switchBackgroundBitmap;  //设置背景
    Bitmap slideButtonBitmap;   //设置滑块
    Paint paint;
    private boolean switchState = false; //设置开关默认false

    float currentX;

    OnSwitchStateUpdateListener onSwitchStateUpdateListener;

    /**
     * 用于代码创建控件
     *
     * @param context
     */
    public ToggleView(Context context) {
        super(context);
        init();
    }

    /**
     * 用于在xml里使用，可指定自定义属性
     *
     * @param context
     * @param attrs
     */
    public ToggleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        String nameSpace="http://schemas.android.com/apk/com.example.toggledemo";
        //获取配置的自定义属性
        if (attrs != null) {
            int switchBackgroundResource= attrs.getAttributeResourceValue(nameSpace,"switch_background",-1);
            int slideButtonResource= attrs.getAttributeResourceValue(nameSpace,"slide_button",-1);
            switchState= attrs.getAttributeBooleanValue(nameSpace,"switch_state",false);
            setSwitchBackgroundResource(switchBackgroundResource);
            setSlideButtonResource(slideButtonResource);
        }

    }

    /**
     * 用于在xml里使用，可指定自定义属性,如指定了style，则走此构造函数
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public ToggleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        paint = new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(switchBackgroundBitmap.getWidth(), switchBackgroundBitmap.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制背景
        canvas.drawBitmap(switchBackgroundBitmap, 0, 0, paint);
        //绘制滑块

        //根据用户触摸到的当前位置画滑块
        if (isTouchMode) {
            //让滑块移动自身宽度一半的位置
            float newLeft = currentX - slideButtonBitmap.getWidth() / 2.0f;
            int maxLeft = switchBackgroundBitmap.getWidth() - slideButtonBitmap.getWidth();
            //限定滑动范围
            if (newLeft < 0) {
                newLeft = 0;
            } else if (newLeft > maxLeft) {
                newLeft = maxLeft;
            }
            canvas.drawBitmap(slideButtonBitmap, newLeft, 0, paint);

        } else {
            //根据开关状态设置滑块位置
            if (switchState) {
                int left = switchBackgroundBitmap.getWidth() - slideButtonBitmap.getWidth();
                canvas.drawBitmap(slideButtonBitmap, left, 0, paint);
            } else {
                canvas.drawBitmap(slideButtonBitmap, 0, 0, paint);
            }
        }
    }

    boolean isTouchMode = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                currentX = event.getX();
                isTouchMode = true;
                break;
            case MotionEvent.ACTION_MOVE:
                currentX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                currentX = event.getX();
                isTouchMode = false;
                float center = switchBackgroundBitmap.getWidth() / 2.0f;
                //根据当前按下的位置和控件中心的位置进行比较
                boolean state= currentX > center;
                //如果开关状态变化了，通知界面开关状态更新了
                if (state!= switchState && onSwitchStateUpdateListener!= null){
                    onSwitchStateUpdateListener.onStateUpdate(state);
                }
                switchState= state;
                break;
        }
        //重绘界面，onDraw()会重新执行
        invalidate();
        return true;
    }

    /**
     * 设置背景图
     *
     * @param switch_background
     */
    public void setSwitchBackgroundResource(int switch_background) {
        switchBackgroundBitmap = BitmapFactory.decodeResource(getResources(), switch_background);
    }

    /**
     * 设置滑动按钮
     *
     * @param slide_button
     */
    public void setSlideButtonResource(int slide_button) {
        slideButtonBitmap = BitmapFactory.decodeResource(getResources(), slide_button);
    }

    /**
     * 设置开关状态
     *
     * @param switchState
     */
    public void setSwitchState(boolean switchState) {
        this.switchState = switchState;
    }

    /**
     * 设置监听接口
     */
    public interface OnSwitchStateUpdateListener {

        //状态回调
        void onStateUpdate(boolean state);
    }

    public void setOnSwitchStateUpdateListener(OnSwitchStateUpdateListener onSwitchStateUpdateListener) {
        this.onSwitchStateUpdateListener = onSwitchStateUpdateListener;
    }
}
