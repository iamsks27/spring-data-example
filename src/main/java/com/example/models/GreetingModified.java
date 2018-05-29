package com.example.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Date;
import java.util.Objects;

@DynamoDBTable(tableName = "Greeting")
public class GreetingModified {

    @DynamoDBHashKey
    @DynamoDBAttribute(attributeName = "hiveId")
    private String hiveid;

    @DynamoDBAttribute
    private String randomId;

    @DynamoDBAttribute
    private String message;

    @DynamoDBAttribute(attributeName = "date")
    private Date createdDate;

    @DynamoDBAttribute(attributeName = "campaign_count")
    private int count;

    public GreetingModified() {
    }

    public GreetingModified(final String hiveId, final String randomId, final String message, final Date createdDate, final int count) {
        this.hiveid = hiveId;
        this.randomId = randomId;
        this.message = message;
        this.createdDate = createdDate;
        this.count = count;
    }

    public String getHiveid() {
        return hiveid;
    }

    public void setHiveid(final String hiveid) {
        this.hiveid = hiveid;
    }

    public String getRandomId() {
        return randomId;
    }

    public void setRandomId(final String randomId) {
        this.randomId = randomId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getCount() {
        return count;
    }

    public void setCount(final int count) {
        this.count = count;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final GreetingModified that = (GreetingModified) o;
        return count == that.count &&
                Objects.equals(hiveid, that.hiveid) &&
                Objects.equals(randomId, that.randomId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(hiveid, randomId, count);
    }

    @Override
    public String toString() {
        return "GreetingModified{" +
                "hiveid='" + hiveid + '\'' +
                ", randomId='" + randomId + '\'' +
                ", message='" + message + '\'' +
                ", createdDate=" + createdDate +
                ", count=" + count +
                '}';
    }
}
