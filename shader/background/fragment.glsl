#version 330 core

in vec2 texCoord;
in vec2 passCoord;

uniform sampler2D myTexture;

out vec4 color;

void main()
{
	color = texture(myTexture, texCoord);
}