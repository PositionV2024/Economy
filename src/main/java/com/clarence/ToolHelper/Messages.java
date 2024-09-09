package com.clarence.ToolHelper;

import org.bukkit.entity.Player;

public enum Messages {
    CONSOLE("CAN'T DO THIS THROUGH CONSOLE"),
    BALANCE_USAGE(Colors.GRAY.getCode() + "USAGE: balance | clear | version | reload"),
    BALANCE_HELP(Colors.GRAY.getCode() + "USAGE: /balance help"),
    BALANCE_FETCHED_DATA(Colors.GRAY.getCode() + "Your balance is " + Colors.GREEN.getCode() + "$");
    private String message;
    Messages (String message) {
        this.message = message;
    }
    public String getMessage() {return message; }
    public static String getBalance(Player player) {
        String d = String.valueOf(uuid.getUUID().get(player.getUniqueId()));
        return d;
    }
}
