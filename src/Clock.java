
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by Eugene Kennedy on 4/28/2016.
 *
 * Java and Web Design HW5
 * Create a clock with an image as a face. Draw a line for minute, hour and second and set up a thread to update the clock every 100 ms.
 *
 */
public class Clock {


    public Clock()
    {
        JFrame panel = new JFrame();
        panel.setSize(800,800);

        ClockGraphics graphics = new ClockGraphics();
        panel.add(graphics);

        UpdateThread t = new UpdateThread(graphics);
        t.start();

        panel.setVisible(true);
    }

}

class UpdateThread extends Thread
{
    
    ClockGraphics clock;
    
    public UpdateThread(ClockGraphics g)
    {
        super();
        clock = g;
    }
    
    public void run()
    {
        while(true)
        {
            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            clock.moveArms(); //Evey 100ms tell clock to update
        }
    }
    
}

class ClockGraphics extends JPanel
{


    BufferedImage clockFace = null;

    public ClockGraphics()
    {
        super();
        try {
            clockFace = ImageIO.read(new File("clockFace.jpg")); //Just use clock face image so I don't have to draw it. Included in the zip file, add path as needed
        } catch (IOException e) {
            System.err.println("Couldn't retrieve image. Clock will not have a face.");
        }
    }


    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        //Draw the image to scale.
        g.drawImage(clockFace, 0, 0, this.getWidth(), this.getHeight(), null);

        Calendar c = Calendar.getInstance();

        //Get the current times in 0-12:0-60:0-60 format.
        int hour = c.get(Calendar.HOUR);
        int minute = c.get(Calendar.MINUTE);
        int sec = c.get(Calendar.SECOND);

        float hourAngle = hour*30-90; //Angle in degrees
        double minuteAngle = minute*6-90;
        double secAngle = sec*6-90; //Need to sub 90 as unit circle is centered around 3 on the clock.

        int centerX = getWidth()/2;
        int centerY = getHeight()/2;

        int length = 300; //Length of the arm

        int endX = centerX + (int)(Math.cos(Math.toRadians(secAngle)) * length); //Simple math to get X,Y of where we end the line
        int endY = centerY + (int)(Math.sin(Math.toRadians(secAngle)) * length);
        g.setColor(Color.RED);
        g.drawLine(centerX, centerY, endX, endY); //second hand

        length = 275;

        endX = centerX + (int)(Math.cos(Math.toRadians(minuteAngle)) * length);
        endY = centerY + (int)(Math.sin(Math.toRadians(minuteAngle)) * length);
        g.setColor(Color.BLACK);
        g.drawLine(centerX, centerY, endX, endY); //minute hand

        length = 200;

        endX = centerX + (int)(Math.cos(Math.toRadians(hourAngle)) * length);
        endY = centerY + (int)(Math.sin(Math.toRadians(hourAngle)) * length);

        g.drawLine(centerX, centerY, endX, endY); //Hour hand
    }

    public void moveArms() {
        repaint();
    } //Just a simple method that repaints the clock whenever the clock thread tells it to
}
