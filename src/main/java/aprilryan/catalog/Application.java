package aprilryan.catalog;

import aprilryan.catalog.entity.Category;
import aprilryan.catalog.entity.Characteristic;
import aprilryan.catalog.entity.Product;
import aprilryan.catalog.entity.ProductInfo;

import javax.persistence.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Application {

    private static final BufferedReader bufferedReaderIn = new BufferedReader(new InputStreamReader(System.in));
    private static final EntityManagerFactory factory = Persistence.createEntityManagerFactory("main");
    private static final EntityManager manager = factory.createEntityManager();

    public static void main(String[] args) {
        // - Создание [1]
        // - Редактирование [2]
        // - Удаление [3]
        // Выберите действие: ___

        try {
            for (; ; ) {
                System.out.println("- Создание категории [1]");
                System.out.println("- Создание продукта [2]");
                System.out.println("- Редактирование продукта [3]");
                System.out.println("- Удаление продукта [4]");
                System.out.println("- Завершить программу [5]");
                System.out.println("Выберите действие: ");

                int userChoice = Integer.parseInt(bufferedReaderIn.readLine());

                if (userChoice == 1) {
                    createCategory();
                } else if (userChoice == 2) {
                    createProduct();
                } else if (userChoice == 3) {
                    updateProduct();
                } else if (userChoice == 4) {
                    deleteProduct();
                } else if (userChoice == 5) {
                    break;
                } else {
                    System.out.println("Такого действия нет");
                }
            }
        } catch (IOException e) {
            System.out.println("I/O error");
        }
    }

    private static void createProduct() {
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

    private static void updateProduct() {
        try {
            manager.getTransaction().begin();

            System.out.println("Введите ID товара, данные которого необходимо обновить: ");
            Long updateProduct = Long.parseLong(bufferedReaderIn.readLine());

            TypedQuery<Product> productTypedQuery = manager.createQuery(
                    "select p from Product p where p.id = ?1", Product.class
            );
            productTypedQuery.setParameter(1, updateProduct);
            List<Product> products = productTypedQuery.getResultList();
            Product product = manager.find(Product.class, products.get(0).getId());

            TypedQuery<Characteristic> characteristicTypedQuery = manager.createQuery(
                    "select ch from Characteristic ch where ch.category = ?1", Characteristic.class
            );
            characteristicTypedQuery.setParameter(1, product.getCategory());
            List<Characteristic> characteristics = characteristicTypedQuery.getResultList();

            if (products.isEmpty()) {
                System.out.println("Данного товара нет в списке");
            } else {
                System.out.println("Введите новое значение (наименование):");
                String updateName = bufferedReaderIn.readLine();
                if (!updateName.isEmpty()) {
                    product.setName(updateName);
                }

                System.out.println("Введите новое значение (цена):");
                String updatePrice = bufferedReaderIn.readLine();
                for (; ; ) {
                    if (!updatePrice.matches("\\d*")) {
                        System.out.println("Введите новое значение только цифрами (цена):");
                        updatePrice = bufferedReaderIn.readLine();
                    } else {
                        break;
                    }
                }

                if (!updatePrice.isEmpty()) {
                    product.setPrice(Integer.parseInt(updatePrice));
                }

                for (Characteristic characteristic : characteristics) {
                    TypedQuery<ProductInfo> productInfoTypedQuery = manager.createQuery(
                            "select pI from ProductInfo pI where pI.product = ?1 and pI.characteristic = ?2", ProductInfo.class
                    );
                    productInfoTypedQuery.setParameter(1, product);
                    productInfoTypedQuery.setParameter(2, characteristic);
                    List<ProductInfo> productInfos = productInfoTypedQuery.getResultList();

                    System.out.println("Введите новое значение (" + characteristic.getName() + "):");
                    String updateProductInfo = bufferedReaderIn.readLine();

                    if (!productInfos.isEmpty() && !updateProductInfo.isEmpty()) {
                        productInfos.get(0).setName(updateProductInfo);
                    } else if (!updateProductInfo.isEmpty()) {
                        ProductInfo productInfo = new ProductInfo();
                        productInfo.setName(updateProductInfo);
                        productInfo.setProduct(product);
                        productInfo.setCharacteristic(characteristic);
                        manager.persist(productInfo);
                    }
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

    private static void deleteProduct() {
        try {
            manager.getTransaction().begin();

            System.out.println("Введите ID товара, который необходимо удалить: ");
            Long deleteProduct = Long.parseLong(bufferedReaderIn.readLine());

            Product product = manager.find(Product.class, deleteProduct);

            Query productInfoQuery = manager.createQuery(
                    "delete from ProductInfo pI where pI.product = ?1"
            );
            productInfoQuery.setParameter(1, product);
            productInfoQuery.executeUpdate();

            Query productQuery = manager.createQuery(
                    "delete from Product p where p.id = ?1"
            );
            productQuery.setParameter(1, deleteProduct);
            productQuery.executeUpdate();

            manager.getTransaction().commit();
        } catch (IOException e) {
            System.out.println("I/O error");
        } catch (Exception e) {
            manager.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    private static void createCategory() {
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