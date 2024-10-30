package com.kkr.common.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.Drive.Files.Update;
import com.google.api.services.drive.model.File;

public class GoogleDriveUtil {
	//private static Log logger=LogFactory.getLog(GoogleDriveUtil.class);
	
	// private static final String APPLICATION_NAME = "LogoLoader";
	 
	 private static final String APPLICATION_NAME = "Zsenia";
	 
	 
	    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	 
	    // Directory to store user credentials for this application.
	    private static final java.io.File CREDENTIALS_FOLDER //
	            = new java.io.File(System.getProperty("user.home"), "credentials");
	 
	    private static final String CLIENT_SECRET_FILE_NAME = "client_secret.json";
	 
	    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
	 
	    // Global instance of the {@link FileDataStoreFactory}.
	    private static FileDataStoreFactory DATA_STORE_FACTORY;
	 
	    // Global instance of the HTTP transport.
	    private static HttpTransport HTTP_TRANSPORT;
	 
	    private static Drive _driveService;
	 
	    static {
	        try {
	            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
	            DATA_STORE_FACTORY = new FileDataStoreFactory(CREDENTIALS_FOLDER);
	        } catch (Exception e) {
	            
	           // logger.error("Error while getting google drive authonication:", e);
	        }
	    }
	 
	    public static Credential getCredentials() {
	 
	    	Credential credential=null;
	    	try {
	        java.io.File clientSecretFilePath = new java.io.File(CREDENTIALS_FOLDER, CLIENT_SECRET_FILE_NAME);
	 
	        if (!clientSecretFilePath.exists()) {
	            throw new FileNotFoundException("Please copy " + CLIENT_SECRET_FILE_NAME //
	                    + " to folder: " + CREDENTIALS_FOLDER.getAbsolutePath());
	        }
	 
	        InputStream in = new FileInputStream(clientSecretFilePath);
	 
	        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
	 
	        // Build flow and trigger user authorization request.
	        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
	                clientSecrets, SCOPES).setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline").build();
	        
	        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
	         credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
	    	}
	        catch(Exception e)
	    	{
	        	//logger.error("Error on getting google drive credential:", e);
	    	}
	        return credential;
	    }
	 
	    public static Drive getDriveService() throws IOException {
	        if (_driveService != null) {
	            return _driveService;
	        }
	        Credential credential = getCredentials();
	        //
	        _driveService = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential) //
	                .setApplicationName(APPLICATION_NAME).build();
	        return _driveService;
	    }
	    
		public static  File uploadStreamContentOnGoogelDrive(AbstractInputStreamContent ais, Drive gDriveService, String fileName, String parentFolderID)
		{
			File file=new File();
			try
			{
	    	File fileMetadata = new File();
	    	fileMetadata.setName(fileName);
	    	fileMetadata.setParents(Collections.singletonList(parentFolderID));
	 
	         file = gDriveService.files().create(fileMetadata, ais)
	    	    .setFields("id, parents,webContentLink,webViewLink")
	    	    .execute(); 		
			}
			catch(Exception e)
			{
				//logger.error("Error on uploading logo at google drive:",e);
			}
			
			return file;
		}
		
		public static  Update updateStreamContentOnGoogleDrive(AbstractInputStreamContent ais, Drive gDriveService, String newFileName, String existingFileID, String parentFolderID)
		{
			try
			{
	    	File fileMetadata = new File();
	    	fileMetadata.setName(newFileName);
	    	fileMetadata.setParents(Collections.singletonList(parentFolderID));
	    	//File existingFile=gDriveService.files().get(existingFileID).execute();

	    	 Update updatedFile=gDriveService.files().update(existingFileID, fileMetadata, ais);
	    	 return updatedFile;
	    	 
			}
			catch(Exception e)
			{
				//logger.error("Error while updating logo on google drive", e);
				return null;
			}
			
		
		}
		
		public static File uploadPDFOnGoogleDrive(Drive driveService, String newFileName, String parentFolderID, FileContent mediaContent) throws Exception
		{
			File fileMetadata = new File();
			fileMetadata.setName(newFileName);
			fileMetadata.setParents(Collections.singletonList(parentFolderID));
			File file = driveService.files().create(fileMetadata, mediaContent)
			    .setFields("id, parents,webContentLink,webViewLink")
			    .execute();
			System.out.println("File ID: " + file.getId());
			System.out.println("Content Link: " + file.getWebContentLink());
			System.out.println("View Link: " + file.getWebViewLink());
			
			return file;
		}
		
		
		public static Update updatePDFOnGoogleDrive(Drive driveService, String newFileName, String parentFolderID, FileContent mediaContent, String existingFileID) throws Exception
		{
			
	    	File fileMetadata = new File();
	    	fileMetadata.setName(newFileName);
	    	fileMetadata.setParents(Collections.singletonList(parentFolderID));
	    	//File existingFile=gDriveService.files().get(existingFileID).execute();

	    	 Update updatedFile=driveService.files().update(existingFileID, fileMetadata, mediaContent);
	    	 return updatedFile;
	    	 	
		}
}
