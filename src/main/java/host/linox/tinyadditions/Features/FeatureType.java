package host.linox.tinyadditions.Features;

public enum FeatureType {

    FORCEMESSAGE("forcemessage"),
    PERMISSIONMESSAGE("permissionedmessage"),
    FORCESUDO("forcesudo"),
    GETTEXTURE("gettexture", "gettextureother"),
    SETTEXTURE("settexture"),
    SPAWN("setspawn", "spawn");

    private String[] commands;

    FeatureType(final String... commands) {
        this.commands = commands;
    }

    public String[] getCommands() {
        return commands;
    }
}
