#version 330 compatibility

const float RADIUS = 12;
const float SOFTNESS = 7.2;
const vec3 SEPIA = vec3(1.2,1.0,0.8);

uniform sampler2D texture;

uniform float time;

in vec4 vColor;
in vec2 vTexCoord;

float getTimeFactor(float original) {
    return original+time;
}

float flash(vec2 seed) {
    return abs(sin(time));
}

float random(vec2 seed) {
    return fract(sin(dot(seed.xy,vec2(12.9898,78.233)))*43758.5453123);
}

float noise(vec2 seed) {
    vec2 i = floor(seed);
    vec2 f = fract(seed);

    // Four corners in 2D of a tile
    float a = random(i);
    float b = random(i + vec2(1.0, 0.0));
    float c = random(i + vec2(0.0, 1.0));
    float d = random(i + vec2(1.0, 1.0));

    vec2 u = f * f * (3.0 - 2.0 * f);

    return mix(a, b, u.x) +
    (c - a)* u.y * (1.0 - u.x) +
    (d - b) * u.x * u.y;
}

#define NUM_OCTAVES 5

float fbm(vec2 seed) {
    float v = 0.0;
    float a = 0.5;
    vec2 shift = vec2(100.0);
    // Rotate to reduce axial bias
    mat2 rot = mat2(cos(0.5), sin(0.5),
    -sin(0.5), cos(0.50));
    for (int i = 0; i < NUM_OCTAVES; ++i) {
        v += a * noise(seed);
        seed = rot*seed*2.0+shift;
        a *= 0.5;
    }
    return v;
}

void main() {
    vec4 texColor = texture2D(texture,vTexCoord);
    vec2 position = vTexCoord.xy/16.0*3.0;
    position += position * abs(sin(time*0.1)*3.0);
    vec3 color = vec3(0.0);

    vec2 q = vec2(0.);
    q.x = fbm( position + 0.00*time);
    q.y = fbm( position + vec2(1.0));

    vec2 r = vec2(0.);
    r.x = fbm( position + 1.0*q + vec2(1.7,9.2)+ 0.15*time );
    r.y = fbm( position + 1.0*q + vec2(8.3,2.8)+ 0.126*time);

    float f = fbm(position+r);

    color = mix(vec3(0.101961,0.619608,0.666667), vec3(0.666667,0.666667,0.498039), clamp((f*f)*4.0,0.0,1.0));

    color = mix(color, vec3(0,0,0.164706), clamp(length(q),0.0,1.0));

    color = mix(color, vec3(0.666667,1,1), clamp(length(r.x),0.0,1.0));

    color = mix(color,texColor.rgb,texColor.a*0.75);

    gl_FragColor = vec4((f*f*f+.6*f*f+.5*f)*color,1.);
}

/*
void main() {
    vec4 texColor = texture2D(texture,vTexCoord);
    vec2 resolution = vec2(width,height);
    vec2 position = vTexCoord.xy-vec2(8.0);
    float len = length(position);
    float vignette = smoothstep(RADIUS,RADIUS-SOFTNESS,len);
    texColor.rgb = mix(texColor.rgb,texColor.rgb*vignette,0.75);
    float flashFactor = flash(position);
    float gray = dot(texColor.rgb, vec3(0.299,0.587,flashFactor));
    vec3 sepiaColor = vec3(gray)*SEPIA;
    //texColor.rgb = mix(texColor.rgb,sepiaColor,0.75);
    gl_FragColor = vec4(texColor.rgb,1.0);
}
*/