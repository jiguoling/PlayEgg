package com.zony.playeggs;

import com.nineoldandroids.animation.TimeAnimator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.Random;

public class Board extends FrameLayout {
    // 控制数量
    public static final int NUM_CATS = 30;

    static Random sRNG = new Random();
    
    private AnimatorEndListener endListener;

    public void setEndListener(AnimatorEndListener endListener) {
        this.endListener = endListener;
    }

    /**
     * 取a-b之间随机数
     * @param a
     * @param b
     * @param f
     * @return
     */
    static float lerp(float a, float b, float f) {
        return (b - a) * f + a;
    }

    static float randfrange(float a, float b) {
        return lerp(a, b, sRNG.nextFloat());
    }

    public class FlyingCat extends ImageView {
        /**
         * 最大速度
         */
        public static final float VMAX = 300.0f;

        /**
         * 最小速度
         */
        public static final float VMIN = 100.0f;

        /**
         * 速度
         */
        public float v;

        /**
         * 计算速度的系数,根据addView时的位置进行初始化
         */
        public float z;

        private Board board;
        
        public FlyingCat(Context context, AttributeSet as,Board board) {
            super(context, as);
            setImageResource(R.drawable.ic_launcher);
            this.board = board;
        }

        public void reset() {
            final float scale = 1;
//            final float scale = lerp(0.5f, 1.5f, z);
//            setScaleX(scale);
//            setScaleY(scale);
            measure(0, 0);
            float x = randfrange(0, Board.this.getWidth() - scale * getMeasuredWidth());
            float y = randfrange(0, Board.this.getHeight() - scale * getMeasuredHeight());
            setX(x);
            setY(y);
            v = lerp(VMIN, VMAX, z);
        }

        /**
         * 更新Y轴位置
         * @param dt 其单位可以理解为时间块
         */
        public void update(float dt) {
            // 根据Y轴漂移
            float y = getY() + v * dt;
            if (y > board.getHeight()) {
                board.removeView(this);
            }else {
                setY(y);
            }
        }
    }

    TimeAnimator mAnim;

    Context mContext;

    public Board(Context context, AttributeSet as) {
        super(context, as);
        this.mContext = context;
    }

    private void reset() {
        removeAllViews();
        final ViewGroup.LayoutParams wrap = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < NUM_CATS; i++) {
            FlyingCat nv = new FlyingCat(getContext(), null,this);
            addView(nv, wrap);
            nv.z = ((float) i / NUM_CATS);
            nv.z *= nv.z;
            nv.reset();
        }
        if (mAnim != null) {
            mAnim.cancel();
        }
        mAnim = new TimeAnimator();
        mAnim.setTimeListener(new TimeAnimator.TimeListener() {
            public void onTimeUpdate(TimeAnimator animation, long totalTime, long deltaTime) {
                int childCount = getChildCount();
                if (childCount > 0) {
                    for (int i = 0; i < childCount; i++) {
                        View v = getChildAt(i);
                        if (!(v instanceof FlyingCat))
                            continue;
                        FlyingCat nv = (FlyingCat) v;
                        nv.update(deltaTime / 200f);//deltaTime为两帧之间的时间查,单位ms,这里除以200主要是为了修正下载速度,可以理解为200ms为一个时间块
                    }
                }else {
                    endListener.onAnimatorEnd();
                }
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        post(new Runnable() {
            public void run() {
                reset();
                mAnim.start();
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mAnim.cancel();
        endListener.onAnimatorEnd();
    }

    @Override
    public boolean isOpaque() {
        return true;
    }
}