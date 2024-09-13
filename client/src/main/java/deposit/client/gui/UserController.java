package deposit.client.gui;

import deposit.domain.Package;
import deposit.domain.User;
import deposit.service.IObserver;
import deposit.service.IService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class UserController implements IObserver {
    private IService service;
    private User user;

    @FXML
    private Button logoutBtn;

    public void init_controller(IService service, User user) {
        this.service = service;
        this.user = user;
    }

    @Override
    public void packageSaved(Package pack) throws Exception {

    }

    @Override
    public void packageUpdated(Package pack) throws Exception {

    }

    @Override
    public void packageDeleted(Long id) throws Exception {

    }

    public void onLogout(ActionEvent actionEvent) throws Exception {
        Stage stage = (Stage) this.logoutBtn.getScene().getWindow();
        stage.close(); // Close the stage

        this.service.logout(this.user, this);
    }
}
