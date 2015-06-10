#version 330 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec3 inColor;

uniform vec3 offset;

out vec3 passColor;

void main()
{
    gl_Position = vec4(position.x + offset.x, position.y + offset.y, position.z + offset.z, 1.0);
    passColor = inColor;
}