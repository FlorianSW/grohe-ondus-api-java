package org.grohe.ondus.api.model;

public class UnexpectedResponse extends RuntimeException {
    public UnexpectedResponse(int expectedStatus, int actualStatus) {
        super("Expected response with status code: " + expectedStatus + ", but got: " + actualStatus);
    }
}
