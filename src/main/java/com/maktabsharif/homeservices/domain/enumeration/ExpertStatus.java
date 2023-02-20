package com.maktabsharif.homeservices.domain.enumeration;

public enum ExpertStatus {
    NEW("New"), APPROVED("Approved"), PENDING_APPROVAL("Pending approval");

    private final String toString;

    private ExpertStatus(String toString) {
        this.toString = toString;
    }

    public String toString(){
        return toString;
    }

}
