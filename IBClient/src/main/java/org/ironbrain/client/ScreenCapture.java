package org.ironbrain.client;

import com.sun.awt.AWTUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

public class ScreenCapture extends JFrame implements MouseMotionListener, MouseListener {
    private int mX, mY;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Robot robot;
    BufferedImage rawImage;
    Rectangle selectRec = new Rectangle(-1, -1, -1, -1);
    Consumer<BufferedImage> onGet;

    public ScreenCapture(Consumer<BufferedImage> onGet) {
        this.onGet = onGet;

        try {
            robot = new Robot();

            setUndecorated(true);
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            AWTUtilities.setWindowOpaque(this, false);
            addMouseMotionListener(this);

            setAlwaysOnTop(true);

            new Timer(10, event -> onGlobalMouseMove()).start();
            hideCursor();

            rawImage = robot.createScreenCapture(new Rectangle(0, 0, screenSize.width, screenSize.height));
            addMouseListener(this);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        //Not used
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        //Not used
    }

    public void onGlobalMouseMove() {
        mX = MouseInfo.getPointerInfo().getLocation().x;
        mY = MouseInfo.getPointerInfo().getLocation().y;
        repaint();
    }

    public void paint(Graphics g) {
        super.paint(g);

        //Draw screen copy
        g.drawImage(rawImage, 0, 0, null);

        //Draw cross lines
        g.setColor(Color.BLUE);
        g.drawLine(mX, 0, mX, screenSize.height);
        g.drawLine(0, mY, screenSize.width, mY);

        //Draw rect
        g.setColor(Color.BLACK);
        if (selectRec.getX() != -1) {
            int x, y;
            if (mX < selectRec.getX()) {
                x = mX;
            } else {
                x = (int) selectRec.getX();
            }
            if (mY < selectRec.getY()) {
                y = mY;
            } else {
                y = (int) selectRec.getY();
            }

            g.drawRect(x, y, Math.abs(mX - (int) selectRec.getX()), Math.abs(mY - (int) selectRec.getY()));
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        //Left click - exit
        if (e.getButton() == MouseEvent.BUTTON3) {
            setVisible(false);
            dispose();
        }
        if (e.getButton() == MouseEvent.BUTTON1) {
            selectRec.setLocation(mX, mY);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int height = Math.abs(mY - (int) selectRec.getY());
        int width = Math.abs(mX - (int) selectRec.getX());

        //Select left x and left y
        int lX;
        if (mX > selectRec.x) {
            lX = selectRec.x;
        } else {
            lX = mX;
        }

        int lY;
        if (mY > selectRec.y) {
            lY = selectRec.y;
        } else {
            lY = mY;
        }

        BufferedImage image = rawImage.getSubimage(lX, lY, width, height);
        onGet.accept(image);

        setVisible(false);
        dispose();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    public void hideCursor() {
        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorImg, new Point(0, 0), "blank cursor");

        setCursor(blankCursor);
    }
}
