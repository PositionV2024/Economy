package com.clarence.ToolHelper;

public enum Messages {
    CONSOLE("\\u001B[0m CAN'T DO THIS THROUGH CONSOLE"),
    CONFIGURATION_ITEM_HAS_ALREADY_BEEN_ADDED(Colors.GREEN.getCode() + "%Shop_item_material%"+Colors.GRAY.getCode()+" has already been added"),
    CONFIGURATION_ITEM_ADDED(Colors.GRAY.getCode() + "Generated a new data for "+Colors.GREEN.getCode() + "%Shop_item_material%"),

    BALANCE_USAGE(Colors.GRAY.getCode() + "USAGE: balance | give | clear | version | reload"),
    BALANCE_HELP(Colors.GRAY.getCode() + "USAGE: /balance help"),
    BALANCE_FETCHED_DATA(Colors.GRAY.getCode() + "Your balance is " + Colors.GREEN.getCode() + "$%hashmap_player%"),

    BALANCE_GIVE_PLAYER_DOESNT_EXIST(Colors.GRAY.getCode() + "Please specific a player"),
    BALANCE_GIVE_AMOUNT_DOESNT_EXIST(Colors.GRAY.getCode() + "Please specific an amount to give"),

    BALANCE_GIVE_TARGET_DOSENT_EXIST(Colors.GRAY.getCode() + "The targeted player doesn't exist"),
    BALANCE_GIVE_TARGET_IS_NOT_IN_BALANCE_FILE(Colors.GRAY.getCode() + "The player doesn't exist in %Balance_file_path%"),

    BALANCE_GIVE_AMOUNT_INVALID(Colors.GRAY.getCode() + "An invalid amount"),
    BALANCE_GIVE_NOT_ENOUGH_FUNDS(Colors.GRAY.getCode() + "You do not have enough funds"),

    BALANCE_GIVE_SUCCESS(Colors.GRAY.getCode() + "You have given " + Colors.GREEN.getCode() + "%target_display_name% $%amount_given%"),
    BALANCE_GIVE_SUCCESS_TARGET_MESSAGE(Colors.GRAY.getCode() + "You have been given " + Colors.GREEN.getCode() + "$%amount_given%"),

    UPDATECHECKER_NOT_UPDATE_FOUND(Colors.GRAY.getCode() + "Your current version is " + Colors.GREEN.getCode() + "%current_version%"),
    UPDATECHECKER_UPDATE_FOUND(Colors.GRAY.getCode() + "There is a new version available. Please download the latest version here: " + Colors.GREEN.getCode() + "%latest_version_url%"),

    BALANCE_CLEAR_HELP(Colors.GRAY.getCode() + "USAGE: all | mine"),
    BALANCE_CLEAR_ALL_BALANCES(Colors.GRAY.getCode() + "Cleared all balances"),
    BALANCE_CLEAR_YOUR_BALANCE(Colors.GRAY.getCode() + "Cleared your balances"),

    SHOP_ITEM_EMPTY(Colors.GRAY.getCode() + "Shop items is empty"),
    SHOP_ITEMS(Colors.GRAY.getCode() + "Available items for purchase: "+Colors.GREEN.getCode()+"%Shop_items%"),
    SHOP_HELP(Colors.GRAY.getCode() + "USAGE: buy | sell | check | add"),

    SHOP_ITEM_CHECK_NOT_SPECIFY(Colors.GRAY.getCode() + "Please specific which item to check"),
    SHOP_ITEM_CHECK_NOT_FOUND(Colors.GRAY.getCode() + "Item doesn't exist"),
    SHOP_ITEM_CHECK(Colors.GRAY.getCode() + "Item material: " +Colors.GREEN.getCode()+"%Shop_item_material%"+Colors.GRAY.getCode()+" Item price:" + Colors.GREEN.getCode()+" $%Shop_item_price%/each"),

    ADD_ITEM_NO_ITEM_FOUND_IN_HAND(Colors.GRAY.getCode() + "Please have an item in your hand"),
    ADD_ITEM_NO_ITEM_PRICE_FOUND(Colors.GRAY.getCode() + "Please add a price to the item %item%");

    private String message;
    Messages (String message) {
        this.message = message;
    }
    public String getMessage() {return message; }
}
