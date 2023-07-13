package in.shelfpay.lobymaker.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConvertUtils {
    public static <E, M> M convert(E entity, Class<M> modelClass) throws IllegalAccessException, InstantiationException {
        M model = modelClass.newInstance();

        // Get all fields from entity and model classes, including superclass fields
        List<Field> entityFields = getAllFields(entity.getClass());
        List<Field> modelFields = getAllFields(modelClass);

        // Iterate over the fields and populate the model object
        for (Field entityField : entityFields) {
            entityField.setAccessible(true);
            for (Field modelField : modelFields) {
                modelField.setAccessible(true);
                if (entityField.getName().equals(modelField.getName())) {
                    modelField.set(model, entityField.get(entity));
                    break;
                }
            }
        }

        return model;
    }

    private static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();

        // Add fields from the current class
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));

        // Recursively add fields from superclasses
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null) {
            fields.addAll(getAllFields(superClass));
        }

        return fields;
    }

    public static <E, M> List<M> convertList(List<E> entityList, Class<M> modelClass) throws IllegalAccessException, InstantiationException {
        List<M> modelList = new ArrayList<>();

        for (E entity : entityList) {
            M model = convert(entity, modelClass);
            modelList.add(model);
        }

        return modelList;
    }
}
