//GameFrame class providing key events and updating a hashmap for reading keystate in the game loop
import org.jbox2d.common.Vec2;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameFrame extends JFrame implements KeyListener {
    private InputManager inputManager = new InputManager();

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        //pass
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        Boolean set = inputManager.KeyStates.replace(keyEvent.getKeyCode(), true);
        if (set == null) {
            inputManager.KeyStates.put(keyEvent.getKeyCode(), true);
        }
        //Add keyCode to HashMap as true if key not found
        //else set value to true
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        inputManager.KeyStates.replace(keyEvent.getKeyCode(), false);
        //set keyCode mapping value to false
    }

    GameFrame() {
        addKeyListener(this);
        //Listen for key events
    }

    public void setWindowSize(Vec2 size) {
        setSize((int)size.x,(int)size.y);
    }
}
