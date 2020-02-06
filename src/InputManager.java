//small class to wrap a static ref to the KeyStates hashmap so that every other class can access it by creating an InputManager instance.

import java.util.HashMap;

public class InputManager {
    public static HashMap<Integer,Boolean> KeyStates = new HashMap<Integer,Boolean>();
}
//at any time an InputManager can be created to get ref to the static HashMap that tracks Key states.
//See GameFrame for key event handling