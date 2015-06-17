#version 330 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec3 inColor;

uniform mat4 transform;

out vec3 passColor;

void main()
{
    gl_Position = vec4(position, 1) * transform;
    passColor = inColor;
}