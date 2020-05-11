package br.com.uabrestingaseca.biblioteca.util;

import br.com.uabrestingaseca.biblioteca.model.Editora;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.IOException;
import java.lang.reflect.Field;

public class ModelUtil {

    private static Object getValue(Field field, Object object){
        try {
            field.setAccessible(true);
            return field.get(object);
        }catch (java.lang.IllegalAccessException e){
            return null;
        }
    }

    public static boolean isNullOrEmpty(String string){
        if (string == null)
            return true;
        return string.trim().length() == 0;
    }

    public static boolean isSomeNullOrEmpty(String... strings){
        for (String string: strings) {
            if (string == null)
                return true;
            if (string.trim().length() == 0)
                return true;
        }
        return false;
    }

    public static boolean isEqualsIgnoringNull(Object o1, Object o2){
        if (o1.getClass().equals(o2.getClass())){
            Field[] fields = o1.getClass().getDeclaredFields();
            for (Field field : fields) {
                Object v1 = getValue(field, o1);
                Object v2 = getValue(field, o2);
                if (v1 != null && v2 != null && !v1.equals(v2))
                    return false;
            }
            return true;
        }
        return false;
    }

    public static boolean onlyIdSet(Object object){
        Field[] fields = object.getClass().getDeclaredFields();
        boolean idSet = false;
        for (Field field : fields) {
            if (field.getName().compareTo("serialVersionUID") != 0) {
                Object value = getValue(field, object);
                if (field.isAnnotationPresent(Id.class)) {
                    if (value != null)
                        idSet = true;
                } else {
                    if (value != null)
                        return false;
                }
            }
        }
        return  idSet;
    }

    public static String toJson(Object object){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        }catch (IOException e){
            return object != null ? object.toString() : null;
        }
    }

    @SuppressWarnings("unused")
    public static void printJson(Object object){
        System.out.println(toJson(object));
    }

}
