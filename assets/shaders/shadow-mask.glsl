#type vertex
#version 330 core
layout (location = 0) in vec2 aPos;
layout (location = 1) in vec2 aTexCoord;

out vec2 TexCoords;

void main() {
    TexCoords = aTexCoord;
    gl_Position = vec4(aPos, 0.0, 1.0);
}

#type fragment
#version 330 core
in vec2 TexCoords;
out vec4 FragColor;

uniform sampler2D uScene;
uniform sampler2D uLightMask;
uniform float shadowIntensity; // How dark the shadows are (0-1, where 1 is completely black)

void main() {
    vec4 sceneColor = texture(uScene, TexCoords);
    vec4 lightMask = texture(uLightMask, TexCoords);

    // The alpha from the light mask determines how much shadow to apply
    // Higher alpha value = more light = less shadow
    float shadow = 1.0 - lightMask.a * shadowIntensity;

    // Apply the shadow as a darkening factor
    vec3 finalColor = sceneColor.rgb * shadow;

    FragColor = vec4(finalColor, sceneColor.a);
}
