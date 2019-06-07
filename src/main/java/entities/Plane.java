package entities;

import matrices.Rotation;
import model.TexturedModel;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;
import render.DisplayManager;

public class Plane extends Entity {

    private static final float FLY_SPEED = 30;
    private static final float TURN_SPEED = 20;
    private static final float ACCELERATION = 5;
    private static final float TURN_ACCELERATION = 30;
    private static final float DECELERATION = 2;
    private static final float BREAK = 8;
    private static final float PITCH_UP = 1.0f;
    private static final float INERTIA = 3.5f;
    private static final float GRAVITY = 9.8f;

    private float currentSpeed = 0;
    private float currentTurnSpeed = 0;
    private float currentAcceleration = 0;
    private float change = 0;
    private float pitch = 0;
    private float xRotation = 0;
    private float zRotation = 0;

    public Plane(TexturedModel texturedModel, Vector3f position, Rotation rotation, float scale) {
        super(texturedModel, position, rotation, scale);
    }

    public void move() {
        currentAcceleration = ACCELERATION + (getRotation().getXRotation() / 90) * GRAVITY;
        checkInputs();
        super.changeRotation(xRotation, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), zRotation);
        float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
        float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotation().getYRotation())));
        float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotation().getYRotation())));
        float dy = -getRotation().getXRotation() / 90 * currentSpeed / 2 * DisplayManager.getFrameTimeSeconds();
        dx *= (1 - Math.abs((getRotation().getXRotation() / 90)));
        dz *= (1 - Math.abs((getRotation().getXRotation() / 90)));
        super.changePosition(dx, dy - pitch, dz);
        //System.out.println("VELOCITY = " + currentSpeed + "\t TURN_VELOCITY: " + currentTurnSpeed);
    }

    private void checkInputs() {
        if (Keyboard.isKeyDown(Keyboard.KEY_W))
            accelerate();
        else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            if (this.currentSpeed > 0 || inTheAir())   //check if we need to stop
                stop();
            else {   //now we're decelerating, so we need a lower speed increase
                decelerate();
            }
        } else {
            applyInertia();
        }
        if ((currentSpeed > 20 && !inTheAir()) || inTheAir()) { //take-off when on the ground, steer while in the air
            if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD8)) {
                if (getRotation().getXRotation() > -90)
                    xRotation = -0.5f;
                if (getRotation().getXRotation() < -90) getRotation().setXRotation(-90);
            } else if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD2)) {
                if (getRotation().getXRotation() < 90)
                    xRotation = 0.5f;
                if (getRotation().getXRotation() > 90) getRotation().setXRotation(90);
            } else {
                xRotation = 0;
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            rollRight();
        } else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            rollLeft();
        } else {
            suppressRoll();
        }
        if (inTheAir() && currentSpeed == 0) {
            fallDown();
        }
    }

    private void suppressRoll() {
        change = TURN_ACCELERATION * 1.8f * DisplayManager.getFrameTimeSeconds();
        if (this.currentTurnSpeed - change > 0 && currentTurnSpeed > 0) {
            this.currentTurnSpeed -= change;
            if(inTheAir()){
                if (getRotation().getZRotation() > -90)
                    zRotation = 0.8f * getRotation().getZRotation() / (-90) + 0.2f;
                if (getRotation().getZRotation() < -90) getRotation().setZRotation(-90);
            }
        } else if (this.currentTurnSpeed - change < 0 && currentTurnSpeed < 0) {
            this.currentTurnSpeed += change;
            if(inTheAir()) {
                if (getRotation().getZRotation() < 90)
                    zRotation = -0.8f * getRotation().getZRotation() / 90 - 0.2f;
                if (getRotation().getZRotation() > 90) getRotation().setZRotation(90);
            }
        } else {
            this.currentTurnSpeed = 0;
            if(inTheAir()) {
                if (getRotation().getZRotation() > 0)
                    zRotation = -0.8f * getRotation().getZRotation() / 90 - 0.2f;
                else if (getRotation().getZRotation() < 0)
                    zRotation = 0.8f * getRotation().getZRotation() / (-90) + 0.2f;
                else {
                    zRotation = 0;
                    getRotation().setZRotation(0);
                }
            }
        }

    }

    private void applyInertia() {
        if (inTheAir()) {
            inertiaInAir();
        } else inertiaOnGround();
    }

    private void rollRight() {
        change = TURN_ACCELERATION * DisplayManager.getFrameTimeSeconds();
        if (this.currentTurnSpeed > -TURN_SPEED) {
            this.currentTurnSpeed -= change;
        }
        if(inTheAir()){
            if (getRotation().getZRotation() < 90)
                zRotation = -currentTurnSpeed / 40;
            else {
                getRotation().setZRotation(90);
                zRotation = 0;
            }
        }
    }

    private void rollLeft() {
        change = TURN_ACCELERATION * DisplayManager.getFrameTimeSeconds();
        if (this.currentTurnSpeed + change < TURN_SPEED) {
            this.currentTurnSpeed += change;
        }
        if(inTheAir()){
            if (getRotation().getZRotation() > -90) {
                zRotation = -currentTurnSpeed / 40;
            } else {
                getRotation().setZRotation(-90);
                zRotation = 0;
            }
        }
    }

    private void fallDown() {
        if (getPosition().y > 0.1f) {
            pitch = GRAVITY * DisplayManager.getFrameTimeSeconds();
            if (getRotation().getXRotation() < 90) xRotation = (getRotation().getXRotation() / 90) * -0.8f;
            else {
                xRotation = 0;
                getRotation().setXRotation(90);
            }
        } else {
            pitch = 0;
            getPosition().setY(0);
            getRotation().setXRotation(0);
        }
    }

    private boolean inTheAir() {
        return getPosition().y > 0;
    }

    private void inertiaInAir() {
        change = INERTIA * DisplayManager.getFrameTimeSeconds();
        if (this.currentSpeed - change > 0)
            this.currentSpeed -= change;
        else {
            this.currentSpeed = 0;
        }
    }

    private void accelerate() {
        change = currentAcceleration * DisplayManager.getFrameTimeSeconds();
        if (inTheAir() && currentSpeed + change > 0.01f) {
            if (this.currentSpeed + change < FLY_SPEED * 3)
                this.currentSpeed += change;
        } else if (!inTheAir()) {
            if (this.currentSpeed + change < FLY_SPEED)
                this.currentSpeed += change;
        } else {
            currentSpeed = 0;
        }
    }

    private void decelerate() {
        change = DECELERATION * DisplayManager.getFrameTimeSeconds();
        if (this.currentSpeed - change > -FLY_SPEED / 2)
            this.currentSpeed -= change;
    }

    private void stop() {
        change = BREAK * DisplayManager.getFrameTimeSeconds();
        if (this.currentSpeed - change > 0)
            this.currentSpeed -= change;
        else {
            this.currentSpeed = 0;
        }
    }

    private void inertiaOnGround() {
        if (this.currentSpeed > 0) {
            change = INERTIA * 2 * DisplayManager.getFrameTimeSeconds();
            if (this.currentSpeed - change > 0.3f)
                this.currentSpeed -= change;
            else {
                this.currentSpeed = 0;
            }
        } else {
            change = INERTIA * 2 * DisplayManager.getFrameTimeSeconds();
            if (this.currentSpeed + change < 0)
                this.currentSpeed += change;
            else {
                this.currentSpeed = 0;
            }
        }
    }
}
