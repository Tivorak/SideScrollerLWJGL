#version 330 core

layout (location = 0) in vec2 position;
layout (location = 1) in vec2 textureCoord;

out vec2 texCoord;
out vec2 passCoord;

void main()
{
    gl_Position = vec4(position, 0, 1);
    texCoord = textureCoord;
    passCoord = position;
}