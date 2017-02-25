//vec4是包含四个分量的向量，
//在位置的上下文中，分别是xyzw坐标
attribute vec4 a_Position;

void main()
{
    gl_Position = a_Position;
    //OpenGL需要我们指定 在屏幕上所显示的点 的大小
    //OpenGL把一个点分解为片段的时候，它会生成一些片段，它们是以gl_Position为中心的四边形，
    //这个四边形的每条边的长度与gl_PointSize相等
    gl_PointSize = 10.0;
}