/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smartaccount;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 *
 * @author ANKIT
 */
public class FXMLMainController implements Initializable {

    Stage prevstage;
    @FXML
    private MenuItem addEmpMenubtn;
    @FXML
    private MenuItem viewEmpMenuBtn;
    @FXML
    private MenuItem viewDealerMenuBtn;

    public void setprevstage(Stage stage) {
        this.prevstage = stage;
    }
    //login function and variable
    @FXML
    private Label actiontarget;
    @FXML
    private TextField userTxt;
    @FXML
    private PasswordField passwordField;

    @FXML
    protected void loginFunction(ActionEvent event) throws IOException {
        String userName = userTxt.getText();
        String password = passwordField.getText();
        if ("alphacom".equals(userName) && "22091ank".equals(password)) {
            Stage stage = new Stage();
            stage.setTitle("SmartAccount");
            Pane mypane = null;
            mypane = FXMLLoader.load(getClass().getResource("FXMLDashboard.fxml"));
            Scene scene = new Scene(mypane);
            stage.setScene(scene);
            stage.setMaximized(true);
            prevstage.close();
            stage.show();
        } else {
            new SendMail("Hello Admin,\nThere is an unauthorized user making invalid sign-in attempt.\nUsername entered:\t"+userName+"\nPassword entered:\t"+password+"\nIf admin is making this attempt then ignore otherwise protect your software.\n\nThank you!"
                    ,"ankitkr31195@gmail.com","SmartAccount Invalid SignIn");
            userTxt.setText("");
            passwordField.setText("");
            actiontarget.setText("Wrong Credential");
        }
    }
    //on button click add employee form
    @FXML
    public Pane dashboardCenterPane;

    @FXML
    protected void addEmpMenuBtn(ActionEvent event) throws IOException {
        dashboardCenterPane.getChildren().clear();
        dashboardCenterPane.getChildren().add(FXMLLoader.load(getClass().getResource("FXMLAddEmployee.fxml")));
        dashboardCenterPane.setLayoutX(0);
        dashboardCenterPane.setLayoutY(0);
    }

    //on button click add dealer form
    @FXML
    protected void addDealerMenuBtn(ActionEvent event) throws IOException {
        dashboardCenterPane.getChildren().clear();
        dashboardCenterPane.getChildren().add(FXMLLoader.load(getClass().getResource("FXMLAddDealer.fxml")));
        dashboardCenterPane.setLayoutX(0);
        dashboardCenterPane.setLayoutY(0);
    }

    //database connection
    Connection con;

    protected void dbConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/accmaindb", "root", "hello");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(FXMLMainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Function to clear textfield
    public void ClearTextField(GridPane pane) {
        for (Node node : pane.getChildren()) {
            if (node instanceof TextField) {
                ((TextField) node).setText("");
            }
//            if(node instanceof DatePicker){
//                ((DatePicker)node).setText("");
//            }
            if (node instanceof GridPane) {
                for (Node gridnode : ((GridPane) node).getChildren()) {
                    if (gridnode instanceof TextField) {
                        ((TextField) gridnode).setText("");
                    }
                }
            }
        }
    }
    //employee detail to database
    DatePicker empDojTxt;
    //employee detail to database
    @FXML
    DatePicker empDobTxt;
    GridPane addEmpPanes;
    TextField empIdTxt;
    @FXML
    TextField empNameTxt, empFatherNameTxt, empFlatTxt, empPoTxt, empPsTxt,
            empStateTxt, empCityTxt, empLandmarkTxt, empPincodeTxt, empMobileTxt, empEmailTxt;

