package djf.ui;

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;
import djf.controller.AppFileController;
import djf.AppTemplate;
import static djf.settings.AppPropertyType.*;
import static djf.settings.AppStartupConstants.FILE_PROTOCOL;
import static djf.settings.AppStartupConstants.PATH_IMAGES;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.StageStyle;

/**
 * This class provides the basic user interface for this application,
 * including all the file controls, but not including the workspace,
 * which would be customly provided for each app.
 * 
 * @author Richard McKenna
 * @version 1.0
 */
public class AppGUI {
    // THIS HANDLES INTERACTIONS WITH FILE-RELATED CONTROLS
    public AppFileController fileController;

    // THIS IS THE APPLICATION WINDOW
    protected Stage primaryStage;

    // THIS IS THE STAGE'S SCENE GRAPH
    protected Scene primaryScene;

    // THIS PANE ORGANIZES THE BIG PICTURE CONTAINERS FOR THE
    // APPLICATION AppGUI. NOTE THAT THE WORKSPACE WILL GO
    // IN THE CENTER REGION OF THE appPane
    protected BorderPane appPane;
    
    // THIS IS THE TOP PANE WHERE WE CAN PUT TOOLBAR
    protected FlowPane topToolbarPane;
    // Handle left for welcome stage
    protected FlowPane leftbarPane;
    public Hyperlink link1;
    public Hyperlink link2;
    public Hyperlink link3;
    
    // Handle right for welcome stage
    protected BorderPane rightbarPane;
    public Hyperlink createLink;
    
    // Handle close welcome stage
    public BorderPane cl;
    public Button closeButton;
    public Label welcomeLabel;
    // THIS IS THE FILE TOOLBAR AND ITS CONTROLS
    protected FlowPane fileToolbar;
    
    // handle  main stage
    public VBox top;
    public Button renameButton;
    public Button aboutButton;
    public String a=""; //handle the name of the code check

    // FILE TOOLBAR BUTTONS
    protected Button newButton;
    protected Button loadButton;
    public Button saveButton;
    public Button saveAsButton;
    protected Button exitButton;
    
    public File linkFile;
    
    // THIS DIALOG IS USED FOR GIVING FEEDBACK TO THE USER
    protected AppYesNoCancelDialogSingleton yesNoCancelDialog;
    
    // THIS TITLE WILL GO IN THE TITLE BAR
    protected String appTitle;
    
    /**
     * This constructor initializes the file toolbar for use.
     * 
     * @param initPrimaryStage The window for this application.
     * 
     * @param initAppTitle The title of this application, which
     * will appear in the window bar.
     * 
     * @param app The app within this gui is used.
     */
    public AppGUI(  Stage initPrimaryStage, 
		    String initAppTitle, 
		    AppTemplate app){
	// SAVE THESE FOR LATER
	primaryStage = initPrimaryStage;
	appTitle = initAppTitle;
	
        initLeftbar(app);
        initRightbar(app);
        welcomeClose();
        
        // INIT THE TOOLBAR
        initTopToolbar(app);
        // AND FINALLY START UP THE WINDOW (WITHOUT THE WORKSPACE)
        initWindow();
        
        // INIT THE STYLESHEET AND THE STYLE FOR THE FILE TOOLBAR
        initStylesheet(app);
        initFileToolbarStyle();        
    }
    
    /**
     * Accessor method for getting the file toolbar controller.
     */
    public AppFileController getFileController() { return fileController; }
    
    /**
     * Accessor method for getting the application pane, within which all
     * user interface controls are ultimately placed.
     * 
     * @return This application GUI's app pane.
     */
    public BorderPane getAppPane() { return appPane; }
    
    /**
     * Accessor method for getting the toolbar pane in the top, within which
     * other toolbars are placed.
     * 
     * @return This application GUI's app pane.
     */    
    public FlowPane getTopToolbarPane() {
        return topToolbarPane;
    }
    
    /**
     * Accessor method for getting the file toolbar pane, within which all
     * file controls are ultimately placed.
     * 
     * @return This application GUI's app pane.
     */    
    public FlowPane getFileToolbar() {
        return fileToolbar;
    }
    
    /**
     * Accessor method for getting this application's primary stage's,
     * scene.
     * 
     * @return This application's window's scene.
     */
    public Scene getPrimaryScene() { return primaryScene; }
    
    /**
     * Accessor method for getting this application's window,
     * which is the primary stage within which the full GUI will be placed.
     * 
     * @return This application's primary stage (i.e. window).
     */    
    public Stage getWindow() { return primaryStage; }

    /**
     * This method is used to activate/deactivate toolbar buttons when
     * they can and cannot be used so as to provide foolproof design.
     * 
     * @param saved Describes whether the loaded Page has been saved or not.
     */
    public void updateToolbarControls(boolean saved) {
        // THIS TOGGLES WITH WHETHER THE CURRENT COURSE
        // HAS BEEN SAVED OR NOT
        saveButton.setDisable(saved);
        saveAsButton.setDisable(true);
        // ALL THE OTHER BUTTONS ARE ALWAYS ENABLED
        // ONCE EDITING THAT FIRST COURSE BEGINS
	newButton.setDisable(false);
        loadButton.setDisable(false);
	exitButton.setDisable(false);

        // NOTE THAT THE NEW, LOAD, AND EXIT BUTTONS
        // ARE NEVER DISABLED SO WE NEVER HAVE TO TOUCH THEM
    }

