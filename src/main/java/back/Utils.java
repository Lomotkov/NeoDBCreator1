package back;

import java.util.*;

public class Utils {

    public static <E> Collection<E> makeCollection(Iterable<E> iter) {
        Collection<E> list = new ArrayList<E>();
        for (E item : iter) {
            list.add(item);
        }
        return list;
    }


    public static ArrayList<String> expandDataTwo(Iterable<Map<String, Object>> list1) {
        ArrayList<Map<String, Object>> list2 = (ArrayList<Map<String, Object>>) Utils.makeCollection(list1);
        ArrayList<Set> list3 = new ArrayList<>();
        ArrayList<String> list4 = new ArrayList<String>();
        list2.forEach(element -> list3.add((Set)element.values())
        );
        list3.forEach(element -> element.forEach(el->{
            list4.add(el.toString());
        }));
        return list4;
    }
}
