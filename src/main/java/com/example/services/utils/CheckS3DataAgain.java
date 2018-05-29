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
public class CheckS3DataAgain {
    private final AmazonS3 amazonS3;

    public CheckS3DataAgain(final AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    private static final String BUCKET_NAME = "lava-data-exchange";
    private static final String FILE_PREFIX = "product/imei_upload/";
    private static final String FILE_EXTENSION = ".csv";
//    private static final long FILE_SIZE = 100;
//    private static final long MOD_DELTA = 15;

    @Scheduled(cron = "*/30 * * * * *")
    public void schedule() {
//        System.out.println("Checking for s3 again");
//        final ListObjectsV2Request req = new ListObjectsV2Request()
//                .withBucketName(BUCKET_NAME)
//                .withPrefix(FILE_PREFIX);
//        final ListObjectsV2Result result = amazonS3.listObjectsV2(req);
//        final List<S3ObjectSummary> objects = result.getObjectSummaries();
//        System.out.println("Total files: " + objects.size());
//        if (checkForModifiedFiles(objects, FILE_EXTENSION)) {
//            System.out.println("Everything is fine");
//        } else {
//            System.out.println("No fine do something");
//        }
    }

    private boolean checkForModifiedFiles(final List<S3ObjectSummary> objects, final String fileExtension) {
        for (S3ObjectSummary os : objects) {
            //System.out.println("files: " + os.getKey() +" : " + os.getLastModified());
            if (os.getKey().endsWith(fileExtension)) {
                System.out.println(os.getKey() + " : " + os.getLastModified());
                final Date lastModified = os.getLastModified();
                if (DateHelper.isModifiedOnCurrentDate(lastModified)) {
                    System.out.println(os.getKey() + ": " + os.getLastModified());
                    //return true;
                }
            }
        }
        return false;
    }
}
