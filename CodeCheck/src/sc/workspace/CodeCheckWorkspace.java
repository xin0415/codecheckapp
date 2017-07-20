/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sc.workspace;

/**
 *
 * @author xin
 */
import djf.components.AppDataComponent;
import djf.components.AppWorkspaceComponent;
import static djf.settings.AppStartupConstants.PATH_WORK;
import static djf.ui.AppGUI.CLASS_BORDERED_PANE;
import djf.ui.AppMessageDialogSingleton;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import sc.CodeCheckApp;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextAreaBuilder;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import properties_manager.PropertiesManager;
import static sc.CodeCheckProp.HOME_BUTTON;
import static sc.CodeCheckProp.PREVIOUS_BUTTON;
import static sc.CodeCheckProp.CAPTION_PROMPT_TEXT;
import static sc.CodeCheckProp.CURRENT_HEIGHT_COLUMN_TEXT;
import static sc.CodeCheckProp.CURRENT_HEIGHT_PROMPT_TEXT;
import static sc.CodeCheckProp.CURRENT_WIDTH_COLUMN_TEXT;
import static sc.CodeCheckProp.CURRENT_WIDTH_PROMPT_TEXT;
import static sc.CodeCheckProp.FILE_NAME_COLUMN_TEXT;
import static sc.CodeCheckProp.FILE_NAME_PROMPT_TEXT;
import static sc.CodeCheckProp.INVALID_IMAGE_PATH_MESSAGE;
import static sc.CodeCheckProp.INVALID_IMAGE_PATH_TITLE;
import static sc.CodeCheckProp.ORIGINAL_HEIGHT_PROMPT_TEXT;
import static sc.CodeCheckProp.ORIGINAL_WIDTH_PROMPT_TEXT;
import static sc.CodeCheckProp.PATH_PROMPT_TEXT;
import static sc.CodeCheckProp.NEXT_BUTTON;
import static sc.CodeCheckProp.UPDATE_BUTTON_TEXT;
import sc.data.CodeCheckData;
import sc.data.CodeHW;
import sc.data.Homework;
import static sc.style.CodeCheckStyle.CLASS_EDIT_BUTTON;
import static sc.style.CodeCheckStyle.CLASS_EDIT_SLIDER;
import static sc.style.CodeCheckStyle.CLASS_EDIT_TEXT_FIELD;
import static sc.style.CodeCheckStyle.CLASS_PROMPT_LABEL;
import static sc.style.CodeCheckStyle.CLASS_SLIDES_TABLE;
import static sc.style.CodeCheckStyle.CLASS_UPDATE_BUTTON;

/**
 * This class serves as the workspace component for the TA Manager
 * application. It provides all the user interface controls in 
 * the workspace area.
 * 
 * @author Richard McKenna
 */
public class CodeCheckWorkspace extends AppWorkspaceComponent {
    // THIS PROVIDES US WITH ACCESS TO THE APP COMPONENTS
    CodeCheckApp app;

    // THIS PROVIDES RESPONSES TO INTERACTIONS WITH THIS WORKSPACE
    CodeCheckController controller;

    // NOTE THAT EVERY CONTROL IS PUT IN A BOX TO HELP WITH ALIGNMENT
    HBox editImagesToolbar;
    public Button homeButton;
    public Button previousButton;
    public Button nextButton;
    
    int step=1;     // use to count step
    // FOR THE SLIDES TABLE
    
    
    // THE EDIT PANE
    GridPane leftPane;  // for left
    GridPane editPane;  // for right
    
    BorderPane workspaceBorderPane;
    // handle step 1
    Label step1;
    Label step1Text;
    ProgressBar extractionBar;
    Label progressLabel;
    public TableView<CodeHW> blackboardView;
    TableColumn<CodeHW,String> fileNameColumn;
    ScrollPane blackboardTableScrollPane;
    FlowPane buttpane;
    FlowPane progflow;
    public Button removeButton;
    public Button refreshButton;
    public Button viewButton;
    public Button extractButton;
    public TextArea extractText;
    ScrollPane textScroll;
    
