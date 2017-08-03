package barrage.gl.com.barrage;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by gl152 on 2017/8/1.
 */

public class XCDanmuView extends RelativeLayout {


    private int mScreenWidth;
    private boolean mIsWorking = false;
    private Context mContext;
    private int mRowNum = 4;//行数
    private int mSpeeds = 5000;//平移速度
    private int mRowPos = 100;//行间距
    int count;//弹幕计数

    public static enum XCDirection {//控制弹幕移动方向
        FROM_RIGHT_TO_LEFT,
        FORM_LEFT_TO_RIGHT
    }

    public enum XCAction {
        SHOW, HIDE
    }

    private XCDirection mDirection = XCDirection.FROM_RIGHT_TO_LEFT;

    public void changeDirection() {
        if (mDirection == XCDirection.FROM_RIGHT_TO_LEFT) {
            mDirection = XCDirection.FORM_LEFT_TO_RIGHT;
        } else {
            mDirection = XCDirection.FROM_RIGHT_TO_LEFT;
        }
    }

    private void init(Context context) {
        mContext = context;
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
    }

    public XCDanmuView(Context context) {
        super(context);
        init(context);
    }

    public XCDanmuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public XCDanmuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setSpeed(int speed) {
        mSpeeds = speed;
    }

    public void setRowNum(int rowNum) {
        mRowNum = rowNum;
    }

    public float getTextWidth(TextView textView, String text) {
        //Rect表示一个矩形，由四条边的坐标组成
        Rect bounds = new Rect();
        TextPaint paint;
        paint = textView.getPaint();
        paint.getTextBounds(text, 0, text.length(), bounds);
        //System.out.println(item.textView.getText()+(bounds.width()+"")+"宽度");
        return bounds.width();
    }


    //创建弹幕item view 并addView到XCDanmuView中
    public void createDanmuView(String content, boolean isMine) {
        count++;
        final TextView textView = new TextView(mContext);
        textView.setTextColor(Color.BLACK);
        if (isMine) {//是我自己的弹幕
            textView.setBackgroundResource(R.drawable.minedanmu);
        }

        textView.setText(content);
        RelativeLayout.LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        int row = count % mRowNum;
        lp.topMargin = row * mRowPos;
        textView.setPadding(40, 2, 40, 2);
        float textwidth = getTextWidth(textView, content) + textView.getPaddingLeft() + textView.getPaddingRight();
        if (mDirection == XCDirection.FROM_RIGHT_TO_LEFT) {
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            lp.rightMargin = (int) -textwidth;
        } else {
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            lp.leftMargin = (int) -textwidth;
        }
        textView.setLayoutParams(lp);

        this.addView(textView, this.getChildCount());


        textView.setClickable(true);
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(mContext, textView.getText(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 50);
                toast.show();
            }
        });

        ViewPropertyAnimator animator;


        if (mDirection == XCDirection.FROM_RIGHT_TO_LEFT) {
            animator = textView.animate()
                    .translationXBy(-(mScreenWidth + textwidth));

        } else {
            animator = textView.animate()
                    .translationXBy(mScreenWidth + textwidth);
        }

        animator.setDuration(mSpeeds);
        animator.setInterpolator(new LinearInterpolator());
        animator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                textView.clearAnimation();
                XCDanmuView.this.removeView(textView);

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator.start();
    }

    public void start() {
        switchAnimation(XCAction.SHOW);
        mIsWorking = true;
    }

    public boolean isWorking() {
        return mIsWorking;
    }

    public void hide() {
        switchAnimation(XCAction.HIDE);
        mIsWorking = false;
    }

    public void stop() {
        this.setVisibility(View.GONE);
        mIsWorking = false;
    }

    private void switchAnimation(final XCAction action) {
        AlphaAnimation animation;
        if (action == XCAction.HIDE) {
            animation = new AlphaAnimation(1.0f, 0.0f);
            animation.setDuration(400);
        } else {
            animation = new AlphaAnimation(0.0f, 1.0f);
            animation.setDuration(1000);
        }
        XCDanmuView.this.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (action == XCAction.HIDE) {
                    XCDanmuView.this.setVisibility(View.GONE);
                } else {
                    XCDanmuView.this.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


}
