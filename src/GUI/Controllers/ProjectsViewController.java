package GUI.Controllers;

import GUI.Models.ProjectModel;
import GUI.Models.UserModel;
import be.Project;

import io.github.palexdev.materialfx.controls.MFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProjectsViewController implements Initializable {

    @FXML
    private Label lableCurrentPage,lableMaxPage;
    @FXML
    private AnchorPane paneProject, paneMainProject;
    ProjectModel projectModel = ProjectModel.getInstance();
    UserModel userModel = UserModel.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(userModel.getLoggedInUser().getType()==2){
            loadUserProjectData();
        }
        else loadData();
    }
    public void loadData(){
        int row = 0;
        int col = 0;
        for (Project project : projectModel.getProjects()) {
            StackPane stackPane = generateEventPane(project);
            paneProject.getChildren().add(stackPane);
            paneProject.setStyle("-fx-background-color: #fafafa; -fx-border-color: #000000");
            AnchorPane.setTopAnchor(stackPane, 10 + row * 145.0);
            AnchorPane.setLeftAnchor(stackPane, 20 + col * 420.0);
            col++;
            if (col == 2) {
                col = 0;
                row++;
            }
        }
    }

    public void loadUserProjectData(){
        int row = 0;
        int col = 0;
        for (Project project : projectModel.getUserProjects(userModel.getLoggedInUser().getId())) {
            StackPane stackPane = generateEventPane(project);
            paneProject.getChildren().add(stackPane);
            paneProject.setStyle("-fx-background-color: #fafafa; -fx-border-color: #000000");
            AnchorPane.setTopAnchor(stackPane, 10 + row * 145.0);
            AnchorPane.setLeftAnchor(stackPane, 20 + col * 420.0);
            col++;
            if (col == 2) {
                col = 0;
                row++;
            }
        }
    }


    /**
     * Generates a stackpane which will be used as visual tile for an event that the user can interact with
     * @param project
     * @return
     */
    public StackPane generateEventPane(Project project) {
        StackPane stackPane = new StackPane();

        stackPane.setPrefSize(400, 132);
        stackPane.setOnMouseEntered(e -> {
            stackPane.setScaleX(1.05);
            stackPane.setScaleY(1.05);
        });

        stackPane.setOnMouseExited(e -> {
            stackPane.setScaleX(1.0);
            stackPane.setScaleY(1.0);
        });


        VBox vBox = new VBox();
        vBox.setPrefSize(stackPane.getPrefWidth(), 132);
        vBox.setId("vbox");

        Label eventName = new Label();
        eventName.setPrefSize(stackPane.getPrefWidth(), 16);
        eventName.setFont(Font.font(16));
        eventName.setStyle("-fx-text-fill: #0C2D48;");
        eventName.setText(project.getName());
        eventName.setTextAlignment(TextAlignment.CENTER);
        eventName.setPadding(new Insets(5, 5, 5, 150));
        eventName.setId("eventName");

        Label customerName = new Label();
        customerName.setPrefSize(stackPane.getPrefWidth(), 11);
        customerName.setFont(Font.font(13));
        customerName.setStyle("-fx-text-fill: #0C2D48;");
        customerName.setText("Customer: " + project.getCustomerName());
        customerName.setPadding(new Insets(5, 5, 5, 10));
        customerName.setId("customerName");

        Label date = new Label();
        date.setPrefSize(stackPane.getPrefWidth(), 11);
        date.setFont(Font.font(13));
        date.setStyle("-fx-text-fill: #0C2D48;");
        date.setText("Last visited: " + project.dateLastVisited());
        date.setPadding(new Insets(5, 5, 5, 10));
        date.setTextAlignment(TextAlignment.CENTER);
        date.setId("date");

        Label address = new Label();
        address.setPrefSize(stackPane.getPrefWidth(), 11);
        address.setFont(Font.font(13));
        address.setStyle("-fx-text-fill: #0C2D48;");
        address.setText("Address: " + project.getCompanyAddress());
        address.setPadding(new Insets(5, 5, 5, 10));
        address.setId("address");


        HBox hbox = new HBox();
        hbox.setPrefSize(stackPane.getPrefWidth(), 132);
        hbox.setPadding(new Insets(0, 0, 0, 3));
        hbox.setId("hbox");

        Button button = new Button();
        button.setStyle("-fx-font-family: arial;\n" +
                "    -fx-font-size: 10px;\n" +
                "    -fx-text-fill: #0C2D48;\n" +
                "    -fx-background-color: #A2999E;");
        button.setText("Edit");
        button.setId("editButton");
        button.setPrefSize(80, 20);

        Button button1 = new Button();
        button1.setStyle("-fx-font-family: arial;\n" +
                "    -fx-font-size: 10px;\n" +
                "    -fx-text-fill: #0C2D48;\n" +
                "    -fx-background-color: #A2999E;");
        button1.setText("Add...");
        button1.setId("addButton");
        button1.setPrefSize(80, 20);


        vBox.getChildren().add(eventName);
        vBox.getChildren().add(customerName);
        vBox.getChildren().add(address);
        vBox.getChildren().add(date);
        vBox.getChildren().add(hbox);
        if(userModel.getLoggedInUser().getType()!=3){
        vBox.getChildren().add(button);}
        if(userModel.getLoggedInUser().getType()==1){
            vBox.getChildren().add(button1);
        }


        stackPane.getChildren().add(vBox);
        stackPane.setStyle("-fx-background-radius: 10px; -fx-background-color: #c6c7c4;");
        stackPane.setCursor(Cursor.HAND);
        stackPane.setOnMousePressed(e -> {
            ProjectModel projectModel = ProjectModel.getInstance();
            projectModel.setSelectedProject(project);
            projectModel.setIsProjectSelected(true);
        });

        return stackPane;
    }

    public void clickPageToFront(ActionEvent actionEvent) {
    }

    public void clickPageBackward(ActionEvent actionEvent) {
    }

    public void clickPageForward(ActionEvent actionEvent) {
    }

    public void clickPageToTheBack(ActionEvent actionEvent) {
    }
}
