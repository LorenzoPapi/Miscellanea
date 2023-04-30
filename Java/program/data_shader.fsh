#ifdef GL_ES
precision highp float;
#endif

uniform float time;
uniform float frameTime;
uniform vec2 dataResolution;
uniform sampler2D backbuffer;
uniform float keyW;
uniform float keyR;
uniform float isFirstFrame;
float firstFrame = isFirstFrame;

vec4 fade(vec4 pos, float fade, float min) {
	vec4 fragCol = texture2D(backbuffer, gl_FragCoord.xy / dataResolution.xy);
	fragCol /= fade;
	fragCol.x = max(fragCol.x, min);
	fragCol.y = fragCol.x;
	fragCol.z = fragCol.x;
	fragCol.w = fragCol.x;
	return fragCol;
}

void up(vec4 frag) {
	for (int i = 0; i < 60; i++) {
		frag.x = i * 5./255.;
	}
}

void main (void) {
	vec2 pos = gl_FragCoord.xy;
	vec4 fragCol = texture2D(backbuffer, gl_FragCoord.xy / dataResolution.xy); // get the last frame's color for this pixel
	float gravity = 9.806;
	//frametime is the time that took the last frame to render, e.g. 1/60
	if (keyR == 1) {
		firstFrame = 1;
	}
	if (pos.x <= 1 && pos.y <= 1) {
		vec4 wPressed = texture2D(backbuffer, vec2(2, 0) / dataResolution.xy);
		if (firstFrame == 1) {
			fragCol.x = 0;
		}
		if (keyW == 1 && wPressed.x == 0) {
			up(fragCol);
		}
//		else {
//			fragCol.x -= 5./255.;
//		}
	} else if (pos.x <= 2 && pos.y <= 1) {
		if (keyW == 1) {
			fragCol.x = 1;
		} else {
			fragCol.x = 0;
		}
	}
	gl_FragColor = fragCol;
}

