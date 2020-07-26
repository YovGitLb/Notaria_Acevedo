package com.example.notary_acevedo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import com.example.notary_acevedo.OnMenuSelectedListener;
import com.example.notary_acevedo.OnMenuStatusChangeListener;

import java.util.ArrayList;
import java.util.List;

import androidx.core.graphics.ColorUtils;

/**
 * Se extrajo esta clase de hitomis con algunas modificciones que se tuvo que realizar para la apariencia
 * <p>
 * github : https://github.com/Hitomis  https://github.com/Hitomis/CircleMenu/blob/master/build.gradle <br/>
 * <p>

 */
public class CircleMenu extends View {

    private static final int STATUS_MENU_OPEN = 1;

    private static final int STATUS_MENU_OPENED = 1 << 1;

    private static final int STATUS_MENU_CLOSE = 1 << 2;

    private static final int STATUS_MENU_CLOSE_CLEAR = 1 << 3;

    private static final int STATUS_MENU_CLOSED = 1 << 4;

    private static final int STATUS_MENU_CANCEL = 1 << 5;

    private static final int MAX_SUBMENU_NUM = 8;

    private final int shadowRadius = 5;

    private int partSize;

    private int iconSize;

    private float circleMenuRadius;

    private int itemNum;

    private float itemMenuRadius;

    private float fraction, rFraction;

    private float pathLength;

    private int mainMenuColor;

    private Drawable openMenuIcon, closeMenuIcon;

    private List<Integer> subMenuColorList;

    private List<Drawable> subMenuDrawableList;

    private List<RectF> menuRectFList;

    private int centerX, centerY;

    private int clickIndex;

    private int rotateAngle;

    private int itemIconSize;

    private int pressedColor;

    private int status;

    private boolean pressed;

    private Paint oPaint, cPaint, sPaint;

    private PathMeasure pathMeasure;

    private Path path, dstPath;

    private OnMenuSelectedListener onMenuSelectedListener;

    private OnMenuStatusChangeListener onMenuStatusChangeListener;

    public CircleMenu(Context context) {
        this(context, null);
    }

