/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sc.workspace;

import djf.ui.AppMessageDialogSingleton;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;
import properties_manager.PropertiesManager;
import sc.CodeCheckApp;
import static sc.CodeCheckProp.APP_PATH_WORK;
import static sc.CodeCheckProp.INVALID_IMAGE_PATH_MESSAGE;
import static sc.CodeCheckProp.INVALID_IMAGE_PATH_TITLE;
import sc.data.CodeCheckData;
import sc.workspace.CodeCheckWorkspace;

/**
 * This class provides responses to all workspace interactions, meaning
 * interactions with the application controls not including the file
 * toolbar.
 * 
 * @author Richard McKenna
 * @version 1.0
 */
public class CodeCheckController {
    // THE APP PROVIDES ACCESS TO OTHER COMPONENTS AS NEEDED
    CodeCheckApp app;

    /**
     * Constructor, note that the app must already be constructed.
     */
    public CodeCheckController(CodeCheckApp initApp) {
        // KEEP THIS FOR LATER
        app = initApp;
    }
    
    // CONTROLLER METHOD THAT HANDLES ADDING A DIRECTORY OF IMAGES
    public void handleAddAllImagesInDirectory() {
        try {
            // ASK THE USER TO SELECT A DIRECTORY
            DirectoryChooser dirChooser = new DirectoryChooser();
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            dirChooser.setInitialDirectory(new File(props.getProperty(APP_PATH_WORK)));
            File dir = dirChooser.showDialog(app.getGUI().getWindow());
            if (dir != null) {
                File[] files = dir.listFiles();
                for (File f : files) {
                    String fileName = f.getName();
                    if (fileName.toLowerCase().endsWith(".png") ||
                            fileName.toLowerCase().endsWith(".jpg") ||
                            fileName.toLowerCase().endsWith(".gif")) {
                        String path = f.getPath();
                        String caption = "";
                        Image slideShowImage = loadImage(path);
                    }
                }
            }
        }
        catch(MalformedURLException murle) {
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            String title = props.getProperty(INVALID_IMAGE_PATH_TITLE);
            String message = props.getProperty(INVALID_IMAGE_PATH_MESSAGE);
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(title, message);
        }
    }
    
    // THIS HELPER METHOD LOADS AN IMAGE SO WE CAN SEE IT'S SIZE
    public Image loadImage(String imagePath) throws MalformedURLException {
	File file = new File(imagePath);
	URL fileURL = file.toURI().toURL();
	Image image = new Image(fileURL.toExternalForm());
	return image;
    }
    public void handleStep(int s){
        if(s==1)
        {
         CodeCheckWorkspace workspace = (CodeCheckWorkspace)app.getWorkspaceComponent();
         workspace.toStep1();
        }else if(s==2){
         CodeCheckWorkspace workspace = (CodeCheckWorkspace)app.getWorkspaceComponent();
         workspace.toStep2();
        }else if (s==3){
         CodeCheckWorkspace workspace = (CodeCheckWorkspace)app.getWorkspaceComponent();
         workspace.toStep3();
        }else if(s==4){
         CodeCheckWorkspace workspace = (CodeCheckWorkspace)app.getWorkspaceComponent();
         workspace.toStep4();
        }else{
         CodeCheckWorkspace workspace = (CodeCheckWorkspace)app.getWorkspaceComponent();
         workspace.toStep5();
        }
        
    }
}