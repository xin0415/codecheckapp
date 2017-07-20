/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sc.data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author xin
 */
public class Homework {
    private StringProperty fileNameProperty;
    private StringProperty pathProperty;
    public Homework(String initFileName,String initPath){
        fileNameProperty=new SimpleStringProperty(initFileName);
        pathProperty=new SimpleStringProperty(initPath);
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
    public StringProperty getPathProperty() {
        return pathProperty;
    }
    public String getPath() {
        return pathProperty.getValue();
    }
    public void setPath(String initPath) {
        pathProperty.setValue(initPath);
    }
}
