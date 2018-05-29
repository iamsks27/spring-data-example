package com.example.services;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.example.models.UserApp;
import com.example.repositories.UserAppRepository;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UserAppService {

    private final UserAppRepository appRepository;
    private final AmazonDynamoDB amazonDynamoDB;
    private final DynamoDB dynamoDB;
    private final DynamoDBMapper mapper;

    private final static int SCAN_ITEM_LIMIT = 1000;
    private final static int PARALLEL_SCAN_THREADS = 10;
    private final static String TABLE_TO_SCAN = "UserApp1";

    public UserAppService(final UserAppRepository appRepository, final AmazonDynamoDB amazonDynamoDB) {
        this.appRepository = appRepository;
        this.amazonDynamoDB = amazonDynamoDB;
        this.dynamoDB = new DynamoDB(this.amazonDynamoDB);
        this.mapper = new DynamoDBMapper(this.amazonDynamoDB);
    }

    public void getAllUserApp() {
        parallelScan();
        //return (List<UserApp>) appRepository.findAll();
    }

    private void parallelScan() {
        ExecutorService executorService = Executors.newFixedThreadPool(PARALLEL_SCAN_THREADS);
        final int totalSegments = PARALLEL_SCAN_THREADS;
        final AtomicInteger counter = new AtomicInteger();
        final CountDownLatch latch = new CountDownLatch(1);
        final Long startTime = Calendar.getInstance().getTimeInMillis();
        for (int i = 0; i < totalSegments; i++) {
            final int segment = i;
            executorService.execute(() -> {
                System.out.println("Scanning " + TABLE_TO_SCAN + " segment " + segment + " out of " + totalSegments
                        + " segments " + SCAN_ITEM_LIMIT + " items at a time...");
                int totalScannedItemCount = 0;
                Table table = dynamoDB.getTable(TABLE_TO_SCAN);
                try {
                    ScanSpec spec = new ScanSpec().withMaxResultSize(100000).withTotalSegments(totalSegments)
                            .withSegment(segment);

                    ItemCollection<ScanOutcome> items = table.scan(spec);
                    Iterator<Item> iterator = items.iterator();

                    Item currentItem;
                    while (iterator.hasNext()) {
                        totalScannedItemCount++;
                        currentItem = iterator.next();
                        //System.out.println(currentItem.toString());
                    }
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                } finally {
                    counter.addAndGet(totalScannedItemCount);
                    if (counter.get() >= 1000000) {
                        latch.countDown();
                    }
                    System.out.println("Scanned " + totalScannedItemCount + " items from segment " + segment + " out of "
                            + totalSegments + " of " + TABLE_TO_SCAN);
                }

            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
        final Long endTime = Calendar.getInstance().getTimeInMillis();
        System.out.println("Total Time taken: " + (endTime - startTime));
        System.out.println("Shutting down the executor service");
        executorService.shutdown();
    }

    public UserApp getUserAppByHiveId(final String id) {
        return appRepository.findByHiveid(id);
    }

    public List<UserApp> getUserAppsUsingPackage(final String s) {
        return appRepository.findByPackagename(s);
    }
}
