//定义所有浮点类型的默认精度
precision mediump float;

//varying会使用混合颜色实现渐变效果
varying vec4 v_Color;

void main(){
    gl_FragColor = v_Color;
}