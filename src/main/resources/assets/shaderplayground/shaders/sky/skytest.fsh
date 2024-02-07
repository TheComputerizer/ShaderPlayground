#version 330 compatibility

const float RADIUS = 0.75f;
const float SOFTNESS = 0.45f;
const vec3 SEPIA = vec3(1.2f,1f,0.8f);

uniform sampler2D texture;

uniform float time;
uniform float width;
uniform float height;

in vec4 vColor;
in vec2 vTexCoord;

float getTimeFactor(float original) {
    return original+time;
}

float flash(vec2 seed) {
    return abs(sin(time));
}

float random(vec2 seed) {
    return abs(noise1(seed.x));
}

void main() {
    vec4 texColor = texture2D(texture,vTexCoord);
    vec2 resolution = vec2(width,height);
    vec2 position = (gl_FragCoord.xy/resolution.xy)-vec2(0.5f);
    float len = length(position);
    float vignette = smoothstep(RADIUS,RADIUS-SOFTNESS,len);
    texColor.rgb = mix(texColor.rgb,texColor.rgb*vignette,0.5f);
    float flashFactor = flash(position);
    float gray = dot(texColor.rgb, vec3(0.299f,0.587f,flashFactor));
    vec3 sepiaColor = vec3(gray)*SEPIA;
    texColor.rgb = mix(texColor.rgb,sepiaColor,0.75f);
    gl_FragColor = vec4(texColor.rgb,1f);
}