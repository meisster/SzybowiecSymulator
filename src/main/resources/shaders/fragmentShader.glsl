#version 150

in vec2 pass_texture_coords;
in vec3 surface_normal;
in vec3 to_light_vector;
in vec3 to_camera_vector;

out vec4 out_colour;

uniform sampler2D texture_map;
uniform sampler2D specular_map;
uniform float use_specular_map;


uniform vec3 light_col;
uniform float shine_damper;
uniform float reflectivity;
uniform vec3 sky_colour;
const float levels = 6.0;

void main(void) {
    vec3 normalized_surface_normal = normalize(surface_normal);
    vec3 normalized_light_vector = normalize(to_light_vector);

    float vec_dot = dot(normalized_light_vector, normalized_surface_normal);
    float brightness = max(vec_dot, 0.2);
    //float level = floor(brightness * levels);
    //brightness = level/levels;
    vec3 diffuse = brightness * light_col;

    vec3 normalized_to_camera_vector = normalize(to_camera_vector);
    vec3 light_direction = -normalized_to_camera_vector;
    vec3 reflected_direction = reflect(light_direction, surface_normal);

    float specular_factor = dot(reflected_direction, normalized_to_camera_vector);
    specular_factor = max(specular_factor, 0.0);
    float damped_factor = pow(specular_factor, shine_damper);
    vec3 final_specular = damped_factor * reflectivity * light_col;

    vec4 texture_colour = texture(texture_map, pass_texture_coords);
    if(texture_colour.a<0.5){
        discard;
    }
    if(use_specular_map > 0.5){
        vec4 mapInfo = texture(specular_map, pass_texture_coords);
        final_specular *= mapInfo.rgb;
    }

    out_colour = vec4(diffuse,1.0) * texture_colour + vec4(final_specular, 1.0);
}
