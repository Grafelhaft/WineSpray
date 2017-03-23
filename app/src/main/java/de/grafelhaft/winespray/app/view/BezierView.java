package de.grafelhaft.winespray.app.view;

/**
 * Created by @author Markus Graf (Grafelhaft) on 26.10.2016.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import de.grafelhaft.winespray.app.R;

@SuppressLint("ViewConstructor")
public class BezierView extends RelativeLayout {

    private Paint paint;

    private Path path;

    private int bezierWidth, bezierHeight;

    private int backgroundColor;

    private Context context;


    public BezierView(Context context, AttributeSet attrs, int defStyleAttr, int backgroundColor) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        /*this.backgroundColor = backgroundColor;*/
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        path = new Path();
        paint.setStrokeWidth(0);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        build((int) getResources().getDimension(R.dimen.centre_content_width),
                (int) getResources().getDimension(R.dimen.space_navigation_height) - (int) getResources().getDimension(R.dimen.main_content_height));
        changeBackgroundColor(R.color.default_color);
    }

    public BezierView(Context context) {
        this(context, null);
    }

    public BezierView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BezierView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, R.color.default_color);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setBackgroundColor(ContextCompat.getColor(context, R.color.transparent));
    }

    @Override
    protected void onDraw(Canvas canvas) {

        /**
         * Set paint color to fill view
         */
        paint.setColor(backgroundColor);

        /**
         * Reset path before drawing
         */
        path.reset();

        /**
         * Start point for drawing
         */
        path.moveTo(0, bezierHeight);

        /**
         * Seth half path of bezier view
         */
        path.cubicTo(bezierWidth / 4, bezierHeight, bezierWidth / 4, 0, bezierWidth / 2, 0);

        /**
         * Seth second part of bezier view
         */
        path.cubicTo((bezierWidth / 4) * 3, 0, (bezierWidth / 4) * 3, bezierHeight, bezierWidth, bezierHeight);

        /**
         * Draw our bezier view
         */
        canvas.drawPath(path, paint);
    }

    /**
     * Build bezier view with given width and height
     *
     * @param bezierWidth  Given width
     * @param bezierHeight Given height
     */
    void build(int bezierWidth, int bezierHeight) {
        this.bezierWidth = bezierWidth;
        this.bezierHeight = bezierHeight;
    }

    /**
     * Change bezier view background color
     *
     * @param backgroundColor Target color
     */
    void changeBackgroundColor(int backgroundColor) {
        this.backgroundColor = ContextCompat.getColor(getContext(), backgroundColor);
        invalidate();
    }
}
