/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sc.workspace;

import com.sun.deploy.uitoolkit.impl.fx.HostServicesFactory;
import com.sun.javafx.application.HostServicesDelegate;
import djf.ui.AppMessageDialogSingleton;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.Thread.sleep;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;
import properties_manager.PropertiesManager;
import sc.CodeCheckApp;
import static sc.CodeCheckProp.APP_PATH_WORK;
import static sc.CodeCheckProp.INVALID_IMAGE_PATH_MESSAGE;
import static sc.CodeCheckProp.INVALID_IMAGE_PATH_TITLE;
import sc.data.CodeCheckData;
import sc.workspace.CodeCheckWorkspace;
import java.util.zip.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sc.data.CodeHW;
import sc.data.Homework;

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
    CodeHW slFile;
    CodeHW selectedFile;
    int selectedIndex;
    String step1msg;
    Homework selected;
    Button okButton;
    Button canButton;

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
         CodeCheckData data = (CodeCheckData)app.getDataComponent();
         workspace.studentSubmitText.setDisable(false);
         workspace.studentSubmitView.setItems(data.getUnzipFile());
         workspace.studentSubmitView.refresh();
         workspace.toStep2();
        }else if (s==3){
         CodeCheckWorkspace workspace = (CodeCheckWorkspace)app.getWorkspaceComponent();
         CodeCheckData data = (CodeCheckData)app.getDataComponent();
         workspace.studentFileView.setItems(data.getStuFile());
         workspace.unzipText.setDisable(false);
         workspace.toStep3();
        }else if(s==4){
         CodeCheckWorkspace workspace = (CodeCheckWorkspace)app.getWorkspaceComponent();
         CodeCheckData data = (CodeCheckData)app.getDataComponent();
         workspace.workdText.setDisable(false);
         workspace.workView.setItems(data.getSt4());
         workspace.toStep4();
        }else{
         CodeCheckWorkspace workspace = (CodeCheckWorkspace)app.getWorkspaceComponent();
         CodeCheckData data = (CodeCheckData)app.getDataComponent();
         workspace.checkText.setDisable(false);
         workspace.sworkView.setItems(data.getSt5());
         workspace.toStep5();
        }
        
    }
    public void handleZipFile(){
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        File dir = new File(props.getProperty(APP_PATH_WORK));
        if (dir != null) {
            File[] files = dir.listFiles();
            for (File f : files) {
                String fileName = f.getName();
                if (fileName.toLowerCase().endsWith(".zip")) {
                    String path = f.getPath();
                    File file=new File(path);
                    CodeCheckData data = (CodeCheckData)app.getDataComponent();
                    CodeHW code=new CodeHW(file.getName(),file);
                    data.addZipFile(code);
                }
            }
        }
    }
    
    public void handleExtract() throws IOException{
        new Thread(new Runnable() {
           @Override public void run(){
               CodeCheckWorkspace workspace = (CodeCheckWorkspace)app.getWorkspaceComponent();
        CodeCheckData data = (CodeCheckData)app.getDataComponent();
        double i=0;
        double filesize=selectedFile.getFile().length();
        ZipInputStream zipInputStream = null;
               try {
                   zipInputStream = new ZipInputStream(new BufferedInputStream(new FileInputStream(selectedFile.getFile().getCanonicalFile())));
               } catch (IOException ex) {
                   Logger.getLogger(CodeCheckController.class.getName()).log(Level.SEVERE, null, ex);
               }
        ZipEntry zip=null;
        String msg="";
        String msgtxt="";
        
               try {
                   while((zip=zipInputStream.getNextEntry())!=null){
                       try{
                           byte[] buffer=new byte[8000];
                           String unzippedFile=selectedFile.getFile().getParent()+"/"+zip.getName();
                           FileOutputStream fileOutputStream=new FileOutputStream(unzippedFile);
                           int size;
                           while ((size=zipInputStream.read(buffer))!=-1){
                               fileOutputStream.write(buffer,0,size);
                           }
                           
                           fileOutputStream.flush();
                           fileOutputStream.close();
                           if(zip.getName().endsWith("zip"))
                               msg+=zip.getName()+"\n";
                           else
                               msgtxt+=zip.getName()+"\n";
                           
                           i+=zip.getSize();
                           workspace.extractionBar.setProgress(i/filesize);
                           sleep(5);
                           //System.out.println(i/filesize);
                           File file=new File(unzippedFile);
                           CodeHW code =new CodeHW(file.getName(),file);
                           if (file.getName().toLowerCase().endsWith(".zip")) {
                               data.addUnzipFile(code);
                           }
                           
                       }catch(Exception ex){
                       }} } catch (IOException ex) {
                   Logger.getLogger(CodeCheckController.class.getName()).log(Level.SEVERE, null, ex);
               }
               try {
                   zipInputStream.close();
               } catch (IOException ex) {
                   Logger.getLogger(CodeCheckController.class.getName()).log(Level.SEVERE, null, ex);
               }
            step1msg="Successfully extracted files:\n"+msg+"\nSubmission Errors:\n"+msgtxt;
            
            workspace.extractText.setText(step1msg);
            workspace.extractText.setDisable(false);
            workspace.extractionBar.setProgress(1);
        }}).start();
    }
    public void handleSelectFile(int s){
        if(s==1){
            CodeCheckWorkspace workspace = (CodeCheckWorkspace)app.getWorkspaceComponent();
            slFile= workspace.blackboardView.getSelectionModel().getSelectedItem();
            selectedFile = workspace.blackboardView.getSelectionModel().getSelectedItem();
            selectedIndex = workspace.blackboardView.getSelectionModel().getSelectedIndex();
        }else if(s==2){
            CodeCheckWorkspace workspace = (CodeCheckWorkspace)app.getWorkspaceComponent();
            selectedFile = workspace.studentSubmitView.getSelectionModel().getSelectedItem();
            selectedIndex = workspace.studentSubmitView.getSelectionModel().getSelectedIndex();
        }else if(s==3){
            CodeCheckWorkspace workspace = (CodeCheckWorkspace)app.getWorkspaceComponent();
            selectedFile = workspace.studentFileView.getSelectionModel().getSelectedItem();
            selectedIndex = workspace.studentFileView.getSelectionModel().getSelectedIndex();
        }
        else if(s==4){
            CodeCheckWorkspace workspace = (CodeCheckWorkspace)app.getWorkspaceComponent();
            selected = workspace.workView.getSelectionModel().getSelectedItem();
            selectedIndex = workspace.workView.getSelectionModel().getSelectedIndex();
        }else if(s==5){
            CodeCheckWorkspace workspace = (CodeCheckWorkspace)app.getWorkspaceComponent();
            selected = workspace.sworkView.getSelectionModel().getSelectedItem();
            selectedIndex = workspace.sworkView.getSelectionModel().getSelectedIndex();
        }
    }
    
    
    public void handleRename(){
        new Thread(new Runnable() {
           @Override public void run(){
        CodeCheckWorkspace workspace = (CodeCheckWorkspace)app.getWorkspaceComponent();
        CodeCheckData data = (CodeCheckData)app.getDataComponent();
        String s="Successfully renamed submissions:\n";
        for(int j=0;j<data.getUnzipFile().size();j++){
        String oldname=data.getUnzipFile().get(j).getFile().getName();
        s+=oldname;
        int i;
        boolean b=false;
        int k=0;
        for(i=0;i<oldname.length();i++){
            if(oldname.charAt(i)=='_'&&b==false)
            {
                b=true;
                k=i+1;
            }
            else if (oldname.charAt(i)=='_'&&b==true)
                break;
        }
        String newname=oldname.substring(k, i);
        s+="\nbecomes "+newname+".zip\n";
        workspace.renameBar.setProgress((double)j/data.getUnzipFile().size());
            try {
                sleep(5);
            } catch (InterruptedException ex) {
                Logger.getLogger(CodeCheckController.class.getName()).log(Level.SEVERE, null, ex);
            }
        //System.out.println((double)j/data.getUnzipFile().size());
        File file=new File(selectedFile.getFile().getParent()+"/"+newname+".zip");
        data.getUnzipFile().get(j).getFile().renameTo(file);
        CodeHW code=new CodeHW(file.getName(),file);
        data.addStuFile(code);
       }
       s+="\nRename Errors:\nnone";
       workspace.studentSubmitText.setText(s);
       workspace.renameBar.setProgress(1);
       }}).start();
    }
    public void handleUnzip() throws IOException{
        new Thread(new Runnable() {
           @Override public void run(){
        CodeCheckWorkspace workspace = (CodeCheckWorkspace)app.getWorkspaceComponent();
        CodeCheckData data = (CodeCheckData)app.getDataComponent();
        String ms="Successfully unzipped files:\n";
        String ems="\nUnzip Errors:\n";
        
        for(int i=0;i<data.getStuFile().size();i++)
        {
        String name=data.getStuFile().get(i).getFile().getName();
        int k=0;
        for(int j=0;j<name.length();j++){
            if(name.charAt(j)=='.')
                break;
            else
                k++;
        }
        String dirname=name.substring(0, k);
        boolean success=(new File(data.getStuFile().get(i).getFile().getParent()+"/"+dirname)).mkdirs();
        Homework h=new Homework(dirname,(data.getStuFile().get(i).getFile().getParent()+"/"+dirname));
        data.addSt4(h);
        if(success==true)
            ms+=data.getStuFile().get(i).getFile().getName()+"\n";
        else
            ems+=data.getStuFile().get(i).getFile().getName()+"\n";
        
        workspace.unzipBar.setProgress((double)i/data.getStuFile().size());
            try {
                sleep(5);
            } catch (InterruptedException ex) {
                Logger.getLogger(CodeCheckController.class.getName()).log(Level.SEVERE, null, ex);
            }
        //System.out.println((double)i/data.getStuFile().size());
        }
        workspace.unzipText.setText(ms+ems);
        workspace.unzipBar.setProgress(1);
        }}).start();
    }
    
    public void handleExtractCode(){
        new Thread(new Runnable() {
           @Override public void run(){
        CodeCheckWorkspace workspace = (CodeCheckWorkspace)app.getWorkspaceComponent();
        CodeCheckData data = (CodeCheckData)app.getDataComponent();
        String mse="Successful code extraction:\n";
        String msee="\nCode Extraction Errors\nnone";
        for(int i=0;i<data.getSt4().size();i++){
            mse+=data.getSt4().get(i).getFileName()+"\n";
        if(workspace.cb1.isSelected()==true)
            mse+=readZip(data.getStuFile().get(i).getFile(),workspace.cb1.getText(),data.getSt4().get(i).getPath());
        if(workspace.cb2.isSelected()==true)
            mse+=readZip(data.getStuFile().get(i).getFile(),workspace.cb2.getText(),data.getSt4().get(i).getPath());
        if(workspace.cb3.isSelected()==true){
            mse+=readZip(data.getStuFile().get(i).getFile(),".c",data.getSt4().get(i).getPath());
            mse+=readZip(data.getStuFile().get(i).getFile(),".h",data.getSt4().get(i).getPath());
            mse+=readZip(data.getStuFile().get(i).getFile(),".cpp",data.getSt4().get(i).getPath());
        }
        if(workspace.cb4.isSelected()==true)
            mse+=readZip(data.getStuFile().get(i).getFile(),workspace.cb4.getText(),data.getSt4().get(i).getPath());
        if(workspace.cb5.isSelected()==true&&!(workspace.cb5L.getText().isEmpty()))
            mse+=readZip(data.getStuFile().get(i).getFile(),workspace.cb5L.getText(),data.getSt4().get(i).getPath());
        Homework hw=new Homework(data.getSt4().get(i).getFileName(),data.getSt4().get(i).getPath());
        data.addSt5(hw);
        
        workspace.CodeBar.setProgress((double)i/data.getStuFile().size());
            try {
                sleep(5);
            } catch (InterruptedException ex) {
                Logger.getLogger(CodeCheckController.class.getName()).log(Level.SEVERE, null, ex);
            }
        //System.out.println((double)i/data.getStuFile().size());
        }
        workspace.workdText.setText(mse+msee);
        workspace.CodeBar.setProgress(1);
        }}).start();
    }
    public String readZip(File f,String t,String p){
        String s="";
    try {
      ZipFile zf = new ZipFile(f.getPath());
      Enumeration entries = zf.entries();
      while (entries.hasMoreElements()) {
        ZipEntry ze = (ZipEntry) entries.nextElement();
        
        
        
        if (ze.getName().toLowerCase().endsWith(t)) {
            for(int k=ze.getName().length()-1;k>=0;k--){
                if(ze.getName().charAt(k)=='/'){
                    s+=ze.getName().substring(k+1)+"\n"; 
                    break;
                }
            }
            byte[] buffer=new byte[1024];
            File file=new File(ze.getName());
            ZipInputStream zis=new ZipInputStream(new BufferedInputStream(new FileInputStream(f.getCanonicalFile())));
            File newFile=new File(p+File.separator+file.getName());
            FileOutputStream fos=new FileOutputStream(newFile);
            int len;
            while((len=zis.read(buffer))>0){
                fos.write(buffer,0,len);
            }
            
            long size = ze.getSize();
            if (size > 0) {
            BufferedReader br = new BufferedReader(
                new InputStreamReader(zf.getInputStream(ze)));
            br.close();
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return s;

    }
    
    
    public void handleRemove(int i){
        if (i==1){
            CodeCheckWorkspace workspace = (CodeCheckWorkspace)app.getWorkspaceComponent();
            CodeHW selected = (CodeHW)workspace.blackboardView.getSelectionModel().getSelectedItem();
            CodeCheckData data = (CodeCheckData)app.getDataComponent();
            data.removeZipFile(selected);
        }else if(i==2){
            CodeCheckWorkspace workspace = (CodeCheckWorkspace)app.getWorkspaceComponent();
            CodeHW selected = (CodeHW)workspace.studentSubmitView.getSelectionModel().getSelectedItem();
            CodeCheckData data = (CodeCheckData)app.getDataComponent();
            selected.getFile().delete();
            data.removeUnzip(selected);
        }else if (i==3){
            CodeCheckWorkspace workspace = (CodeCheckWorkspace)app.getWorkspaceComponent();
            CodeHW selected = (CodeHW)workspace.studentFileView.getSelectionModel().getSelectedItem();
            CodeCheckData data = (CodeCheckData)app.getDataComponent();
            selected.getFile().delete();
            data.removeStuFile(selected);
        }
        else if (i==4){
            CodeCheckWorkspace workspace = (CodeCheckWorkspace)app.getWorkspaceComponent();
            Homework selected = (Homework)workspace.workView.getSelectionModel().getSelectedItem();
            CodeCheckData data = (CodeCheckData)app.getDataComponent();
            data.removeSt4(selected);
        }else if (i==5){
            CodeCheckWorkspace workspace = (CodeCheckWorkspace)app.getWorkspaceComponent();
            Homework selected = (Homework)workspace.sworkView.getSelectionModel().getSelectedItem();
            CodeCheckData data = (CodeCheckData)app.getDataComponent();
            data.removeSt5(selected);
        }
    }
    public void handleRemoveRequest(int i){
        Stage stage=new Stage();
        stage.setTitle("Remove");
        
        okButton=new Button("OK");
        canButton=new Button("Cancel");
        FlowPane flow=new FlowPane();
        
        Group root=new Group();
        Scene scene=new Scene(root, 400, 50);
        stage.setScene(scene);

        GridPane grid=new GridPane();
        grid.setPadding(new Insets(10,10,10,10));
        grid.setVgap(5);
        grid.setHgap(5);
        scene.setRoot(grid);
        if(i==1){
        CodeCheckWorkspace workspace = (CodeCheckWorkspace)app.getWorkspaceComponent();
        CodeHW selected = (CodeHW)workspace.blackboardView.getSelectionModel().getSelectedItem();
        Label remove=new Label(" Do you want to remove  "+selected.getFileName());
        flow.getChildren().addAll(remove, okButton, canButton);
        }else if(i==2){
        CodeCheckWorkspace workspace = (CodeCheckWorkspace)app.getWorkspaceComponent();
        CodeHW selected = (CodeHW)workspace.studentSubmitView.getSelectionModel().getSelectedItem();
        Label remove=new Label(" Do you want to remove  "+selected.getFileName());
        flow.getChildren().addAll(remove, okButton, canButton);
        }else if(i==3){
        CodeCheckWorkspace workspace = (CodeCheckWorkspace)app.getWorkspaceComponent();
        CodeHW selected = (CodeHW)workspace.studentFileView.getSelectionModel().getSelectedItem();
        Label remove=new Label(" Do you want to remove  "+selected.getFileName());
        flow.getChildren().addAll(remove, okButton, canButton);
        }else if(i==4){
        CodeCheckWorkspace workspace = (CodeCheckWorkspace)app.getWorkspaceComponent();
        Homework selected = (Homework)workspace.workView.getSelectionModel().getSelectedItem();
        Label remove=new Label(" Do you want to remove  "+selected.getFileName());
        flow.getChildren().addAll(remove, okButton, canButton);
        }else if(i==5){
        CodeCheckWorkspace workspace = (CodeCheckWorkspace)app.getWorkspaceComponent();
        Homework selected = (Homework)workspace.sworkView.getSelectionModel().getSelectedItem();
        Label remove=new Label(" Do you want to remove  "+selected.getFileName());
        flow.getChildren().addAll(remove, okButton, canButton);
        }
        
        grid.getChildren().addAll(flow);
        okButton.setOnAction(e->{
            handleRemove(i);
            stage.close();
        });
        canButton.setOnAction(e->{
            stage.close();
        });
        stage.sizeToScene();
        stage.show();
    }
    
    public void handleCodeCheck(){
        CodeCheckWorkspace workspace = (CodeCheckWorkspace)app.getWorkspaceComponent();
        workspace.checkText.setText("Student Plaglarism Check Results can be found at\nhttp://www.exampleresults.edu");
        
        new Thread(new Runnable() {
           @Override public void run(){
               for(int i=0;i<=10;i++){
                   workspace.checkBar.setProgress((double)i/10);
                   try {
                       sleep(100);
                   } catch (InterruptedException ex) {
                       Logger.getLogger(CodeCheckController.class.getName()).log(Level.SEVERE, null, ex);
                   }
               }
        
        }}).start();
        
        
        
    }
    public void handleViewResult(){
        Stage stage=new Stage();
        stage.setTitle("View Result");
        Hyperlink link=new Hyperlink("http://www.exampleresults.edu");
        VBox box=new VBox();
        
        Group root=new Group();
        Scene scene=new Scene(root, 500, 500);
        stage.setScene(scene);

        GridPane grid=new GridPane();
        grid.setPadding(new Insets(10,10,10,10));
        grid.setVgap(5);
        grid.setHgap(5);
        scene.setRoot(grid);
        
        WebView browser=new WebView();
        WebEngine web=browser.getEngine();
        
        link.setOnAction(new EventHandler<ActionEvent>(){
            @Override public void handle(ActionEvent e){
                web.load("http://www.exampleresults.edu");
            }
        });
        
        
        Label view=new Label(" Click the link below to review result:");
        box.getChildren().addAll(view,link,browser);
        grid.getChildren().addAll(box);
        stage.sizeToScene();
        stage.show();
    }
    
    public void handleView(int s){
        Stage stage=new Stage();
        stage.setTitle("View Information");
        VBox box=new VBox();
        
        Group root=new Group();
        Scene scene=new Scene(root, 400, 200);
        stage.setScene(scene);
        
        GridPane grid=new GridPane();
        grid.setPadding(new Insets(10,10,10,10));
        grid.setVgap(5);
        grid.setHgap(5);
        scene.setRoot(grid);
        if(s==1){
            CodeCheckData data = (CodeCheckData)app.getDataComponent();
            for(int i=0;i<data.getZipFile().size();i++){
                Label view=new Label("file name: "+data.getZipFile().get(i).getFileName()+"\tpath: "+data.getZipFile().get(i).getFile().getPath());
                box.getChildren().addAll(view);
            }
        }else if(s==2){
            CodeCheckData data = (CodeCheckData)app.getDataComponent();
            for(int i=0;i<data.getUnzipFile().size();i++){
                Label view=new Label("file name: "+data.getUnzipFile().get(i).getFileName()+"\tpath: "+data.getUnzipFile().get(i).getFile().getPath());
                box.getChildren().addAll(view);
            }
        }else if(s==3){
            CodeCheckData data = (CodeCheckData)app.getDataComponent();
            for(int i=0;i<data.getStuFile().size();i++){
                Label view=new Label("file name: "+data.getStuFile().get(i).getFileName()+"\tpath: "+data.getStuFile().get(i).getFile().getPath());
                box.getChildren().addAll(view);
            }
        }else if(s==4){
            CodeCheckData data = (CodeCheckData)app.getDataComponent();
            for(int i=0;i<data.getSt4().size();i++){
                Label view=new Label("file name: "+data.getSt4().get(i).getFileName()+"\tpath: "+data.getSt4().get(i).getPath());
                box.getChildren().addAll(view);
            }
        }else if(s==5){
            CodeCheckData data = (CodeCheckData)app.getDataComponent();
            for(int i=0;i<data.getSt5().size();i++){
                Label view=new Label("file name: "+data.getSt5().get(i).getFileName()+"\tpath: "+data.getSt5().get(i).getPath());
                box.getChildren().addAll(view);
            }
        }
        
        grid.getChildren().addAll(box);
        stage.sizeToScene();
        stage.show();
    }
    public void handleRefresh(int i) throws IOException{
        if(i==1){
            CodeCheckData data = (CodeCheckData)app.getDataComponent();
            data.getZipFile().clear();
            handleZipFile();
        }else if (i==2){
            
        }else if(i==3){
            
        }else if(i==4){
            
        }else if(i==5){
            
        }
    }
}