    // handle step2
    Label step2;
    Label step2Text;
    public TableView<CodeHW> studentSubmitView;
    TableColumn <CodeHW,String> studentSubmitColumn;
    ScrollPane studentSubScroll;
    FlowPane renameFlow;
    ProgressBar renameBar;
    public TextArea studentSubmitText;
    ScrollPane studentSubmitScrooll;
    Label renameLabel;
    Button renButton;
    
    // handle step3
    Label step3;
    Label step3Text;
    public TableView<CodeHW> studentFileView;
    TableColumn <CodeHW,String> studentFileColumn;
    ScrollPane studentFileScroll;
    FlowPane unzipPane;
    Label unzipLabel;
    ProgressBar unzipBar;
    Button unzipButton;
    public TextArea unzipText;
    ScrollPane unzipScroll;
    
    // handle step4
    Label step4;
    Label step4Text;
    TableView<Homework> workView;
    TableColumn<Homework,StringProperty> workColumn;
    ScrollPane workScroll;
    GridPane cbPane;
    Label type;
    CheckBox cb1;
    CheckBox cb2;
    CheckBox cb3;
    CheckBox cb4;
    FlowPane cb5Pane;
    CheckBox cb5;
    TextField cb5L;
    FlowPane CodePane;
    Label CodeLabel;
    ProgressBar CodeBar;
    Button ExtractCodeButton;
    public TextArea workdText;
    ScrollPane workdScroll;
    
    // handle step 5
    Label step5;
    Label step5Text;
    TableView sworkView;
    TableColumn sworkColumn;
    ScrollPane sworkScroll;
    FlowPane checkPane;
    FlowPane cvPane;
    Label checkLabel;
    ProgressBar checkBar;
    Button codeCheckButton;
    Button viewResultButton;
    public TextArea checkText;
    ScrollPane checkScroll;
    
    /**
     * The constructor initializes the user interface for the
     * workspace area of the application.
     */
    public CodeCheckWorkspace(CodeCheckApp initApp) {
        // KEEP THIS FOR LATER
        app = initApp;

        // WE'LL NEED THIS TO GET LANGUAGE PROPERTIES FOR OUR UI
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        // LAYOUT THE APP
        initLayout();
        
        // HOOK UP THE CONTROLLERS
        initControllers();
        
        // AND INIT THE STYLE FOR THE WORKSPACE
        initStyle();
    }
    
