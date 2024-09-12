package deposit.client.gui;

import deposit.domain.User;
import deposit.service.IService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    private IService service;
    private UserController userController;

    @FXML
    private TextField textField;
    @FXML
    private PasswordField passwordField;

    public void initController(IService service) {
        this.service = service;
    }

    public void setUserController(UserController userController) {
        this.userController = userController;
    }

    public void onLogin(ActionEvent actionEvent) throws Exception
    {
        String username = this.textField.getText();
        String password = this.passwordField.getText();

        if (username.isEmpty())
            MessageWindow.showMessage(null, Alert.AlertType.ERROR, "Error", "Completati Username!");
        if (password.isEmpty())
            MessageWindow.showMessage(null, Alert.AlertType.ERROR, "Error", "Completati Password!");

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("user-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 800);

        this.userController = fxmlLoader.getController();

        try
        {
            this.service.login(username, password, this.userController);

            this.textField.clear();
            this.passwordField.clear();

            //TODO
//            User user = this.service.getUserByUsername(username);
//            this.userController.init_controller(this.service, user);
            Stage stage = new Stage();
            stage.setTitle(username);
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e)
        {
            MessageWindow.showMessage(null, Alert.AlertType.ERROR, "Autentication Failed!", "Wrong username or password!");
            this.passwordField.clear();
        }
    }
}
