
package com.hantsylab.example.ee7.blog;

import java.util.ArrayList;
import java.util.List;
import org.modelmapper.ModelMapper;

/**
 *
 * @author Hantsy Bai<hantsy@gmail.com>
 */
public class DTOUtils {

    //private static final Logger log = LoggerFactory.getLogger(DTOUtils.class);

    private static final ModelMapper INSTANCE = new ModelMapper();

    public static <S, T> T map(S source, Class<T> targetClass) {
        return INSTANCE.map(source, targetClass);
    }

    public static <S, T> void mapTo(S source, T dist) {
        INSTANCE.map(source, dist);
    }

    public static <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        List<T> list = new ArrayList<>();
        for (int i = 0; i < source.size(); i++) {
            T target = INSTANCE.map(source.get(i), targetClass);
            list.add(target);
        }

        return list;
    }


}
