package com.ff.igtest.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;

public class Order implements Serializable {

    private String account;
    private Date submittedAt;
    private Date receivedAt;
    private String market;
    private Action action;
    private Double size;

    public Order() {
    }

    public Order(String account, Date submittedAt, Date receivedAt, String market, Action action, double size) {
        this.account = account;
        this.submittedAt = submittedAt;
        this.receivedAt = receivedAt;
        this.market = market;
        this.action = action;
        this.size = size;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Date getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Date submittedAt) {
        this.submittedAt = submittedAt;
    }

    public Date getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(Date receivedAt) {
        this.receivedAt = receivedAt;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    @Override
    public boolean equals(Object that) {
        return EqualsBuilder.reflectionEquals(this, that);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public static class OrderBuilder {
        private String account;
        private Date submittedAt;
        private Date receivedAt;
        private String market;
        private Action action;
        private Double size;

        public OrderBuilder account(String account) {
            this.account = account;
            return this;
        }

        public OrderBuilder submittedAt(Date submittedAt) {
            this.submittedAt = submittedAt;
            return this;
        }

        public OrderBuilder receivedAt(Date receivedAt) {
            this.receivedAt = receivedAt;
            return this;
        }

        public OrderBuilder market(String market) {
            this.market = market;
            return this;
        }

        public OrderBuilder action(Action action) {
            this.action = action;
            return this;
        }

        public OrderBuilder size(Double size) {
            this.size = size;
            return this;
        }

        public Order build() {
            return new Order(account, submittedAt, receivedAt, market, action, size);
        }
    }
}