    public CircleMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        status = STATUS_MENU_CLOSED;
        init();
    }

    private void init() {
        initTool();

        mainMenuColor = Color.parseColor("#323754");

        openMenuIcon = new GradientDrawable();
        closeMenuIcon = new GradientDrawable();

        subMenuColorList = new ArrayList<>();
        subMenuDrawableList = new ArrayList<>();
        menuRectFList = new ArrayList<>();
    }

    private void initTool() {
        oPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        oPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        cPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        cPaint.setStyle(Paint.Style.STROKE);
        cPaint.setStrokeCap(Paint.Cap.ROUND);

        sPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        sPaint.setStyle(Paint.Style.FILL);

        path = new Path();
        dstPath = new Path();
        pathMeasure = new PathMeasure();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int measureWidthSize = width, measureHeightSize = height;

        if (widthMode == MeasureSpec.AT_MOST) {
            measureWidthSize = dip2px(20) * 20;
        }

        if (heightMode == MeasureSpec.AT_MOST) {
            measureHeightSize = dip2px(20) * 20;
        }
        setMeasuredDimension(measureWidthSize, measureHeightSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int minSize = Math.min(getMeasuredWidth(), getMeasuredHeight());

        partSize = minSize / 10;
        iconSize = partSize * 9 / 5; //el tamaño del icono dentro del circulo
        circleMenuRadius = partSize * 3; //distancia entre botones al alrededor

        centerX = getMeasuredWidth() / 2;//lado de la pantalla donde se ubicara el circlemenu el 2 representa que estara centrado
        centerY = getMeasuredHeight() / 2;
        resetMainDrawableBounds();

        path.addCircle(centerX, centerY, circleMenuRadius, Path.Direction.CW);
        pathMeasure.setPath(path, true);
        pathLength = pathMeasure.getLength();

        RectF mainMenuRectF = new RectF(centerX - partSize, centerY - partSize, centerX + partSize, centerY + partSize);
        menuRectFList.add(mainMenuRectF);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        switch (status) {
            case STATUS_MENU_CLOSED:
                drawMainMenu(canvas);
                break;
            case STATUS_MENU_OPEN:
                drawMainMenu(canvas);
                drawSubMenu(canvas);
                break;
            case STATUS_MENU_OPENED:
                drawMainMenu(canvas);
                drawSubMenu(canvas);
                break;
            case STATUS_MENU_CLOSE:
                drawMainMenu(canvas);
                drawSubMenu(canvas);
                drawCircleMenu(canvas);
                break;
            case STATUS_MENU_CLOSE_CLEAR:
                drawMainMenu(canvas);
                drawCircleMenu(canvas);
                break;
            case STATUS_MENU_CANCEL:
                drawMainMenu(canvas);
                drawSubMenu(canvas);
                break;
        }
    }


    //Dibuja un camino circular rodeado de submenús
    private void drawCircleMenu(Canvas canvas) {
        if (status == STATUS_MENU_CLOSE) {
            drawCirclePath(canvas);
            drawCircleIcon(canvas);
        } else {
            cPaint.setStrokeWidth(partSize * 2 + partSize * .5f * fraction);
            cPaint.setColor(calcAlphaColor(getClickMenuColor(), true));
            canvas.drawCircle(centerX, centerY, circleMenuRadius + partSize * .5f * fraction, cPaint);
        }
    }

    private int getClickMenuColor() {

           return clickIndex == 0 ? mainMenuColor : subMenuColorList.get(clickIndex - 1);
    }

//Dibuja el ícono cuando el submenú gira
    private void drawCircleIcon(Canvas canvas) {
        canvas.save();
        Drawable selDrawable = subMenuDrawableList.get(clickIndex - 1);
        if (selDrawable == null) return;
        int startAngle = (clickIndex - 1) * (360 / itemNum);//cantidad de vueltas que da el circulo al hacer click en ella 360= 1 vuelta
        int endAngle = 360 + startAngle;
        int itemX = (int) (centerX + Math.sin(Math.toRadians((endAngle - startAngle) * fraction + startAngle)) * circleMenuRadius);
        int itemY = (int) (centerY - Math.cos(Math.toRadians((endAngle - startAngle) * fraction + startAngle)) * circleMenuRadius);
        canvas.rotate(360 * fraction, itemX, itemY);
        selDrawable.setBounds(itemX - iconSize / 2, itemY - iconSize / 2, itemX + iconSize / 2, itemY + iconSize / 2);
        selDrawable.draw(canvas);
        canvas.restore();
    }

//Dibuje la trayectoria cuando el elemento del submenú gira
    private void drawCirclePath(Canvas canvas) {
        canvas.save();
        canvas.rotate(rotateAngle, centerX, centerY);
        dstPath.reset();
        dstPath.lineTo(0, 0);
        pathMeasure.getSegment(0, pathLength * fraction, dstPath, true);
        cPaint.setStrokeWidth(partSize * 2);
        cPaint.setColor(getClickMenuColor());
        canvas.drawPath(dstPath, cPaint);
        canvas.restore();
    }

//Dibujar botones alrededor de elementos del submenú
    private void drawSubMenu(Canvas canvas) {
        int itemX, itemY, angle;
        final float offsetRadius = 1.5f;
        RectF menuRectF;
        for (int i = 0; i < itemNum; i++) {
            angle = i * (360 / itemNum);
            if (status == STATUS_MENU_OPEN) {
                itemX = (int) (centerX + Math.sin(Math.toRadians(angle)) * (circleMenuRadius - (1 - fraction) * partSize * offsetRadius));
                itemY = (int) (centerY - Math.cos(Math.toRadians(angle)) * (circleMenuRadius - (1 - fraction) * partSize * offsetRadius));
                oPaint.setColor(calcAlphaColor(subMenuColorList.get(i), false));
                sPaint.setColor(calcAlphaColor(subMenuColorList.get(i), false));
            } else if (status == STATUS_MENU_CANCEL) {
                itemX = (int) (centerX + Math.sin(Math.toRadians(angle)) * (circleMenuRadius - fraction * partSize * offsetRadius));
                itemY = (int) (centerY - Math.cos(Math.toRadians(angle)) * (circleMenuRadius - fraction * partSize * offsetRadius));
                oPaint.setColor(calcAlphaColor(subMenuColorList.get(i), true));
                sPaint.setColor(calcAlphaColor(subMenuColorList.get(i), true));
            } else {
                itemX = (int) (centerX + Math.sin(Math.toRadians(angle)) * circleMenuRadius);
                itemY = (int) (centerY - Math.cos(Math.toRadians(angle)) * circleMenuRadius);
                oPaint.setColor(subMenuColorList.get(i));
                sPaint.setColor(subMenuColorList.get(i));
            }
            if (pressed && clickIndex - 1 == i) {
                oPaint.setColor(pressedColor);
            }
            drawMenuShadow(canvas, itemX, itemY, itemMenuRadius);
            canvas.drawCircle(itemX, itemY, itemMenuRadius, oPaint);
            drawSubMenuIcon(canvas, itemX, itemY, i);
            menuRectF = new RectF(itemX - partSize, itemY - partSize, itemX + partSize, itemY + partSize);
            if (menuRectFList.size() - 1 > i) {
                menuRectFList.remove(i + 1);
            }
            menuRectFList.add(i + 1, menuRectF);
        }
    }

//Dibujar icono de elemento de submenú
    private void drawSubMenuIcon(Canvas canvas, int centerX, int centerY, int index) {
        int diff;
        if (status == STATUS_MENU_OPEN || status == STATUS_MENU_CANCEL) {
            diff = itemIconSize / 2;
        } else {
            diff = iconSize / 2;
        }
        resetBoundsAndDrawIcon(canvas, subMenuDrawableList.get(index), centerX, centerY, diff);
    }

    private void resetBoundsAndDrawIcon(Canvas canvas, Drawable drawable, int centerX, int centerY, int diff) {
        if (drawable == null) return;
        drawable.setBounds(centerX - diff, centerY - diff, centerX + diff, centerY + diff);
        drawable.draw(canvas);
    }

    //Dibuja el botón de cambio del menú central

    private void drawMainMenu(Canvas canvas) {
        float centerMenuRadius, realFraction;
        if (status == STATUS_MENU_CLOSE) {
            //El botón central del menú principal se aleja al doble de la velocidad
            realFraction = (1 - fraction * 2) == 0 ? 0 : (1 - fraction * 2);
            centerMenuRadius = partSize * realFraction;
        } else if (status == STATUS_MENU_CLOSE_CLEAR) {
            //El botón central del menú principal se expande cuatro veces más rápido
            realFraction = fraction * 4 >= 1 ? 1 : fraction * 4;
            centerMenuRadius = partSize * realFraction;
        } else if (status == STATUS_MENU_CLOSED || status == STATUS_MENU_CANCEL) {
            centerMenuRadius = partSize;
        } else {
            centerMenuRadius = partSize;
        }
        if (status == STATUS_MENU_OPEN || status == STATUS_MENU_OPENED || status == STATUS_MENU_CLOSE) {
            oPaint.setColor(calcPressedEffectColor(0, .5f));
        } else if (pressed && clickIndex == 0) {
            oPaint.setColor(pressedColor);
        } else {
            oPaint.setColor(mainMenuColor);
            sPaint.setColor(mainMenuColor);
        }
        drawMenuShadow(canvas, centerX, centerY, centerMenuRadius);
        canvas.drawCircle(centerX, centerY, centerMenuRadius, oPaint);
        drawMainMenuIcon(canvas);
    }


    private void drawMainMenuIcon(Canvas canvas) {
        canvas.save();
        switch (status) {
            case STATUS_MENU_CLOSED:
                if (openMenuIcon != null)
                    openMenuIcon.draw(canvas);
                break;
            case STATUS_MENU_OPEN:
                canvas.rotate(45 * (fraction - 1), centerX, centerY);
                resetBoundsAndDrawIcon(canvas, closeMenuIcon, centerX, centerY, iconSize / 2);
                break;
            case STATUS_MENU_OPENED:
                resetBoundsAndDrawIcon(canvas, closeMenuIcon, centerX, centerY, iconSize / 2);
                break;
            case STATUS_MENU_CLOSE:
                resetBoundsAndDrawIcon(canvas, closeMenuIcon, centerX, centerY, itemIconSize / 2);
                break;
            case STATUS_MENU_CLOSE_CLEAR:
                canvas.rotate(90 * (rFraction - 1), centerX, centerY);
                resetBoundsAndDrawIcon(canvas, openMenuIcon, centerX, centerY, itemIconSize / 2);
                break;
            case STATUS_MENU_CANCEL:
                canvas.rotate(-45 * fraction, centerX, centerY);
                if (closeMenuIcon != null)
                    closeMenuIcon.draw(canvas);
                break;
        }
        canvas.restore();
    }

    //Pintar sombra de botón de menú
    private void drawMenuShadow(Canvas canvas, int centerX, int centerY, float radius) {
        if (radius + shadowRadius > 0) {
            sPaint.setShader(new RadialGradient(centerX, centerY, radius + shadowRadius,
                    Color.BLACK, Color.TRANSPARENT, Shader.TileMode.CLAMP));
            canvas.drawCircle(centerX, centerY, radius + shadowRadius, sPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (status == STATUS_MENU_CLOSE || status == STATUS_MENU_CLOSE_CLEAR) return true;
        int index = clickWhichRectF(event.getX(), event.getY());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pressed = true;
                if (index != -1) {
                    clickIndex = index;
                    updatePressEffect(index, pressed);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (index == -1) {
                    pressed = false;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                pressed = false;
                if (index != -1) {
                    clickIndex = index;
                    updatePressEffect(index, pressed);
                }
                //Hizo clic en el botón central
                if (index == 0) {
                    if (status == STATUS_MENU_CLOSED) {
                        status = STATUS_MENU_OPEN;
                        startOpenMenuAnima();
                    } else if (status == STATUS_MENU_OPENED) {
                        status = STATUS_MENU_CANCEL;
                        startCancelMenuAnima();
                    }
                } else { //Haga clic en el botón de los elementos del submenú circundantes
                    if (status == STATUS_MENU_OPENED && index != -1) {
                        status = STATUS_MENU_CLOSE;
                        if (onMenuSelectedListener != null)
                            onMenuSelectedListener.onMenuSelected(index - 1);
                        rotateAngle = clickIndex * (360 / itemNum) - (360 / itemNum) - 90;
                        startCloseMeunAnima();
                    }
                }
                break;
        }
        return true;
    }


//Actualizar estado del botón
    private void updatePressEffect(int menuIndex, boolean press) {
        if (press) {
            pressedColor = calcPressedEffectColor(menuIndex, .15f);
        }
        invalidate();
    }

    //Obtenga el color del botón presionado
    //El rango de valores es [0, 1]. Cuanto mayor sea el valor, más oscuro será el color
    private int calcPressedEffectColor(int menuIndex, float depth) {
        int color = menuIndex == 0 ? mainMenuColor : subMenuColorList.get(menuIndex - 1);
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= (1.f - depth);
        return Color.HSVToColor(hsv);
    }

//Se usa para completar el efecto de animación del anillo que se extiende y desaparece gradualmente en la Vista
    //Ajuste el valor alfa del color según la fracción
    //Ajuste el orden de opaco a transparente, de lo contrario invierta el orden

    private int calcAlphaColor(int color, boolean reverse) {
        int alpha;
        if (reverse) { //De opaco a transparente
            alpha = (int) (255 * (1.f - fraction));
        } else { //De transparente a opaco
            alpha = (int) (255 * fraction);
        }
        if (alpha >= 255) alpha = 255;
        if (alpha <= 0) alpha = 0;
        return ColorUtils.setAlphaComponent(color, alpha);
    }

    //Comienza a abrir la animación del menú
    private void startOpenMenuAnima() {
        ValueAnimator openAnima = ValueAnimator.ofFloat(1.f, 100.f);
        openAnima.setDuration(500);
        openAnima.setInterpolator(new OvershootInterpolator());
        openAnima.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                fraction = valueAnimator.getAnimatedFraction();
                itemMenuRadius = fraction * partSize;
                itemIconSize = (int) (fraction * iconSize);
                invalidate();
            }
        });
        openAnima.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                status = STATUS_MENU_OPENED;
                if (onMenuStatusChangeListener != null)
                    onMenuStatusChangeListener.onMenuOpened();
            }
        });
        openAnima.start();
    }

