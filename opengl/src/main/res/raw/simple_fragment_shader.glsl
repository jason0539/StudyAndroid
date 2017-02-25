//定义所有浮点类型的默认精度
precision mediump float;

//uniform会让每个定点都是用同一个值
//vec4在颜色的上下文中代表RGBA
uniform vec4 u_Color;

void main(){
    gl_FragColor = u_Color;
}