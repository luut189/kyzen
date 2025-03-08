#type vertex
#version 330 core
layout (location = 0) in vec2 aPos;
layout (location = 1) in vec2 aTexCoord;

out vec2 TexCoords; // Make sure this matches the fragment shader input

void main() {
    TexCoords = aTexCoord;
    gl_Position = vec4(aPos, 0.0, 1.0);
}

#type fragment
#version 330 core
in vec2 TexCoords;
out vec4 FragColor;

uniform sampler2D uScene;
uniform sampler2D uBloom;
uniform float uBloomStrength;
uniform float uGamma;

void main() {
    // Make sure we're sampling using the correct texture coordinates
    vec3 sceneColor = texture(uScene, TexCoords).rgb;
    vec3 bloomColor = texture(uBloom, TexCoords).rgb;

    // Add bloom to the scene color
    vec3 finalColor = sceneColor + (bloomColor * uBloomStrength);

    // Apply gamma correction
    finalColor = pow(finalColor, vec3(1.0 / uGamma));

    FragColor = vec4(finalColor, 1.0);
}
