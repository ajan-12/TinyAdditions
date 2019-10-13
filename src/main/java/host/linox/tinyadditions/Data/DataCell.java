package host.linox.tinyadditions.Data;

import host.linox.tinyadditions.Features.Feature;
import host.linox.tinyadditions.Features.FeatureType;

import org.bukkit.configuration.file.FileConfiguration;

public abstract class DataCell extends Feature {

    public DataCell(final FeatureType type) {
        super(type);
    }

    @Override
    public void toggle(final boolean state) {
        super.toggle(state);
        DataStorage.getInstance().getDataCells().put(getType(), this);
    }

    public abstract void load(final FileConfiguration cfg);
    public abstract void save(final FileConfiguration cfg);
}
