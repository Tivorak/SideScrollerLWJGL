#version 330 core

in vec3 passColor;

out vec4 color;

void main()
{
    color = vec4(passColor, 1.0);
}