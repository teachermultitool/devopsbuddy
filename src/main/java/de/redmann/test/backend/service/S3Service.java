package de.redmann.test.backend.service;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by redmann on 07.11.16.
 */
@Service
@Slf4j
public class S3Service
{
	public static final String		PROFILE_PICTURE_FILE_NAME	= "profilePicture";
	
	@Value ("${aws.s3.root.bucket.name}")
	private String					bucketName;
	@Value ("${aws.s3.profile}")
	private String					awsProfileName;
	@Value ("${image.store.tmp.folder}")
	private String					tmpImageStore;
	
	private final AmazonS3Client	s3Client;
	
	
	
	@Autowired
	public S3Service(AmazonS3Client s3Client)
	{
		this.s3Client = s3Client;
	}
	
	
	
	public String storeProfileImage(MultipartFile uploadedFile, String username) throws IOException
	{
		String profileImageUrl = null;
		if (uploadedFile != null && !uploadedFile.isEmpty())
		{
			byte[] bytes = uploadedFile.getBytes();
			Path tmpImageStoreFolder = Paths.get(tmpImageStore, username);
			if (Files.notExists(tmpImageStoreFolder))
			{
				log.info("Creating the tmp root for the s3 assets");
				Files.createDirectories(tmpImageStoreFolder);
			}
			Path tmpProfileImageFile = Paths.get(tmpImageStoreFolder.toAbsolutePath().toString(),
					PROFILE_PICTURE_FILE_NAME + "." + FilenameUtils.getExtension(uploadedFile.getOriginalFilename()));
			
			log.info("Tmp file will be saved to {}", tmpProfileImageFile.toAbsolutePath().toString());
			
			try (BufferedOutputStream stream = new BufferedOutputStream(Files.newOutputStream(tmpProfileImageFile)))
			{
				stream.write(bytes);
			}
			
			profileImageUrl = storeProfileImageToS3(tmpProfileImageFile, username);
			
			Files.deleteIfExists(tmpProfileImageFile);
			
		}
		
		return profileImageUrl;
	}
	
	
	
	private String storeProfileImageToS3(Path resource, String username)
	{
		String resourceUrl = null;
		
		if (!Files.exists(resource))
		{
			log.error("The file {} does not exist. Throwing an exception", resource.toAbsolutePath().toString());
			throw new IllegalArgumentException("The file " + resource.toAbsolutePath().toString() + " doesn't exist");
		}
		
		String rootBucketUrl = ensureBucketExists(bucketName);
		
		if (rootBucketUrl == null)
		{
			log.error(
					"The bucket {} does not exist and the application was not able to create it. The image won't be stored with the profile",
					bucketName);
		}
		else
		{
			AccessControlList acl = new AccessControlList();
			acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);
			
			String key = username + "/" + PROFILE_PICTURE_FILE_NAME + "."
					+ FilenameUtils.getExtension(resource.getFileName().toString());
			
			try
			{
				s3Client.putObject(new PutObjectRequest(bucketName, key, resource.toFile()).withAccessControlList(acl));
				resourceUrl = s3Client.getResourceUrl(bucketName, key);
			}
			catch (AmazonClientException ace)
			{
				log.error(
						"A client exception occurred while trying to store the profile image {} on s3. The profile image won't be stored",
						resource.toAbsolutePath().toString(), ace);
			}
		}
		return resourceUrl;
	}
	
	
	
	private String ensureBucketExists(String bucketName)
	{
		String bucketUrl = null;
		
		try
		{
			if (!s3Client.doesBucketExist(bucketName))
			{
				log.info("Bucket {} doesn't exists... Creating one", bucketName);
				s3Client.createBucket(bucketName, Region.EU_Frankfurt);
				log.info("Created bucket: {}", bucketName);
			}
			bucketUrl = s3Client.getResourceUrl(bucketName, null) + bucketName;
		}
		catch (AmazonClientException ace)
		{
			log.error("A client exception occurerd while connecting toS3. Will not execute action for bucket: {}", bucketName,
					ace);
		}
		return bucketUrl;
	}
}
