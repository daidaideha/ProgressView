渐变颜色一共四种方向，如下：
 * 渐变颜色方向<br>
public enum ColorOrientation {
     * 水平方向 从左到右<br>
    HORIZONTAL,
     * 垂直方向 从上到下<br>
    VERTICAL,
     * 从左下角到右上角<br>
    LEFT_TOP_RIGHT,
     * 从左上角到右下角<br>
    LEFT_BOTTOM_RIGHT
}

xml自定义属性<br>
app:emptyStrokeWidth="5dp" // 空状态的圈宽度<br>
app:fullStrokeWidth="8dp" // 进度圈的宽度<br>
app:shaderEndColor="@color/colorPrimary" // 渐变结束颜色值<br>
app:shaderStartColor="@color/colorAccent" // 渐变开始颜色值<br>
app:textSize="24sp" // 文字大小<br>
app:startAngle // 起始点角度<br>
app:colorOrientation // 渐变方向
