package com.clarence.ToolHelper;

public enum GithubInfo {
    AUTHOR("PositionV2024"),
    REPOSITORYNAME("Economy"),
    CURRENTVERSION(Util.getEconomyPlugin().getDescription().getVersion());

    private String name;

    GithubInfo (String name) {
        this.name = name;
    }
    public String getName() { return name; }
}
