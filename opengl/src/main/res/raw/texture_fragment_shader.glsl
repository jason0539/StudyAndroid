precision mediump float;

//二维纹理数据的数组
uniform sampler2D u_TextureUnit;
//纹理坐标
varying vec2 v_TextureCoordinates;

void main(){
    //纹理坐标和纹理数据被传递给着色器函数texture2D()
    //它会读入纹理中那个特定坐标处的颜色值，接着通过把结果赋值给gl_FragColor设置片段的颜色
    gl_FragColor = texture2D(u_TextureUnit,v_TextureCoordinates);
}