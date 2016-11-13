/**
 * The game engine!
 * This class is handling all the events in the game.
 * Declaring and painting the game field, spawns the paddles and the ball.
 * Handling key events and key presses to move the paddles and pause the game.
 */

package game.pong;

import game.Settings;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;


public class Pong extends Settings implements ActionListener, KeyListener {

    //Instance variables
    int width = 1000, height = 1000; // The size of the play field
    private int gameStatus = 2;  //1 = Paused, 2 = Playing, 3 = Over
    private int scoreLimit = 5;
    private int botDifficulty = this.getBotDifficulty(), botMoves, botCooldown = 0;

    private Renderer renderer;

    private Paddle player1;
    private Paddle player2;

    private String playerOneName;
    private String playerTwoName;
    private String playerWon;

    private Ball ball;

    private boolean bot;
    private boolean w, s, up, down;

    private BufferedImage background = ImageIO.read(Class.class.getResourceAsStream("/resources/background.png ")); //Background image buffer.


    public Pong() throws IOException, UnsupportedAudioFileException, LineUnavailableException {

        Timer timer = new Timer(10, this); //Setting the timer (game speed).

        //Rendering the main game frame.
        JFrame jframe = new JFrame("SpacePong");
        renderer = new Renderer();
        jframe.setSize(width + 5, height + 27);
        jframe.setResizable(false);
        jframe.setVisible(true);
        jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jframe.add(renderer);
        jframe.addKeyListener(this);
        jframe.setLocationRelativeTo(null);

        //Collecting game data from the settings class.
        bot = this.getBotEnabled();
        scoreLimit = this.getScoreLimit();
        playerOneName = this.getPlayerOneName();
        playerTwoName = this.getPlayerTwoName();

        //Starts the timer and the game.
        timer.start();
        start();

        //Creating a sound clip for the background music.
        //Adjusting the volume and then starts the sound.
        Clip gameSound = AudioSystem.getClip();
        URL url = this.getClass().getResource("/resources/game-sound.wav");
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
        gameSound.open(audioIn);
        FloatControl gainControl = (FloatControl) gameSound.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(-15.0f);
        gameSound.start();
    }

    //Settings the game status to 2, and declaring the players and the ball.
    private void start() throws IOException, UnsupportedAudioFileException {
        gameStatus = 2;
        player1 = new Paddle(this, 1);
        player2 = new Paddle(this, 2);

        ball = new Ball(this);

    }

    //Checking if a players score is bigger then the score limit, if it is it's starting the score screen.
    //Other vise it is checking if key is pressed and calls the according move method for that player.
    private void update() throws IOException, LineUnavailableException {
        if (player1.score >= scoreLimit) {
            playerWon = playerOneName;
            gameStatus = 3;
        }
        if (player2.score >= scoreLimit) {
            playerWon = playerTwoName;
            gameStatus = 3;
        }
        if (w) {
            player1.move(true);
        }
        if (s) {
            player1.move(false);
        }
        if (!bot) {
            if (up) {
                player2.move(true);
            }
            if (down) {
                player2.move(false);
            }
        } else {
            if (botCooldown > 0) {
                botCooldown--;
                if (botCooldown == 0) {
                    botMoves = 0;
                }
            }
            if (botMoves < 10) {
                if (player2.y + player2.height / 2 < ball.y) {
                    player2.move(false);
                    botMoves++;
                }
                if (player2.y + player2.height / 2 > ball.y) {
                    player2.move(true);
                    botMoves++;
                }
                if (botDifficulty == 0) {
                    botCooldown = 30;
                }
                if (botDifficulty == 1) {
                    botCooldown = 20;
                }
                if (botDifficulty == 2) {
                    botCooldown = 15;
                }
            }
        }
        ball.update(player1, player2);
    }

    //Rendering the game frame according to the game status.
    //Rendering players name and score on the play field and the winner in the score screen.
    private void render(Graphics2D g) {
        g.drawImage(background, 0, 0, null);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //If game paused, rendering pause screen.
        if (gameStatus == 1) {
            g.setColor(Color.orange);
            g.setFont(new Font("Arial", 1, 50));
            g.drawString("PAUSED", width / 2 - 103, height / 2 - 25);
        }

        //Rendering play field when game is running.
        if (gameStatus == 1 || gameStatus == 2) {
            g.setColor(Color.ORANGE);

            //Player (or bot) name tag and score.
            g.setFont(new Font("Arial", Font.BOLD, 35));
            g.drawString(String.valueOf(player1.score), width / 4 - 50, 100);
            g.drawString(String.valueOf(player2.score), width / 2 + 250, 100);
            g.drawString(playerOneName, width / 4 - 100, 50);
            if (bot) {
                g.drawString("Bot", width / 2 + 230, 50);
            } else {
                g.drawString(playerTwoName, width / 2 + 200, 50);
            }
            player1.render(g);
            player2.render(g);
            ball.render(g);
        }

        //Score screen and player winning.
        if (gameStatus == 3) {
            g.setColor(Color.orange);
            g.setFont(new Font("Arial", 1, 50));

            if (bot && playerWon.equals(playerTwoName)) {
                g.drawString("The Bot Wins!", width / 2 - 170, 200);
            } else {
                g.drawString(playerWon + " Wins!", width / 2 - 150, 200);
            }

            g.setFont(new Font("Arial", 1, 30));
            g.drawString("Press Space to Play Again", width / 2 - 185, height / 2 - 25);
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameStatus == 2) try {
            update();
        } catch (IOException | LineUnavailableException e1) {
            e1.printStackTrace();
        }
        renderer.repaint();
    }

    //Key listener method.
    @Override
    public void keyPressed(KeyEvent e) {
        int id = e.getKeyCode();

        //Sets W/S/UP/DOWN to true, to move the players paddles.
        if (id == KeyEvent.VK_W) {
            w = true;
        } else {
            if (id == KeyEvent.VK_S) {
                s = true;
            } else {
                if (id == KeyEvent.VK_UP) {
                    up = true;
                } else {
                    if (id == KeyEvent.VK_DOWN) {
                        down = true;
                    }
                    //If in game press space to pause the game, other vise start the game again
                    else {
                        if (id == KeyEvent.VK_SPACE) {
                            if (gameStatus == 0 || gameStatus == 3) {
                                try {
                                    start();
                                } catch (IOException | UnsupportedAudioFileException e1) {
                                    e1.printStackTrace();
                                }
                            }
                            else if (gameStatus == 1) {
                                gameStatus = 2;
                            } else if (gameStatus == 2) {
                                gameStatus = 1;
                            }
                        }
                    }
                }
            }
        }
    }

    //Set keys to false, to stop players paddles to move.
    @Override
    public void keyReleased(KeyEvent e) {
        int id = e.getKeyCode();

        if (id == KeyEvent.VK_W) {
            w = false;
        } else {
            if (id == KeyEvent.VK_S) {
                s = false;
            } else {
                if (id == KeyEvent.VK_UP) {
                    up = false;
                } else if (id == KeyEvent.VK_DOWN) {
                    down = false;
                }
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    //The rendering class that extends 2D graphics for everything in the game.
    private class Renderer extends JPanel {
        private static final long serialVersionUID = 1l;

        //Method to paint an object.
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            render((Graphics2D) g);
        }
    }
}