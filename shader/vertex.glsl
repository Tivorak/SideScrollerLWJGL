#version 330 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec3 inColor;
layout (location = 2) in vec2 textureCoord;

uniform vec3 offset;

out vec3 passColor;
out vec2 texCoord;

void main()
{
    gl_Position = vec4(position.x + offset.x, position.y + offset.y, position.z + offset.z, 1.0);
    texCoord = textureCoord;
    passColor = inColor;
}