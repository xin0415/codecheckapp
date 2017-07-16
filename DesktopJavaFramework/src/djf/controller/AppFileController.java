package djf.controller;

import djf.ui.AppYesNoCancelDialogSingleton;
import djf.ui.AppMessageDialogSingleton;
import djf.ui.AppGUI;
import djf.components.AppDataComponent;
import java.io.File;
import java.io.IOException;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import properties_manager.PropertiesManager;
import djf.AppTemplate;
import static djf.settings.AppPropertyType.LOAD_ERROR_MESSAGE;
import static djf.settings.AppPropertyType.LOAD_ERROR_TITLE;
import static djf.settings.AppPropertyType.LOAD_WORK_TITLE;
import static djf.settings.AppPropertyType.WORK_FILE_EXT;
import static djf.settings.AppPropertyType.WORK_FILE_EXT_DESC;
import static djf.settings.AppPropertyType.NEW_COMPLETED_MESSAGE;
import static djf.settings.AppPropertyType.NEW_COMPLETED_TITLE;
import static djf.settings.AppPropertyType.NEW_ERROR_MESSAGE;
import static djf.settings.AppPropertyType.NEW_ERROR_TITLE;
import static djf.settings.AppPropertyType.SAVE_COMPLETED_MESSAGE;
import static djf.settings.AppPropertyType.SAVE_COMPLETED_TITLE;
import static djf.settings.AppPropertyType.SAVE_ERROR_MESSAGE;
import static djf.settings.AppPropertyType.SAVE_ERROR_TITLE;
import static djf.settings.AppPropertyType.SAVE_UNSAVED_WORK_MESSAGE;
import static djf.settings.AppPropertyType.SAVE_UNSAVED_WORK_TITLE;
import static djf.settings.AppPropertyType.SAVE_WORK_TITLE;
import static djf.settings.AppStartupConstants.PATH_WORK;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * This class provides the event programmed responses for the file controls
 * that are provided by this framework.
 * 
 * @author Richard McKenna
 * @version 1.0
 */
public class AppFileController {
    // HERE'S THE APP
    AppTemplate app;
    
    // WE WANT TO KEEP TRACK OF WHEN SOMETHING HAS NOT BEEN SAVED
    boolean saved;
    public File saveAs;
    // THIS IS THE FILE FOR THE WORK CURRENTLY BEING WORKED ON
    File currentWorkFile;
    public String a;

    /**
     * This constructor just keeps the app for later.
     * 
     * @param initApp The application within which this controller
     * will provide file toolbar responses.
     */
    public AppFileController(AppTemplate initApp) {
        // NOTHING YET
        saved = true;
        app = initApp;
    }
    
    /**
     * This method marks the appropriate variable such that we know
     * that the current Work has been edited since it's been saved.
     * The UI is then updated to reflect this.
     * 
     * @param gui The user interface editing the Work.
     */
    public void markAsEdited(AppGUI gui) {
        // THE WORK IS NOW DIRTY
        saved = false;
        
        // LET THE UI KNOW
        gui.updateToolbarControls(saved);
    }

