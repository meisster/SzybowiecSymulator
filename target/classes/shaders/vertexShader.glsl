#version 400 core

in vec3 position;
in vec2 texture_coords;
in vec3 normal;

out vec2 pass_texture_coords;
out vec3 surface_normal;
out vec3 to_light_vector[4];
out vec3 to_camera_vector;

uniform mat4 transformation_matrix;
uniform mat4 projection_matrix;
uniform mat4 view_matrix;
uniform vec3 light_pos[4];

uniform float use_fake_lighting;


void main(void){
    vec4 world_position = transformation_matrix * vec4(position, 1.0);
    gl_Position = projection_matrix * view_matrix * world_position;
    pass_texture_coords = texture_coords;

    vec3 actual_normal = normal;
    if(use_fake_lighting > 0.5){
        actual_normal = vec3(0.0, 1.0, 0.0);
    }

    surface_normal = (transformation_matrix * vec4(actual_normal, 0.0)).xyz;
    for(int i=0; i<4; i++){
        to_light_vector[i] = light_pos[i] - world_position.xyz;
    }
    to_camera_vector = (inverse(view_matrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - world_position.xyz;
}