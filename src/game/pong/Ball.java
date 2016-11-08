/**
 * Class that's creates and handling all the aspects of the ball.
 * Here you can set the ball image and ball sound.
 * Different methods for spawning the ball and making it move,
 * also methods for calculating collisions of walls and paddles.
 */

package game.pong;

import game.Settings;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

/**
 * Created by erica on 2016-10-27.
 */
public class Ball extends Settings {

    //Instance variables.
    int x, y;
    private int width = this.getBallSize(), height = this.getBallSize(); //Ball position and size.
    private int motionX, motionY;
    private int amountOfHits;
    private int speed = this.getBallSpeed();

    private Random random;
    private Pong pong;
    private BufferedImage ballImg = ImageIO.read(Class.class.getResourceAsStream("/resources/ball-red.png")); //Image buffer
    private Clip ballSound;

    //Declare and spawn the ball.
    public Ball(Pong pong) throws IOException, UnsupportedAudioFileException {
        this.pong = pong;
        this.random = new Random();

        spawn();
    }

    //Ball motion and checking if the ball is going past a paddle, and then adding score.
    void update(Paddle paddle1, Paddle paddle2) throws IOException, LineUnavailableException {

        this.x += motionX * speed;
        this.y += motionY * speed;

        if (this.y + height - motionY > pong.height || this.y + motionY < 0) {
            if (this.motionY < 0) {
                this.y = 0;
                this.motionY = random.nextInt(3);

                if (motionY == 0) {
                    motionY = 1;
                }

            } else {
                this.motionY = -random.nextInt(3);
                this.y = pong.height - height;

                if (motionY == 0) {
                    motionY = -1;
                }
            }
        }
        //Paddle 1 collision test.
        //If collision change direction, and add 1 to collision counter.
        if (checkCollision(paddle1) == 1) {
            this.motionX = 1 + (amountOfHits / 5);
            this.motionY = -2 + random.nextInt(3);
            if (motionY == 0) {
                motionY = 1;
            }
            amountOfHits++;
        } else if (checkCollision(paddle2) == 1) {
            this.motionX = -1 - (amountOfHits / 5);
            this.motionY = -2 + random.nextInt(3);
            if (motionY == 0) {
                motionY = 1;
            }
            amountOfHits++;
        }
        //Paddle 2 collision test.
        //If collision change direction, and add 1 to collision counter.
        if (checkCollision(paddle1) == 2) {
            paddle2.score++;
            spawn();
        } else if (checkCollision(paddle2) == 2) {
            paddle1.score++;
            spawn();
        }
    }

    //Spawn the ball at the beginning of the game and set a random start motion.
    private void spawn() {
        this.amountOfHits = 0;
        this.x = pong.width / 2 - this.width / 2;
        this.y = pong.height / 2 - this.height / 2;

        this.motionY = -2 + random.nextInt(2);

        if (motionY == 0) {
            motionY = 1;
        }
        if (random.nextBoolean()) {
            motionX = 1;
        } else {
            motionX = -1;
        }
    }

    //Check if collision with paddle.
    private int checkCollision(Paddle paddle) throws IOException, LineUnavailableException {

        //Returns 1 if it is a collision with paddle and plays a collision sound.
        if (this.x < paddle.x + paddle.width && this.x + width > paddle.x && this.y < paddle.y + paddle.height && this.y + height > paddle.y) {
            this.stopBallSound();
            this.playBallSound();

            return 1; //bounce

        //Returns 2  if not a collision with paddle.
        } else if ((paddle.x > x && paddle.paddleNumber == 1) || (paddle.x < x - width && paddle.paddleNumber == 2)) {
            return 2; //score
        }
        return 0; //nothing
    }

    //Rendering the ball
    void render(Graphics g) {
        g.drawImage(ballImg, x, y, this.width, this.height, null);
    }

    //Method to play ball collision sound.
    private void playBallSound() {
        if (ballSound == null) {
            try {
                URL url = this.getClass().getResource("/resources/ball-sound.wav");
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);

                ballSound = AudioSystem.getClip();
                ballSound.open(audioIn);
                ballSound.start();

            } catch (UnsupportedAudioFileException | IOException ignored) {

            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        }
    }

    //Method to stop the collision sound if any so it can be played again.
    private void stopBallSound() {
        if (ballSound == null) {

        } else {
            ballSound.stop();
        }
        ballSound = null;
    }
}
