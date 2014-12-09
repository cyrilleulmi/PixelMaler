package ch.appquest.pixelmaler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Die DrawingView ist für die Darstellung und Verwaltung der Zeichenfläche
 * zuständig.
 */
public class DrawingView extends View {

	private static final int GRID_SIZE = 11;
	
	private Path drawPath = new Path();
	private Paint drawPaint = new Paint();
	private Paint linePaint = new Paint();
	private Paint fillPaint = new Paint();
	private boolean isErasing = false;
    private List<TouchedGridPoint> touchedPoints;
    private Paint[][] paintMap = new Paint[11][11];

	public DrawingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.drawPaint.setAntiAlias(true);
		this.drawPaint.setStrokeWidth(20);
		this.drawPaint.setStyle(Paint.Style.STROKE);
		this.drawPaint.setStrokeJoin(Paint.Join.ROUND);
		this.drawPaint.setStrokeCap(Paint.Cap.ROUND);

		this.linePaint.setColor(0xFF666666);
		this.linePaint.setAntiAlias(true);
		this.linePaint.setStrokeWidth(1.0f);
		this.linePaint.setStyle(Paint.Style.STROKE);

        this.fillPaint.setColor(0xFF666666);
        this.fillPaint.setStrokeWidth(0);
        this.fillPaint.setStyle(Paint.Style.FILL);

        this.touchedPoints = new ArrayList<TouchedGridPoint>();
	}

    public Paint[][] getPaintMap(){
        return paintMap;
    }

	@Override
	protected void onDraw(Canvas canvas) {

		final int maxX = canvas.getWidth();
		final int maxY = canvas.getHeight();

		final int stepSizeX = (int) Math.ceil((double) maxX / GRID_SIZE);
		final int stepSizeY = (int) Math.ceil((double) maxY / GRID_SIZE);

        this.drawSquares(canvas);
		this.drawGrid(canvas);

		// Zeichnet einen Pfad der dem Finger folgt
		canvas.drawPath(drawPath, drawPaint);
	}

    private void drawSquares(Canvas canvas) {
        for(int x = 0; x < 11; x++){
            for (int y = 0; y < 11; y++){
                if (this.paintMap[x][y] != null){
                    Rect rectToDraw = this.getRectFromCoordinates(x, y);
                    canvas.drawRect(rectToDraw, paintMap[x][y]);
                }
            }
        }
    }

    private Rect getRectFromCoordinates(int x, int y) {
        Rect rect = new Rect();

        rect.left = (this.getWidth() / 11) * x;
        rect.right = (this.getWidth() / 11) * (x + 1);

        rect.top = (this.getHeight() / 11) * y;
        rect.bottom = (this.getHeight() / 11) * (y + 1);


        return rect;
    }

    @SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float touchX = event.getX();
		float touchY = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			drawPath.moveTo(touchX, touchY);
            this.touchedPoints.add(new TouchedGridPoint(touchX, touchY));

			break;
		case MotionEvent.ACTION_MOVE:
			drawPath.lineTo(touchX, touchY);
            this.touchedPoints.add(new TouchedGridPoint(touchX, touchY));

			break;
		case MotionEvent.ACTION_UP:
            this.DrawSquares();
            this.touchedPoints.clear();
			drawPath.reset();
			break;
		default:
			return false;
		}

		invalidate();
		return true;
	}

    private void DrawSquares() {
        for (TouchedGridPoint touchedPoint : this.touchedPoints){
            HandleTouchedGridPoint(touchedPoint);
        }
    }

    private void HandleTouchedGridPoint(TouchedGridPoint touchedPoint) {
        int xSquareCoordinate = (int)(touchedPoint.getPointX() / (this.getWidth() / 11));
        int ySquareCoordinate = (int)(touchedPoint.getPointY() / (this.getHeight() / 11));

        try {
            if (this.isErasing){
                this.EraseColorForPoint(xSquareCoordinate, ySquareCoordinate);
            }
            else{
                this.SaveColorForPoint(xSquareCoordinate, ySquareCoordinate);
            }
        }
        catch (Exception e){
        }
    }

    private void EraseColorForPoint(int xSquareCoordinate, int ySquareCoordinate) {
        this.paintMap[xSquareCoordinate][ySquareCoordinate] = null;
    }

    private void SaveColorForPoint(int xSquareCoordinate, int ySquareCoordinate) {
        Paint paint = new Paint();
        paint.setColor(this.fillPaint.getColor());
        paint.setStrokeWidth(this.fillPaint.getStrokeWidth());
        paint.setStyle(this.fillPaint.getStyle());
        this.paintMap[xSquareCoordinate][ySquareCoordinate] = paint;
    }

    public void startNew() {

		// TODO Gitter löschen
        this.paintMap = new Paint[11][11];
		invalidate();
	}

	public void setErase(boolean isErase) {
		isErasing = isErase;
		if (isErasing) {
			drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		} else {
			drawPaint.setXfermode(null);
		}
	}

	public boolean isErasing() {
		return isErasing;
	}

	public void setColor(String color) {
		invalidate();
		drawPaint.setColor(Color.parseColor(color));
        fillPaint.setColor(Color.parseColor(color));
	}

    private void drawGrid(Canvas canvas) {
        for (int i = 0; i < 11; i++){
           this.drawHorizontalLineAt(canvas ,i);
           this.drawVerticalLineAt(canvas ,i);
        }
    }

    private void drawVerticalLineAt(Canvas canvas, int position) {
        float xPosition = this.getHeight() / 11 * position;
        canvas.drawLine(xPosition, 0,xPosition, this.getHeight(), this.linePaint);
    }

    private void drawHorizontalLineAt(Canvas canvas, int position) {
        float yPosition = this.getWidth() / 11 * position;
        canvas.drawLine(0, yPosition, this.getWidth(), yPosition, this.linePaint);
    }
}
