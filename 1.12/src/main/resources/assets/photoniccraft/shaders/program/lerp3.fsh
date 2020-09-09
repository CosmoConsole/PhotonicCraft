#version 120

uniform sampler2D DiffuseSampler;

varying vec2 texCoord;
varying vec2 oneTexel;

uniform vec2 InSize;

uniform vec3 Gray   = vec3(0.3, 0.59, 0.11);
uniform vec3 Dark   = vec3(0.0, 0.0, 0.0);
uniform vec3 Mid    = vec3(0.5, 0.5, 0.5);
uniform vec3 Light  = vec3(1.0, 1.0, 1.0);

void main() {
    vec4 InTexel = texture2D(DiffuseSampler, texCoord);
    
    float Luma = dot(vec3(InTexel), Gray);

    vec3 OutColor = mix(mix(Dark, Mid, clamp(Luma * 2.0, 0.0, 1.0)), 
                                Light, clamp(Luma * 2.0 - 1.0, 0.0, 1.0));

    gl_FragColor = vec4(OutColor, 1.0);
}
