package com.maktabsharif.homeservices.domain.enumeration;

public enum ExpertStatus {
    NEW("New"), PENDING_APPROVAL("Pending approval"), APPROVED("Approved");

    private final String toString;

    private ExpertStatus(String toString) {
        this.toString = toString;
    }

    public String toString(){
        return toString;
    }

}
