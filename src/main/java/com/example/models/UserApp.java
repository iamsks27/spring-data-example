package com.example.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "UserApp1")
public class UserApp {

    @DynamoDBHashKey
    @DynamoDBAttribute
    private String hiveid;

    @DynamoDBAttribute
    private String packagename;

    @DynamoDBAttribute
    private String uninstall_dd;

    @DynamoDBAttribute
    private String uninstall_hh;

    @DynamoDBAttribute
    private String uninstall_MM;

    @DynamoDBAttribute
    private String uninstall_ts;

    @DynamoDBAttribute
    private String uninstallcount;

    public UserApp() {
    }

    public String getHiveid() {
        return hiveid;
    }

    public void setHiveid(final String hiveid) {
        this.hiveid = hiveid;
    }

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(final String packagename) {
        this.packagename = packagename;
    }

    public String getUninstall_dd() {
        return uninstall_dd;
    }

    public void setUninstall_dd(final String uninstall_dd) {
        this.uninstall_dd = uninstall_dd;
    }

    public String getUninstall_hh() {
        return uninstall_hh;
    }

    public void setUninstall_hh(final String uninstall_hh) {
        this.uninstall_hh = uninstall_hh;
    }

    public String getUninstall_MM() {
        return uninstall_MM;
    }

    public void setUninstall_MM(final String uninstall_MM) {
        this.uninstall_MM = uninstall_MM;
    }

    public String getUninstall_ts() {
        return uninstall_ts;
    }

    public void setUninstall_ts(final String uninstall_ts) {
        this.uninstall_ts = uninstall_ts;
    }

    public String getUninstallcount() {
        return uninstallcount;
    }

    public void setUninstallcount(final String uninstallcount) {
        this.uninstallcount = uninstallcount;
    }
}
