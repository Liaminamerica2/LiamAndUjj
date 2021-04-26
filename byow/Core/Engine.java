package byow.Core;

import byow.InputDemo.InputSource;
import byow.InputDemo.KeyboardInputSource;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;


public class Engine {
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 80;
    public static long seed = 0;


    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        /*
            your program must have a menu screen that has
            New World (N)
                When entering New World, the user should enter an integer seed followed by the S key. The world should be generated and displayed.
                The UI should show the numbers entered so far when the user is giving the seed
            Load (L)
            Quit (Q)
            options, navigable by the keyboard, which are all case insensitive.
         */
        InputSource inputSource;
        inputSource = new KeyboardInputSource();

        TileWorld world = new TileWorld(WIDTH, HEIGHT);
        world.randomlyGenerateWold();

        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        while (inputSource.possibleNextInput()) {
            char c = inputSource.getNextKey();

            if (((Character) c).equals('q') || ((Character) c).equals('Q')) {
                break;
            }
            world = interactWithKeyboardHelper(c, world);
            ter.renderFrame(world.getTiles());
        }
    }

    private TileWorld interactWithKeyboardHelper(char input, TileWorld world) {
        switch (input) {
            case ('w'):
            case ('W'):
                world.player.move(1);
        }
        return world;
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, both of these calls:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        TileWorld world = new TileWorld(WIDTH, HEIGHT);
//        world.randomlyGenerateWold();
        TETile[][] ret = interactWithInputStringHelper(input, world).getTiles();
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(ret);
        return world.getTiles();
    }

    private TileWorld interactWithInputStringHelper(String input, TileWorld world) {
        if (input.length() == 0) {
            return world;
        }
        char enteredChar = input.charAt(0);
        switch (enteredChar) {
            case 'n':// 5197880843569031643
                seed = Long.parseLong(input.substring(input.indexOf("n") + 1, input.indexOf("s")));
                world.randomlyGenerateWold();
                return interactWithInputStringHelper(input.substring(input.indexOf("s") + 1), world);
            case 'd':
            case 'D':
                world.player.move(0, world);
                break;
            case 'w':
            case 'W':
                world.player.move(1, world);
                break;
            case 'a':
            case 'A':
                world.player.move(2, world);
                break;
            case 's':
            case 'S':
                world.player.move(3, world);
                break;
        }
        makeTurns(world);
        return interactWithInputStringHelper(input.substring(1), world);
    }

    private void makeTurns(TileWorld world) {
        for (Enemy i : world.enemies) {
            i.makeTurn(world);
        }
    }
}