    private void initLayout() {
        // WE'LL USE THIS TO GET UI TEXT
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        // FIRST MAKE ALL THE COMPONENTS
        editImagesToolbar = new HBox();
        homeButton = new Button(props.getProperty(HOME_BUTTON));
        homeButton.setDisable(true);
        homeButton.setOnAction(e->{
        nextButton.setDisable(false);
        step=1;
        controller.handleStep(step);
        });
        // "+ button"
        previousButton = new Button(props.getProperty(PREVIOUS_BUTTON));
        previousButton.setOnAction(e -> {
            step--;
            controller.handleStep(step);
        });
        previousButton.setDisable(true);
        //"-" button
        nextButton = new Button(props.getProperty(NEXT_BUTTON));
        nextButton.setOnAction(e ->{
            step++;
            controller.handleStep(step);
        });
        nextButton.setDisable(true);
        blackboardTableScrollPane = new ScrollPane();
        editPane = new GridPane();
        
        // THEM ORGANIZE THEM
        editImagesToolbar.getChildren().add(homeButton);
        editImagesToolbar.getChildren().add(previousButton);
        editImagesToolbar.getChildren().add(nextButton);
        
        /////////////////////////// step 1
        leftPane = new GridPane();
        step1=new Label("Step 1:Extract Submissions");
        step1Text=new Label("Select the submissions below from which to extract student work");
        blackboardView=new TableView<CodeHW>();
        fileNameColumn=new TableColumn("Blackboard Submissions");
        fileNameColumn.prefWidthProperty().bind(blackboardView.widthProperty());
        blackboardView.getColumns().add(fileNameColumn);
        fileNameColumn.setCellValueFactory(
                new PropertyValueFactory<CodeHW,String>("fileName")
        );
        blackboardTableScrollPane.setContent(blackboardView);
        blackboardTableScrollPane.setFitToWidth(true);
        blackboardTableScrollPane.setFitToHeight(true);
        removeButton=new Button("Remove");
        refreshButton=new Button("Refresh");
        viewButton=new Button("View");
        buttpane=new FlowPane();
        buttpane.getChildren().addAll(removeButton,refreshButton, viewButton);
        leftPane.add(step1,0,0);
        leftPane.add(step1Text,0,1);
        leftPane.add(blackboardTableScrollPane, 0, 2);
        leftPane.add(buttpane,0,3);
        
        editPane = new GridPane();
        progressLabel=new Label("Extraction Progress:");
        progflow=new FlowPane();
        extractionBar=new ProgressBar();
        extractionBar.setPrefWidth(200);
        extractionBar.setProgress(0);
        extractionBar.setId("47%");
        extractButton=new Button("Extract");
        
        extractText=TextAreaBuilder.create().prefWidth(500).wrapText(true).build();
        extractText.setDisable(true);
        extractText.setPrefHeight(500);
        
        textScroll=new ScrollPane();
        textScroll.setContent(extractText);
        progflow.getChildren().addAll(progressLabel,extractionBar);
        editPane.add(progflow, 0, 0);
        editPane.add(extractButton, 0, 1);
        editPane.add(textScroll, 0, 2);
        
        extractButton.setOnAction(e ->{
            try {
                controller.handleExtract();
            } catch (IOException ex) {
                Logger.getLogger(CodeCheckWorkspace.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        CodeCheckData data = (CodeCheckData)app.getDataComponent();
        //controller.handleZipFile();
        ObservableList<CodeHW> model = data.getZipFile();
        blackboardView.setItems(model);
        /////////////////////////////////////////////////////
        
        //////////////////////Step2
        step2=new Label("Step 2: Rename Student Submissions");
        step2Text=new Label("Click the Rename button to rename all submissions");
        studentSubmitView=new TableView();
        studentSubmitColumn=new TableColumn("Student Submissions");
        studentSubmitColumn.prefWidthProperty().bind(blackboardView.widthProperty());
        studentSubmitColumn.setCellValueFactory(
                new PropertyValueFactory<CodeHW,String>("fileName")
        );
        studentSubmitView.getColumns().add(studentSubmitColumn);
        studentSubScroll= new ScrollPane();
        studentSubScroll.setContent(studentSubmitView);
        studentSubScroll.setFitToWidth(true);
        studentSubScroll.setFitToHeight(true);
        renameFlow=new FlowPane();
        renameLabel=new Label("Rename Progress");
        renameBar= new ProgressBar();
        renameBar.setPrefWidth(200);
        renameBar.setProgress(0);
        renameFlow.getChildren().addAll(renameLabel,renameBar);
        renButton=new Button("Rename");
        
        studentSubmitText=TextAreaBuilder.create().prefWidth(500).wrapText(true).build();
        studentSubmitText.setDisable(true);
        studentSubmitText.setPrefHeight(500);
        studentSubmitScrooll=new ScrollPane();
        studentSubmitScrooll.setContent(studentSubmitText);
        
        renButton.setOnAction(e->{
            renButton.setDisable(true);
            controller.handleRename();
        });
        
        
        /////////////////// handle step3
        step3=new Label("Step 3: Unzip Student Submissions");
        step3Text=new Label("Select student submissions and click Unzip");
        studentFileView=new TableView();
        studentFileColumn=new TableColumn("Student ZIP Files");
        studentFileColumn.prefWidthProperty().bind(blackboardView.widthProperty());
        studentFileColumn.setCellValueFactory(
                new PropertyValueFactory<CodeHW,String>("fileName")
        );
        studentFileView.getColumns().add(studentFileColumn);
        studentFileScroll= new ScrollPane();
        studentFileScroll.setContent(studentFileView);
        studentFileScroll.setFitToWidth(true);
        studentFileScroll.setFitToHeight(true);
        unzipPane=new FlowPane();
        unzipLabel=new Label("Unzip Progress");
        unzipBar=new ProgressBar();
        unzipBar.setPrefWidth(200);
        unzipBar.setProgress(0);
        unzipPane.getChildren().addAll(unzipLabel, unzipBar);
        unzipButton=new Button("Unzip");
        
        unzipText=TextAreaBuilder.create().prefWidth(500).wrapText(true).build();
        unzipText.setDisable(true);
        unzipText.setPrefHeight(500);
        
        unzipScroll=new ScrollPane();
        unzipScroll.setContent(unzipText);
        unzipButton.setOnAction(e->{
            try {
                unzipButton.setDisable(true);
                controller.handleUnzip();
            } catch (IOException ex) {
                Logger.getLogger(CodeCheckWorkspace.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
       
        /////step 4
        step4=new Label("Step 4: Extract SourceCode");
        step4Text=new Label("Select students and click Extract Code");
        workView=new TableView();
        workColumn=new TableColumn("Student Work Directories");
        workColumn.prefWidthProperty().bind(blackboardView.widthProperty());
        workView.getColumns().add(workColumn);
        workColumn.setCellValueFactory(
                new PropertyValueFactory<Homework,StringProperty>("fileName")
        );
        workScroll= new ScrollPane();
        workScroll.setContent(workView);
        workScroll.setFitToWidth(true);
        workScroll.setFitToHeight(true);
        cbPane=new GridPane();
        type=new Label("Source File Type");
        cb1=new CheckBox();
        cb1.setText(".java");
        cb1.setSelected(true);
        cb2=new CheckBox();
        cb2.setText(".js");
        cb2.setSelected(true);
        cb3=new CheckBox(".c, .h, .cpp");
        cb3.setSelected(true);
        cb4=new CheckBox(".cs");
        cb4.setSelected(true);
        cb5Pane=new FlowPane();
        cb5=new CheckBox();
        cb5.setSelected(true);
        cb5L=new TextField();
        cb5Pane.getChildren().addAll(cb5,cb5L);
        CodePane=new FlowPane();
        CodeLabel=new Label("Code Progress");
        CodeBar=new ProgressBar();
        CodeBar.setPrefWidth(200);
        CodeBar.setProgress(0);
        CodePane.getChildren().addAll(CodeLabel,CodeBar);
        ExtractCodeButton=new Button("Extract Code");
        
        workdText=TextAreaBuilder.create().prefWidth(500).wrapText(true).build();
        workdText.setDisable(true);
        workdText.setPrefHeight(500);
        
        workdScroll=new ScrollPane();
        workdScroll.setContent(workdText);
        
        ExtractCodeButton.setOnAction(e->{
            ExtractCodeButton.setDisable(true);
            controller.handleExtractCode();
        });
        
        // handle step 5
        step5=new Label("Step 5: Code Check");
        step5Text=new Label("Select students and click Code Check");
        sworkView=new TableView();
        sworkColumn=new TableColumn("Student Work");
        sworkColumn.prefWidthProperty().bind(blackboardView.widthProperty());
        sworkView.getColumns().add(sworkColumn);
        sworkScroll= new ScrollPane();
        sworkScroll.setContent(sworkView);
        sworkScroll.setFitToWidth(true);
        sworkScroll.setFitToHeight(true);
        checkPane=new FlowPane();
        checkLabel=new Label("Check Progress");
        checkBar=new ProgressBar();
        checkBar.setPrefWidth(200);
        checkBar.setProgress(0.45);
        checkPane.getChildren().addAll(checkLabel,checkBar);
        cvPane=new FlowPane();
        codeCheckButton=new Button("Code Check");
        viewResultButton=new Button("View Result");
        cvPane.getChildren().addAll(codeCheckButton,viewResultButton);
        
        checkText=TextAreaBuilder.create().prefWidth(500).wrapText(true).build();
        checkText.setDisable(true);
        checkText.setPrefHeight(500);
        checkScroll=new ScrollPane();
        checkScroll.setContent(checkText);
        
        // AND THEN PUT EVERYTHING INSIDE THE WORKSPACE
        app.getGUI().getTopToolbarPane().getChildren().add(editImagesToolbar);
        workspaceBorderPane = new BorderPane();
        workspaceBorderPane.setCenter(leftPane);
        workspaceBorderPane.setRight(editPane);
        
        // AND SET THIS AS THE WORKSPACE PANE
        workspace = workspaceBorderPane;
    }
    
    public void activeButton(){
        homeButton.setDisable(true);
        previousButton.setDisable(true);
        nextButton.setDisable(false);;
    }
    
    private void initControllers() {
        // NOW LET'S SETUP THE EVENT HANDLING
        controller = new CodeCheckController(app);
        blackboardView.getSelectionModel().selectedItemProperty().addListener(e->{
            controller.handleSelectFile();
        });
    }
    
    
    // WE'LL PROVIDE AN ACCESSOR METHOD FOR EACH VISIBLE COMPONENT
    // IN CASE A CONTROLLER OR STYLE CLASS NEEDS TO CHANGE IT
    
    private void initStyle() {
        editImagesToolbar.getStyleClass().add(CLASS_BORDERED_PANE);
        homeButton.getStyleClass().add(CLASS_EDIT_BUTTON);
        previousButton.getStyleClass().add(CLASS_EDIT_BUTTON);
        nextButton.getStyleClass().add(CLASS_EDIT_BUTTON);
        removeButton.getStyleClass().add(CLASS_EDIT_BUTTON);
        refreshButton.getStyleClass().add(CLASS_EDIT_BUTTON);
        viewButton.getStyleClass().add(CLASS_EDIT_BUTTON);
        extractButton.getStyleClass().add(CLASS_EDIT_BUTTON);
        renButton.getStyleClass().add(CLASS_EDIT_BUTTON);
        unzipButton.getStyleClass().add(CLASS_EDIT_BUTTON);
        ExtractCodeButton.getStyleClass().add(CLASS_EDIT_BUTTON);
        codeCheckButton.getStyleClass().add(CLASS_EDIT_BUTTON);
        viewResultButton.getStyleClass().add(CLASS_EDIT_BUTTON);
        // THE SLIDES TABLE
        
        
        editPane.getStyleClass().add(CLASS_BORDERED_PANE);
        leftPane.getStyleClass().add(CLASS_BORDERED_PANE);
        
        blackboardView.getStyleClass().add(CLASS_SLIDES_TABLE);
        fileNameColumn.getStyleClass().add(CLASS_SLIDES_TABLE);
        step1.getStyleClass().add(CLASS_PROMPT_LABEL);
        step1Text.getStyleClass().add(CLASS_PROMPT_LABEL);
        progressLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        
        studentSubmitView.getStyleClass().add(CLASS_SLIDES_TABLE);
        studentSubmitColumn.getStyleClass().add(CLASS_SLIDES_TABLE);
        step2.getStyleClass().add(CLASS_PROMPT_LABEL);
        step2Text.getStyleClass().add(CLASS_PROMPT_LABEL);
        renameLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        
        studentFileView.getStyleClass().add(CLASS_SLIDES_TABLE);
        studentFileColumn.getStyleClass().add(CLASS_SLIDES_TABLE);
        step3.getStyleClass().add(CLASS_PROMPT_LABEL);
        step3Text.getStyleClass().add(CLASS_PROMPT_LABEL);
        unzipLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        
        workView.getStyleClass().add(CLASS_SLIDES_TABLE);
        workColumn.getStyleClass().add(CLASS_SLIDES_TABLE);
        step4.getStyleClass().add(CLASS_PROMPT_LABEL);
        step4Text.getStyleClass().add(CLASS_PROMPT_LABEL);
        type.getStyleClass().add(CLASS_PROMPT_LABEL);
        CodeLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        
        sworkView.getStyleClass().add(CLASS_SLIDES_TABLE);
        sworkColumn.getStyleClass().add(CLASS_SLIDES_TABLE);
        step5.getStyleClass().add(CLASS_PROMPT_LABEL);
        step5Text.getStyleClass().add(CLASS_PROMPT_LABEL);
        checkLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
    }
    public void toStep1(){
        homeButton.setDisable(true);
        previousButton.setDisable(true);
        workspaceBorderPane.getChildren().clear();
        leftPane.getChildren().clear();
        editPane.getChildren().clear();
        
        leftPane.add(step1,0,0);
        leftPane.add(step1Text,0,1);
        leftPane.add(blackboardTableScrollPane, 0, 2);
        leftPane.add(buttpane,0,3);
        
        editPane.add(progflow, 0, 0);
        editPane.add(extractButton, 0, 1);
        editPane.add(textScroll, 0, 2);
        
        workspaceBorderPane.setCenter(leftPane);
        workspaceBorderPane.setRight(editPane);
    }
    
    public void toStep2(){
        previousButton.setDisable(false);
        homeButton.setDisable(false);
        workspaceBorderPane.getChildren().clear();
        leftPane.getChildren().clear();
        editPane.getChildren().clear();
        leftPane.add(step2,0,0);
        leftPane.add(step2Text,0,1);
        leftPane.add(studentSubScroll,0,2);
        leftPane.add(buttpane,0,3);
        
        editPane.add(renameFlow, 0, 0);
        editPane.add(renButton, 0, 1);
        editPane.add(studentSubmitScrooll, 0, 2);
        
        workspaceBorderPane.setCenter(leftPane);
        workspaceBorderPane.setRight(editPane);
    }
    
    public void toStep3(){
        workspaceBorderPane.getChildren().clear();
        leftPane.getChildren().clear();
        editPane.getChildren().clear();
        
        leftPane.add(step3,0,0);
        leftPane.add(step3Text,0,1);
        leftPane.add(studentFileScroll,0,2);
        leftPane.add(buttpane,0,3);
        
        editPane.add(unzipPane, 0, 0);
        editPane.add(unzipButton, 0, 1);
        editPane.add(unzipScroll, 0, 2);
        
        workspaceBorderPane.setCenter(leftPane);
        workspaceBorderPane.setRight(editPane);
    }
    
    public void toStep4(){
        nextButton.setDisable(false);
        workspaceBorderPane.getChildren().clear();
        leftPane.getChildren().clear();
        editPane.getChildren().clear();
        
        leftPane.add(step4,0,0);
        leftPane.add(step4Text,0,1);
        leftPane.add(workScroll,0,2);
        leftPane.add(buttpane,0,3);
        leftPane.add(type, 0, 4);
        leftPane.add(cb1, 0, 5);
        leftPane.add(cb2, 1, 5);
        leftPane.add(cb3, 0, 6);
        leftPane.add(cb4, 1, 6);
        leftPane.add(cb5Pane, 0, 7);
        
        editPane.add(CodePane, 0, 0);
        editPane.add(ExtractCodeButton, 0, 1);
        editPane.add(workdScroll, 0, 2);
        
        workspaceBorderPane.setCenter(leftPane);
        workspaceBorderPane.setRight(editPane);
    }
    
    public void toStep5(){
        nextButton.setDisable(true);
        workspaceBorderPane.getChildren().clear();
        leftPane.getChildren().clear();
        editPane.getChildren().clear();
        
        leftPane.add(step5,0,0);
        leftPane.add(step5Text,0,1);
        leftPane.add(sworkScroll,0,2);
        leftPane.add(buttpane,0,3);
        
        editPane.add(checkPane, 0, 0);
        editPane.add(cvPane, 0, 1);
        editPane.add(checkScroll, 0, 2);
        
        workspaceBorderPane.setCenter(leftPane);
        workspaceBorderPane.setRight(editPane);
    }
    @Override
    public void resetWorkspace() {
        activeButton();
        controller.handleZipFile();
    }
    
    @Override
    public void reloadWorkspace(AppDataComponent dataComponent) {

    }

}