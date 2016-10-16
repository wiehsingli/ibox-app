package edu.csupomona.cs585.ibox;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import static org.mockito.Mockito.*;

import org.mockito.Mockito;

import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.Drive.Files.List;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import edu.csupomona.cs585.ibox.sync.GoogleDriveFileSyncManager;



public class GoogleDriveFileSyncManagerTest {

	
	public Drive MockService;
	public GoogleDriveFileSyncManager FileSyncManager;
	public java.io.File localFile;
	public File file;
	public Files files;
	public java.util.List<File> ListofFiles;
	public FileList fileList;
	public String FileName = "testFile.txt";
	
	@Before
	public void setup(){
		
		MockService = mock(Drive.class);
		FileSyncManager = new GoogleDriveFileSyncManager(MockService);
		localFile = mock(java.io.File.class);
		
		file = new File();
		file.setId("test");
		file.setTitle(FileName);
		
		fileList = new FileList();
		
		ListofFiles= new ArrayList();
		ListofFiles.add(file);
		
		fileList.setItems(ListofFiles);
	}
	
//	@Override
//	public void addFile(java.io.File localFile) throws IOException {
//		//Insert a file
//		File body = new File();
//		body.setTitle(localFile.getName());
//		FileContent mediaContent = new FileContent("*/*", localFile);
//		File file = service.files().insert(body, mediaContent).execute();
//		System.out.println("File ID: " + file.getId());
//	}	
	
	
	@Test
	public void addFileTest() throws IOException{
		
		files = mock(Files.class);

		Files.Insert ins = mock(Files.Insert.class);
		when(MockService.files()).thenReturn(files);
		
			//Used Mockito any() to match file type
		when(files.insert(Mockito.any(File.class), Mockito.any(FileContent.class))).thenReturn(ins);
		when(ins.execute()).thenReturn(file);
		
		FileSyncManager.addFile(localFile);
		
		//confirm correct file
		assertEquals("test", file.getId());
		verify(ins).execute();
	}
	
//	public String getFileId(String fileName) {
//		try {
//			List request = service.files().list();
//			FileList files = request.execute();
//			for(File file : files.getItems()) {
//				if (file.getTitle().equals(fileName)) {
//					return file.getId();
//				}
//			}
//		} catch (IOException e) {
//			System.out.println("An error occurred: " + e);
//		}
//		return null;
//	}
	
	
//	public void updateFile(java.io.File localFile) throws IOException {
//		String fileId = getFileId(localFile.getName());
//		if (fileId == null) {
//			addFile(localFile);
//		} else {
//			File body = new File();
//			body.setTitle(localFile.getName());
//			FileContent mediaContent = new FileContent("*/*", localFile);
//			File file = service.files().update(fileId, body, mediaContent).execute();
//			System.out.println("File ID: " + file.getId());
//		}
//	}
	
	
	@Test
	public void updateFileTest() throws IOException{
		
		
		files = mock(Files.class);
		Files.Update update = mock(Files.Update.class);
		List request = mock(List.class);		
		
		//Mocking GetFileID
		when(localFile.getName()).thenReturn(FileName);
		when(files.list()).thenReturn(request);
		when(request.execute()).thenReturn(fileList);
		
		//mocking updatefile
		when(MockService.files()).thenReturn(files);
		when(files.update(any(String.class), any(File.class), any(FileContent.class))).thenReturn(update);
		when(update.execute()).thenReturn(file);
		
		FileSyncManager.updateFile(localFile);
		//confirm correct file
		
		verify(update).execute();
		assertEquals("test", file.getId());
	}
	
	
//	public void deleteFile(java.io.File localFile) throws IOException {
//		String fileId = getFileId(localFile.getName());
//		if (fileId == null) {
//			throw new FileNotFoundException();
//		} else {
//			service.files().delete(fileId).execute();
//		}
//	}

	@Test
	public void deleteFileTest() throws IOException{
		
		files = mock(Files.class);
		Files.Delete del = mock(Files.Delete.class);
		
		List request = mock(List.class);
		
		//Mocking GetFileID
		when(localFile.getName()).thenReturn(FileName);
		when(files.list()).thenReturn(request);
		when(request.execute()).thenReturn(fileList);
		
		//mocking deleteFile
		when(MockService.files()).thenReturn(files);
		when(files.delete(any(String.class))).thenReturn(del);
		when(del.execute()).thenReturn(null);	//file is gone
		
		FileSyncManager.deleteFile(localFile);
		verify(del).execute();
	}
	
	@Test
	public void getFileIdTest() throws IOException{
		
		files = mock(Files.class);
		List request = mock(List.class);
		
		//Mock Get File ID
		when(MockService.files()).thenReturn(files);
		when(files.list()).thenReturn(request);
		when(request.execute()).thenReturn(fileList);
		
		
		assertEquals("test",FileSyncManager.getFileId(FileName));
	}
	
}