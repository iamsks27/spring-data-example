package com.example.services.utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CheckS3Data {

    private final AmazonS3 amazonS3;

    public CheckS3Data(final AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    private static final String BUCKET_NAME = "tertiarydata";
    private static final String FILE_PREFIX = "csv/";
    private static final String FILE_EXTENSION = ".csv";
    private static final long FILE_SIZE = 100;
    private static final long MOD_DELTA = 15;

    @Scheduled(cron = "0 */1 * * * *")
    public void listFiles() {
//        System.out.println("Checking for s3");
//        final ListObjectsV2Request req = new ListObjectsV2Request()
//                .withBucketName(BUCKET_NAME)
//                .withPrefix(FILE_PREFIX);
//        final ListObjectsV2Result result = amazonS3.listObjectsV2(req);
//        final List<S3ObjectSummary> objects = result.getObjectSummaries();
//        if (checkForModifiedFiles(objects, FILE_EXTENSION, MOD_DELTA, FILE_SIZE)) {
//            System.out.println("Everything is fine");
//        }
//        System.out.println("No fine do something");
    }

    private boolean checkForModifiedFiles(final List<S3ObjectSummary> objects, final String fileExtension, final Long modifiedDeltaDuration, final Long fileSize) {
        for (S3ObjectSummary os : objects) {
            System.out.println(os.getKey() + " : " + os.getSize() + " : " + os.getLastModified());
            if (os.getKey().endsWith(fileExtension)) {
                final Date lastModified = os.getLastModified();
                final long size = os.getSize();
                if (DateHelper.isModifiedOnCurrentDate(lastModified) && DateHelper.isModifiedInGivenMinutes(lastModified, modifiedDeltaDuration) && size >= fileSize) {
                    return true;
                }
            }
        }
        return false;
    }
}
