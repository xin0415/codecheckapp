/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sc.data;

import javafx.collections.ObservableList;
import djf.components.AppDataComponent;
import java.io.File;
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
    public ObservableList <CodeHW> zipFile;
    public ObservableList <CodeHW> unzipFile;
    public ObservableList<CodeHW> stuFile;
    public ObservableList<Homework> step4File;
    public ObservableList<Homework> step5File;

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
        zipFile= FXCollections.observableArrayList();
        unzipFile=FXCollections.observableArrayList();
        stuFile=FXCollections.observableArrayList();
        step4File=FXCollections.observableArrayList();
        step5File=FXCollections.observableArrayList();
        
        // MAKE THE SLIDES MODEL
    }
    
    public void addZipFile(CodeHW e){
        zipFile.add(e);
    }
    public ObservableList <CodeHW> getZipFile(){
        return zipFile;
    }
    public void removeZipFile(CodeHW e){
        zipFile.remove(e);
    }
    
    public void addUnzipFile(CodeHW e){
        unzipFile.add(e);
    }
    
    public ObservableList <CodeHW> getUnzipFile(){
        return unzipFile;
    }
    
    public void removeUnzip(CodeHW e){
        unzipFile.remove(e);
    }
    
    
    public void addStuFile(CodeHW e){
        stuFile.add(e);
    }
    public ObservableList <CodeHW> getStuFile(){
        return stuFile;
    }
    public void removeStuFile(CodeHW e){
        stuFile.remove(e);
    }
    
    
    public ObservableList <Homework> getSt4(){
        return step4File;
    }
    public void removeSt4(Homework e){
        step4File.remove(e);
    }
    
    public void addSt5(Homework h){
        step5File.add(h);
    }
    public ObservableList <Homework> getSt5(){
        return step5File;
    }
    
    public void removeSt5(Homework e){
        step5File.remove(e);
    }

    @Override
    public void resetData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void addSt4(Homework h) {
        step4File.add(h);
    }
    
}