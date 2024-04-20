package com.divum.MeetingRoomBlocker.Implementation;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.divum.MeetingRoomBlocker.Config.AwsS3Config;
import com.divum.MeetingRoomBlocker.DTO.ImageUploadResponseDTO;
import com.divum.MeetingRoomBlocker.Service.AwsServices;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class AwsServicesImplementations implements AwsServices {
    private final AwsS3Config awsS3Config;
    @Value("${aws.s3.credentials.bucket.name}")
    private String bucketName;
    @Override
    public ImageUploadResponseDTO uploadImage(MultipartFile multipartFile) {
        AmazonS3 amazonS3 = awsS3Config.awsS3ClientBuilder();
        String fileName = System.currentTimeMillis() + "-" + multipartFile.getOriginalFilename();
        try {
            try (InputStream inputStream = multipartFile.getInputStream()) {
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(multipartFile.getSize());
                metadata.setContentType(multipartFile.getContentType());
                amazonS3.putObject(new PutObjectRequest(bucketName, fileName, inputStream, metadata));
                amazonS3.setObjectAcl(bucketName, fileName, CannedAccessControlList.PublicRead);
                String imageUrl = amazonS3.getUrl(bucketName, fileName).toString();
                return ImageUploadResponseDTO.builder()
                        .error(false)
                        .imageUrl(imageUrl)
                        .errorString(null)
                        .build();
            }
        } catch (Exception e) {
            amazonS3.deleteObject(bucketName,fileName);
            return ImageUploadResponseDTO.builder()
                    .error(true)
                    .imageUrl(null)
                    .errorString(e.getMessage())
                    .build();
        }
    }
}
