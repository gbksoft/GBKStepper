package com.vfaraday.stepper;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StepView extends FrameLayout {

    private Context mContext;

    private int progressStepCount;
    private Drawable activeStepDrawable;
    private Drawable nonActiveStepDrawable;
    private Drawable progressDrawable;
    private AnimationDrawable animationDrawable;
    private int progress;
    private int step;
    private List<CharSequence> textProgress;
    private ProgressBar progressBar;

    private LinearLayout llStep;
    private LinearLayout llText;
    private LinearLayout.LayoutParams layoutParams;
    private LinearLayout.LayoutParams viewP;

    private float activeTextSize;
    private int activeTextColor;
    private float nonActiveTextSize;
    private int nonActiveTextColor;
    private LinearLayout.LayoutParams layoutParamsText;
    private Animation progressAnimation;

    private ImageView [] imageV;
    private TextView [] textV;

    private int width;

    public StepView(Context context) {
        super(context);
        mContext = context;
        init(null);
    }

    public StepView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
    }

    public StepView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {

        inflate(mContext, R.layout.step_item, this);

        textProgress = new ArrayList<>();

        llStep = findViewById(R.id.llStepContainer);
        llText = findViewById(R.id.llTextContainer);

        llStep.removeAllViews();
        llText.removeAllViews();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;

        if (attrs != null) {
            TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.StepView,
                    0, 0);

            CharSequence[] text = typedArray.getTextArray(R.styleable.StepView_stepTexts);
            if (text != null) {
                textProgress = Arrays.asList(text);
            }
            progressStepCount = typedArray.getInteger(R.styleable.StepView_progressStepCount, 2);
            activeStepDrawable = mContext.getResources().getDrawable(typedArray.getResourceId(R.styleable.StepView_activeStepDrawable, R.drawable.ic_step_active));
            nonActiveStepDrawable =  mContext.getResources().getDrawable(typedArray.getResourceId(R.styleable.StepView_nonActiveStepDrawable, R.drawable.ic_step_non_active));
            progressDrawable =  mContext.getResources().getDrawable(typedArray.getInteger(R.styleable.StepView_progressDrawable, R.drawable.progress_drawable));
            activeTextSize = typedArray.getDimension(R.styleable.StepView_activeTextSize, mContext.getResources().getDimension(R.dimen.textSize));
            nonActiveTextSize = typedArray.getDimension(R.styleable.StepView_nonActiveTextSize, mContext.getResources().getDimension(R.dimen.textSize));
            activeTextColor = typedArray.getColor(R.styleable.StepView_activeTextColor, mContext.getResources().getColor(R.color.colorText));
            nonActiveTextColor = typedArray.getColor(R.styleable.StepView_nonActiveTextColor, mContext.getResources().getColor(R.color.colorNonActive));
            typedArray.recycle();
        } else {
            progressStepCount = 2;
            activeStepDrawable =  mContext.getResources().getDrawable(R.drawable.ic_step_active);
            nonActiveStepDrawable =  mContext.getResources().getDrawable(R.drawable.ic_step_non_active);
            activeTextColor = mContext.getResources().getColor(R.color.colorText);
            nonActiveTextColor = mContext.getResources().getColor(R.color.colorNonActive);
            activeTextSize = mContext.getResources().getDimension(R.dimen.textSize);
            nonActiveTextSize = mContext.getResources().getDimension(R.dimen.textSize);
        }

        imageV = new ImageView[progressStepCount];
        textV = new TextView[progressStepCount];

        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(progressStepCount * 100 - 100);
