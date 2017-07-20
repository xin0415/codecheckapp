/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sc.data;

import java.io.File;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author xin
 */
public class CodeHW {
    private StringProperty fileNameProperty;
    private File file;
    public CodeHW(String initFileName,File initfile){
        fileNameProperty=new SimpleStringProperty(initFileName);
        file=initfile;
    }
    public StringProperty getFileNameProperty() {
        return fileNameProperty;
    }
    public String getFileName() {
        return fileNameProperty.getValue();
    }
    public void setFileName(String initFileName) {
        fileNameProperty.setValue(initFileName);
    }
    public void setFile(File f){
        file=f;
    }
    public File getFile(){
        return file;
    }
}
