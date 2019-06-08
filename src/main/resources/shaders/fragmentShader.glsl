#version 150

in vec2 pass_texture_coords;
in vec3 surface_normal;
in vec3 to_light_vector[4];
in vec3 to_camera_vector;

out vec4 out_colour;

uniform sampler2D texture_map;
uniform sampler2D specular_map;
uniform float use_specular_map;


uniform vec3 light_col[4];
uniform float shine_damper;
uniform vec3 attenuation[4];
uniform float reflectivity;
uniform vec3 sky_colour;
//const float levels = 6.0;

void main(void) {
    vec3 normalized_surface_normal = normalize(surface_normal);
    vec3 normalized_to_camera_vector = normalize(to_camera_vector);

    vec3 total_diffuse = vec3(0.0);
    vec3 total_specular = vec3(0.0);

    for(int i = 0; i < 4; i++){
        float distance = length(to_light_vector[i]);
        float attFactor = attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].z * distance * distance);

        vec3 normalized_light_vector = normalize(to_light_vector[i]);
        float vec_dot = dot(normalized_light_vector, normalized_surface_normal);
        float brightness = max(vec_dot, 0);
        vec3 light_direction = -normalized_to_camera_vector;
        vec3 reflected_direction = reflect(light_direction, surface_normal);
        float specular_factor = dot(reflected_direction, normalized_to_camera_vector);
        specular_factor = max(specular_factor, 0.0);
        float damped_factor = pow(specular_factor, shine_damper);

        total_diffuse += brightness * light_col[i]/attFactor;
        total_specular += damped_factor * reflectivity * light_col[i]/attFactor;
    }
    total_diffuse = max(total_diffuse, 0.1f);

    vec4 texture_colour = texture(texture_map, pass_texture_coords);
    if(texture_colour.a<0.5){
        discard;
    }
    if(use_specular_map > 0.5){
        vec4 mapInfo = texture(specular_map, pass_texture_coords);
        total_specular *= mapInfo.rgb;
    }

    out_colour = vec4(total_diffuse,1.0) * texture_colour + vec4(total_specular, 1.0);
    //out_colour = mix(vec4(sky_colour,1.0),out_colour);
}