//Comience a cancelar la animación
    private void startCancelMenuAnima() {

        ValueAnimator cancelAnima = ValueAnimator.ofFloat(1.f, 100.f);
        cancelAnima.setDuration(500);
        cancelAnima.setInterpolator(new AnticipateInterpolator());
        cancelAnima.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                fraction = valueAnimator.getAnimatedFraction();
                itemMenuRadius = (1 - fraction) * partSize;
                itemIconSize = (int) ((1 - fraction) * iconSize);
                invalidate();
            }
        });
        cancelAnima.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                status = STATUS_MENU_CLOSED;
                if (onMenuStatusChangeListener != null)
                    onMenuStatusChangeListener.onMenuClosed();
            }
        });
        cancelAnima.start();
    }

//El elemento de menú seleccionado gira una semana y es impulsado por la animación.
    private void startCloseMeunAnima() {

        ValueAnimator aroundAnima = ValueAnimator.ofFloat(1.f, 100.f);
        aroundAnima.setDuration(600);
        aroundAnima.setInterpolator(new AccelerateDecelerateInterpolator());
        aroundAnima.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                fraction = valueAnimator.getAnimatedFraction();
                // 中心主菜单图标以两倍速度缩小
                float animaFraction = fraction * 2 >= 1 ? 1 : fraction * 2;
                itemIconSize = (int) ((1 - animaFraction) * iconSize);
                invalidate();
            }
        });
        aroundAnima.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                status = STATUS_MENU_CLOSE_CLEAR;
            }
        });

        // //Animación impulsada por la desaparición de la trayectoria circular.
        ValueAnimator spreadAnima = ValueAnimator.ofFloat(1.f, 100.f);
        spreadAnima.setInterpolator(new LinearInterpolator());
        spreadAnima.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                fraction = valueAnimator.getAnimatedFraction();
            }
        });

        //
        //Unidad de animación de rotación del menú principal
        ValueAnimator rotateAnima = ValueAnimator.ofFloat(1.f, 100.f);
        rotateAnima.setInterpolator(new OvershootInterpolator());
        rotateAnima.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                rFraction = valueAnimator.getAnimatedFraction();
                itemIconSize = (int) (rFraction * iconSize);
                invalidate();
            }
        });

        AnimatorSet closeAnimaSet = new AnimatorSet();
        closeAnimaSet.setDuration(500);
        closeAnimaSet.play(spreadAnima).with(rotateAnima);
        closeAnimaSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                status = STATUS_MENU_CLOSED;
                if (onMenuStatusChangeListener != null)
                    onMenuStatusChangeListener.onMenuClosed();
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(aroundAnima).before(closeAnimaSet);
        animatorSet.start();
    }

