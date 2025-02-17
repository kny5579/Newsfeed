package com.example.newsfeed.common.consts;

public enum OrderBy {

    CREATED_AT("createdAt"),
    UPDATED_AT("updatedAt"),
    LIKE_CNT("likeCnt");

    private final String fieldName;

    OrderBy(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
