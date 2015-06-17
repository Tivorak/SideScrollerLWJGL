#version 330 core

in vec3 passColor;
in vec2 texCoord;

uniform sampler2D wallTex;
uniform sampler2D smileyTex;

out vec4 color;

void main()
{
    color = mix(texture(wallTex, texCoord), texture(smileyTex, texCoord), 0.2);
}