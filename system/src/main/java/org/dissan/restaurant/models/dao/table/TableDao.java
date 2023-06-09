package org.dissan.restaurant.models.dao.table;

import org.dissan.restaurant.models.Table;
import org.json.JSONArray;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Logger;

public class TableDao {
    private static final Map<String,Table> LOCAL_CACHE  = new HashMap<>();
    private static final Logger LOGGER = Logger.getLogger(TableDao.class.getSimpleName());

    private TableDao(){}



    private static void fillLocalCache() {
        try (InputStream stream = TableDao.class.getResourceAsStream("tables.json")) {
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

    public static List<Table> pullTables() {
        List<Table> tableList = new ArrayList<>();
        if(LOCAL_CACHE.isEmpty()){
            fillLocalCache();
        }

        for (Map.Entry<String, Table> entry:
             LOCAL_CACHE.entrySet()) {
            tableList.add(entry.getValue());
        }

        return tableList;
    }
}
