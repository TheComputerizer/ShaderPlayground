/*
 * Author The_Computerizer - 2024
*/
#version 330 compatibility

/*
 * Constants
*/

#define NUM_OCTAVES 5

const vec3 SEPIA = vec3(1.2,1.0,0.8);
const vec3 OUTLINE_COLOR = vec3(0.0,0.0,0.0);

/*
 * Uniforms
*/

//Texture sampler
uniform sampler2D texture;

//Equal to World#getgetWorldTime()%65536L from Minecraft#world or 1 if the world does not exist
uniform float time;
uniform float timeScale;
uniform float radius;
uniform float outlineThickness;
uniform float animationScale;

/*
 * Inputs passed from the vertex shader
*/

in vec4 vColor;
in vec2 vTexCoord;

/*
 * Gets the normalized position of the fragment between 0-1 by utilizng the texture coords.
 * Assumes the texture size is 16x16
*/
vec2 getNormalizedPosition() {
    return vTexCoord.xy/16.0;
}

/*
 * Gets a pseudo-random float using the input vector as a seed
*/
float random(vec2 seed) {
    return fract(sin(dot(seed.xy,vec2(12.9898,78.233)))*43758.5453123);
}

/*
 * Adapted from The Book of Shaders
 * https://thebookofshaders.com/13/
 * Based on Morgan McGuire @morgan3d
 * https://www.shadertoy.com/view/4dS3Wd
*/
float noise(vec2 seed) {
    vec2 i = floor(seed);
    vec2 f = fract(seed);

    //Four corners of a 2D tile
    float a = random(i);
    float b = random(i + vec2(1.0, 0.0));
    float c = random(i + vec2(0.0, 1.0));
    float d = random(i + vec2(1.0, 1.0));

    vec2 u = f * f * (3.0 - 2.0 * f);

    return mix(a, b, u.x) +
    (c - a)* u.y * (1.0 - u.x) +
    (d - b) * u.x * u.y;
}

/*
 * Adapted from The Book of Shaders
 * https://thebookofshaders.com/13/
*/
float fbm(vec2 seed) {
    float v = 0.0;
    float a = 0.5;
    vec2 shift = vec2(100.0);
    //Rotate to reduce axial bias
    mat2 rot = mat2(cos(0.5), sin(0.5),
    -sin(0.5), cos(0.50));
    for (int i = 0; i < NUM_OCTAVES; ++i) {
        v += a * noise(seed);
        seed = rot*seed*2.0+shift;
        a *= 0.5;
    }
    return v;
}

/*
 * Computes the Fractal Brownian Motion output for the fragment from a normalized input position
 * Adapted from The Book of Shaders
 * https://thebookofshaders.com/13/
*/
vec4 mainFBM(vec2 nPos) {
    //Scaled position to adjust noise
    vec2 pos = nPos*animationScale;

    //Scaled time factor to adjust the animation speed
    float timeFactor = time*timeScale;

    //Changes the noise type
    //pos += pos * abs(sin(timeFactor*0.1)*3.0);

    //Color mixer
    vec3 color = vec3(0.0);

    vec2 q = vec2(0.0);
    q.x = fbm(pos+0.00*timeFactor);
    q.y = fbm(pos+vec2(1.0));

    vec2 r = vec2(0.);
    r.x = fbm(pos+1.0*q+vec2(1.7,9.2)+0.15*timeFactor);
    r.y = fbm(pos+1.0*q+vec2(8.3,2.8)+0.126*timeFactor);

    float f = fbm(pos+r);

    color = mix(vec3(0.101961,0.619608,0.666667),vec3(0.666667,0.666667,0.498039),clamp((f*f)*4.0,0.0,1.0));

    color = mix(color,vec3(0,0,0.164706),clamp(length(q),0.0,1.0));

    color = mix(color,vec3(0.666667,1,1),clamp(length(r.x),0.0,1.0));

    return vec4((f*f*f+.6*f*f+.5*f)*color*0.75,1.);
}

/*
 * Returns 1 if the position is in the box and 0 if it is not
*/
float box(vec2 pos) {
    float left = step(-0.1,pos.x);
    float bottom = step(-0.1,pos.y);
    float right = 1.0-step(0.1,pos.x);
    float top = 1.0-step(0.1,pos.y);
    return left*bottom*right*top;
}

/*
 * Makes a hole from the input center position, radius, and color vectors
 * The radius vector should be (outer,inner)
*/
vec4 makeHole(vec2 center, vec2 radius, vec4 holeColor, vec4 outerColor) {
    float len = length(center);
    vec3 outlinedHole = mix(holeColor.rgb,OUTLINE_COLOR.rgb,step(radius.y,len));
    return vec4(mix(outlinedHole.rgb,outerColor.rgb,step(radius.x,len)),1.0);
}

/*
 * Shader entry point.
 * Any method or field calls must be built-in or defined above this
 * Fractal Brownian Motion adapted from The Book of Shaders by Patricio Gonzalez Vivo & Jen Lowe
 * https://thebookofshaders.com/13/
*/
void main() {
    //RGBA texture color vector
    vec4 texColor = texture2D(texture,vTexCoord);

    //Normalized fragment position between 0-1
    vec2 nPos = getNormalizedPosition();

    //Center position
    vec2 cPos = nPos-vec2(0.5);

    //Fractal Brownian Motion color output
    vec4 brownian = mainFBM(nPos);

    //Redii of the hole
    vec2 radii = vec2(radius+outlineThickness,radius);

    //Simple box
    float bBox = box(cPos);

    //Color for the inside of the hole
    vec4 innerColor = vec4(brownian.rgb*bBox,brownian.a);

    //Color for the outside of the hole
    vec4 outerColor = texColor;

    //Sets the final color of the fragment
    gl_FragColor = makeHole(cPos,radii,innerColor,outerColor);
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