package deposit.client.gui;

import deposit.domain.Package;
import deposit.domain.User;
import deposit.service.IObserver;
import deposit.service.IService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class UserController implements IObserver {
    private IService service;
    private User user;

    @FXML
    private Button logoutBtn;

    @FXML
    private TableView<Package> packageTableView;
    private ObservableList<Package> modelPackages = FXCollections.observableArrayList();
    @FXML
    private TableColumn<Package, Long> packageIdTableColumn;
    @FXML
    private TableColumn<Package, String> packageNameTableColumn;
    @FXML
    private TableColumn<Package, String> packageFromTableColumn;
    @FXML
    private TableColumn<Package, String> packageToTableColumn;
    @FXML
    private TableColumn<Package, Float> packageWeightTableColumn;
    @FXML
    private TableColumn<Package, String> packageFragileTableColumn;

    @FXML
    private TextArea packageDescriptionTextArea;

    public void init_controller(IService service, User user) throws Exception {
        this.service = service;
        this.user = user;

        this.initModel();
    }

    @FXML
    public void initialize()
    {
        packageIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        packageNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        packageFromTableColumn.setCellValueFactory(new PropertyValueFactory<>("p_from"));
        packageToTableColumn.setCellValueFactory(new PropertyValueFactory<>("p_to"));
        packageWeightTableColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
        packageFragileTableColumn.setCellValueFactory(new PropertyValueFactory<>("fragile"));

        packageTableView.setItems(modelPackages);
    }

    private void initModel() throws Exception
    {
        System.out.println("Init Model!");
        var packages = this.service.getAllPackages();
        this.modelPackages.setAll(packages);
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
