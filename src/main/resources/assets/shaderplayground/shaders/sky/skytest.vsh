#version 330 compatibility

const vec4 DEFAULT_COLOR = vec4(1f,1f,1f,1f);

out vec4 vColor;
out vec2 vTexCoord;

void main() {
    vColor = DEFAULT_COLOR;
    vTexCoord = gl_MultiTexCoord0.xy;
    gl_Position = gl_ModelViewProjectionMatrix*gl_Vertex;
}