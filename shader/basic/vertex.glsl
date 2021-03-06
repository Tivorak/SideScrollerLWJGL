#version 330 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec3 inColor;
layout (location = 2) in vec2 textureCoord;

out vec3 passColor;
out vec2 texCoord;

uniform mat4 transform;

void main()
{
    gl_Position = vec4(position, 1) * transform;
    texCoord = textureCoord;
    passColor = inColor;
}