    /****************************************************************************/
    /* BELOW ARE ALL THE PRIVATE HELPER METHODS WE USE FOR INITIALIZING OUR AppGUI */
    /****************************************************************************/
    
    /**
     * This function initializes all the buttons in the toolbar at the top of
     * the application window. These are related to file management.
     */
    private void initLeftbar(AppTemplate app){
        leftbarPane=new FlowPane();
        Label recent=new Label("Recent Work");
        recent.setFont(new Font("Cambria",40));
        
        link1=new Hyperlink("CSE219Summer2017HW2");
        link1.setFont(new Font("Cambria",32));
        link1.setOnAction(e->{
            linkFile=new File("file:./work/CSE219Summer2017HW2");
            a=linkFile.getName();
            nextWindow();
            fileController.loadUI();
        });
        link2=new Hyperlink("CSE219Summer2017HW1");
        link2.setFont(new Font("Cambria",32));
        link2.setOnAction(e->{
            linkFile=new File("file:./work/CSE219Summer2017HW1");
            a=linkFile.getName();
            nextWindow();
            fileController.loadUI();
        });
        link3=new Hyperlink("CSE219Spring2017HW6");
        link3.setFont(new Font("Cambria",32));
        link3.setOnAction(e->{
            linkFile=new File("file:./work/CSE219Spring2017HW6");
            a=linkFile.getName();
            a="CSE219Spring2017HW6";
            nextWindow();
            fileController.loadUI();
        });
        
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        
        leftbarPane.setPrefWidth(bounds.getWidth()/3);
        leftbarPane.setStyle("-fx-background-color:#DDA0DD ");
        leftbarPane.getChildren().addAll(recent,link1,link2,link3);
    }
    
    private void initRightbar(AppTemplate app){
        rightbarPane=new BorderPane();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String appIcon = FILE_PROTOCOL + PATH_IMAGES + props.getProperty(APP_LOGO);
        //Image i=new Image(appIcon);
        createLink=new Hyperlink(props.getProperty(APP_CREATE_NEW_CODE_CHECK));
        createLink.setFont(new Font("Cambria",32));
        createLink.setOnAction(e->{
            fileController.handleCreateNewRequest();
        });
        
        rightbarPane.setCenter(new ImageView(appIcon));
        BorderPane.setAlignment(createLink,Pos.BOTTOM_CENTER);
        rightbarPane.setBottom(createLink);
    }
    public void welcomeClose(){
        cl=new BorderPane();
        cl.setStyle("-fx-background-color:#DA70D6;");
        welcomeLabel=new Label(appTitle);
        closeButton=new Button("X");
        closeButton.setOnAction(e->{
            nextWindow();
        });
        closeButton.setStyle("-fx-background-color:#DA70D6;");
        cl.setLeft(welcomeLabel);
        cl.setRight(closeButton);
    }
    
