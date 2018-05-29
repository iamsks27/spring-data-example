package com.example.services.utils;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class ProvisionThroughputCheckerService {

    private final DynamoDB dynamoDB;

    private static final Logger logger = LoggerFactory.getLogger(ProvisionThroughputCheckerService.class);

    private static final String TABLE_NAME = "Greeting";


    public ProvisionThroughputCheckerService(final DynamoDB dynamoDB) {
        this.dynamoDB = dynamoDB;
    }

    @Scheduled(cron = "0 0 */1 * * *")
    public void changeThroughput() throws InterruptedException {
        /*logger.info("Going to change the provisioned throughput of the table: " + TABLE_NAME);
        if (isEvenHour()) {
            updateThroughput(true);
        } else {
            updateThroughput(false);
        }*/
    }

    private void updateThroughput(final boolean increase) throws InterruptedException {
        final Table table = dynamoDB.getTable(TABLE_NAME);
        final Long readCapacityUnits = table.describe().getProvisionedThroughput().getReadCapacityUnits();
        final Long readCapacity, writeCapacity;
        if (increase) {
            readCapacity = 5L;
            writeCapacity = 5L;
        } else {
            readCapacity = 1L;
            writeCapacity = 1L;
        }
        if (!readCapacity.equals(readCapacityUnits)) {
            logger.info("Changing the provisioned throughput for " + TABLE_NAME);
            logger.info("Old throughput: " + readCapacityUnits);
            logger.info("New throughput: " + readCapacity);
            final ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput()
                    .withReadCapacityUnits(readCapacity)
                    .withWriteCapacityUnits(writeCapacity);
            table.updateTable(provisionedThroughput);
            table.waitForActive();
            logger.info("Provisioned throughput is changed for " + TABLE_NAME);
        }
    }

    private boolean isEvenHour() {
        logger.info("Current Hours: " + Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
        return (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) % 2 == 0);
    }
}