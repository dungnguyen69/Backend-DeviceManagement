package com.fullstack.Backend.constant;

public class constant {
    public static final String DEFAULT_PAGE_NUMBER = "1";
    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_SORT_BY = "id";
    public static final String DEFAULT_SORT_DIRECTION = "desc";

    // For device keyword suggestion
    public static final int DEVICE_NAME_COLUMN = 0;
    public static final int DEVICE_PLATFORM_NAME_COLUMN = 1;
    public static final int DEVICE_PLATFORM_VERSION_COLUMN = 2;
    public static final int DEVICE_RAM_COLUMN = 3;
    public static final int DEVICE_SCREEN_COLUMN = 4;
    public static final int DEVICE_STORAGE_COLUMN = 5;
    public static final int DEVICE_OWNER_COLUMN = 6;
    public static final int DEVICE_INVENTORY_NUMBER_COLUMN = 7;
    public static final int DEVICE_SERIAL_NUMBER_COLUMN = 8;
    public static final int DEVICE_KEEPER_COLUMN = 9;

    // For request keyword suggestion
    public static final int REQUEST_REQUEST_ID_COLUMN = 0;
    public static final int REQUEST_DEVICE_NAME_COLUMN = 1;
    public static final int REQUEST_DEVICE_SERIAL_NUMBER_COLUMN = 2;
    public static final int REQUEST_REQUESTER_COLUMN = 3;
    public static final int REQUEST_CURRENT_KEEPER_COLUMN = 4;
    public static final int REQUEST_NEXT_KEEPER_COLUMN = 5;
    public static final int REQUEST_APPROVER_COLUMN = 6;

    // For user keyword suggestion
    public static final int USER_BADGE_ID_COLUMN = 0;
    public static final int USER_NAME_COLUMN = 1;
    public static final int USER_FIRST_NAME_COLUMN = 2;
    public static final int USER_LAST_NAME_COLUMN = 3;
    public static final int USER_EMAIL_COLUMN = 4;
    public static final int USER_PHONE_NUMBER_COLUMN = 5;

    // For import
    public static final int DEVICE_NAME = 0;
    public static final int DEVICE_STATUS = 1;
    public static final int DEVICE_ITEM_TYPE = 2;
    public static final int DEVICE_PLATFORM = 3;
    public static final int DEVICE_RAM = 4;
    public static final int DEVICE_SCREEN = 5;
    public static final int DEVICE_STORAGE = 6;
    public static final int DEVICE_INVENTORY_NUMBER = 7;
    public static final int DEVICE_SERIAL_NUMBER = 8;
    public static final int DEVICE_PROJECT = 9;
    public static final int DEVICE_ORIGIN = 10;
    public static final int DEVICE_COMMENTS = 11;

    // Request status
    public static final int APPROVED = 0;
    public static final int CANCELLED = 2;
    public static final int TRANSFERRED = 3;
    public static final int PENDING = 4;
    public static final int RETURNED = 5;
    public static final int EXTENDING = 6;
}
