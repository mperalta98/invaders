package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Assets;
import com.mygdx.game.Controls;

public class Ship {

    enum State {
        IDLE, LEFT, RIGHT, SHOOT, DYING, DEAD, LIVE;
    }

    Vector2 position;

    State state;
    float stateTime;
    float speed = 5;
    int lives = 3;

    TextureRegion frame;

    Weapon weapon;

    Ship(int initialPosition){
        position = new Vector2(initialPosition, 10);
        state = State.IDLE;
        stateTime = 0;

        weapon = new Weapon();
    }


    void setFrame(Assets assets){
        switch (state){
            case IDLE:
                frame = assets.naveidle.getKeyFrame(stateTime, true);
                break;
            case DYING:
                frame = assets.navedamaged.getKeyFrame(stateTime, true);
                break;
            case LEFT:
                frame = assets.naveleft.getKeyFrame(stateTime, true);
                break;
            case RIGHT:
                frame = assets.naveright.getKeyFrame(stateTime, true);
                break;
            case SHOOT:
                frame = assets.naveshoot.getKeyFrame(stateTime, true);
                break;
            default:
                frame = assets.naveidle.getKeyFrame(stateTime, true);
                break;
        }

    }

    void render(SpriteBatch batch){
        batch.draw(frame, position.x, position.y);

        weapon.render(batch);
    }

    public void update(float delta, Assets assets) {
        stateTime += delta;

        if (Controls.isLeftPressed()) {
            moveLeft();
        } else if (Controls.isRightPressed()) {
            moveRight();
        } else {
            idle();
        }

        if (Controls.isShootPressed()) {
            shoot();
            assets.shootSound.play();
        }

        // probando para matar a la nave
        if (state == State.DYING) {
            if (assets.navedamaged.isAnimationFinished(stateTime)) {
                state = State.DEAD;
            }

            setFrame(assets);

            weapon.update(delta, assets);
        }

        System.out.println(frame);
    }

    void idle(){
        state = State.IDLE;
    }

    void moveLeft(){
        position.x -= speed;
        state = State.LEFT;
    }

    void moveRight(){
        position.x += speed;
        state = State.RIGHT;
    }

    void shoot(){
        state = State.SHOOT;
        weapon.shoot(position.x +16);
    }

    void damage() {
        lives -= 1;
        if(lives == 0) {
            System.out.println("Muerto");
        }

        state = State.DYING;
        stateTime = 0;

    }

}