//        if (progressDrawable != null) {
//            progressBar.setProgressDrawable(progressDrawable);
//        }
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;

        width = (width / (progressStepCount - 1)) - (activeStepDrawable.getMinimumWidth() / 2);

        layoutParamsText = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParamsText.gravity = Gravity.CENTER;

        viewP = new  LinearLayout.LayoutParams(0, 1);

        viewP.gravity = Gravity.CENTER_HORIZONTAL;
        viewP.weight = 1;

        setupSteps();
        setupText();
    }

    private void setupSteps() {
        llStep.removeAllViews();
        for (int i = 0; i < progressStepCount; i++) {
            AppCompatImageView imageView = new AppCompatImageView(mContext);
            imageView.setLayoutParams(layoutParams);
            if (i <= progress - 1) {
                imageView.setImageDrawable(activeStepDrawable);
            } else {
                imageView.setImageDrawable(nonActiveStepDrawable);
            }

            imageV[i] = imageView;

            llStep.addView(imageView);
            if (i != progressStepCount -1) {
                View view = new View(mContext);
                view.setLayoutParams(viewP);
                llStep.addView(view);
            }
        }
    }

    private void setupText() {
        llText.removeAllViews();
        for (int i = 0; i < progressStepCount; i++) {
            AppCompatTextView textView = new AppCompatTextView(mContext);
            textView.setLayoutParams(layoutParamsText);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, activeTextSize);
            textView.setTextColor(activeTextColor);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            if (i <= (progress / 100) - 1) {
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, activeTextSize);
                textView.setTextColor(activeTextColor);
            } else {
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, nonActiveTextSize);
                textView.setTextColor(nonActiveTextColor);
            }
            if (textProgress.size() > i) {
                if (!TextUtils.isEmpty(textProgress.get(i))) {
                    textView.setText(textProgress.get(i));
                } else {
                    textView.setText("");
                }
            }
            textV[i] = textView;
            llText.addView(textView);
            if (i != progressStepCount -1) {
                View view = new View(mContext);
                view.setLayoutParams(viewP);
                llText.addView(view);
            }
        }
    }

    public void setProgressStepCount(int progressStepCount) {
        if (progressStepCount < 2) {
            throw new IllegalArgumentException();
        } else {
            progressBar.setMax(progressStepCount - 1);
        }
    }

    public void setProgressDrawable(int progressDrawable) {
        this.progressDrawable =  mContext.getResources().getDrawable(progressDrawable);
        progressBar.setProgressDrawable(this.progressDrawable);

    }

    public void setProgressDrawable(Drawable progressDrawable) {
        this.progressDrawable = progressDrawable;
        progressBar.setProgressDrawable(progressDrawable);
    }

    public void setActiveStepDrawable(int activeStepDrawable) {
        this.activeStepDrawable =  mContext.getResources().getDrawable(activeStepDrawable);
        setupSteps();
    }

    public void setActiveStepDrawable(Drawable activeStepDrawable) {
        this.activeStepDrawable = activeStepDrawable;
        setupSteps();
    }

    public void setNonActiveStepDrawable(int nonActiveStepDrawable) {
        this.nonActiveStepDrawable =  mContext.getResources().getDrawable(nonActiveStepDrawable);
        setupSteps();
    }

    public void setNonActiveStepDrawable(Drawable nonActiveStepDrawable) {
        this.nonActiveStepDrawable = nonActiveStepDrawable;
        setupSteps();
    }

    public void setProgressText(List<String> args) {
        textProgress.clear();
        textProgress.addAll(args);
        setupText();
    }

    public void setProgress(int progress) throws IllegalArgumentException {
        if (progress < 0 && progress > progressStepCount) {
            throw new IllegalArgumentException();
        } else {
            ImageView imageView;
            TextView textView;
            ObjectAnimator an;
            for (int i = 1; i <= progressStepCount; i++) {
                imageView = imageV[i-1];
                textView = textV[i-1];
                if (i <= progress) {
                    textView.setTextColor(activeTextColor);
                    imageView.animate().alpha(1f).setDuration(2000);
                    imageView.setImageDrawable(activeStepDrawable);
                } else {
                    textView.setTextColor(nonActiveTextColor);
                    imageView.setImageDrawable(nonActiveStepDrawable);
                }
            }

//            if (progress <= 0) {
////                for (int i = progressStepCount; i > 0; i--) {
////                    imageView = imageV[i -1];
////                    textView = textV[i -1];
////                    textView.setTextColor(nonActiveTextColor);
//////                    imageView.setImageDrawable(nonActiveStepDrawable);
////                    an = ObjectAnimator.ofInt(imageView, "imageResource", R.drawable.ic_step_non_active, R.drawable.ic_step_non_active);
////                    an.setEvaluator(new ArgbEvaluator());
////                    an.setDuration(500);
////                    an.start();
////                }
////            } else if (progress + 1 != this.progress / 100 + 1 && progress -1 != this.progress / 100 -1) {
////
////            }
//            if (progress * 100 < this.progress) {
//                imageView = imageV[progress];
//                textView = textV[progress];
//                textView.setTextColor(nonActiveTextColor);
//                an = ObjectAnimator.ofInt(imageView, "imageResource", R.drawable.ic_step_active, R.drawable.ic_step_non_active);
//                an.setDuration(500);
//                an.start();
//            } else {
//                imageView = imageV[progress - 1];
//                textView = textV[progress - 1];
//                textView.setTextColor(activeTextColor);
//                an = ObjectAnimator.ofInt(imageView, "imageResource", R.drawable.ic_step_non_active, R.drawable.ic_step_active);
//                an.setDuration(1000);
//                an.start();
//            }
            this.progress = progress * 100;
            ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", this.progress - 100);
            animation.setDuration(500);
            animation.setInterpolator(new AccelerateDecelerateInterpolator());
            animation.start();

        }
    }

    public void setTextSize(int textSize) {
        this.activeTextSize = textSize;
        setupText();
    }

    public void setTextColor(int textColor) {
        this.activeTextColor = textColor;
        setupText();
    }

    public int getProgressStepCount() {
        return progressStepCount;
    }


    public float getNonActiveTextSize() {
        return nonActiveTextSize;
    }

    public void setNonActiveTextSize(float nonActiveTextSize) {
        this.nonActiveTextSize = nonActiveTextSize;
    }

    public int getNonActiveTextColor() {
        return nonActiveTextColor;
    }

    public void setNonActiveTextColor(int nonActiveTextColor) {
        this.nonActiveTextColor = nonActiveTextColor;
    }

    public Animation getProgressAnimation() {
        return progressAnimation;
    }

    public void setProgressAnimation(Animation progressAnimation) {
        this.progressAnimation = progressAnimation;
    }

    public void setImageAnimation(Animation activeImageAnimation) {
        for (int i = 0; i < progressStepCount; i++) {
            imageV[i].setAnimation(activeImageAnimation);
        }
    }
}
