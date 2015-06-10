#version 330 core

in vec3 passColor;
in vec2 texCoord;

uniform sampler2D myTexture;

out vec4 color;

void main()
{
    color = texture(myTexture, texCoord);
}