#version 120

uniform sampler2D texture;

uniform float millis;
uniform float width;
uniform float height;

float getTimeFactor(float original) {
    return original+(millis/50f);
}

float flash() {
    return fract(abs(sin(millis/50f)));
}

float random (in vec2 _st) {
    return fract(sin(dot(_st.xy,vec2(12.9898,78.233)))*43758.5453123);
}

void main() {
    vec4 mask = texture2D(texture,gl_TexCoord[0].xy);
    //vec2 coord = gl_FragCoord.xy/vec2(width,height);
    gl_FragColor = vec4(mask.xyz,random(gl_FragCoord.xy));
}