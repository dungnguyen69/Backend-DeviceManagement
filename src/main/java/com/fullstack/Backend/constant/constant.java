package com.fullstack.Backend.constant;

public class constant {
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_SORT_BY = "id";
    public static final String DEFAULT_SORT_DIRECTION = "desc";
    public static final int EMPTY_LIST = 0;
    public static final Boolean SUCCESSFUL = true;

    // For keyword suggestion
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

    public static final int REQUEST_REQUEST_ID_COLUMN = 0;
    public static final int REQUEST_DEVICE_NAME_COLUMN = 1;
    public static final int REQUEST_REQUESTER_COLUMN = 2;
    public static final int REQUEST_CURRENT_KEEPER_COLUMN = 3;
    public static final int REQUEST_NEXT_KEEPER_COLUMN = 4;
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
    public static final int DEVICE_OWNER = 11;
    public static final int DEVICE_COMMENTS = 12;

    public static final int APPROVED = 0;
    public static final int REJECTED = 1;
    public static final int CANCELLED = 2;
    public static final int TRANSFERRED = 3;
    public static final int PENDING = 4;
    public static final int RETURNED = 5;
    public static final int EXTENDING = 6;

    public static final String SERVER_LOCATION = "src/main/resources/templates";
    public static final String TEMPLATE_IMPORT = "Template_Import_Device";
    public static final String EXTENSION = ".xlsx";

}
