/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smartaccount;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.Dimension;
import javafx.scene.layout.Pane;

/**
 *
 * @author ANKIT
 */
public class SmartAccount extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("SmartAccount");
        FXMLLoader myloader = new FXMLLoader(getClass().getResource("FXMLMain.fxml"));
        Pane mypane = myloader.load();
        FXMLMainController maincontroller = myloader.getController();
        maincontroller.setprevstage(stage);
        Scene scene = new Scene(mypane);
        stage.getIcons().add(new Image(LayoutSample.class.getResourceAsStream(
                            "icon.png")));
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
        mypane.setStyle("-fx-background-image: url(file:///C://Users//ANKIT//Documents//NetBeansProjects//SmartAccount//assets//img//loginbackground.jpg)");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        splashInit();           // initialize splash overlay drawing parameters
        appInit();              // simulate what an application would do 
                                // before starting
        if (mySplash != null)   // check if we really had a spash screen
            mySplash.close();   // if so we're now done with it
        launch(args);
    }
    static SplashScreen mySplash = SplashScreen.getSplashScreen();
    static Graphics2D splashGraphics;
    static Rectangle2D splashTextArea;
    static Rectangle2D splashProgressArea;
    public static void splashInit()
    {
        
        if (mySplash != null)
        {   // if there are any problems displaying the splash this will be null
            Dimension ssDim = mySplash.getSize();
            int height = ssDim.height;
            int width = ssDim.width;
            // stake out some area for our status information
            splashTextArea = new Rectangle2D.Double(15., height*0.85, width * .45, 18.);
            splashProgressArea = new Rectangle2D.Double(width * .55, height*.85, width*.4, 18 );

            // create the Graphics environment for drawing status info
            splashGraphics = mySplash.createGraphics();
            Font font = new Font("Dialog", Font.PLAIN, 12);
            splashGraphics.setFont(font);
            
            // initialize the status info
            splashText("Starting");
            splashProgress(0);
        }
    }
    
    public static void splashText(String str)
    {
        if (mySplash != null && mySplash.isVisible())
        {   // important to check here so no other methods need to know if there
            // really is a Splash being displayed

            // erase the last status text
            splashGraphics.setPaint(Color.BLACK);
            splashGraphics.fill(splashTextArea);

            // draw the text
            splashGraphics.setPaint(Color.WHITE);
            splashGraphics.drawString(str, (int)(splashTextArea.getX() + 10),(int)(splashTextArea.getY() + 15));

            // make sure it's displayed
            mySplash.update();
        }
    }
    
    public static void splashProgress(int pct)
    {
        if (mySplash != null && mySplash.isVisible())
        {

            // Note: 3 colors are used here to demonstrate steps
            // erase the old one
            splashGraphics.setPaint(Color.BLACK);
            splashGraphics.fill(splashProgressArea);

            // draw an outline
            splashGraphics.setPaint(Color.BLACK);
            splashGraphics.draw(splashProgressArea);

            // Calculate the width corresponding to the correct percentage
            int x = (int) splashProgressArea.getMinX();
            int y = (int) splashProgressArea.getMinY();
            int wid = (int) splashProgressArea.getWidth();
            int hgt = (int) splashProgressArea.getHeight();

            int doneWidth = Math.round(pct*wid/100.f);
            doneWidth = Math.max(0, Math.min(doneWidth, wid-1));  // limit 0-width

            // fill the done part one pixel smaller than the outline
            splashGraphics.setPaint(Color.GRAY);
            splashGraphics.fillRect(x, y+1, doneWidth, hgt-1);

            // make sure it's displayed
            mySplash.update();
        }
    }
    
    public static void appInit()
    {
        for(int i=1;i<=10;i++)
        {
            int pctDone = i * 10;
            splashText("Loading.....");
            splashProgress(pctDone);
            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException ex)
            {
                // ignore it
            }
        }
    }
    
}
