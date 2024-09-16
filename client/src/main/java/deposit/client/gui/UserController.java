package deposit.client.gui;

import deposit.domain.Package;
import deposit.domain.User;
import deposit.service.IObserver;
import deposit.service.IService;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;


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

    @FXML
    private TextField nameTextField;
    @FXML
    private TextField fromTextField;
    @FXML
    private TextField toTextField;
    @FXML
    private TextArea descrtiptionTextArea;
    @FXML
    private TextField weghtTextField;
    @FXML
    private CheckBox fragileCheckBox;

    @FXML
    private Button saveButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;

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

        // Add listener for row selection changes
        packageTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Package>() {
            @Override
            public void changed(ObservableValue<? extends Package> observable, Package oldValue, Package newValue) {
                if (newValue != null) {
                    // Update the text area with the description of the selected package
                    packageDescriptionTextArea.setText(newValue.getDescription());

                    updateButton.setDisable(false);
                    deleteButton.setDisable(false);
                } else {
                    // Clear the text area if no package is selected
                    packageDescriptionTextArea.clear();

                    updateButton.setDisable(true);
                    deleteButton.setDisable(true);
                }
            }
        });
    }

    private void initModel() throws Exception
    {
        System.out.println("Init Model!");
        var packages = this.service.getAllPackages();
        this.modelPackages.setAll(packages);

        updateButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    @Override
    public void packageSaved(Package pack) throws Exception {
        Platform.runLater(() -> {
            try {
                var packages = this.service.getAllPackages();
                modelPackages.setAll(packages);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void packageUpdated(Package pack) throws Exception {
        Platform.runLater(() -> {
            try {
                var packages = this.service.getAllPackages();
                modelPackages.setAll(packages);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void packageDeleted(Long id) throws Exception {
        Platform.runLater(() -> {
            try {
                var packages = this.service.getAllPackages();
                modelPackages.setAll(packages);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void onLogout(ActionEvent actionEvent) throws Exception {
        Stage stage = (Stage) this.logoutBtn.getScene().getWindow();
        stage.close(); // Close the stage

        this.service.logout(this.user, this);
    }


    public void onSavePackage(ActionEvent actionEvent) {
        try {
            String name = this.nameTextField.getText();
            String from = this.nameTextField.getText();
            String to = this.nameTextField.getText();
            String description = this.descrtiptionTextArea.getText();
            // Float weight = Float.valueOf(this.weghtTextField.getText());
            float weight = 0F;
            try {
                if (!this.weghtTextField.getText().isEmpty())
                    weight = Float.parseFloat(this.weghtTextField.getText());
            }
            catch(Exception ignored){}
            Boolean fragile = this.fragileCheckBox.isSelected();

            if (name.isEmpty() || from.isEmpty() || to.isEmpty())
                MessageWindow.showMessage(null, Alert.AlertType.ERROR, "Error", "The following fields are mandatory: name, from, to");
            else
                this.service.savePackage(name, from, to, description, weight, fragile);
        }
        catch (Exception e) {
            MessageWindow.showMessage(null, Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }
    public void onUpdatePackage(ActionEvent actionEvent) {
    }
    public void onDeletePackage(ActionEvent actionEvent) {
    }
}
