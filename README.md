# barrage
## 一个简易的弹幕demo，支持开关，支持左右移动切换

* 自定义XCDanmuView继承relativelayout，每产生一次弹幕就创建一个textview添加到XCDanmuView中，并添加属性动画，当动画结束时remove该textview<br>
```
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
```
#### 效果如图：<brl>
![](https://github.com/Mrgl1203/barrage/blob/master/barrage-gif2.gif)
    
