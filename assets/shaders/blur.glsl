#type vertex
#version 330 core
layout (location = 0) in vec2 aPos; // Screen quad position
layout (location = 1) in vec2 aTexCoord; // Texture coordinates

out vec2 TexCoords; // Changed from TexCoord to TexCoords to match fragment shader

void main() {
    TexCoords = aTexCoord;
    gl_Position = vec4(aPos, 0.0, 1.0);
}

#type fragment
#version 330 core
in vec2 TexCoords;
out vec4 FragColor;

uniform sampler2D image;
uniform bool horizontal;

void main() {
    float weight[5] = float[](0.227027, 0.1945946, 0.1216216, 0.054054, 0.016216);
    vec2 tex_offset = 1.0 / textureSize(image, 0);
    vec3 result = texture(image, TexCoords).rgb * weight[0];

    for (int i = 1; i < 5; i++) {
        result += texture(image, TexCoords + vec2(horizontal ? tex_offset.x * i : 0.0, horizontal ? 0.0 : tex_offset.y * i)).rgb * weight[i];
        result += texture(image, TexCoords - vec2(horizontal ? tex_offset.x * i : 0.0, horizontal ? 0.0 : tex_offset.y * i)).rgb * weight[i];
    }

    FragColor = vec4(result, 1.0);
}