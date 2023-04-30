#version 130

#ifdef GL_ES
precision highp float;
#endif

in vec2 surfacePosition;
uniform float time;
uniform vec2 mouse;
uniform vec2 resolution;
uniform vec2 dataResolution;
uniform sampler2D texture1;
uniform sampler2D dataTexture;
uniform vec2 surfaceSize;
uniform sampler2D backbuffer;

vec2 origin = vec2(0.5, 0.6);
vec2 offset = vec2(0.53, 0.3);
vec2 offset1 = vec2(0.5, 0.2);
vec2 offset2 = vec2(0.55,0.5);

float time1 = time * 3.;

//https://www.shadertoy.com/view/4ljfRD
vec2 getInterp(vec2 start, vec2 end, vec2 pos) {
	float d = distance(start, end);
	float duv = distance(start, pos);
	vec2 interpolated = mix(start, end, clamp(duv / d, 0., 1.));
	return interpolated;
}

float drawLine(vec2 p1, vec2 p2, vec2 uv, float a) {
	//	a *= 2.;
	float one_px = 1. / resolution.x;
	vec2 interpolated = getInterp(p1, p2, uv);
	float r = (1. - ((distance(interpolated, uv) * 10.) / a));
	return r;
}

bool isPartOfCircle(vec2 pos, vec2 center, float rad) {
	return max(0., ceil(1. - length(pos - center) / rad)) == 1 ? true : false;
}

void main( void ) {
	vec2 position = surfacePosition;
	//Color of pixel
	vec4 fragCol = vec4(0);
	vec4 playerY = texture2D(texture1, vec2(-1, -1));
	position.y -= playerY.x;
	if (isPartOfCircle(position, vec2(0, -0.5), 0.05)) {
		fragCol += vec4(0.6, 1, 0.3, 1);
	}
	vec4 data = texture2D(dataTexture, gl_FragCoord.xy / resolution);
	if (data.x != 0 || data.y != 0 || data.z != 0) fragCol = data;
//	if (isPartOfCircle(position, vec2(0, 0), 0.1)) {
//		data += vec4(0.6, 0.5, 0.3, 1);
//	}
	fragCol += texture2D(backbuffer, gl_FragCoord.xy / resolution);
	gl_FragColor = max(fragCol, 0.);
}
