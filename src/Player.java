//Player class handling player movement, Body & lives

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

import java.util.HashMap;

public class Player {
    private static BodyImage shipImage = new BodyImage("data/Ship.png", 2f);
    public StaticBody body;
    private InputManager input;
    private static float speed = 25f;
    private int lives = 5;
    private long lastFireTime = 0;
    private World world;

    Player(World world) {
        body = new StaticBody(world, new BoxShape(1f,1f));
        body.addImage(shipImage);
        body.setPosition( new Vec2(25f,5f));
        input = new InputManager();
        this.world = world;
        //store ref to the game world obj
    }

    public void hitObstacle() {
        //subtract a life upon hitting an obstacle
        lives -= 1;
        System.out.println("Hit an obstacle, lives: " + lives);
    }

    public int getLives() {
        return lives;
    }

    public void restart() {
        body.setPosition(new Vec2(25f,5f));
        lives = 5;
    }

    public void Update(float dT) {
        HashMap<Integer, Boolean> KeyStates = input.KeyStates;

        if (KeyStates.getOrDefault(37, false)) {
            //if LEFT_ARROW pressed
            body.move(new Vec2(-speed * dT, 0));
            //frame-rate consistent movement rate (pixel speed @16fps == pixel speed @64fps)
        }
        if (KeyStates.getOrDefault(39, false)) {
            //if RIGHT_ARROW pressed
            body.move(new Vec2(speed * dT, 0));
        }
        if (KeyStates.getOrDefault(32, false)) {
            //if SPACE_BAR pressed
            if (System.currentTimeMillis() - lastFireTime > 1000) {
                lastFireTime = System.currentTimeMillis();
                new Bullet(world,this);
            }
        }

        Vec2 pos = body.getPosition();

        float clampedX = Math.min(49f, Math.max(1f, pos.x));
        body.setPosition(new Vec2(clampedX, pos.y));
        //clamp body to viewport dimensions
    }
}
