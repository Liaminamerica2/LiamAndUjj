package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import javax.sound.sampled.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Date;

public class Engine {
    /* Feel free to change the width and height. */
    public static final int WIDTH = 50;
    public static final int HEIGHT = 50;
    public static long seed = 0;
    private int i = 0;


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
        Clip background = playSound("Background.wav");
        drawMenu();
        TileWorld world = null;
        boolean shouldBreak = false;
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                switch (StdDraw.nextKeyTyped()) {
                    case 'N':
                    case 'n':
                        int i = 0;
                        while (true) {
                            boolean shouldBreak2 = false;
                            if (StdDraw.hasNextKeyTyped()) {
                                char c = StdDraw.nextKeyTyped();
                                switch (c) {
                                    case ('s'):
                                    case ('S'):
                                        shouldBreak2 = true;
                                        shouldBreak = true;
                                        break;
                                    case ('0'):
                                    case ('1'):
                                    case ('2'):
                                    case ('3'):
                                    case ('4'):
                                    case ('5'):
                                    case ('6'):
                                    case ('7'):
                                    case ('8'):
                                    case ('9'):
                                        seed = seed * 10 + Integer.parseInt("" + c);
                                        StdDraw.textLeft(.1 + ++i * .05, 9.0 / 16, "" + c);
                                        break;
                                }
                            }
                            if (shouldBreak2) {
                                world = new TileWorld(WIDTH, HEIGHT);
                                world.randomlyGenerateWold();
                                world.getTiles();
                                break;
                            }
                        }
                        break;
                    case ('L'):
                    case ('l'):
                        world = TileWorld.externalize();
                        shouldBreak = true;
                        break;
                    default:
                        world = null;
                        break;
                }
            }
            if (shouldBreak) {
                break;
            }
        }
        background.stop();
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        boolean isOver = false;
        while (true) {
            boolean maketurn = false;
            if (StdDraw.hasNextKeyTyped()) {
                switch (StdDraw.nextKeyTyped()) {
                    case 'd':
                    case 'D':
                        world.player.move(0, world);
                        maketurn = true;
                        playSound("Move.wav");
                        break;
                    case 'w':
                    case 'W':
                        playSound("Move.wav");
                        world.player.move(1, world);
                        maketurn = true;
                        break;
                    case 'a':
                    case 'A':
                        playSound("Move.wav");
                        world.player.move(2, world);
                        maketurn = true;
                        break;
                    case 's':
                    case 'S':
                        world.player.move(3, world);
                        playSound("Move.wav");
                        maketurn = true;
                        break;
                    case ':':
                        if (StdDraw.hasNextKeyTyped()) {
                            char c = StdDraw.nextKeyTyped();
                            if (c == 'q' || c == 'Q') {
                                world.serialize();
                                System.exit(0);
                            }
                        }
                        world.serialize();
                        return;
                    default:
                        break;
                }
            }
            if (maketurn) {
                makeTurns(world);
                if (world.isPlayerDamage()) {
                    world.player.health--;
                    playSound("Damage.wav");
                }
                if (world.player.health <= 0) {
                    isOver = true;
                }
            }
            render(ter, world);
            if (isOver) {
                playSound("Over.wav");
                break;
            }
        }
        drawEndGame();
    }

    private void drawEndGame() {
        StdDraw.clear();
        StdDraw.setPenColor(Color.RED);
        StdDraw.setFont(new Font("Arial", Font.BOLD, 50));
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "GAME OVER");
        StdDraw.show();
    }

    public Clip playSound(String soundFile) {
        File f = new File("sounds" + File.separator + soundFile);
        AudioInputStream audioIn = null;
        Clip clip = null;
        try {
            audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
            clip = AudioSystem.getClip();
            clip.open(audioIn);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        clip.start();
        return clip;
    }

    private void render(TERenderer renderer, TileWorld world) {
        StdDraw.setFont();
        renderer.renderFrame(world.getTiles());
        drawUI(world);
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void drawUI(TileWorld world) {
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(new Font("Arial", Font.BOLD, 20));
        if (world.getTile((int) StdDraw.mouseX(), (int) StdDraw.mouseY()) == null) {
            return;
        }
        StdDraw.textLeft(0.35, HEIGHT - 1, world.getTile((int) StdDraw.mouseX(), (int) StdDraw.mouseY()).description());
        String health = "";
        for (int i = 0; i < world.player.health; i++) {
            health += "♥";
        }
        StdDraw.setPenColor(Color.RED);
        StdDraw.setFont(new Font("Arial", Font.BOLD, 30));
        StdDraw.text(WIDTH / 2, HEIGHT - 1, health);
        StdDraw.setPenColor(Color.white);
        StdDraw.setFont(new Font("Arial", Font.BOLD, 20));
        StdDraw.textRight(WIDTH, HEIGHT - 1, (new Date()).toString());
        StdDraw.show();
    }

    private void drawMenu() {
        StdDraw.setFont(new Font("Arial", Font.BOLD, 30));
        StdDraw.setPenColor(Color.black);
        StdDraw.text(.5, 3.0 / 4, "CS61B: THE GAME");
        StdDraw.text(.5, 1.0 / 2, "New Game (N)");
        StdDraw.text(.5, 6.0 / 16, "Load Game (L)");
        StdDraw.text(.5, 4.0 / 16, "Quit (Q)");
        StdDraw.show();
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
            case ':':
                if (input.charAt(1) != 'q' && input.charAt(1) != 'Q') {
                    break;
                }
                world.serialize();
                return world;
            case 'L':
            case 'l':
                world = TileWorld.externalize();
                world.setRectTiles();
                return interactWithInputStringHelper(input.substring(1), world);
        }

//        n7193300625454684331saaawasdaawdwsd  14
//          == true
//        n7193300625454684331saaawasdaawd:q 11
//        lwsd 3

        makeTurns(world);
        return interactWithInputStringHelper(input.substring(1), world);
    }

    private void makeTurns(TileWorld world) {
        for (Enemy i : world.enemies) {
            i.makeTurn(world);
        }
    }
}
