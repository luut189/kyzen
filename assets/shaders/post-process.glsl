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
uniform float bloomIntensity = 0.8;

void main() {
    vec4 sceneColor = texture(uScene, TexCoords);
    vec3 bloomColor = texture(uBloom, TexCoords).rgb;

    vec3 finalColor = sceneColor.rgb + (bloomColor * bloomIntensity);

    FragColor = vec4(finalColor, sceneColor.a);
}
