package cl.architeq.zkpush.util;

import java.util.Arrays;
import java.util.List;

import lombok.NonNull;

public class ValueUtil {

    public static final String getValue(@NonNull String key, @NonNull String row) {

        List<String> list = Arrays.asList(row.split("\t"));
        for(String column:list) {

            if(column.toUpperCase().startsWith(key.toUpperCase()) ||
                    column.toUpperCase().contains(key.toUpperCase())) {

                /*
                String col[] = column.split("=");
                if(col.length > 1)
                    return col[1];
                else
                    return "";
                */

                Integer index = column.indexOf("=");
                if (index == -1) return "";
                return column.substring(index + 1, column.strip().length());

            }
        }
        return "";
    }

    /**
     * this will split row content with '&' as separator.
     * @param key
     * @param row
     * @return
     */
    public static final String getValueWtihAnd(@NonNull String key, @NonNull String row) {

        List<String> list = Arrays.asList(row.split("&"));
        for(String column:list) {

            if(column.toUpperCase().startsWith(key.toUpperCase()) ||
                    column.toUpperCase().contains(key.toUpperCase())) {

                String col[] = column.split("=");
                if(col.length > 1)
                    return col[1];
                else
                    return "";
            }
        }
        return "";
    }


}
