package com.clarence.ToolHelper;

public enum Messages {
    CONSOLE("CAN'T DO THIS THROUGH CONSOLE"),

    BALANCE_USAGE(Colors.GRAY.getCode() + "USAGE: balance | give | clear | version | reload"),
    BALANCE_HELP(Colors.GRAY.getCode() + "USAGE: /balance help"),
    BALANCE_FETCHED_DATA(Colors.GRAY.getCode() + "Your balance is " + Colors.GREEN.getCode() + "$%hashmap_player%"),

    BALANCE_GIVE_PLAYER_DOESNT_EXIST(Colors.GRAY.getCode() + "Please specific a player"),
    BALANCE_GIVE_AMOUNT_DOESNT_EXIST(Colors.GRAY.getCode() + "Please specific an amount to give"),

    BALANCE_GIVE_TARGET_DOSENT_EXIST(Colors.GRAY.getCode() + "The targeted player doesn't exist"),
    BALANCE_GIVE_TARGET_IS_NOT_IN_BALANCE_FILE(Colors.GRAY.getCode() + "The player doesn't exist in %Balance_file_path%"),

    BALANCE_GIVE_AMOUNT_INVALID(Colors.GRAY.getCode() + "An invalid amount"),
    BALANCE_GIVE_NOT_ENOUGH_FUNDS(Colors.GRAY.getCode() + "You do not have enough funds"),

    BALANCE_GIVE_SUCCESS(Colors.GRAY.getCode() + "You have given " + Colors.GREEN.getCode() + "%target_display_name% $%amount_given%");
    private String message;
    Messages (String message) {
        this.message = message;
    }
    public String getMessage() {return message; }
}
