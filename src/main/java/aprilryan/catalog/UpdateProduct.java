package aprilryan.catalog;

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

public class UpdateProduct {

    public static void main(String[] args) {
        // Запрашиваем товар
        // Не должна меняться категория товара

        // Если ничего не вводится, то ничего не должно обновляться

        BufferedReader bufferedReaderIn = new BufferedReader(new InputStreamReader(System.in));

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("main");
        EntityManager manager = factory.createEntityManager();

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

                /*List<ProductInfo> productInfos = product.getProductInfos();   // фигня
                for (ProductInfo pInfo : productInfos) {
                    ProductInfo productInfo = manager.find(ProductInfo.class, pInfo.getId());
                    System.out.println("Введите новое значение (" + pInfo.getCharacteristic().getName() + "):");
                    String updateProductInfo = bufferedReaderIn.readLine();
                    pInfo.setName(updateProductInfo);
                }*/
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
