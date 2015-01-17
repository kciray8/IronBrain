package org.ironbrain.client;

import com.tulskiy.keymaster.common.Provider;
import org.glassfish.tyrus.server.Server;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.websocket.DeploymentException;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Main implements ClipboardOwner {
    public static final String SCREEN_SHOT_FILE_FORMAT = "jpg";

    int port = 9993;//Default port

    public static void main(String[] args) throws Throwable {
        EventQueue.invokeLater(()-> new Main(args));
    }

    public Main(String[] args) {
        if (args.length != 0) {
            port = Integer.valueOf(args[0]);
        }

        //URL like ws://localhost:9993/websockets/ib
        Server server = new Server("localhost", port, "/websockets", null, IBServerEndpoint.class);

        try {
            server.start();
            createTray();
            registerGlobalHotkeys();
        } catch (DeploymentException exc) {
            JOptionPane.showMessageDialog(null, String.format("Port %d already in use", port));
        } catch (AWTException | IOException e) {
            e.printStackTrace();
        }
    }

    private void registerGlobalHotkeys() {
        Provider provider = Provider.getCurrentProvider(true);

        provider.register(KeyStroke.getKeyStroke("control PRINTSCREEN"), key ->{
            captureScreen(null);
        });
    }

    private void createTray() throws IOException, AWTException {
        if (SystemTray.isSupported()) {
            Image image = ImageIO.read(getClass().getResource("/gear.png"));

            final PopupMenu popup = new PopupMenu();
            final TrayIcon trayIcon = new TrayIcon(image);
            final SystemTray tray = SystemTray.getSystemTray();

            MenuItem portInfo = new MenuItem("Port - " + port);
            popup.add(portInfo);
            popup.addSeparator();

            MenuItem captureScreenItem = new MenuItem("Capture screen (Ctrl + PrtScr)");
            captureScreenItem.addActionListener(this::captureScreen);
            popup.add(captureScreenItem);
            popup.addSeparator();

            MenuItem exitItem = new MenuItem("Exit");
            exitItem.addActionListener(event -> {
                System.exit(0);
            });

            popup.add(exitItem);

            trayIcon.setPopupMenu(popup);

            tray.add(trayIcon);

            /*
            trayIcon.displayMessage("Client successfully launched",
                    "Web socket port - " + port,
                    TrayIcon.MessageType.INFO);*/
        }
    }

    private void captureScreen(ActionEvent e) {
        ScreenCapture screenCapture = new ScreenCapture(img -> {
            try {
                saveImage(img);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        screenCapture.setVisible(true);
    }

    private void saveImage(BufferedImage bufferedImage) throws IOException {
        TransferableImage trans = new TransferableImage(bufferedImage);
        Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();

        c.setContents(trans, this);
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {

    }
}