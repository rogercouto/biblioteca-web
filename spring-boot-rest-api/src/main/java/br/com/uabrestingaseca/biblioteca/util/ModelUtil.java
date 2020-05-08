package br.com.uabrestingaseca.biblioteca.util;

import br.com.uabrestingaseca.biblioteca.exceptions.ValidationException;
import br.com.uabrestingaseca.biblioteca.model.Editora;
import br.com.uabrestingaseca.biblioteca.model.Usuario;
import org.springframework.hateoas.Link;

import javax.persistence.Column;
import javax.persistence.Id;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

public class ModelUtil {

    private static Object getValue(Field field, Object object){
        try {
            field.setAccessible(true);
            return field.get(object);
        }catch (java.lang.IllegalAccessException e){
            return null;
        }
    }

    @SuppressWarnings("unused")
    public static void printObject(Object object){
        StringBuilder builder = new StringBuilder();
        Field[] fields = object.getClass().getDeclaredFields();
        builder.append("{\n");
        for (Field field : fields) {
            if (field.getName().compareTo("serialVersionUID") != 0){
                builder.append("\t\"");
                builder.append(field.getName());
                builder.append("\": \"");
                Object value = getValue(field, object);
                builder.append(value != null ? value.toString(): "null");
                builder.append("\",\n");
            }
        }
        builder.append("}");
        System.out.println(builder.toString());
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

    public static void main(String[] args) {
        Editora e1 = new Editora();
        e1.setId(1);
        e1.setNome("Editora Teste");
        Editora e2 = new Editora();
        e2.setId(1);
        System.out.println(isEqualsIgnoringNull(e1, e2));
        System.out.println(onlyIdSet(e1));
        System.out.println(onlyIdSet(e2));
    }

}
