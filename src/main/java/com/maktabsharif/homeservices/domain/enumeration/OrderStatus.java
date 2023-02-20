package com.maktabsharif.homeservices.domain.enumeration;

public enum OrderStatus {
    AWAITING_EXPERTS_SUGGESTION("Awaiting expert suggestion"),
    AWAITING_EXPERT_SELECTION("Awaiting expert selection"),
    AWAITING_EXPERT_TO_COME_TO_YOUR_PLACE("Awaiting expert to come to your place"),
    STARTED("Started"),
    DONE("Done"),
    PAID("Paid");
    private final String toString;

    private OrderStatus(String toString) {
        this.toString = toString;
    }

    public String toString(){
        return toString;
    }
}
