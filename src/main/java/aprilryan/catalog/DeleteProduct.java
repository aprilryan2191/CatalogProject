package aprilryan.catalog;

import aprilryan.catalog.entity.Product;

import javax.persistence.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DeleteProduct {

    public static void main(String[] args) {

        BufferedReader bufferedReaderIn = new BufferedReader(new InputStreamReader(System.in));

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("main");
        EntityManager manager = factory.createEntityManager();

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
}
