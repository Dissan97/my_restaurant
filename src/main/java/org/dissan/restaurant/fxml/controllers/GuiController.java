package org.dissan.restaurant.fxml.controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.dissan.restaurant.beans.UserBean;
import org.dissan.restaurant.cli.utils.OutStream;
import org.dissan.restaurant.patterns.structural.facade.CustomerOrderFacade;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class GuiController extends Application {
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 720;
    private static final int ERROR_HEIGHT = 360;
    private static final int ERROR_WIDTH = 512;
    private static Stage mainStage;
    private static final String FXML = ".fxml";
    private static Scene previousScene;
    private static Scene currentScene;
    private static Scene homeScene;




    @Override
    public void start(@NotNull Stage stage){
        setMainStage(stage);
        mainStage.setTitle("RESTAURANT MANAGER");
        openNewWindow(Scenes.HOME_MENU);
        mainStage.setResizable(false);
    }

    public static void launchGui(String...args){
        launch(args);
    }

    public static void setMainStage(Stage mainStage) {
        GuiController.mainStage = mainStage;
    }

    public static void openNewWindow(@NotNull Scenes ui){
        openNewWindow(ui, null);
    }

    public static void openNewWindow(@NotNull Scenes ui, UserBean userBean) {
        FXMLLoader loader = new FXMLLoader(GuiController.class.getResource(ui.name() + FXML));

        try {
            Scene scene = new Scene(loader.load(), WIDTH, HEIGHT);
            currentScene = scene;
            if (userBean != null){
                setUpController(loader, userBean);
            }
            setPreviousScenes(scene, ui);
            changeScene();
        } catch (IOException e) {
            GuiController.internalError(e);
            e.printStackTrace();
        }
    }

    public static void openCustomerWindow(@NotNull Scenes ui, CustomerOrderFacade facade) {
        FXMLLoader loader = new FXMLLoader(GuiController.class.getResource(ui.name() + FXML));

        try {
            Scene scene = new Scene(loader.load(), WIDTH, HEIGHT);
            currentScene = scene;
            CustomerOrderControllerGui controllerGui = loader.getController();
            controllerGui.setFacade(facade);
            setPreviousScenes(scene, ui);
            changeScene();
        } catch (IOException e) {
            GuiController.internalError(e);
            e.printStackTrace();
        }
    }
    private static void changeScene() {
        mainStage.setScene(currentScene);
        mainStage.show();
    }

    /**
     *
     * @param scene The scene that will be loaded
     * @param ui control if it is new not home or AttendantGuiController or
     *           CookerGuiController or ManagerGuiController
     */
    private static void setPreviousScenes(Scene scene, @NotNull Scenes ui) {
        switch (ui){
            //SETUP HOME SCENES
            case ATTENDANT_VIEW:
            case MANAGER_VIEW:
            case COOKER_VIEW:
            case HOME_MENU:
                homeScene = scene;
                previousScene = scene;
                break;
            case LOGIN_VIEW:
            case SIGN_UP_VIEW:
            case ACCOUNT:
            case ORDER_HOME:
            case CUSTOMER_ORDER_VIEW:
                previousScene = mainStage.getScene();
                break;
            default:
                break;
        }
    }

    private static void setUpController(@NotNull FXMLLoader loader,UserBean userBean) {
        AccountControllerGui controllerGui = loader.getController();
        controllerGui.setUserBean(userBean);
        if (controllerGui instanceof AccountInfoGui){
            ((AccountInfoGui) controllerGui).showAccount();
        }
    }

    public static void getBack(){
        if (previousScene != currentScene){
            currentScene = previousScene;
            changeScene();
        }else {
           getHome();
        }
    }

    public static void getHome(){
        if (currentScene != homeScene){
            currentScene = homeScene;
            changeScene();
        }
    }

    private static void internalError(@NotNull Exception e){
        OutStream.println("ERROR " + e.getMessage());
    }

    public static void popUpError(@NotNull Exception e){
        FXMLLoader loader = new FXMLLoader(GuiController.class.getResource(Scenes.ERROR.name() + FXML));
        try {
            Scene scene = new Scene(loader.load(), ERROR_WIDTH, ERROR_HEIGHT);
            Stage stage = new Stage();
            TextFlow errorMessage = (TextFlow) scene.lookup("#errorArea");
            Text text = new Text(e.getMessage());
            errorMessage.getChildren().add(text);
            stage.setScene(scene);
            stage.setTitle(Scenes.ERROR.name());
            stage.showAndWait();
        } catch (IOException ex) {
            internalError(ex);
        }


    }

}
