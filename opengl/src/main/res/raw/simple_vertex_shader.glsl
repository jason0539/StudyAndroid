//vec4是包含四个分量的向量，
//在位置的上下文中，分别是xyzw坐标
attribute vec4 a_Position;
attribute vec4 a_Color;

//用来实现颜色混合渐变效果
varying vec4 v_Color;

//代表一个4x4矩阵
uniform mat4 u_Matrix;

void main()
{
    v_Color = a_Color;

    gl_Position = a_Position * u_Matrix;
    //OpenGL需要我们指定 在屏幕上所显示的点 的大小
    //OpenGL把一个点分解为片段的时候，它会生成一些片段，它们是以gl_Position为中心的四边形，
    //这个四边形的每条边的长度与gl_PointSize相等
    gl_PointSize = 10.0;
}