//Obtenga en qué botón de menú se hace clic actualmente
//     * El subíndice del menú central es 0, y el menú circundante cuenta 1 ~ 5 en sentido horario desde directamente arriba
    private int clickWhichRectF(float x, float y) {
        int which = -1;
        for (RectF rectF : menuRectFList) {
            if (rectF.contains(x, y)) {
                which = menuRectFList.indexOf(rectF);
                break;
            }
        }
        return which;
    }

    private Drawable convertDrawable(int iconRes) {
        return getResources().getDrawable(iconRes);
    }

    private Drawable convertBitmap(Bitmap bitmap) {
        return new BitmapDrawable(getResources(), bitmap);
    }

    private void resetMainDrawableBounds() {
        openMenuIcon.setBounds(centerX - iconSize / 2, centerY - iconSize / 2,
                centerX + iconSize / 2, centerY + iconSize / 2);
        closeMenuIcon.setBounds(centerX - iconSize / 2, centerY - iconSize / 2,
                centerX + iconSize / 2, centerY + iconSize / 2);
    }

    //mainMenuColor Color de fondo del menú principal
    //openMenuRes Ícono de menú abierto
    //closeMenuRes Ícono de cerrar menú
    //Establezca el color de fondo del menú principal y el icono de encendido / apagado
    public com.example.notary_acevedo.CircleMenu setMainMenu(int mainMenuColor, int openMenuRes, int closeMenuRes) {
        openMenuIcon = convertDrawable(openMenuRes);
        closeMenuIcon = convertDrawable(closeMenuRes);
        this.mainMenuColor = mainMenuColor;
        return this;

    }


    public com.example.notary_acevedo.CircleMenu setMainMenu(int mainMenuColor, Bitmap openMenuBitmap, Bitmap closeMenuBitmap) {
        openMenuIcon = convertBitmap(openMenuBitmap);
        closeMenuIcon = convertBitmap(closeMenuBitmap);
        this.mainMenuColor = mainMenuColor;
        return this;
    }


    public com.example.notary_acevedo.CircleMenu setMainMenu(int mainMenuColor, Drawable openMenuDrawable, Drawable closeMenuDrawable) {
        openMenuIcon = openMenuDrawable;
        closeMenuIcon = closeMenuDrawable;
        this.mainMenuColor = mainMenuColor;
        return this;
    }

