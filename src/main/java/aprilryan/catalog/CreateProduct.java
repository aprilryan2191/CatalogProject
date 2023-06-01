package aprilryan.catalog;

import aprilryan.catalog.entity.Category;
import aprilryan.catalog.entity.Characteristic;
import aprilryan.catalog.entity.Product;
import aprilryan.catalog.entity.ProductInfo;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class CreateProduct {

    public static void main(String[] args) {
        // - Процессоры [1]
        // - Мониторы [2]
        // - Смартфоны [3]
        // - Видеокарты [4]
        // Введите категорию товара: ___
        // Введите название товара: ___
        // Введите стоимость товара: ___

        // Выберите категорию: 1 (Процессоры)
        // Введите название: ___
        // Введите стоимость: ___
        // Производитель: ___
        // Сокет: ___
        // Частота: ___

        BufferedReader bufferedReaderIn = new BufferedReader(new InputStreamReader(System.in));

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("main");
        EntityManager manager = factory.createEntityManager();

        try {
            manager.getTransaction().begin();

            System.out.println("Введите ID категории товара: ");
            Long existingCategory = Long.parseLong(bufferedReaderIn.readLine());

            TypedQuery<Category> categoriesTypedQuery = manager.createQuery(
                    "select c from Category c where c.id = ?1", Category.class
            );
            categoriesTypedQuery.setParameter(1, existingCategory);
            List<Category> categoriesName = categoriesTypedQuery.getResultList();

            if (categoriesName.isEmpty()) {
                System.out.println("Данной категории не существует");
            } else {
                Category category = manager.find(Category.class, categoriesName.get(0).getId());

                System.out.println("Введите название товара: ");
                String newProductName = bufferedReaderIn.readLine();
                System.out.println("Введите стоимость товара: ");
                Integer newProductPrice = Integer.parseInt(bufferedReaderIn.readLine());

                Product product = new Product();
                product.setName(newProductName);
                product.setPrice(newProductPrice);
                product.setCategory(category);
                manager.persist(product);

                List<Characteristic> characteristicsName = category.getCharacteristics();

                for (Characteristic chName : characteristicsName) {
                    ProductInfo productInfo = new ProductInfo();
                    System.out.println("Введите значение (" + chName.getName() + "): ");
                    String newProductInfo = bufferedReaderIn.readLine();
                    productInfo.setName(newProductInfo);
                    productInfo.setProduct(product);
                    productInfo.setCharacteristic(chName);
                    manager.persist(productInfo);
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
