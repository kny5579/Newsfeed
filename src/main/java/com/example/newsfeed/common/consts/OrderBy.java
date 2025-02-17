package com.example.newsfeed.common.consts;

public enum OrderBy {

    CREATED_AT("createdAt"),
    UPDATED_AT("updatedAt"),
    LIKE("like");


    private final String fieldName;

    OrderBy(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
