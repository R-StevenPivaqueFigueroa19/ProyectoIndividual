package application;

import demo_jdbc.models.DriverResult;
import demo_jdbc.respositories.DriverResultRepository;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class Main extends Application {

    private DriverResultRepository driverResultRepository = new DriverResultRepository();
    private TableView<DriverResult> tableView = new TableView<>();
    private ComboBox<Integer> yearComboBox = new ComboBox<>();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("RESULTADOS DE LA FORMULA 1");

        // Label para el ComboBox
        Label selectYearLabel = new Label("SELECCIONE EL AÑO QUE DESEA CONSULTAR: ");

        // ComboBox para seleccionar el año
        List<Integer> years = driverResultRepository.getAvailableYears();
        yearComboBox.setItems(FXCollections.observableArrayList(years));
        yearComboBox.setOnAction(e -> updateTable(yearComboBox.getValue()));

        // HBox para contener el Label y el ComboBox
        HBox hbox = new HBox(10, selectYearLabel, yearComboBox);

        // TableView para mostrar los resultados
        TableColumn<DriverResult, String> driverNameColumn = new TableColumn<>("Driver Name");
        driverNameColumn.setCellValueFactory(new PropertyValueFactory<>("driverName"));

        TableColumn<DriverResult, Integer> winsColumn = new TableColumn<>("Wins");
        winsColumn.setCellValueFactory(new PropertyValueFactory<>("wins"));

        TableColumn<DriverResult, Integer> totalPointsColumn = new TableColumn<>("Total Points");
        totalPointsColumn.setCellValueFactory(new PropertyValueFactory<>("totalPoints"));

        TableColumn<DriverResult, Integer> rankColumn = new TableColumn<>("Rank");
        rankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));

        tableView.getColumns().addAll(driverNameColumn, winsColumn, totalPointsColumn, rankColumn);

        // Layout
        VBox vbox = new VBox(10, hbox, tableView);
        vbox.setPadding(new Insets(10));

        primaryStage.setScene(new Scene(vbox, 600, 400));
        primaryStage.show();
    }

    private void updateTable(int year) {
        List<DriverResult> results = driverResultRepository.getResultByYear(year);
        ObservableList<DriverResult> data = FXCollections.observableArrayList(results);
        tableView.setItems(data);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

