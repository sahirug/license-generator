package com.home.test.model;

/**
 * @author Sahiru Gunawardene
 *
 * */

import java.util.Date;

public class License {
    String name;
    Date startDate;
    Date endDate;
    String computerId;
    int agentCount;
    int threadCount;

    public License() {

    }

    public License(String name, Date startDate, Date endDate, String computerId, int agentCount, int threadCount) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.computerId = computerId;
        this.agentCount = agentCount;
        this.threadCount = threadCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getComputerId() {
        return computerId;
    }

    public void setComputerId(String computerId) {
        this.computerId = computerId;
    }

    public int getAgentCount() {
        return agentCount;
    }

    public void setAgentCount(int agentCount) {
        this.agentCount = agentCount;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }
}
