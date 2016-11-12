/**
 * Class that's creates and handling all the aspects of the paddles.
 * Here you can set the image of the paddles.
 * Methods for spawning and rendering the paddles,
 * also for moving it.
 */

package game.pong;

import game.Settings;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by erica on 2016-10-27.
 */
public class Paddle extends Settings {

    //Instance variables
    int paddleNumber;
    public int x, y, width = 50, height = this.getPaddleHeight(); //Set position and size of the paddles.
    int score;


    private BufferedImage paddleImage = ImageIO.read(Class.class.getResourceAsStream("/resources/paddle.png")); //Image buffer

    //Declaring size and starting position of the paddles.
    public Paddle(Pong pong, int paddleNumb) throws IOException {

        this.paddleNumber = paddleNumb;
        this.score = 0;
        this.x = 0;
        this.y = 0;

        if (paddleNumber == 1) {
            this.x = 0;
        } else if (paddleNumber == 2) {
            this.x = pong.width - width;
        }
        this.y = pong.height / 2 - this.height / 2;
    }

    //Rendering the paddles.
    void render(Graphics g) {
        g.drawImage(paddleImage, x, y, this.width, this.height, null);
    }

    //Setting sped and moving called paddle according to the speed.
    void move(boolean up) {
        int speed = 13; //Speed is according to the game timer.

        if (up) {
            if (y - speed > 0) {
                y -= speed;
            } else {
                y = 0;
            }
        } else {
            int pongHeight = 1000;
            if (y + this.height + speed < pongHeight) {
                y += speed;
            } else {
                y = pongHeight - this.height;
            }
        }
    }
}
