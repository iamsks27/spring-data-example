package com.example.services;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.example.models.Greeting;
import com.example.models.GreetingModified;
import com.example.repositories.GreetingModifiedRepository;
import com.example.repositories.GreetingRepository;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class GreetingService {

    private final GreetingRepository greetingRepository;
    private final AmazonDynamoDB amazonDynamoDB;
    private final DynamoDB dynamoDB;
    private final DynamoDBMapper mapper;
    private static final String TABLE_NAME = "Greeting";
    private final GreetingModifiedRepository greetingModifiedRepository;

    public GreetingService(GreetingRepository greetingRepository, AmazonDynamoDB amazonDynamoDB, final GreetingModifiedRepository greetingModifiedRepository) {
        this.greetingRepository = greetingRepository;
        this.amazonDynamoDB = amazonDynamoDB;
        this.greetingModifiedRepository = greetingModifiedRepository;
        this.dynamoDB = new DynamoDB(this.amazonDynamoDB);
        this.mapper = new DynamoDBMapper(this.amazonDynamoDB);
    }


    public void createTable() throws InterruptedException {
        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
        CreateTableRequest tableRequest = dynamoDBMapper.generateCreateTableRequest(Greeting.class).withTableName(TABLE_NAME);
        tableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));

        TableUtils.createTableIfNotExists(amazonDynamoDB, tableRequest);
        TableUtils.waitUntilActive(amazonDynamoDB, TABLE_NAME);
        DescribeTableRequest describeTableRequest = new DescribeTableRequest().withTableName(TABLE_NAME);
        TableDescription tableDescription = amazonDynamoDB.describeTable(describeTableRequest).getTable();
        System.out.println("Table Description: " + tableDescription);
    }

    public Greeting insertNewGreeting(Greeting greeting) {
        return greetingRepository.save(greeting);
    }

    public List<Greeting> getAllGreetings() {
        return greetingRepository.findAll();
    }

    public List<Greeting> getAllGreetingInLastThirtyDays() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, -30);
        Date date = c.getTime();

        final List<Greeting> greetings = greetingRepository.findByCreateDateGreaterThanEqual(date);
        return greetings.stream()
                .sorted(Comparator.comparing(Greeting::getCreateDate).reversed())
                .collect(Collectors.toList());
    }

    public List<Greeting> getAllGreetingSorted() {
        final List<Greeting> greetings = greetingRepository.findAll();
        return greetings.parallelStream()
                .sorted(Comparator.comparing(Greeting::getCreateDate).reversed())
                .collect(Collectors.toList());
    }

    public void insetInBulk() {
        final List<Greeting> greetings = generatedRandomGreetingItems();
//
//        Map<String, AttributeValue> firstAttribute = new HashMap<>();
//        firstAttribute.put("message", new AttributeValue().withS("Hello World, from dynamodb"));
        SimpleDateFormat dateFormatter = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        final String format = dateFormatter.format(new Date());
//        firstAttribute.put("createDate", new AttributeValue().withS(format));
//        PutRequest firstPutRequest = new PutRequest();
//        firstPutRequest.setItem(firstAttribute);
//        WriteRequest firstWriteRequest = new WriteRequest();
//        firstWriteRequest.setPutRequest(firstPutRequest);
//        List<WriteRequest> batchList = new ArrayList<>();
//        batchList.add(firstWriteRequest);
//        Map<String, List<WriteRequest>> batchTableRequests = new HashMap<>();
//        batchTableRequests.put("Greeting", batchList);
//        BatchWriteItemRequest batchWriteItemRequest = new BatchWriteItemRequest();
//        batchWriteItemRequest.setRequestItems(batchTableRequests);
//
//        amazonDynamoDB.batchWriteItem(batchWriteItemRequest);
//        try {
//
//            TableWriteItems greetingTableBatchWriteItems = new TableWriteItems("Greeting");
//            Item item = new Item();
//            item.withPrimaryKey("id", "dynamodb-random-key");
//            item.withString("message", "hello, world from dynamodb");
//            item.with("createDate", format);
//            greetingTableBatchWriteItems.withItemsToPut(item);
//
//            System.out.println("Making batch insert");
//            BatchWriteItemOutcome batchWriteItemOutcome = dynamoDB.batchWriteItem(greetingTableBatchWriteItems);
//            do {
//                final Map<String, List<WriteRequest>> unprocessedItems = batchWriteItemOutcome.getUnprocessedItems();
//                if (unprocessedItems.size() == 0) {
//                    System.out.println("No unprocessed items found");
//                } else {
//                    System.out.println("Retrieving the unprocessed items");
//                    batchWriteItemOutcome = dynamoDB.batchWriteItemUnprocessed(unprocessedItems);
//                }
//            } while (batchWriteItemOutcome.getUnprocessedItems().size() > 0);
//        } catch (Exception e) {
//            System.out.println("Failed to retrieve items: ");
//            e.printStackTrace(System.err);
//        }

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        int taskCount = 0;
        for (final List<Greeting> greetingList : Lists.partition(greetings, 25)) {
            final int count = ++taskCount;
            Runnable runnable = () -> {
                System.out.println("Running task: " + count);
                mapper.batchSave(greetingList);
                System.out.println("Done Running task: " + count);
            };
            executorService.execute(runnable);
        }

        executorService.shutdown();
        //mapper.batchSave(greetings);
    }

    private List<Greeting> generatedRandomGreetingItems() {
        final List<Greeting> greetings = new ArrayList<>();
        Greeting greeting;
        String message;
        for (int i = 0; i < 10000; i++) {
            message = "Hello, " + i;
            greeting = new Greeting(message, new Date());
            greeting.setId(String.valueOf(i));
            greetings.add(greeting);
        }
        return greetings;
    }

    public void updateGreeting() {
        final Table table = dynamoDB.getTable(TABLE_NAME);
        Map<String, String> expressionAttributeNames = new HashMap<>();
        expressionAttributeNames.put("#message", "message");
        //expressionAttributeNames.put("#createDate", "createDate");

        Map<String, Object> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":message", "Hello, 65061-12");
        //expressionAttributeValues.put(":createDate", String.valueOf(new Date()));

        UpdateItemOutcome updateItemOutcome = table.updateItem(
                new PrimaryKey("id", "65061")
                , "set #message = :message"
                , expressionAttributeNames
                , expressionAttributeValues
        );

    }

    public void insertNewGreetingModified() {
        final List<String> hiveIds = new ArrayList<>();
        String randomId = "5004";
        for (int i = 1100; i <= 1201; i++) {
            hiveIds.add(String.valueOf(i));
        }
        ExecutorService service = Executors.newFixedThreadPool(20);
        int totalPartitions = Lists.partition(hiveIds, 5).size();
        System.out.println("Total Partitions: " + totalPartitions);
        final CountDownLatch latch = new CountDownLatch(totalPartitions);
        for (final List<String> partitionedHiveIds : Lists.partition(hiveIds, 5)) {
            service.submit(() -> {
                try {
                    System.out.println("Currently running thread: " + Thread.currentThread().getName());
                    final List<GreetingModified> greetingModified = new ArrayList<>();
                    List<GreetingModified> filteredGreetingModified = greetingModifiedRepository.findByHiveidIn(partitionedHiveIds);
                    final Map<String, List<GreetingModified>> greetingModifiedGroupedByHiveId = filteredGreetingModified.stream()
                            .collect(Collectors.groupingBy(GreetingModified::getHiveid));
                    List<String> newHiveIds = partitionedHiveIds;
                    if (filteredGreetingModified.size() > 0) {
                        final List<String> hiveIdsReceivedLessThanThreeCampaigns = greetingModifiedGroupedByHiveId.entrySet()
                                .stream()
                                .filter(entry -> entry.getValue().size() < 3)
                                .map(Map.Entry::getKey)
                                .collect(Collectors.toList());
                        System.out.println("HiveIds received less than 3 campaigns: " + hiveIdsReceivedLessThanThreeCampaigns.size());
                        final List<String> hiveIdsReceivedEqualOrGreaterThanThreeCampaigns = greetingModifiedGroupedByHiveId.entrySet()
                                .stream()
                                .filter(entry -> entry.getValue().size() >= 3)
                                .map(Map.Entry::getKey)
                                .collect(Collectors.toList());
                        System.out.println("HiveIds received equal or more than 3 campaigns: " + hiveIdsReceivedEqualOrGreaterThanThreeCampaigns.size());
                        newHiveIds = partitionedHiveIds.stream()
                                .filter(hiveid -> !hiveIdsReceivedLessThanThreeCampaigns.contains(hiveid))
                                .collect(Collectors.toList());
                        if (hiveIdsReceivedEqualOrGreaterThanThreeCampaigns.size() > 0) {
                            newHiveIds.removeAll(hiveIdsReceivedEqualOrGreaterThanThreeCampaigns);
                        }
                        for (final String hiveIdsReceivedLessThanThreeCampaign : hiveIdsReceivedLessThanThreeCampaigns) {
                            final int size = greetingModifiedGroupedByHiveId.get(hiveIdsReceivedLessThanThreeCampaign).size() + 1;
                            final GreetingModified greetingModified1 = new GreetingModified(hiveIdsReceivedLessThanThreeCampaign, randomId, UUID.randomUUID().toString(), new Date(), size);
                            greetingModified.add(greetingModified1);
                            //System.out.println(greetingModified1);
                        }
                    }
                    System.out.println("Total new hive ids: " + newHiveIds.size());
                    for (final String newHiveId : newHiveIds) {
                        greetingModified.add(new GreetingModified(newHiveId, randomId, UUID.randomUUID().toString(), new Date(), 1));
                    }
                    mapper.batchSave(greetingModified);
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }
        service.shutdown();

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Finally hiveIds are inserted");
//        //System.out.println(searchHiveIds);
//        Set<GreetingModified> filteredGreetingModified = greetingModifiedRepository.findByHiveidIn(hiveIds);
//        final Map<String, List<GreetingModified>> greetingModifiedGroupedByHiveId = filteredGreetingModified.stream()
//                .collect(Collectors.groupingBy(GreetingModified::getHiveid));
//        Set<String> newHiveIds = hiveIds;
//        if (hiveIds.size() > 0 && filteredGreetingModified.size() > 0) {
//            final Set<String> filteredHiveIds = greetingModifiedGroupedByHiveId.entrySet()
//                                                        .stream()
//                                                        .filter(entry -> entry.getValue().size() < 3)
//                                                        .map(Map.Entry::getKey)
//                                                        .collect(Collectors.toSet());
//            System.out.println("Available hiveIds: " + filteredHiveIds.size());
//            newHiveIds = hiveIds.stream()
//                    .filter(id -> !filteredHiveIds.contains(id))
//                    .collect(Collectors.toCollection(LinkedHashSet::new));
//            System.out.println("New hiveIds: " + newHiveIds.size());
//
//            for (final String filteredHiveId : filteredHiveIds) {
//                final int size = greetingModifiedGroupedByHiveId.get(filteredHiveId).size() + 1;
//                uuid = UUID.randomUUID().toString();
//                final GreetingModified greetingModified1 = new GreetingModified(filteredHiveId, randomId, uuid, new Date(), size);
//                greetingModified.add(greetingModified1);
//                System.out.println(greetingModified1);
//            }
//        }
//        for (final String newHiveId : newHiveIds) {
//            uuid = UUID.randomUUID().toString();
//            greetingModified.add(new GreetingModified(newHiveId, randomId, uuid, new Date(), 1));
//        }
//
//        System.out.println("Total No of insertion: " + greetingModified.size());
////        for (int i = 1; i <= 1000; i++) {
////            uuid = UUID.randomUUID().toString() + "-" + i;
////            randomId = "5000";
////            message = "Hello, " + uuid;
////            greetingModified.add(new GreetingModified(hiveId, randomId, message, new Date()));
////        }
//        ExecutorService executorService = Executors.newFixedThreadPool(10);
//        int taskCount = 0;
//        for (final List<GreetingModified> greetingList : Lists.partition(greetingModified, 25)) {
//            //System.out.println(greetingList);
//            final int count = ++taskCount;
//            Runnable runnable = () -> {
//                System.out.println("Running task: " + count);
//                mapper.batchSave(greetingList);
//                System.out.println("Done Running task: " + count);
//            };
//            executorService.execute(runnable);
//        }
//
//        executorService.shutdown();
    }

    public List<GreetingModified> getAllGreetingModifiedSorted() {
        final List<String> randomId = new ArrayList<>();
        for (int i = 1; i <= 1000; i++) {
            randomId.add(String.valueOf(i));
        }
        System.out.println(randomId);
        //final List<GreetingModified> byRandomId = greetingModifiedRepository.findByRandomId(randomId);
        final List<GreetingModified> random = greetingModifiedRepository.findByRandomId("500");
        final List<GreetingModified> greetingModified = new ArrayList<>(random);
        System.out.println(greetingModified);
        return greetingModified.stream()
                .sorted(Comparator.comparing(GreetingModified::getCreatedDate).reversed())
                .collect(Collectors.toList());
    }

    public Iterable<GreetingModified> getAllGreetingModified() {
        return greetingModifiedRepository.findAll();
    }
}