    private void initTopToolbar(AppTemplate app) {
        fileToolbar = new FlowPane();

        // HERE ARE OUR FILE TOOLBAR BUTTONS, NOTE THAT SOME WILL
        // START AS ENABLED (false), WHILE OTHERS DISABLED (true)
        newButton = initChildButton(fileToolbar,NEW_BUTTON.toString(),NEW_BUTTON.toString(),false);
        loadButton = initChildButton(fileToolbar,LOAD_BUTTON.toString(),LOAD_BUTTON.toString(),false);
        renameButton=initChildButton(fileToolbar,RENAME_BUTTON.toString(),RENAME_BUTTON.toString(),false);
        aboutButton=initChildButton(fileToolbar,ABOUT_BUTTON.toString(), ABOUT_BUTTON.toString(),false);
        saveButton = new Button();
        saveAsButton=new Button();
        exitButton = new Button("X");
        Tooltip buttonTooltip = new Tooltip("Exit");
        exitButton.setTooltip(buttonTooltip);
        exitButton.setStyle("-fx-background-color:#DA70D6;");
       

	// AND NOW SETUP THEIR EVENT HANDLERS
        fileController = new AppFileController(app);
        renameButton.setOnAction(e->{
            fileController.handleRenameRequest();
        });
        newButton.setOnAction(e -> {
            fileController.handleNewRequest();
        });
        loadButton.setOnAction(e -> {
            fileController.handleLoadRequest();
        });
        aboutButton.setOnAction(e->{
            fileController.handleAboutRequest();
        });
        exitButton.setOnAction(e -> {
            fileController.handleExitRequest();
        });
        saveAsButton.setOnAction(e -> {
            try {
                fileController.handleSaveAsRequest();
            } catch (IOException ex) {
                Logger.getLogger(AppGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        // NOW PUT THE FILE TOOLBAR IN THE TOP TOOLBAR, WHICH COULD
        // ALSO STORE OTHER TOOLBARS
        topToolbarPane = new FlowPane();
        topToolbarPane.getChildren().add(fileToolbar);
    }

    // INITIALIZE THE WINDOW (i.e. STAGE) PUTTING ALL THE CONTROLS
    // THERE EXCEPT THE WORKSPACE, WHICH WILL BE ADDED THE FIRST
    // TIME A NEW Page IS CREATED OR LOADED
    private void initWindow() {
        // SET THE WINDOW TITLE
        primaryStage.initStyle(StageStyle.UNDECORATED);
        // GET THE SIZE OF THE SCREEN
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        // AND USE IT TO SIZE THE WINDOW
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());

        // ADD THE TOOLBAR ONLY, NOTE THAT THE WORKSPACE
        // HAS BEEN CONSTRUCTED, BUT WON'T BE ADDED UNTIL
        // THE USER STARTS EDITING A COURSE
        appPane = new BorderPane();
        appPane.setTop(cl);
        appPane.setLeft(leftbarPane);
        appPane.setRight(rightbarPane);
        //appPane.setTop(topToolbarPane);
        primaryScene = new Scene(appPane);
        
        // SET THE APP ICON
	PropertiesManager props = PropertiesManager.getPropertiesManager();
        String appIcon = FILE_PROTOCOL + PATH_IMAGES + props.getProperty(APP_LOGO);
        primaryStage.getIcons().add(new Image(appIcon));

        // NOW TIE THE SCENE TO THE WINDOW
        primaryStage.setScene(primaryScene);
    }
    public void nextWindow(){
        top=new VBox();
        appPane.getChildren().clear();
        cl.getChildren().clear();
        welcomeLabel.setText("Code Check-"+a);
        cl.setLeft(welcomeLabel);
        cl.setRight(exitButton);
        top.getChildren().addAll(cl,topToolbarPane);
        appPane.setTop(top);
    }
    
    /**
     * This is a public helper method for initializing a simple button with
     * an icon and tooltip and placing it into a toolbar.
     * 
     * @param toolbar Toolbar pane into which to place this button.
     * 
     * @param icon Icon image file name for the button.
     * 
     * @param tooltip Tooltip to appear when the user mouses over the button.
     * 
     * @param disabled true if the button is to start off disabled, false otherwise.
     * 
     * @return A constructed, fully initialized button placed into its appropriate
     * pane container.
     */
    public Button initChildButton(Pane toolbar, String icon, String tooltip, boolean disabled) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
	
	// NOW MAKE THE BUTTON
        Button button = new Button(props.getProperty(icon));
        button.setDisable(disabled);
        Tooltip buttonTooltip = new Tooltip(props.getProperty(tooltip));
        button.setTooltip(buttonTooltip);
        button.setStyle("-fx-font-size: 14pt;-fx-font-weight: bold;-fx-border-radius: 15 15 15 15;-fx-background-radius: 20 20 20 20;");
        button.setMaxWidth(Double.MAX_VALUE);
	
	// PUT THE BUTTON IN THE TOOLBAR
        toolbar.getChildren().add(button);
	
	// AND RETURN THE COMPLETED BUTTON
        return button;
    }
    
   /**
     *  Note that this is the default style class for the top file toolbar
     * and that style characteristics for this type of component should be 
     * put inside app_properties.xml.
     */
    public static final String CLASS_BORDERED_PANE = "bordered_pane";

   /**
     *  Note that this is the default style class for the file buttons in
     * the top file toolbar and that style characteristics for this type
     * of component should be put inside app_properties.xml.
     */
    public static final String CLASS_FILE_BUTTON = "file_button";
    
    /**
     * This function sets up the stylesheet to be used for specifying all
     * style for this application. Note that it does not attach CSS style
     * classes to controls, that must be done separately.
     */
    private void initStylesheet(AppTemplate app) {
	// SELECT THE STYLESHEET
	PropertiesManager props = PropertiesManager.getPropertiesManager();
	String stylesheet = props.getProperty(APP_PATH_CSS);
	stylesheet += props.getProperty(APP_CSS);
        Class appClass = app.getClass();
	URL stylesheetURL = appClass.getResource(stylesheet);
	String stylesheetPath = stylesheetURL.toExternalForm();
	primaryScene.getStylesheets().add(stylesheetPath);	
    }
    
    /**
     * This function specifies the CSS style classes for the controls managed
     * by this framework.
     */
    private void initFileToolbarStyle() {
	topToolbarPane.getStyleClass().add(CLASS_BORDERED_PANE);
        fileToolbar.getStyleClass().add(CLASS_BORDERED_PANE);
	newButton.getStyleClass().add(CLASS_FILE_BUTTON);
	loadButton.getStyleClass().add(CLASS_FILE_BUTTON);
	saveButton.getStyleClass().add(CLASS_FILE_BUTTON);
	exitButton.getStyleClass().add(CLASS_FILE_BUTTON);
        saveAsButton.getStyleClass().add(CLASS_FILE_BUTTON);
    }
}
