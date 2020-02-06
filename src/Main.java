import city.cs.engine.*;
import org.jbox2d.common.Vec2;

import javax.swing.JFrame;
import java.awt.Color;

import java.util.ArrayList;
import java.util.Random;

public class Main {
    public static long startTime;
    private static int score;

    public static void main(String[] args) {
        startTime = System.currentTimeMillis();

        Random random = new Random();

        World world = new World();

        UserView view = new UserView(world, 500, 500);
        view.setZoom(10);
        view.setCentre( new Vec2(25f,25f) );
        view.setBackground(new Color(0,0,0));
        //Center to window size and zoom params

        GameFrame gameFrame = new GameFrame();
        gameFrame.setTitle("Simple Game Example");
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setLocation(200,200);
        gameFrame.add(view);
        gameFrame.setResizable(false);
        gameFrame.pack();
        gameFrame.setVisible(true);
        //window params

        Player player = new Player(world);
        ArrayList<DynamicBody> obstacles = new ArrayList<DynamicBody>();


        {
            StepListener s = new StepListener() {
                @Override
                public void preStep(StepEvent stepEvent) {
                    //pass
                }

                @Override
                public void postStep(StepEvent stepEvent) {
                    float dT = stepEvent.getStep();
                    int frameObstacleCap = (int)((System.currentTimeMillis() - startTime) / 1000);
                    //calculate difficulty based on time passed

                    player.Update(dT);
                    //call player Update void for handling player logic in player Class

                    if (player.getLives() <= 0) {
                        System.out.println("You lost, starting again!");
                        startTime = System.currentTimeMillis();
                        player.restart();

                        for (DynamicBody obstacle: obstacles) {
                            obstacle.destroy();
                        }
                        obstacles.clear();
                        //if player is out of lives, reset difficulty, player position and score
                        System.out.println("Your score was: "+score);
                        score = 0;
                        //reset score after output
                    }

                    if (obstacles.size() < frameObstacleCap) {
                        DynamicBody newObstacle = new DynamicBody(world, new BoxShape(1f, 1f));

                        float randX = random.nextFloat() * 50f;
                        newObstacle.setPosition(new Vec2(randX, 50f));
                        //spawn random horizontal position at top of screen

                        newObstacle.setGravityScale(0);
                        newObstacle.setLinearVelocity(new Vec2(0, -10f));
                        //fall at constant rate

                        CollisionListener obstacleCollision = new CollisionListener() {
                            @Override
                            public void collide(CollisionEvent collisionEvent) {
                                if (collisionEvent.getOtherBody() == player.body) {
                                    //hit player
                                    player.hitObstacle();
                                    obstacles.remove(newObstacle);
                                    newObstacle.destroy();
                                    //if it hits the player, destroy the obstacle and reduce player's lives
                                }
                                else if(collisionEvent.getOtherBody() instanceof Bullet) {
                                    obstacles.remove(newObstacle);
                                    newObstacle.destroy();
                                    collisionEvent.getOtherBody().destroy();
                                    //if it hits a bullet, destroy the bullet and the obstacle
                                    //shooting an obstacle destroys it before it reaches the bottom, so it won't add score
                                }
                            }
                        };
                        newObstacle.addCollisionListener(obstacleCollision);

                        obstacles.add(newObstacle);
                    }

                    ArrayList<DynamicBody> passedObstacles = new ArrayList<DynamicBody>();
                    for (DynamicBody obstacle: obstacles) {
                        if (obstacle.getPosition().y < 0) {
                            passedObstacles.add(obstacle);
                        }
                    }
                    obstacles.removeAll(passedObstacles);
                    for (DynamicBody passedObstacle: passedObstacles) {
                        passedObstacle.destroy();
                        score += 1;
                    }
                    //collect all passed obstacles and clean them up (destroy and remove list nodes)
                    //add points for all passed obstacles
                }
            };
            world.addStepListener(s);
        }//StepListener main loop hook

        world.start();
    }
}
