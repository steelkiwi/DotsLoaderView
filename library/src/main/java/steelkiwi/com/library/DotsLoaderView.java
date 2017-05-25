package steelkiwi.com.library;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yaroslav on 5/24/17.
 */

public class DotsLoaderView extends RelativeLayout {

    private static final int START_DELAY = 120;
    private static final int REPEAT_ANIMATION_DELAY = 1800;
    private static final int TRANSLATION_DELAY = 1200;
    private static final int SCALE_DELAY = 500;
    private static final int ITEM_SIZE = 3;

    private Paint backgroundPaint;
    private Path fullPath;
    private List<View> points = new ArrayList<>();
    private Handler handler;
    private int startDelay = 0;
    private boolean isLastView = false;
    private int viewWidth;
    private int viewHeight;
    private int arcWidth;
    private int arcHeight;
    private int bottomLineMargin;
    private int lineWidth;
    private boolean isStop;
    private int lineColor;
    private Drawable drawable;
    private boolean isAnimationFinish;


    public DotsLoaderView(Context context) {
        super(context);
        init(null);
    }

    public DotsLoaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public DotsLoaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        handler = new Handler();
        prepareParameters(attrs);
        prepareSize();
        initializePaints();
        initializePath();
        preparePath();
        setAnimationFinish(true);
        setStop(false);
    }

    private void prepareParameters(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.DotsLoaderView);
        setLineColor(typedArray.getInt(R.styleable.DotsLoaderView_dlv_line_color, 0));
        setDrawable(typedArray.getDrawable(R.styleable.DotsLoaderView_dlv_item_drawable));
        typedArray.recycle();
    }

    private void prepareSize() {
        setViewWidth(getResources().getDisplayMetrics().widthPixels);
        setViewHeight(getResources().getDisplayMetrics().heightPixels);
        setArcWidth(getResources().getDimensionPixelSize(R.dimen.arc_width));
        setArcHeight(getResources().getDimensionPixelSize(R.dimen.arc_height));
        setBottomLineMargin(getResources().getDimensionPixelSize(R.dimen.bottom_line_margin));
        setLineWidth(getResources().getDimensionPixelSize(R.dimen.line_width));
    }

    private void initializePaints() {
        setBackgroundResource(android.R.color.transparent);
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(getLineColor());
        backgroundPaint.setStyle(Paint.Style.STROKE);
    }

    private void initializePath() {
        fullPath = new Path();
    }

    private void preparePath() {
        int centerX = getViewWidth() / 2;
        int centerY = getViewHeight() / 2;
        // prepare start x and y
        int startX = centerX - getArcWidth();
        int startY = centerY - getArcHeight();
        // prepare path for animation
        fullPath.moveTo(startX, startY);
        fullPath.quadTo(centerX - getArcHeight(), startY, centerX, centerY);
        fullPath.moveTo(centerX, centerY);
        fullPath.quadTo(centerX + getArcHeight(), startY, centerX + getArcWidth(), startY);
    }

    private void preparePointViews() {
        for(int i = 0; i < ITEM_SIZE; i++) {
            View view = inflateView();
            prepareViewScale(view, 0f);
            points.add(view);
            addView(view);
        }
    }

    private View inflateView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.circle_layout, this, false);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        preparePointViews();
    }

    public void show() {
        if(isAnimationFinish()) {
            setVisibility(VISIBLE);
            setStop(false);
            animateViews();
            setAnimationFinish(false);
        }
    }

    public void hide() {
        setStop(true);
        setAnimationFinish(true);
        handler.removeCallbacks(repeatRunnable);
    }

    public void animateViews() {
        if(!isStop()) {
            setStartDelay(0);
            for (int i = 0; i < points.size(); i++) {
                View view = getView(i);
                prepareViewScale(view, 0f);
                translateView(view, getStartDelay());
                scaleViewX(view, getStartDelay());
                scaleViewY(view, getStartDelay());
                startDelay += START_DELAY;
                manageLastView(i);
            }
            repeatAnimation();
        }
    }

    private void repeatAnimation() {
        handler.postDelayed(repeatRunnable, REPEAT_ANIMATION_DELAY);
    }

    private Runnable repeatRunnable = new Runnable() {
        @Override
        public void run() {
            animateViews();
        }
    };

    private void prepareViewScale(View view, float scale) {
        view.setBackground(getDrawable());
        view.setScaleX(scale);
        view.setScaleY(scale);
    }

    private void translateView(View view, int startDelay) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "x", "y", fullPath).setDuration(TRANSLATION_DELAY);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setStartDelay(startDelay);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if(isLastView() && isStop()) {
                    setVisibility(GONE);
                }
            }
        });
        animator.start();
    }

    private void scaleViewX(final View view, final int startDelay) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "scaleX", 1f);
        animator.setDuration(SCALE_DELAY);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setStartDelay(startDelay);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                ObjectAnimator animator = ObjectAnimator.ofFloat(view, "scaleX", 0f);
                animator.setDuration(SCALE_DELAY);
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.setStartDelay(startDelay);
                animator.start();
            }
        });
        animator.start();
    }

    private void scaleViewY(final View view, final int startDelay) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "scaleY", 1f);
        animator.setDuration(SCALE_DELAY);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setStartDelay(startDelay);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                ObjectAnimator animator = ObjectAnimator.ofFloat(view, "scaleY", 0f);
                animator.setDuration(SCALE_DELAY);
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.setStartDelay(startDelay);
                animator.start();
            }
        });
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBottomLine(canvas);
    }

    private void drawBottomLine(Canvas canvas) {
        for (int i = 0; i < points.size(); i++) {
            View view = getView(i);
            float width = getLineWidth() * view.getScaleX();
            float centerX = view.getX() + (view.getWidth() / 2);
            float startX = centerX - (width / 2);
            float startY = getViewHeight() / 2 + getBottomLineMargin();
            float endX = centerX + (width / 2);
            float endY = getViewHeight() / 2 + getBottomLineMargin();
            canvas.drawLine(startX, startY, endX, endY, backgroundPaint);
        }
        postInvalidateDelayed(10); // set time here

    }

    private void manageLastView(int position) {
        if(position == points.size() - 1) {
            setLastView(true);
        }
    }

    private View getView(final int position) {
        return points.get(position);
    }

    private void setStartDelay(int startDelay) {
        this.startDelay = startDelay;
    }

    private int getStartDelay() {
        return startDelay;
    }

    private boolean isLastView() {
        return isLastView;
    }

    private void setLastView(boolean lastView) {
        isLastView = lastView;
    }

    private int getViewWidth() {
        return viewWidth;
    }

    private void setViewWidth(int viewWidth) {
        this.viewWidth = viewWidth;
    }

    private int getViewHeight() {
        return viewHeight;
    }

    private void setViewHeight(int viewHeight) {
        this.viewHeight = viewHeight;
    }

    private int getArcWidth() {
        return arcWidth;
    }

    private void setArcWidth(int arcWidth) {
        this.arcWidth = arcWidth;
    }

    private int getArcHeight() {
        return arcHeight;
    }

    private void setArcHeight(int arcHeight) {
        this.arcHeight = arcHeight;
    }

    private int getBottomLineMargin() {
        return bottomLineMargin;
    }

    private void setBottomLineMargin(int bottomLineMargin) {
        this.bottomLineMargin = bottomLineMargin;
    }

    private int getLineWidth() {
        return lineWidth;
    }

    private void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    private boolean isStop() {
        return isStop;
    }

    private void setStop(boolean stop) {
        isStop = stop;
    }

    private int getLineColor() {
        return lineColor;
    }

    private void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    private Drawable getDrawable() {
        return drawable;
    }

    private void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    private boolean isAnimationFinish() {
        return isAnimationFinish;
    }

    private void setAnimationFinish(boolean animationFinish) {
        isAnimationFinish = animationFinish;
    }
}
