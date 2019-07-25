package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;

public class CanvasClass extends Application {

    Group root;
    Scene scene ;
    Canvas canvas;
    int width = 700, height = 600;
    GraphicsContext graphicsContext;
    String path = "PixelArt1.png";
    ImageView personaje;
    Image personajeZ;
    boolean goUp, goDown, goLeft, goRight, run;


    @Override
    public void start(Stage primaryStage) throws Exception {
        root  = new Group();
        scene = new Scene(root);
        personaje = new ImageView();
        FileInputStream is = new FileInputStream(path);
        personajeZ = new Image(path);
        personaje.setImage(personajeZ);
        personaje.setFitWidth(225);
        personaje.setFitHeight(225);

        personaje.setFocusTraversable(true);
        canvas = new Canvas(width, height);

        graphicsContext = canvas.getGraphicsContext2D();

        final long startNanoTime = System.nanoTime();

        new AnimationTimer(){
            public void handle(long currentNanoTime){
                int ejeX = 0, ejeY = 0;
                if(goUp) ejeY -= 5;
                if(goDown) ejeY += 5;
                if(goRight) ejeX += 5;
                if(goLeft) ejeX -=5;
                if(run) {ejeX *=3; ejeY *= 3;}

                moveHeroBy(ejeX, ejeY);
                //graphicsContext.drawImage((ImageView)personaje, x, y);

            }
        }.start();

        personaje.setOnMouseClicked(e->{
            Stage popup = new Stage();
            popup.initOwner(primaryStage);
            popup.show();
        });

        root.getChildren().addAll(canvas, personaje);
        listeners();
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    //-----------------------------------------------------------------------------------

    private void listeners(){

            scene.setOnKeyPressed(e -> {

                switch(e.getCode()){
                    case UP: goUp =true; break;
                    case DOWN: goDown = true; break;
                    case LEFT: goLeft = true; break;
                    case RIGHT: goRight = true; break;
                    case SHIFT: run = true; break;

                }
            });

            scene.setOnKeyReleased(e ->{
                switch(e.getCode()){
                    case UP: goUp =false; break;
                    case DOWN: goDown = false; break;
                    case LEFT: goLeft = false; break;
                    case RIGHT: goRight = false; break;
                    case SHIFT: run = false; break;

                }

            });

    }

    //-----------------------------------------------------------------------------------
    private void moveHeroBy(int ejeX, int ejeY){
        if(ejeX == 0 && ejeY == 0) return;

        final double cx = personaje.getBoundsInLocal().getWidth() /2;
        final double cy = personaje.getBoundsInLocal().getHeight() /2;

        double x = cx + personaje.getLayoutX() + ejeX;
        double y = cy + personaje.getLayoutY() + ejeY;

        moveHeroTo(x, y);
    }


    //-----------------------------------------------------------------------------------
    private void moveHeroTo(double x, double y){
        final double cx = personaje.getBoundsInLocal().getWidth() /2;
        final double cy = personaje.getBoundsInLocal().getHeight() /2;

        if(x - cx>=0&&
                x + cx <= width &&
                y - cy >= 0 &&
                y + cy <= height){
            personaje.relocate(x - cx, y -cy);
        }
    }


    //Main
    //-----------------------------------------------------------------------------------
    public static void main (String[] args){ launch(args); }

}
