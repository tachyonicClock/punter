package nz.ac.waikato.assignmentseven;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import androidx.annotation.Nullable;
import android.util.AttributeSet;

public class Game extends View {
    Paint paint;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawText("AMAZING GAME HERE", canvas.getWidth()/2 - 200, canvas.getHeight()/2, paint);
    }

    private void init(){
        paint = new Paint(getResources().getColor(R.color.colorPrimary, getContext().getTheme()));
        paint.setTextSize(40);
    }

    public Game(Context context) {
        super(context);
        init();
    }

    public Game(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Game(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public Game(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

}
