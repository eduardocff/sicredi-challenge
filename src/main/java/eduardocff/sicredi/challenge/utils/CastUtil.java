package eduardocff.sicredi.challenge.utils;

import com.google.gson.Gson;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Type;

@UtilityClass
public class CastUtil {
    /**
     * converts one object to another through Gson.
     * The idea is to convert the DTO to Domain and vice versa.
     * @param from class that will be converted
     * @param classnameTo will be converted to that class
     * @param <T> from object
     * @param <Y> class name to cast
     * @return
     */
    public static <T extends Object, Y extends Object> Object copyFields(T from, Class classnameTo) {
        Gson gson = new Gson();

        String jsonString = gson.toJson(from);

        return gson.fromJson(jsonString, (Type) classnameTo);
    }
}