//Agregue un elemento de submenú, incluido el color de fondo y el ícono del submenú
    public com.example.notary_acevedo.CircleMenu addSubMenu(int menuColor, int menuRes) {
        if (subMenuColorList.size() < MAX_SUBMENU_NUM && subMenuDrawableList.size() < MAX_SUBMENU_NUM) {
            subMenuColorList.add(menuColor);
            subMenuDrawableList.add(convertDrawable(menuRes));
            itemNum = Math.min(subMenuColorList.size(), subMenuDrawableList.size());
        }
        return this;
    }


    public com.example.notary_acevedo.CircleMenu addSubMenu(int menuColor, Bitmap menuBitmap) {
        if (subMenuColorList.size() < MAX_SUBMENU_NUM && subMenuDrawableList.size() < MAX_SUBMENU_NUM) {
            subMenuColorList.add(menuColor);
            subMenuDrawableList.add(convertBitmap(menuBitmap));
            itemNum = Math.min(subMenuColorList.size(), subMenuDrawableList.size());
        }
        return this;
    }

    public com.example.notary_acevedo.CircleMenu addSubMenu(int menuColor, Drawable menuDrawable) {
        if (subMenuColorList.size() < MAX_SUBMENU_NUM && subMenuDrawableList.size() < MAX_SUBMENU_NUM) {
            subMenuColorList.add(menuColor);
            subMenuDrawableList.add(menuDrawable);
            itemNum = Math.min(subMenuColorList.size(), subMenuDrawableList.size());
        }
        return this;
    }


    public void openMenu() {
        if (status == STATUS_MENU_CLOSED) {
            status = STATUS_MENU_OPEN;
            startOpenMenuAnima();
        }
    }


    public void closeMenu() {
        if (status == STATUS_MENU_OPENED) {
            status = STATUS_MENU_CANCEL;
            startCancelMenuAnima();
        }
    }


    public boolean isOpened() {
        return status == STATUS_MENU_OPENED;
    }

    public com.example.notary_acevedo.CircleMenu setOnMenuSelectedListener(OnMenuSelectedListener listener) {
        this.onMenuSelectedListener = listener;
        return this;
    }

    public com.example.notary_acevedo.CircleMenu setOnMenuStatusChangeListener(OnMenuStatusChangeListener listener) {
        this.onMenuStatusChangeListener = listener;
        return this;
    }

    private int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
