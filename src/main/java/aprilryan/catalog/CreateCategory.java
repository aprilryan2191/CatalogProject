package aprilryan.catalog;

import aprilryan.catalog.entity.Category;
import aprilryan.catalog.entity.Characteristic;

import javax.persistence.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class CreateCategory {
    public static void main(String[] args) {
        // 1. Введите название категории: ___
        // 2. Введите характеристики категории (через запятую): А, В, С
        // 3. Если категории с введенным названием уже существует необходимо вывести соответствующее сообщение. Если же
        // название категории свободно, то перейти к заполнению характеристик.

        // После ввода названия категории в консоль должна быть создана соответствующая запись в таблице и
        // харакетеристики к ней.

        BufferedReader bufferedReaderIn = new BufferedReader(new InputStreamReader(System.in));

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("main");
        EntityManager manager = factory.createEntityManager();

        try {
            manager.getTransaction().begin();

            System.out.println("Введите название категории:");
            String newCategory = bufferedReaderIn.readLine();
            Category category = new Category();
            category.setName(newCategory);

            TypedQuery<String> categoriesNameTypedQuery = manager.createQuery(
                    "select c.name from Category c where c.name = ?1", String.class
            );
            categoriesNameTypedQuery.setParameter(1, category.getName());
            List<String> categoriesName = categoriesNameTypedQuery.getResultList();

            if (!categoriesName.isEmpty()) {
                System.out.println("Данная категория уже существует");
            } else {
                manager.persist(category);
                System.out.println("Введите характеристики категории(через запятую):");
                String newCharacteristics = bufferedReaderIn.readLine();
                String[] fragments = newCharacteristics.split(",");

                for (int i = 1; i < fragments.length; i++) {
                    Characteristic characteristic = new Characteristic();
                    characteristic.setCategory(category);
                    characteristic.setName(fragments[i]);
                    manager.persist(characteristic);
                }
            }

            manager.getTransaction().commit();
        } catch (IOException e) {
            System.out.println("I/O error");
        } catch (Exception e) {
            manager.getTransaction().rollback();
            e.printStackTrace();
        }
    }
}
