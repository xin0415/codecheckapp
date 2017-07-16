/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sc.data;

import javafx.collections.ObservableList;
import djf.components.AppDataComponent;
import javafx.collections.FXCollections;
import sc.CodeCheckApp;
import sc.workspace.CodeCheckWorkspace;

/**
 * This is the data component for SlideshowCreatorApp. It has all the data needed
 * to be set by the user via the User Interface and file I/O can set and get
 * all the data from this object
 * 
 * @author Richard McKenna
 */
public class CodeCheckData implements AppDataComponent {

    // WE'LL NEED ACCESS TO THE APP TO NOTIFY THE GUI WHEN DATA CHANGES
    public CodeCheckApp app;

    // NOTE THAT THIS DATA STRUCTURE WILL DIRECTLY STORE THE
    // DATA IN THE ROWS OF THE TABLE VIEW

    /**
     * This constructor will setup the required data structures for use.
     * 
     * @param initApp The application this data manager belongs to. 
     */
    public CodeCheckData(CodeCheckApp initApp) {
        // KEEP THIS FOR LATER
        app = initApp;
        
        // MAKE THE SLIDES MODEL
    }

    @Override
    public void resetData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}