package com.clarence.ToolHelper;

import java.util.HashMap;
import java.util.UUID;

public class uuid {
    private static HashMap<UUID, Integer> UUID = new HashMap<>();
    public static HashMap<UUID, Integer> getUUID() { return uuid.UUID; }
}
