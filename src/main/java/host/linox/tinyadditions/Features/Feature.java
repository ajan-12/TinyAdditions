package host.linox.tinyadditions.Features;

import host.linox.tinyadditions.Data.DataStorage;

import org.bukkit.command.CommandExecutor;

public abstract class Feature implements CommandExecutor {

    private final FeatureType type;
    private boolean state;

    public Feature(final FeatureType type) {
        this.type = type;
    }

    public void enable() {}
    public void disable() {}

    public FeatureType getType() { return type; }
    public boolean isEnabled() { return state; }

    public void toggle(final boolean state) {
        if (state) {
            enable();
            DataStorage.getInstance().getFeatures().put(type, this);
        } else {
            disable();
            DataStorage.getInstance().getFeatures().remove(type);
        }
        this.state = state;
    }
}
