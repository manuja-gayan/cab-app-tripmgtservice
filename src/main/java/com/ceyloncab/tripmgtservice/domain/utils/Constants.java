package com.ceyloncab.tripmgtservice.domain.utils;

import lombok.Getter;

public class Constants {
    public static final String UNHANDLED_ERROR_CODE = "TMS3000";
    public static final String TOPIC_TRIP = "trip";
    public static final String TOPIC_TRIP_DATA = "trip-data";
    public static final String TOPIC_VEHICLE_DATA = "vehicle-data";
    @Getter
    public enum ResponseData {
        CREATE_SUCCESS("TMS1000", "Success","201"),
        QUERY_SUCCESS("TMS1001", "Query Success","200"),
        VERIFY_SUCCESS("TMS1002", "Verified","200"),
        PASSWORD_NOT_MATCHED("TMS2001", "Password not matched","400"),
        COMMON_FAIL("TMS2000", "Failed","400"),
        DRIVER_NOT_FOUND("TMS2002", "Driver not found","400"),
        CUSTOMER_NOT_FOUND("TMS2003", "Customer not found","400"),
        ADMIN_NOT_FOUND("UMS2002", "Admin not found","400"),
        INTERNAL_SERVER_ERROR("TMS3001", "Internal Server Error","500"),

        //KAFKA ERRORS
        NO_DRIVERS_FOUND("TMS4000", "No drivers found.Please try-again later","400");

        private final String code;
        private final String message;
        private final String responseCode;

        ResponseData(String code, String message, String responseCode) {
            this.code = code;
            this.message = message;
            this.responseCode= responseCode;
        }
    }
}
