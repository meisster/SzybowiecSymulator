package entities;

import lombok.Getter;
import lombok.Setter;
import matrices.Rotation;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;


@Getter
@Setter
public class Camera {

    private Vector3f position = new Vector3f(0, 10, 0);

    private float distanceFromPlayer = 30;
    private float angleAroundPlayer = 0;

    private float pitch = 10;
    private float yaw;
    private float roll;

    private Plane plane;

    public Camera(Plane plane) {
        this.plane = plane;
        Mouse.setGrabbed(true);
    }

    public void setPlayer(Plane plane) {
        this.plane = plane;
    }

    private void calculateZoom() {
        float zoomLevel = Mouse.getDWheel() * 0.1f;
        if (distanceFromPlayer - zoomLevel > 0) distanceFromPlayer -= zoomLevel;
    }

    private void calculatePitch() {
        //if (Mouse.isButtonDown(1)) {
            float pitchChange = Mouse.getDY() * 0.1f;
            //if(getPosition().getY() < 5){
            //    if(pitch < 10) pitch += 0.2f;
            //    if(pitch < 10 && getPosition().getY() == 0.5f) pitch = 10;
            //    if (pitch - pitchChange > 10) pitch -= pitchChange;
            //}
            //else{pitch -= pitchChange;}
            pitch -= pitchChange;
    }

    private void calculateAngleAroundPlayer() {
        //if (Mouse.isButtonDown(0)) {
            float angleChange = Mouse.getDX() * 0.3f;
            angleAroundPlayer -= angleChange;
    }

    private float calculateHorizontalDistance() {
        return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
    }

    private float calculateVerticalDistance() {
        return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
    }

    private void calculateCameraPosition(float hor, float ver) {
        float theta = plane.getRotation().getYRotation() + angleAroundPlayer;
        float offsetX = (float) (hor * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float) (hor * Math.cos(Math.toRadians(theta)));
        position.x = plane.getPosition().getX() - offsetX;
        position.z = plane.getPosition().getZ() - offsetZ;
        position.y = plane.getPosition().getY() + ver;
    }

    public void move() {
        calculateZoom();
        calculatePitch();
        calculateAngleAroundPlayer();
        //plane.setRotation(new Rotation(getPitch(), - getYaw(), getRoll()));
        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();
        calculateCameraPosition(horizontalDistance, verticalDistance);
        this.yaw = 180 - (plane.getRotation().getYRotation() + angleAroundPlayer);
    }
}
