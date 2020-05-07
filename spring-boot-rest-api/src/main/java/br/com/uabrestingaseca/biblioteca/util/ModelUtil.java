package br.com.uabrestingaseca.biblioteca.util;

import br.com.uabrestingaseca.biblioteca.exceptions.ValidationException;
import br.com.uabrestingaseca.biblioteca.model.Usuario;
import org.springframework.hateoas.Link;

import javax.persistence.Column;
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
            builder.append("\t\"");
            builder.append(field.getName());
            builder.append("\": \"");
            Object value = getValue(field, object);
            builder.append(value != null ? value.toString(): "null");
            builder.append("\",\n");
        }
        builder.append("}");
        System.out.println(builder.toString());
    }

}
