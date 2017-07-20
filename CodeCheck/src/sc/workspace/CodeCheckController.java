/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sc.workspace;

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
import javafx.stage.FileChooser;
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
    File selectedFile;
    int selectedIndex;
    String step1msg;

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
         workspace.studentSubmitView.setItems(data.getUnzipFile());
         workspace.extractText.clear();
         workspace.studentSubmitView.refresh();
         workspace.toStep2();
        }else if (s==3){
         CodeCheckWorkspace workspace = (CodeCheckWorkspace)app.getWorkspaceComponent();
         workspace.extractText.clear();
         CodeCheckData data = (CodeCheckData)app.getDataComponent();
         workspace.studentFileView.setItems(data.getStuFile());
         workspace.toStep3();
        }else if(s==4){
         CodeCheckWorkspace workspace = (CodeCheckWorkspace)app.getWorkspaceComponent();
         workspace.extractText.clear();
         CodeCheckData data = (CodeCheckData)app.getDataComponent();
         workspace.workView.setItems(data.getSt4());
         workspace.toStep4();
        }else{
         CodeCheckWorkspace workspace = (CodeCheckWorkspace)app.getWorkspaceComponent();
         workspace.extractText.clear();
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
                    data.addZipFile(file);
                }
            }
        }
    }
    
    public void handleExtract() throws IOException{
        CodeCheckWorkspace workspace = (CodeCheckWorkspace)app.getWorkspaceComponent();
        CodeCheckData data = (CodeCheckData)app.getDataComponent();
        double i=0;
        double filesize=selectedFile.length();
        ZipInputStream zipInputStream=new ZipInputStream(new BufferedInputStream(new FileInputStream(selectedFile.getCanonicalFile())));
        ZipEntry zip=null;
        String msg="";
        String msgtxt="";
        
        while((zip=zipInputStream.getNextEntry())!=null){
            try{
                byte[] buffer=new byte[8000];
                String unzippedFile=selectedFile.getParent()+"/"+zip.getName();
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
                System.out.println(i/filesize);
                File file=new File(unzippedFile);
                if (file.getName().toLowerCase().endsWith(".zip")) {
                    data.addUnzipFile(file);
                }
                
            }catch(Exception ex){
            }}
            zipInputStream.close();
            step1msg="Successfully extracted files:\n"+msg+"\nSubmission Errors:\n"+msgtxt;
            
            workspace.extractText.setText(step1msg);
            workspace.extractText.setDisable(false);
            workspace.extractionBar.setProgress(1);
    }
    public void handleSelectFile(){
        CodeCheckWorkspace workspace = (CodeCheckWorkspace)app.getWorkspaceComponent();
        selectedFile = workspace.blackboardView.getSelectionModel().getSelectedItem();
        selectedIndex = workspace.blackboardView.getSelectionModel().getSelectedIndex();
    }
    
    public void sleep(int timeToSleep) {
	try {
	    Thread.sleep(timeToSleep);
	} catch (InterruptedException ie) {
	    ie.printStackTrace();
	}
    }
    
    public void handleRename(){
        CodeCheckWorkspace workspace = (CodeCheckWorkspace)app.getWorkspaceComponent();
        CodeCheckData data = (CodeCheckData)app.getDataComponent();
        String s="Successfully renamed submissions:\n";
        for(int j=0;j<data.getUnzipFile().size();j++){
        String oldname=data.getUnzipFile().get(j).getName();
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
        System.out.println((double)j/data.getUnzipFile().size());
        File file=new File(selectedFile.getParent()+"/"+newname+".zip");
        data.getUnzipFile().get(j).renameTo(file);
        data.addStuFile(file);
       }
       s+="\nRename Errors:\nnone";
       workspace.extractText.setText(s);
       workspace.renameBar.setProgress(1);
    }
    public void handleUnzip() throws IOException{
        CodeCheckWorkspace workspace = (CodeCheckWorkspace)app.getWorkspaceComponent();
        CodeCheckData data = (CodeCheckData)app.getDataComponent();
        String ms="Successfully unzipped files:\n";
        String ems="\nUnzip Errors:\n";
        
        for(int i=0;i<data.getStuFile().size();i++)
        {
        String name=data.getStuFile().get(i).getName();
        int k=0;
        for(int j=0;j<name.length();j++){
            if(name.charAt(j)=='.')
                break;
            else
                k++;
        }
        String dirname=name.substring(0, k);
        boolean success=(new File(data.getStuFile().get(i).getParent()+"/"+dirname)).mkdirs();
        createUnzip(data.getStuFile().get(i),data.getStuFile().get(i).getParent()+"/"+dirname);
        Homework h=new Homework(dirname,(data.getStuFile().get(i).getParent()+"/"+dirname));
        data.addSt4(h);
        if(success==false)
            ms+=data.getStuFile().get(i).getName()+"\n";
        else
            ems+=data.getStuFile().get(i).getName()+"\n";
        
        workspace.unzipBar.setProgress((double)i/data.getStuFile().size());
        System.out.println((double)i/data.getStuFile().size());
        }
        workspace.extractText.setText(ms+ems);
        workspace.unzipBar.setProgress(1);
    }
    public void createUnzip(File f,String p) throws IOException{
        ZipInputStream zipInputStream=new ZipInputStream(new BufferedInputStream(new FileInputStream(f.getCanonicalFile())));
        ZipEntry zip=null;
        
        while((zip=zipInputStream.getNextEntry())!=null){
            try{
                byte[] buffer=new byte[8000];
                String unzippedFile=p+"/"+zip.getName();
                FileOutputStream fileOutputStream=new FileOutputStream(unzippedFile);
                int size;
                while ((size=zipInputStream.read(buffer))!=-1){
                    fileOutputStream.write(buffer,0,size);
                }
                
                fileOutputStream.flush();
                fileOutputStream.close();
                
            }catch(Exception ex){
            }}
            zipInputStream.close();
    }
    
    public void handleExtractCode(){
        CodeCheckWorkspace workspace = (CodeCheckWorkspace)app.getWorkspaceComponent();
        CodeCheckData data = (CodeCheckData)app.getDataComponent();
        String mse="Successful code extraction:\n";
        String msee="\nCode Extraction Errors\nnone";
        for(int i=0;i<data.getStuFile().size();i++){
            mse+=data.getSt4().get(i).getFileName()+"\n";
        if(workspace.cb1.isSelected()==true)
            mse+=readZip(data.getStuFile().get(i),workspace.cb1.getText());
        if(workspace.cb2.isSelected()==true)
            mse+=readZip(data.getStuFile().get(i),workspace.cb2.getText());
        if(workspace.cb3.isSelected()==true){
            mse+=readZip(data.getStuFile().get(i),".c");
            mse+=readZip(data.getStuFile().get(i),".h");
            mse+=readZip(data.getStuFile().get(i),".cpp");
        }
        if(workspace.cb4.isSelected()==true)
            mse+=readZip(data.getStuFile().get(i),workspace.cb4.getText());
        if(workspace.cb5.isSelected()==true&&!(workspace.cb5L.getText().isEmpty()))
            mse+=readZip(data.getStuFile().get(i),workspace.cb5L.getText());
        }
        workspace.extractText.setText(mse+msee);
    }
    public String readZip(File f,String t){
        String s="";
    try {
      ZipFile zf = new ZipFile(f.getPath());
      Enumeration entries = zf.entries();
      while (entries.hasMoreElements()) {
        ZipEntry ze = (ZipEntry) entries.nextElement();
        
        if (ze.getName().toLowerCase().endsWith(t)) {
          s+=ze.getName()+"\n";
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
}