    /**
     * This method starts the process of editing new Work. If work is
     * already being edited, it will prompt the user to save it first.
     * 
     */
    public void handleNewRequest() {
        
	Stage stage=new Stage();
        stage.setTitle("Enter the name of the code check");

        Group root=new Group();
        Scene scene=new Scene(root, 300, 50);
        stage.setScene(scene);

        GridPane grid=new GridPane();
        grid.setPadding(new Insets(10,10,10,10));
        grid.setVgap(5);
        grid.setHgap(5);
        scene.setRoot(grid);

        Label name=new Label("Student Name");
        GridPane.setConstraints(name,0,0);
        grid.getChildren().add(name);

        TextField filename=new TextField();
        filename.setPromptText("Enter the name of code check");

        GridPane.setConstraints(filename,1,0);
        grid.getChildren().add(filename);

        filename.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent ke)
            {
                if (ke.getCode().equals(KeyCode.ENTER))
                {
                    if (!(filename.getText().isEmpty())){
                        a=filename.getText();
                        app.getGUI().a=a;
                        app.getGUI().welcomeLabel.setText("Code Check-"+a);
                        loadUI();
                        stage.close();
                    }
                }
            }
        });

        stage.sizeToScene();
        stage.show();
    }
    public void loadUI(){
        app.getWorkspaceComponent().resetWorkspace();
        app.getWorkspaceComponent().reloadWorkspace(app.getDataComponent());
        app.getWorkspaceComponent().activateWorkspace(app.getGUI().getAppPane());
        
    }
    public void handleRenameRequest(){
        Stage stage=new Stage();
        stage.setTitle("Rename");

        Group root=new Group();
        Scene scene=new Scene(root, 300, 50);
        stage.setScene(scene);

        GridPane grid=new GridPane();
        grid.setPadding(new Insets(10,10,10,10));
        grid.setVgap(5);
        grid.setHgap(5);
        scene.setRoot(grid);

        Label name=new Label("New Student Name");
        GridPane.setConstraints(name,0,0);
        grid.getChildren().add(name);

        TextField filename=new TextField();
        filename.setPromptText("Enter the new name of code check");

        GridPane.setConstraints(filename,1,0);
        grid.getChildren().add(filename);

        filename.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent ke)
            {
                if (ke.getCode().equals(KeyCode.ENTER))
                {
                    if (!(filename.getText().isEmpty())){
                        a=filename.getText();
                        app.getGUI().welcomeLabel.setText("Code Check-"+a);
                        loadUI();
                        stage.close();
                    }
                }
            }
        });

        stage.sizeToScene();
        stage.show();
    }
    
    public void handleCreateNewRequest(){
        Stage stage=new Stage();
        stage.setTitle("Enter the name of the code check");

        Group root=new Group();
        Scene scene=new Scene(root, 300, 50);
        stage.setScene(scene);

        GridPane grid=new GridPane();
        grid.setPadding(new Insets(10,10,10,10));
        grid.setVgap(5);
        grid.setHgap(5);
        scene.setRoot(grid);

        Label name=new Label("Student Name");
        GridPane.setConstraints(name,0,0);
        grid.getChildren().add(name);

        TextField filename=new TextField();
        filename.setPromptText("Enter the name of code check");

        GridPane.setConstraints(filename,1,0);
        grid.getChildren().add(filename);

        filename.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent ke)
            {
                if (ke.getCode().equals(KeyCode.ENTER))
                {
                    if (!(filename.getText().isEmpty())){
                        a=filename.getText();
                        app.getGUI().a=a;
                        app.getGUI().nextWindow();
                        loadUI();
                        stage.close();
                    }
                }
            }
        });

        stage.sizeToScene();
        stage.show();
    }
    /**
     * This method lets the user open a Course saved to a file. It will also
     * make sure data for the current Course is not lost.
     * 
     * @param gui The user interface editing the course.
     */
    public void handleAboutRequest(){
        Stage stage=new Stage();
        stage.setTitle("About the code check");

        Group root=new Group();
        Scene scene=new Scene(root, 400, 300);
        stage.setScene(scene);

        GridPane grid=new GridPane();
        grid.setPadding(new Insets(10,10,10,10));
        grid.setVgap(5);
        grid.setHgap(5);
        scene.setRoot(grid);

        Label about=new Label("Teaching Assistants in Computer Science courses typically must grade large numbers of student assignments each semester. The setup time for each assignment can be lengthy, depending on the technology used by a given course for managing student submissions. Stony Brook University uses Blackboard for providing student assignment grades, which also has a feature for students to submit their work. The problem with Blackboard is the process of retrieving student submissions and then extracting them into a format that works for grading is time consuming and involves a number of different steps and tools.    \n" +
"Code Check will be a desktop application that will make it easier for Teaching Assistants to extract student submissions into a proper format to setup grading as well as setting up checks for code plagiarism (what we’ll call a code check). ");
        about.setWrapText(true);
        //GridPane.setColumnSpan(name,2);
        grid.getChildren().add(about);


        stage.sizeToScene();
        stage.show();
    }
    public void handleLoadRequest() {
        try {
            // WE MAY HAVE TO SAVE CURRENT WORK
            boolean continueToOpen = true;
            if (!saved) {
                // THE USER CAN OPT OUT HERE WITH A CANCEL
                continueToOpen = promptToSave();
            }

            // IF THE USER REALLY WANTS TO OPEN A Course
            if (continueToOpen) {
                // GO AHEAD AND PROCEED LOADING A Course
                promptToOpen();
            }
            
        } catch (IOException ioe) {
            // SOMETHING WENT WRONG
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    PropertiesManager props = PropertiesManager.getPropertiesManager();
	    dialog.show(props.getProperty(LOAD_ERROR_TITLE), props.getProperty(LOAD_ERROR_MESSAGE));
        }
    }
    public void handleSaveAsRequest() throws IOException{
        if(saveAs!=null){
            saveWork(saveAs);
            saved=true;
        }
        else
            handleSaveRequest();
    }
    /**
     * This method will save the current course to a file. Note that we already
     * know the name of the file, so we won't need to prompt the user.
     * 
     * 
     * @param courseToSave The course being edited that is to be saved to a file.
     */
    public void handleSaveRequest() {
	// WE'LL NEED THIS TO GET CUSTOM STUFF
	PropertiesManager props = PropertiesManager.getPropertiesManager();
        app.getGUI().saveButton.setDisable(true);
        try {
	    // MAYBE WE ALREADY KNOW THE FILE
	    if (currentWorkFile != null) {
		saveWork(currentWorkFile);
	    }
	    // OTHERWISE WE NEED TO PROMPT THE USER
	    else {
		// PROMPT THE USER FOR A FILE NAME
		FileChooser fc = new FileChooser();
		fc.setInitialDirectory(new File(PATH_WORK));
		fc.setTitle(props.getProperty(SAVE_WORK_TITLE));
		fc.getExtensionFilters().addAll(
		new ExtensionFilter(props.getProperty(WORK_FILE_EXT_DESC), props.getProperty(WORK_FILE_EXT)));

		File selectedFile = fc.showSaveDialog(app.getGUI().getWindow());
		if (selectedFile != null) {
		    saveWork(selectedFile);
		}
	    }
        } catch (IOException ioe) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(LOAD_ERROR_TITLE), props.getProperty(LOAD_ERROR_MESSAGE));
        }
    }
    
    // HELPER METHOD FOR SAVING WORK
    private void saveWork(File selectedFile) throws IOException {
	// SAVE IT TO A FILE
	app.getFileComponent().saveData(app.getDataComponent(), selectedFile.getPath());
	
	// MARK IT AS SAVED
	currentWorkFile = selectedFile;
	saved = true;
	
	// TELL THE USER THE FILE HAS BEEN SAVED
	AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	PropertiesManager props = PropertiesManager.getPropertiesManager();
        dialog.show(props.getProperty(SAVE_COMPLETED_TITLE),props.getProperty(SAVE_COMPLETED_MESSAGE));
		    
	// AND REFRESH THE GUI, WHICH WILL ENABLE AND DISABLE
	// THE APPROPRIATE CONTROLS
	app.getGUI().updateToolbarControls(saved);	
    }
    
    /**
     * This method will exit the application, making sure the user doesn't lose
     * any data first.
     * 
     */
    public void handleExitRequest() {
        try {
            // WE MAY HAVE TO SAVE CURRENT WORK
            boolean continueToExit = true;
            if (!saved) {
                // THE USER CAN OPT OUT HERE
                continueToExit = promptToSave();
            }

            // IF THE USER REALLY WANTS TO EXIT THE APP
            if (continueToExit) {
                // EXIT THE APPLICATION
                System.exit(0);
            }
        } catch (IOException ioe) {
                AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
		PropertiesManager props = PropertiesManager.getPropertiesManager();
                dialog.show(props.getProperty(SAVE_ERROR_TITLE), props.getProperty(SAVE_ERROR_MESSAGE));
        }
    }

    /**
     * This helper method verifies that the user really wants to save their
     * unsaved work, which they might not want to do. Note that it could be used
     * in multiple contexts before doing other actions, like creating new
     * work, or opening another file. Note that the user will be
     * presented with 3 options: YES, NO, and CANCEL. YES means the user wants
     * to save their work and continue the other action (we return true to
     * denote this), NO means don't save the work but continue with the other
     * action (true is returned), CANCEL means don't save the work and don't
     * continue with the other action (false is returned).
     *
     * @return true if the user presses the YES option to save, true if the user
     * presses the NO option to not save, false if the user presses the CANCEL
     * option to not continue.
     */
    private boolean promptToSave() throws IOException {
	PropertiesManager props = PropertiesManager.getPropertiesManager();
	
	// CHECK TO SEE IF THE CURRENT WORK HAS
	// BEEN SAVED AT LEAST ONCE
	
        // PROMPT THE USER TO SAVE UNSAVED WORK
	AppYesNoCancelDialogSingleton yesNoDialog = AppYesNoCancelDialogSingleton.getSingleton();
        yesNoDialog.show(props.getProperty(SAVE_UNSAVED_WORK_TITLE), props.getProperty(SAVE_UNSAVED_WORK_MESSAGE));
        
        // AND NOW GET THE USER'S SELECTION
        String selection = yesNoDialog.getSelection();

        // IF THE USER SAID YES, THEN SAVE BEFORE MOVING ON
        if (selection.equals(AppYesNoCancelDialogSingleton.YES)) {
            // SAVE THE DATA FILE
            AppDataComponent dataManager = app.getDataComponent();
	    
	    if (currentWorkFile == null) {
		// PROMPT THE USER FOR A FILE NAME
		FileChooser fc = new FileChooser();
		fc.setInitialDirectory(new File(PATH_WORK));
		fc.setTitle(props.getProperty(SAVE_WORK_TITLE));
		fc.getExtensionFilters().addAll(
		new ExtensionFilter(props.getProperty(WORK_FILE_EXT_DESC), props.getProperty(WORK_FILE_EXT)));

		File selectedFile = fc.showSaveDialog(app.getGUI().getWindow());
		if (selectedFile != null) {
		    saveWork(selectedFile);
		    saved = true;
		}
	    }
	    else {
		saveWork(currentWorkFile);
		saved = true;
	    }
        } // IF THE USER SAID CANCEL, THEN WE'LL TELL WHOEVER
        // CALLED THIS THAT THE USER IS NOT INTERESTED ANYMORE
        else if (selection.equals(AppYesNoCancelDialogSingleton.CANCEL)) {
            return false;
        }

        // IF THE USER SAID NO, WE JUST GO ON WITHOUT SAVING
        // BUT FOR BOTH YES AND NO WE DO WHATEVER THE USER
        // HAD IN MIND IN THE FIRST PLACE
        return true;
    }

    /**
     * This helper method asks the user for a file to open. The user-selected
     * file is then loaded and the GUI updated. Note that if the user cancels
     * the open process, nothing is done. If an error occurs loading the file, a
     * message is displayed, but nothing changes.
     */
    private void promptToOpen() {
	// WE'LL NEED TO GET CUSTOMIZED STUFF WITH THIS
	PropertiesManager props = PropertiesManager.getPropertiesManager();
	
        // AND NOW ASK THE USER FOR THE FILE TO OPEN
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(PATH_WORK));
	fc.setTitle(props.getProperty(LOAD_WORK_TITLE));
        File selectedFile = fc.showOpenDialog(app.getGUI().getWindow());
        saveAs=selectedFile;

        // ONLY OPEN A NEW FILE IF THE USER SAYS OK
        if (selectedFile != null) {
            try {
                
                // RESET THE WORKSPACE
		app.getWorkspaceComponent().resetWorkspace();
                app.getGUI().welcomeLabel.setText("Code Check-"+selectedFile.getName());
                // RESET THE DATA
                //app.getDataComponent().resetData();
                
                // LOAD THE FILE INTO THE DATA
                //app.getFileComponent().loadData(app.getDataComponent(), selectedFile.getAbsolutePath());
                
		// MAKE SURE THE WORKSPACE IS ACTIVATED
		app.getWorkspaceComponent().activateWorkspace(app.getGUI().getAppPane());
                
                // AND MAKE SURE THE FILE BUTTONS ARE PROPERLY ENABLED
                //saved = true;
            } catch (Exception e) {
                AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                dialog.show(props.getProperty(LOAD_ERROR_TITLE), props.getProperty(LOAD_ERROR_MESSAGE));
            }
        }
    }

    /**
     * This mutator method marks the file as not saved, which means that when
     * the user wants to do a file-type operation, we should prompt the user to
     * save current work first. Note that this method should be called any time
     * the course is changed in some way.
     */
    public void markFileAsNotSaved() {
        saved = false;
    }

    /**
     * Accessor method for checking to see if the current work has been saved
     * since it was last edited.
     *
     * @return true if the current work is saved to the file, false otherwise.
     */
    public boolean isSaved() {
        return saved;
    }
}