    @FXML
    protected void empDetailIntodb(ActionEvent event) throws IOException {
        DatePicker datePicker = new DatePicker();
        String pattern = "yyyy-MM-dd";
        datePicker.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
        try {
            String empId = empIdTxt.getText();
            String empName = empNameTxt.getText();
            String fatherName = empFatherNameTxt.getText();
            LocalDate doj = empDojTxt.getValue();
            LocalDate dob = empDobTxt.getValue();
            String flatno = empFlatTxt.getText();
            String ps = empPsTxt.getText();
            String po = empPoTxt.getText();
            String landmark = empLandmarkTxt.getText();
            String state = empStateTxt.getText();
            String city = empCityTxt.getText();
            int pincode = Integer.parseInt(empPincodeTxt.getText());
            String email = empEmailTxt.getText();
            Long mobile = Long.parseLong(empMobileTxt.getText());
            dbConnection();
            Statement st = con.createStatement();
            String sql = "INSERT INTO `empinfo` (`empid`, `empname`, `fathername`, `doj`, `dob`, `flatno`, `po`, `ps`, `landmark`, `state`, `city`, `pincode`, `email`, `mobile`) VALUES ('" + empId + "', '" + empName + "', '" + fatherName + "', '" + doj + "', '" + dob + "', '" + flatno + "', '" + po + "', '" + ps + "', '" + landmark + "', '" + state + "', '" + city + "', '" + pincode + "', '" + email + "', '" + mobile + "');";
            int res = st.executeUpdate(sql);
            if (res == 1) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Information");
                alert.setContentText("Successfully Updated!!");
                alert.showAndWait();
                ClearTextField(addEmpPanes);
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Error");
                alert.setContentText("Updation Failed");
                alert.showAndWait();
            }
        } catch (SQLException ex) {
            Logger.getLogger(FXMLMainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //dealer detail to database
    GridPane addDealerPanes;
    TextField dealerIdTxt;
    @FXML
    TextField dealerNameTxt, ownerNameTxt, dealerFlatTxt, dealerPoTxt, dealerPsTxt,
            dealerStateTxt, dealerCityTxt, dealerLandmarkTxt, dealerPincodeTxt, dealerMobileTxt, dealerEmailTxt;

    @FXML
    protected void dealerDetailIntodb(ActionEvent event) throws IOException {
        try {
            String dealerId = dealerIdTxt.getText();
            String dealerName = dealerNameTxt.getText();
            String ownerName = ownerNameTxt.getText();
            String flatno = dealerFlatTxt.getText();
            String ps = dealerPsTxt.getText();
            String po = dealerPoTxt.getText();
            String landmark = dealerLandmarkTxt.getText();
            String state = dealerStateTxt.getText();
            String city = dealerCityTxt.getText();
            int pincode = Integer.parseInt(dealerPincodeTxt.getText());
            String email = dealerEmailTxt.getText();
            Long mobile = Long.parseLong(dealerMobileTxt.getText());
            dbConnection();
            Statement st = con.createStatement();
            String sql = "INSERT INTO `dealerinfo` (`dealerId`, `dealerName`, `ownerName`, `flatno`, `po`, `ps`, `landmark`, `city`, `state`, `pincode`, `email`, `mobile`) VALUES ('" + dealerId + "', '" + dealerName + "', '" + ownerName + "', '" + flatno + "', '" + po + "', '" + ps + "', '" + landmark + "', '" + city + "', '" + state + "', '" + pincode + "', '" + email + "', '" + mobile + "');";
            int res = st.executeUpdate(sql);
            if (res == 1) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Information");
                alert.setContentText("Successfully Updated!!");
                alert.showAndWait();
                ClearTextField(addDealerPanes);
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Error");
                alert.setContentText("Updation Failed");
                alert.showAndWait();
            }

        } catch (SQLException ex) {
            Logger.getLogger(FXMLMainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //employee detail to tableview
    //ScrollPane empDetailView;
    //GridPane empViewGridpane = new GridPane();
    private TableView<EmpLessDetailView> tableEmp = new TableView<EmpLessDetailView>();
    TableColumn empidCol = new TableColumn("EMP_ID");

    TableColumn empnameCol = new TableColumn("EMP_Name");

    TableColumn stateCol = new TableColumn("STATE");

    TableColumn cityCol = new TableColumn("CITY");

    TableColumn viewCol = new TableColumn("VIEW");

    private ObservableList<EmpLessDetailView> data;

    @FXML
    protected void viewEmpDetailMenuBtn(ActionEvent event) throws IOException {
        dashboardCenterPane.getChildren().clear();
        dashboardCenterPane.getChildren().add(FXMLLoader.load(getClass().getResource("FXMLEmpView.fxml")));
        dashboardCenterPane.setLayoutX(0);
        dashboardCenterPane.setLayoutY(0);

        tableEmp.setLayoutY(125);

        empidCol.setMinWidth(150);
        empidCol.setResizable(false);
        empidCol.setCellValueFactory(new PropertyValueFactory<>("empid"));

        empnameCol.setMinWidth(150);
        empnameCol.setResizable(false);
        empnameCol.setCellValueFactory(new PropertyValueFactory<>("empname"));

        stateCol.setMinWidth(150);
        stateCol.setResizable(false);
        stateCol.setCellValueFactory(new PropertyValueFactory<>("state"));

        cityCol.setMinWidth(150);
        cityCol.setResizable(false);
        cityCol.setCellValueFactory(new PropertyValueFactory<>("city"));

        viewCol.setMinWidth(100);
        viewCol.setResizable(false);
        viewCol.setSortable(false);

        viewCol.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<EmpLessDetailView, Boolean>, ObservableValue<Boolean>>() {

            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<EmpLessDetailView, Boolean> p) {
                return new SimpleBooleanProperty(p.getValue() != null);
            }
        });

        viewCol.setCellFactory(
                new Callback<TableColumn<EmpLessDetailView, Boolean>, TableCell<EmpLessDetailView, Boolean>>() {

            @Override
            public TableCell<EmpLessDetailView, Boolean> call(TableColumn<EmpLessDetailView, Boolean> p) {
                return new ButtonCell();
            }

        });

        dbConnection();
        data = FXCollections.observableArrayList();
        try {
            Statement st = con.createStatement();
            String sql = "SELECT `empid`, `empname`, `state`, `city` FROM `empinfo`;";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                data.add(new EmpLessDetailView(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));
            }
            tableEmp.setItems(data);
            tableEmp.getColumns().clear();
            tableEmp.getColumns().addAll(empidCol, empnameCol, stateCol, cityCol, viewCol);
            dashboardCenterPane.getChildren().add(tableEmp);

        } catch (SQLException ex) {
            Logger.getLogger(FXMLMainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public class ButtonCell extends TableCell<EmpLessDetailView, Boolean> {

        final Button cellButton = new Button("View");
        ResultSet rs;

        /*TextFields
        @FXML
        public TextField EmpViewDetailId = new TextField();
        @FXML
        public TextField EmpViewDetailName = new TextField();
        @FXML
        public TextField EmpViewDetailFatherName = new TextField();
        @FXML
        public TextField EmpViewDetailDOB = new TextField();
        @FXML
        public TextField EmpViewDetailDOJ = new TextField();
        @FXML
        public TextField EmpViewDetailState = new TextField();
        @FXML
        public TextField EmpViewDetailCity = new TextField();
        @FXML
        public TextField EmpViewDetailPincode = new TextField();
        @FXML
        public TextField EmpViewDetailEmail = new TextField();
        @FXML
        public TextField EmpViewDetailFlat = new TextField();
        @FXML
        public TextField EmpViewDetailLandmark = new TextField();
        @FXML
        public TextField EmpViewDetailPO = new TextField();
        @FXML
        public TextField EmpViewDetailPS = new TextField();
        @FXML
        public TextField EmpViewDetailMobile = new TextField();
        //label
        @FXML
        public Label EmpViewDetailIdLbl = new Label("Id");
        @FXML
        public Label EmpViewDetailNameLbl = new Label("Name");
        @FXML
        public Label EmpViewDetailFatherNameLbl = new Label("Father Name");
        @FXML
        public Label EmpViewDetailDOBLbl = new Label("DOB");
        @FXML
        public Label EmpViewDetailDOJLbl = new Label("DOJ");
        @FXML
        public Label EmpViewDetailStateLbl = new Label("State");
        @FXML
        public Label EmpViewDetailCityLbl = new Label("City");
        @FXML
        public Label EmpViewDetailPincodeLbl = new Label("Pincode");
        @FXML
        public Label EmpViewDetailEmailLbl = new Label("Email Id");
        @FXML
        public Label EmpViewDetailFlatLbl = new Label("Flat No.");
        @FXML
        public Label EmpViewDetailLandmarkLbl = new Label("Landmark");
        @FXML
        public Label EmpViewDetailPOLbl = new Label("PO");
        @FXML
        public Label EmpViewDetailPSLbl = new Label("PS");
        @FXML
        public Label EmpViewDetailMobileLbl = new Label("Mobile No.");
         */
        ButtonCell() {

            cellButton.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {
                    try {
                        //dashboardCenterPane.getChildren().clear();
                        String EmpId = tableEmp.getItems().get(getTableRow().getIndex()).getEmpid();
                        System.out.println(EmpId);
                        dbConnection();
                        Statement st = con.createStatement();
                        String sql = "SELECT * FROM `empinfo` WHERE empid LIKE '" + EmpId + "';";
                        rs = st.executeQuery(sql);
                        rs.next();
                        /*while (rs.next()) {
                            EmpViewDetailId.setText(rs.getString(1));
                            EmpViewDetailName.setText(rs.getString(2));
                            EmpViewDetailFatherName.setText(rs.getString(3));
                            EmpViewDetailDOJ.setText(String.valueOf(rs.getDate(4)));
                            EmpViewDetailDOB.setText(String.valueOf(rs.getDate(5)));
                            EmpViewDetailFlat.setText(rs.getString(6));
                            EmpViewDetailPO.setText(rs.getString(7));
                            EmpViewDetailPS.setText(rs.getString(8));
                            EmpViewDetailLandmark.setText(rs.getString(9));
                            EmpViewDetailState.setText(rs.getString(10));
                            EmpViewDetailCity.setText(rs.getString(11));
                            EmpViewDetailPincode.setText(String.valueOf(rs.getInt(12)));
                            EmpViewDetailEmail.setText(rs.getString(13));
                            EmpViewDetailMobile.setText(String.valueOf(rs.getInt(12)));*/
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Employee Detail");
                        alert.setHeaderText(rs.getString(2));
                        alert.setContentText("Id\t\t\t\t" + rs.getString(1) + "\nName\t\t\t" + rs.getString(2) + "\nFather Name\t\t" + rs.getString(3) + "\nDOB\t\t\t\t" + String.valueOf(rs.getDate(5)) + "\nDOJ\t\t\t\t" + String.valueOf(rs.getDate(4))
                                + "\nAddress" + "\nFlat No.\t\t\t" + rs.getString(6) + "\nPO\t\t\t\t" + rs.getString(7) + "\nPS\t\t\t\t" + rs.getString(8) + "\nLanmark\t\t\t" + rs.getString(9) + "\nState\t\t\t" + rs.getString(10) + "\nCity\t\t\t\t" + rs.getString(11) + "\nPincode\t\t\t" + String.valueOf(rs.getInt(12)) + "\nEmail Id\t\t\t" + rs.getString(13) + "\nMobile No.\t\t" + String.valueOf(rs.getInt(12)));

                        alert.showAndWait();
                        //dashboardCenterPane.getChildren().addAll(EmpViewDetailId, EmpViewDetailName, EmpViewDetailFatherName,
                        //       EmpViewDetailDOJ, EmpViewDetailDOB, EmpViewDetailFlat, EmpViewDetailPO, EmpViewDetailPS, EmpViewDetailLandmark,
                        //       EmpViewDetailState, EmpViewDetailCity, EmpViewDetailPincode, EmpViewDetailEmail, EmpViewDetailMobile);

                        // }
                    } catch (SQLException ex) {
                        Logger.getLogger(FXMLMainController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        }

        //Display button if the row is not empty
        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if (!empty) {
                setGraphic(cellButton);
            }
        }
    }

    //view dealer
    @FXML
    protected void viewDealerDetailMenuBtn(ActionEvent event) {
        System.out.println("hello");
    }
    //update and email
    @FXML
    private TextField sample1Txt;
    @FXML
    private TextField sample2Txt;
    @FXML
    public ProgressBar EmailProgressBar;

    @FXML
    public void updateAndEmail(ActionEvent event) {
        try {
            double price1 = Double.parseDouble(sample1Txt.getText());
            double price2 = Double.parseDouble(sample2Txt.getText());
            dbConnection();
            Statement st = con.createStatement();
            String sql1 = "UPDATE `updateprice` SET `productprice`=" + price1 + " WHERE productid LIKE 'P101'";
            st.executeUpdate(sql1);
            String sql2 = "UPDATE `updateprice` SET `productprice`=" + price2 + " WHERE productid LIKE 'P102'";
            st.executeUpdate(sql2);
            String sql3 = "SELECT `dealerName`, `email` FROM `dealerinfo`;";
            ResultSet rs = st.executeQuery(sql3);
            rs.last();
            int row = rs.getRow();
            rs.beforeFirst();
            for (int i = 1; i <= row; i++) {
                rs.next();
                String message = "Hello " + rs.getString(1) + ",\nProduct 1 price Rs." + price1 + "\nProduct 2 price Rs." + price2 + "\n\n\nWarm Regards \n\nXYZ Pvt. Ltd.";
                String toemail = rs.getString(2);
                String subject = "Price Update";
                new SendMail(message, toemail, subject);
                EmailProgressBar.setProgress(0.5);
            }
            EmailProgressBar.setProgress(1);
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Information");
            alert.setContentText("Successfully Updated and Mailed!!");
            alert.showAndWait();

        } catch (SQLException ex) {
            Logger.getLogger(FXMLMainController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    // email client
    @FXML
    public void emailClientBtnClick(ActionEvent event) {
        try {
            dashboardCenterPane.getChildren().clear();
            dashboardCenterPane.getChildren().add(FXMLLoader.load(getClass().getResource("FXMLEmailClient.fxml")));
            dashboardCenterPane.setLayoutX(0);
            dashboardCenterPane.setLayoutY(0);
        } catch (IOException ex) {
            Logger.getLogger(FXMLMainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private TextField ToEmailTxt;
    @FXML
    private TextArea MsgEmailTxt;
    @FXML
    private TextField SubjectEmailTxt;

    @FXML
    public void sendEmailClientBtnClick(ActionEvent event) {
        String message = MsgEmailTxt.getText();
        String to = ToEmailTxt.getText();
        String subject = SubjectEmailTxt.getText();
        new SendMail(message, to, subject);
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Information");
        alert.setContentText("Email Sent Successfully!!");
        alert.showAndWait();
    }

    @FXML
    public void dashboardMenuBtn(ActionEvent event) {
        try {
            dashboardCenterPane.getChildren().clear();
            dashboardCenterPane.getChildren().add(FXMLLoader.load(getClass().getResource("FXMLCompanyInfo.fxml")));
            dashboardCenterPane.setLayoutX(0);
            dashboardCenterPane.setLayoutY(0);
        } catch (IOException ex) {
            Logger.getLogger(FXMLMainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void expensesBtnClick(ActionEvent event) {
        try {
            ResultSet rs;
            dbConnection();
            Statement st = con.createStatement();
            String sql = "SELECT * FROM expensesdetail;";
            rs = st.executeQuery(sql);
            Dialog dialog = new Dialog();
            dialog.setTitle("Expenses Detail");
            dialog.setHeaderText("Information");
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));
            grid.add(new Label("Month"), 0, 0);
            grid.add(new Label("Year"), 1, 0);
            grid.add(new Label("Electricity Bill"), 2, 0);
            grid.add(new Label("Employee Payment"), 3, 0);
            grid.add(new Label("Maintainance"), 4, 0);
            grid.add(new Label("Raw Material"), 5, 0);
            int i = 0;
            while (rs.next()) {
                i++;
                grid.add(new Label(rs.getString(2)), 0, i);
                grid.add(new Label(rs.getString(3)), 1, i);
                grid.add(new Label(rs.getString(4)), 2, i);
                grid.add(new Label(rs.getString(5)), 3, i);
                grid.add(new Label(rs.getString(6)), 4, i);
                grid.add(new Label(rs.getString(7)), 5, i);
            }
            dialog.getDialogPane().setContent(grid);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
            dialog.showAndWait();

        } catch (SQLException ex) {
            Logger.getLogger(FXMLMainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void stockRecordBtnClick(ActionEvent event) {
        try {
            ResultSet rs;
            dbConnection();
            Statement st = con.createStatement();
            String sql = "SELECT * FROM stockdetail;";
            rs = st.executeQuery(sql);
            Dialog dialog = new Dialog();
            dialog.setTitle("Stock Detail");
            dialog.setHeaderText("Information");
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));
            grid.add(new Label("Month"), 0, 0);
            grid.add(new Label("Year"), 1, 0);
            grid.add(new Label("Raw Material 1(in Ton)"), 2, 0);
            grid.add(new Label("Raw Material 2(in Ton)"), 3, 0);
            grid.add(new Label("Raw Material 3(in Ton)"), 4, 0);
            grid.add(new Label("Raw Material 4(in Ton)"), 5, 0);
            int i = 0;
            while (rs.next()) {
                i++;
                grid.add(new Label(rs.getString(2)), 0, i);
                grid.add(new Label(rs.getString(3)), 1, i);
                grid.add(new Label(rs.getString(4)), 2, i);
                grid.add(new Label(rs.getString(5)), 3, i);
                grid.add(new Label(rs.getString(6)), 4, i);
                grid.add(new Label(rs.getString(7)), 5, i);
            }
            dialog.getDialogPane().setContent(grid);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
            dialog.showAndWait();

        } catch (SQLException ex) {
            Logger.getLogger(FXMLMainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void productDetailBtnClick(ActionEvent event) {
        Dialog dialog = new Dialog();
        dialog.setTitle("Stock Detail");
        dialog.setHeaderText("Information");
        FlowPane flow = new FlowPane();
        flow.setPadding(new Insets(10, 10, 10, 10));
        flow.setVgap(10);
        flow.setHgap(20);
        flow.setPrefWrapLength(480); // preferred width allows for two columns
        flow.setStyle("-fx-background-color: #ffffff;");
        

        ImageView pages[] = new ImageView[4];
        
        for (int i = 0; i < 4; i++) {
            pages[i] = new ImageView(
                    new Image(LayoutSample.class.getResourceAsStream(
                            "image" + (i + 1) + ".png"),150,150,true,true));
            flow.getChildren().addAll(pages[i],new Label("Product "+(i+1)+"\nDetail"));
        }
        dialog.getDialogPane().setContent(flow);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
        dialog.showAndWait();
    }

    //employee detail left pane button
    @FXML
    public void employeeDetailLeftBtnClick(ActionEvent event) {
        try {
            viewEmpDetailMenuBtn(event);
        } catch (IOException ex) {
            Logger.getLogger(FXMLMainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //calculator
    @FXML
    private TextField calcinputTxt;
    @FXML
    private TextField calcoutputTxt;

    @FXML
    protected void calconeBtn(ActionEvent event) {
        if (calcinputTxt.getText() == null) {
            calcinputTxt.setText("1");
        } else {
            calcinputTxt.setText(calcinputTxt.getText() + "1");
        }
    }

    @FXML
    protected void calctwoBtn(ActionEvent event) {
        if (calcinputTxt.getText() == null) {
            calcinputTxt.setText("2");
        } else {
            calcinputTxt.setText(calcinputTxt.getText() + "2");
        }
    }

    @FXML
    protected void calcthreeBtn(ActionEvent event) {
        if (calcinputTxt.getText() == null) {
            calcinputTxt.setText("3");
        } else {
            calcinputTxt.setText(calcinputTxt.getText() + "3");
        }
    }

    @FXML
    protected void calcfourBtn(ActionEvent event) {
        if (calcinputTxt.getText() == null) {
            calcinputTxt.setText("4");
        } else {
            calcinputTxt.setText(calcinputTxt.getText() + "4");
        }
    }

    @FXML
    protected void calcfiveBtn(ActionEvent event) {
        if (calcinputTxt.getText() == null) {
            calcinputTxt.setText("5");
        } else {
            calcinputTxt.setText(calcinputTxt.getText() + "5");
        }
    }

    @FXML
    protected void calcsixBtn(ActionEvent event) {
        if (calcinputTxt.getText() == null) {
            calcinputTxt.setText("6");
        } else {
            calcinputTxt.setText(calcinputTxt.getText() + "6");
        }
    }

    @FXML
    protected void calcsevenBtn(ActionEvent event) {
        if (calcinputTxt.getText() == null) {
            calcinputTxt.setText("7");
        } else {
            calcinputTxt.setText(calcinputTxt.getText() + "7");
        }
    }

    @FXML
    protected void calceightBtn(ActionEvent event) {
        if (calcinputTxt.getText() == null) {
            calcinputTxt.setText("8");
        } else {
            calcinputTxt.setText(calcinputTxt.getText() + "8");
        }
    }

    @FXML
    protected void calcnineBtn(ActionEvent event) {
        if (calcinputTxt.getText() == null) {
            calcinputTxt.setText("9");
        } else {
            calcinputTxt.setText(calcinputTxt.getText() + "9");
        }
    }

    @FXML
    protected void calczeroBtn(ActionEvent event) {
        if (calcinputTxt.getText() == null) {
            calcinputTxt.setText("0");
        } else {
            calcinputTxt.setText(calcinputTxt.getText() + "0");
        }
    }

    @FXML
    protected void calcaddBtn(ActionEvent event) {
        if (calcinputTxt.getText() == null) {
            calcinputTxt.setText("");
        } else {
            calcinputTxt.setText(calcinputTxt.getText() + " + ");
        }
    }

    @FXML
    protected void calcsubBtn(ActionEvent event) {
        if (calcinputTxt.getText() == null) {
            calcinputTxt.setText("");
        } else {
            calcinputTxt.setText(calcinputTxt.getText() + " - ");
        }
    }

    @FXML
    protected void calcmulBtn(ActionEvent event) {
        if (calcinputTxt.getText() == null) {
            calcinputTxt.setText("");
        } else {
            calcinputTxt.setText(calcinputTxt.getText() + " * ");
        }
    }

    @FXML
    protected void calcdivBtn(ActionEvent event) {
        if (calcinputTxt.getText() == null) {
            calcinputTxt.setText("");
        } else {
            calcinputTxt.setText(calcinputTxt.getText() + " / ");
        }
    }

    @FXML
    protected void calcpowerBtn(ActionEvent event) {
        if (calcinputTxt.getText() == null) {
            calcinputTxt.setText("");
        } else {
            calcinputTxt.setText(calcinputTxt.getText() + " ^ ");
        }
    }

    @FXML
    protected void calcpercentBtn(ActionEvent event) {
        if (calcinputTxt.getText() == null) {
            calcinputTxt.setText("");
        } else {
            calcinputTxt.setText(calcinputTxt.getText() + " % ");
        }
    }
    double calcresult;

    @FXML
    protected void calcequalBtn(ActionEvent event) {
        String input = calcinputTxt.getText();
        String[] inarr = input.split("\\s");
        try {
            if ("+".equals(inarr[1])) {
                calcresult = Double.parseDouble(inarr[0]) + Double.parseDouble(inarr[2]);
            }
            if ("-".equals(inarr[1])) {
                calcresult = Double.parseDouble(inarr[0]) - Double.parseDouble(inarr[2]);
            }
            if ("*".equals(inarr[1])) {
                calcresult = Double.parseDouble(inarr[0]) * Double.parseDouble(inarr[2]);
            }
            if ("/".equals(inarr[1])) {
                calcresult = Double.parseDouble(inarr[0]) / Double.parseDouble(inarr[2]);
            }
            if ("^".equals(inarr[1])) {
                calcresult = (double) Math.pow(Double.parseDouble(inarr[0]), Double.parseDouble(inarr[2]));
            }
            if ("%".equals(inarr[3])) {
                if ("*".equals(inarr[1])) {
                    calcresult = (Double.parseDouble(inarr[0]) * Double.parseDouble(inarr[2])) / 100;
                } else if ("/".equals(inarr[1])) {
                    calcresult = (Double.parseDouble(inarr[0]) / Double.parseDouble(inarr[2])) * 100;
                }
            }
        } catch (Exception e) {
            calcoutputTxt.setText("Syntax Error");
        }
        calcoutputTxt.setText(String.valueOf(calcresult));
    }

    @FXML
    protected void calcacBtn(ActionEvent event) {
        calcinputTxt.setText("");
        calcoutputTxt.setText("");
    }

    @FXML
    protected void calcdelBtn(ActionEvent event) {
        String in = calcinputTxt.getText();
        calcinputTxt.setText((String) in.subSequence(0, in.length() - 1));
    }

    @FXML
    protected void calcdecimalBtn(ActionEvent event) {
        if (calcinputTxt.getText() == null) {
            calcinputTxt.setText("0.");
        } else {
            calcinputTxt.setText(calcinputTxt.getText() + ".");
        }
    }

    @FXML
    private void calculateBtn(KeyEvent event) {
        calcoutputTxt.setText("3456");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
}
