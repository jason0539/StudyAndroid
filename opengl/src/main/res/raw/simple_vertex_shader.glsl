//vec4是包含四个分量的向量，
//在位置的上下文中，分别是xyzw坐标
attribute vec4 a_Position;

void main()
{
    gl_Position = a_Position;
}