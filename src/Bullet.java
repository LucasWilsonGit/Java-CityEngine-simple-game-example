import city.cs.engine.*;
import org.jbox2d.common.Vec2;

public class Bullet extends DynamicBody {
    public Bullet(World world, Player player) {
        super(world, new BoxShape(.2f,1f));
        addImage(new BodyImage("data/Bullet.png", 2f));

        setPosition(player.body.getPosition().add(new Vec2(0f,2f)));
        setGravityScale(0f);
        setLinearVelocity(new Vec2(0f,10f));

        setAlwaysOutline(true);
    }
}
