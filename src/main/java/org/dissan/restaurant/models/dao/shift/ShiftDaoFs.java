package org.dissan.restaurant.models.dao.shift;

import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.logging.Logger;

public class ShiftDaoFs {

    private static final Logger LOGGER = Logger.getLogger(ShiftDaoFs.class.getSimpleName());
    public static @Nullable JSONObject getShiftList(){

        try (InputStream stream = ShiftDaoFs.class.getResourceAsStream("shifts.json")) {
            JSONTokener jsonTokener = new JSONTokener(Objects.requireNonNull(stream));
            return new JSONObject(jsonTokener);

        }catch (IOException | NullPointerException e){
            LOGGER.warning(e.getMessage());
            return null;
        }
    }

}
