package edu.csupomona.cs585.ibox;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.*;

import com.google.api.services.drive.Drive;

import edu.csupomona.cs585.ibox.sync.GoogleDriveFileSyncManager;
import edu.csupomona.cs585.ibox.sync.GoogleDriveServiceProvider;


public class GoogleDriveFileSyncManagerIntegrationTest {

	public static Drive drive;
	public File file;
	
	public String filePath, fileName;
	public GoogleDriveFileSyncManager FileSyncManager;
	
	@Before
	public void setup(){
		
		FileSyncManager = new GoogleDriveFileSyncManager(GoogleDriveServiceProvider.get().getGoogleDriveClient());
		filePath= "C:/Users/Wie Hsing Li/Desktop/CS585/test.txt";
	}

	@Test
	public void addFileTest() throws IOException{

		//creating a file to add using addFile
		file = new File(filePath);
		file.createNewFile();
		fileName = file.getName();
		System.out.println("Created file: " + fileName + " in " + filePath);
		
		//calling addFile with created file
		FileSyncManager.addFile(file);
		
		System.out.println("File added successfully");
		System.out.println("File: " + fileName + "  id:  " + FileSyncManager.getFileId(fileName));
		file.delete(); //delete after done testing
		System.out.println("Deleted file after testing");
	}
	
	@Test
	public void updateFileTest() throws IOException{
		
		//creating a file
		file = new File(filePath);
		file.createNewFile();
		String updateText = "test update";
		
		FileSyncManager.addFile(file);
		
		//Need to change file to test update
		BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true));
		writer.write(updateText);
		writer.flush();
		writer.close();
			
		//calling update File
		FileSyncManager.updateFile(file);
			
		System.out.println("File was modified and updated successfully");
		file.delete();	//delete file after done updating
		System.out.println("Deleted file after testing");
	}
	
	@Test
	public void deleteFileTest() throws IOException{
		
		//creating a file
		file = new File(filePath);
		file.createNewFile();
		fileName = file.getName();
		System.out.println("Created file: " + fileName + " in " + filePath);
		
		//adding file to be deleted
		FileSyncManager.addFile(file); 
		
		//deleting file using FileSyncManager
		FileSyncManager.deleteFile(file);  
		
		file.delete();
		
		assertFalse(file.exists());
		
		System.out.println("file deleted sucessfully");
	}

}
