#type vertex
#version 330 core
layout (location = 0) in vec2 aPos;

uniform mat4 projection;
uniform mat4 view;
uniform vec2 lightPos;
uniform float lightRadius;

out vec2 FragPos;

void main() {
    // Scale the vertices by the light radius and position at the light center
    vec2 worldPos = aPos * lightRadius + lightPos;
    FragPos = worldPos;

    // Transform with view and projection
    gl_Position = projection * view * vec4(worldPos, 0.0, 1.0);
}

#type fragment
#version 330 core
in vec2 FragPos;
out vec4 FragColor;

uniform vec3 lightColor;
uniform vec2 lightPos;
uniform float lightRadius;
uniform float ambientStrength;

void main() {
    // Calculate distance from the fragment to the light's position
    float distance = length(FragPos - lightPos);

    // Attenuation: smoothstep function for the falloff of the light intensity
    float attenuation = 1.0 - smoothstep(0.0, lightRadius * 0.7, distance);

    // Final color with attenuation and ambient light strength
    FragColor = vec4(lightColor * attenuation * ambientStrength, 1.0);
}
