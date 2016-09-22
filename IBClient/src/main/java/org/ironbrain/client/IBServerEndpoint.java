package org.ironbrain.client;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.CountDownLatch;

@ServerEndpoint("/ib")
public class IBServerEndpoint {
    BufferedImage image;

    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            OutputStream sendStream = session.getBasicRemote().getSendStream();

            switch (message) {
                case "screenshot":
                    CountDownLatch latch = new CountDownLatch(1);
                    ScreenCapture screenCapture = new ScreenCapture(img -> {
                        image = img;
                        latch.countDown();
                    });
                    screenCapture.setVisible(true);
                    latch.await();

                    ImageWriter writer = ImageIO.getImageWritersByFormatName(Main.SCREEN_SHOT_FILE_FORMAT).next();
                    ImageWriteParam imgParam = writer.getDefaultWriteParam();
                    imgParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                    imgParam.setCompressionQuality(1.0F); //Highest quality

                    ImageOutputStream ios = ImageIO.createImageOutputStream(sendStream);
                    writer.setOutput(ios);
                    IIOImage iioImage = new IIOImage(image, null, null);
                    writer.write(null, iioImage, imgParam);
                    sendStream.close();

                    break;
            }
        } catch (IOException | InterruptedException e1) {
            e1.printStackTrace();
        }
    }
}