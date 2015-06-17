#version 330 core

in vec3 passColor;
in vec2 texCoord;

uniform sampler2D myTexture1;
uniform sampler2D myTexture2;

out vec4 color;

void main()
{
    color = mix(texture(myTexture1, texCoord), texture(myTexture2, texCoord), 0.2);
}