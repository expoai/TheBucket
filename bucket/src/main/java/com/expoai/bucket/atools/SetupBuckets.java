package com.expoai.bucket.atools;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.SetBucketPolicyArgs;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SetupBuckets {

    private final MinioClient minioClient ;

    @Value("${minio.public-bucket}")
    private String publicBucket;

    @Value("${minio.private-bucket}")
    private String privateBucket;

    @Value("${minio.student-bucket:}")
    private String studentBucket;

    @PostConstruct
    public void initBuckets() throws Exception {
        createIfMissing(publicBucket, true);
        createIfMissing(privateBucket, false);

        if (!studentBucket.isBlank()) {
            createIfMissing(studentBucket, true);
        }
    }

    private void createIfMissing(String bucketName, boolean makePublic) throws Exception {
        boolean exists = minioClient.bucketExists(
                BucketExistsArgs.builder().bucket(bucketName).build()
        );

        if (!exists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());

            if (makePublic) {
                String policy = """
            {
              "Version":"2012-10-17",
              "Statement":[
                {
                  "Action":["s3:GetObject"],
                  "Effect":"Allow",
                  "Principal":"*",
                  "Resource":["arn:aws:s3:::%s/*"]
                }
              ]
            }
            """ ;

                minioClient.setBucketPolicy(SetBucketPolicyArgs.builder()
                        .bucket(bucketName)
                        .config(policy)
                        .build());
            }
        }
    }

}