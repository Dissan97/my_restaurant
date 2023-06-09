package org.dissan.restaurant.fxml.controllers;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.dissan.restaurant.beans.UserBean;
import org.dissan.restaurant.controllers.exceptions.EmployeeDaoException;
import org.dissan.restaurant.patterns.structural.facade.CustomerOrderFacade;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.logging.Logger;

public class GuiController extends Application {
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 720;
    private static final int POP_UP_HEIGHT = 360;
    private static final int POP_UP_WIDTH = 512;
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
        }
    }
    private static void changeScene() {
        mainStage.setScene(currentScene);
        mainStage.show();
    }

    public static void openManageEmployeeSchedule(@NotNull Scenes ui,@NotNull UserBean userBean) {
        FXMLLoader loader = new FXMLLoader(GuiController.class.getResource(ui.name() + FXML));
        try {
            currentScene = new Scene(loader.load(), WIDTH, HEIGHT);
            changeScene();
            EmployeeShiftManagerGui controllerGui = loader.getController();
            controllerGui.setUserBean(userBean);
        } catch (IOException | EmployeeDaoException e) {
            GuiController.internalError(e);
        }
    }

    /**
     *
     * @param scene The scene that will be loaded
     * @param ui control if it is new not home or AttendantGuiController or
     *           CookerGuiController or ManagerGuiController
     */
    private static void setPreviousScenes(Scene scene, @NotNull Scenes ui) {
        //SETUP HOME SCENES
        if (ui == Scenes.ATTENDANT_VIEW || ui == Scenes.MANAGER_VIEW || ui == Scenes.COOKER_VIEW || ui == Scenes.HOME_MENU) {
            homeScene = scene;
            previousScene = scene;
        } else if (ui == Scenes.LOGIN_VIEW || ui == Scenes.SIGN_UP_VIEW || ui == Scenes.ACCOUNT || ui == Scenes.ORDER_HOME || ui == Scenes.CUSTOMER_ORDER_VIEW) {
            previousScene = mainStage.getScene();
        }
    }

    private static void setUpController(@NotNull FXMLLoader loader,UserBean userBean) {
        AccountControllerGui controllerGui = loader.getController();
        controllerGui.setUserBean(userBean);
        if (controllerGui instanceof AccountInfoGui accountInfoGui){
            accountInfoGui.showAccount();
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
        Logger.getLogger(GuiController.class.getSimpleName()).warning("ERROR " + e.getMessage());
    }


    public static void popUpMessage(String message){
        popUpMessage(message, false,false);
    }

    private static void popUpMessage(String message,boolean error ,boolean wait) {
        FXMLLoader loader = new FXMLLoader(GuiController.class.getResource(Scenes.POP_UP.name() + FXML));
        try {
            Scene scene = new Scene(loader.load(), POP_UP_WIDTH, POP_UP_HEIGHT);
            Stage stage = new Stage();
            TextFlow messageFlow = (TextFlow) scene.lookup("#messageArea");
            Label label = (Label) scene.lookup("#messageLabel");
            if (error){
                label.setText("ERROR");
            }
            Text text = new Text(message);
            messageFlow.getChildren().add(text);
            stage.setScene(scene);
            stage.setTitle(Scenes.POP_UP.name());
            stage.show();
            if (!wait){
                Duration duration = Duration.seconds(1);
                PauseTransition delay = new PauseTransition(duration);
                delay.setOnFinished(event -> stage.close());
                delay.play();
            }
        } catch (IOException ex) {
            internalError(ex);
        }
    }


    public static void popUpError(@NotNull Exception e){
        String exceptionName = e.getClass().getSimpleName();
        popUpMessage(exceptionName + ": " + e.getMessage(), true, true);
    }

}
