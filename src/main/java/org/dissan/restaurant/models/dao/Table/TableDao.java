package org.dissan.restaurant.models.dao.Table;

import org.dissan.restaurant.models.Table;
import org.dissan.restaurant.models.dao.shift.ShiftDaoFs;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Logger;

public class TableDao {
    private static final Map<String,Table> LOCAL_CACHE  = new HashMap<>();
    private static final Logger LOGGER = Logger.getLogger(TableDao.class.getSimpleName());
    public static @NotNull List<String> getTables(){
        if (LOCAL_CACHE.isEmpty()){
            fillLocalCache();
        }
        return new ArrayList<>(LOCAL_CACHE.keySet());
    }

    private static void fillLocalCache() {
        try (InputStream stream = ShiftDaoFs.class.getResourceAsStream("tables.json")) {
            JSONTokener jsonTokener = new JSONTokener(Objects.requireNonNull(stream));
            JSONArray tables = new JSONArray(jsonTokener);
            for (int i = 0; i < tables.length(); i++) {
                String name = tables.getString(i);
                Table table = new Table(name);
                LOCAL_CACHE.put(name, table);
            }

        }catch (IOException | NullPointerException e){
            LOGGER.warning(e.getMessage());
        }
    }
}
