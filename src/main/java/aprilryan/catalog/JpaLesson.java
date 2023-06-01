package aprilryan.catalog;

import aprilryan.catalog.entity.Category;
import aprilryan.catalog.entity.Product;

import javax.persistence.*;
import java.util.List;

public class JpaLesson {

    public static void main(String[] args) {
        // Maven - отдельная программа ответственная за автоматизацию сборки приложений, написанных с применением JDK.

        // JDK - комплекс программных средств, содержащий в себе инструменты для работы с языком Java и его стандартную
        // библиотеку.

        // Когда встает необходимость использования в проекте дополнительной библиотеки, которая не входит в JDK,
        // разработчик сталкивается с определенными трудностями:
        // 1) Библиотеку необходимо скачать.
        // 2) Проверить совместимость версии библиотеки с остальными компонентами проекта.
        // 3) Компилятору Java необходимо указать, где эта библиотека находится, чтобы он учитывал ее при сборке проекта.
        // 4) При отправке проекта коллеге он будет вынужден пройтись по всем описанным этапам заново.

        // Для решения описанных выше задач были придуманы программы для автоматизации сборки проектов с дополнительными
        // библиотеками.

        // На данный момент в мире JDK существуют два современных решения для автоматизации сборки: Maven, Gradle.

        // `mvnrepository.com` - сайт, где хранятся все современные зависимости. Центральное хранилище библиотек
        // доступных для подключения, написанных при помощи JDK.

        // `pom.xml` - центральный конфигурационный файл проекта, собирающийся при помощи системы сборки Maven. В данном
        // файле прописывается информация об авторе проекта, его названии, версии, конфигурационых параметров и
        // зависимостях.

        // Зависимость - внешняя библиотека, без которой проект не сможет функционировать.

        // Приложение, написанное с применением JDK, не может напрямую взаимодействовать с сервером базой данных.
        // Посредником между приложением и базой данных выступает отдельная программа, называемая драйвером.

        // Java -> PostgreSQL Java Driver -> PostgreSQL
        // C++  -> PostgreSQL C++ Driver  -> PostgreSQL

        // В случае, если драйвер не будет подключен к приложению, оно просто не сможет выстроить подключение к базе
        // данных и не сможет отправять запросы на выполнение.

        // Драйверы для работы с различными базами данных не входят в стандартный комплект поставки JDK, т.е. драйверы
        // нужно будет скачивать и подключать дополнительно.

        // Процесс подключение драйвера, как и любой другой внешней библиотеки, можно упростить используя системы
        // автоматизированной сборки Maven и Gradle.

        // JDBC (Java DataBase Connectivity) - модуль для работы с базами данных, входящий в стандартный набор
        // инструментов JDK.

        // JDBC обладает следующими проблемами:
        // * Написание запросов на диалекте, специфичном для отдельно взятой базы данных.

        // Проект с БД PostgreSQL (170 запросов);
        // Проектс БД Oracle (170 запросов);
        // Может случится такое, что нужно будет поменять БД, к примеру с PostgreSQL на Oracle. В таком случае [часть]
        // запросы нужны будет переписывать.

        // * Не удобные объекты для взаимодействия с результатом запросов (нет конкретики по типам данных и наличии тех
        // или иных полей в результате).

        // JPA (Java Persistence API) - спецификация модуля Java EE, предназначенного для взаимодействия с реляционными
        // базами данных по принципу ORM. Техдок крч. Hibernate - его реализация (вроде).

        // ORM (Object Relational Mapping) - система объектно-реляционного сопоставления, позволяющая переводить данные
        // из табличного вида в объекты и наоборот.

        // table users          -> ORM -> class User
        // id serial8           -> ORM -> Long id
        // first_name varchar   -> ORM -> String firstName
        // last_name varchar    -> ORM -> String lastName
        // birthdate date       -> ORM -> LocalDate birthdate

        // ORM -> JPA -> JDBC -> Driver -> SQL

        // Сущность(Entity) - класс, объекты которого будут являться интерпретацией табличных данных (class User сверху).

        // Для использования возможностей JPA необходимо подключить библиотеку, реализующую данный стандарт.
        // Сущетсвует много различных реализации стандарта JPA:
        // 1) Hibernate;
        // 2) EclipseLink;
        // 3) ...;

        // persistence.xml - конфигурационный файл стандарта JPA, содержащий в себе блоки конфигураций (persistence
        // unit), каждый из которых имеет свое уникальное наименование. Данный файл должен находиться в папке META-INF.
        // В свою очередь папка META-INF должна быть включена в итоговую сборку.

        // EntityManagerFactory - объект, при помощий которого формируется подключение к серверу базы данных и создаются
        // объекты специального класса EntityManager для непосредственного взаимодействия сущностями.

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("main");
        EntityManager manager = factory.createEntityManager();

        /* // select c.* from categories c where c.id = ?
        Category category = manager.find(Category.class, 2L);
        System.out.println(category.getName()); // 3L кидает нам NullPointerException
        */

        /*Product product = manager.find(Product.class, 1L);
        System.out.println(product.getCategory().getName());
        System.out.println(product.getName());
        System.out.println(product.getPrice());*/

        /*Category category = manager.find(Category.class, 1L);
        List<Product> products = category.getProducts();
        for (Product product : products) {
            System.out.println(product.getName() + " -> " + product.getPrice());
        }*/

        // Транзакция - группа запросов отправляемых на сервер базы данных единовременно. В одну транзакцию может
        // входить несколько запросов. Транзакции работают по принципу все или ничего, то есть если не выполнится хотя
        // бы один запрос из транзакции, изменения всех остальных будут отменены. По умолчанию любые действия
        // подразумевающие изменение на сервере базы данных должны выполняться в контексте транзакции.

        try {
            manager.getTransaction().begin();

            /*Category category = new Category();
            category.setName("Мебель");
            manager.persist(category);*/    // Создание новой категории

            /*Category category = manager.find(Category.class, 3L);
            category.setName("Ноутбуки");*/   // Переименовывание категории

            /*Category category = manager.find(Category.class, 3L);
            manager.remove(category);*/

            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            e.printStackTrace();
        }

        // Для применения метода find() нам необходимо знать значение первичного ключа.

        // JPQL - язык, используемый для построения запросов в рамках стандарта JPA, ориентированный на взаимодействия
        // с сущностями. В отличиие от SQL, запросы написанные на JPQL будут одинаковыми для любой РБД.

        // JPQL -> ORM (Hibernate) -> PostgreSQL Dialect -> PostgreSQL
        // JPQL -> ORM (Hibernate) -> MySQL Dialect -> MySQL

        // SQL -> select p.* from products p
        // JPQL -> select p from Product p

        // SQL -> select p.* from products p where p.price between 10000 and 20000
        // JPQL -> select p from Product p where p.price between 10000 and 20000

        // SQL -> update products set name = 'New name', price = 777 where id = 3
        // JPQL -> update Product p set p.name = 'New name', p.price = 777 where p.id = 3

        // `Query` - объект стандарта JPA предназначенный для выполнения запросов, не подразумевающие наличие результаты,
        // то есть запросов на изменение данных (update, delete). Для того, чтобы в базе данных фиксировались изменения
        // запросов update и delete их обязательно необходимо выполнять в рамках транзакции.

        // `TypedQuery<T>` - объект стандарта JPA предназначенный для выполнения запросов, подразумевающих наличие
        // результатов (select), где аргумент T это тип данных результата запроса.

        try {
            manager.getTransaction().begin();

            /*Query query = manager.createQuery(
                    "update Product p set p.price = p.price * 1.1 where p.category.id = 1"
            );
            query.executeUpdate();*/

            /*Query query = manager.createQuery(
                    "delete from Product p where p.category.id = 1"
            );
            query.executeUpdate();*/

            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            e.printStackTrace();
        }

        /*TypedQuery<Product> productTypedQuery = manager.createQuery(
                "select p from Product p order by p.id", Product.class
        );
        List<Product> products = productTypedQuery.getResultList();
        for (Product product : products) {
            System.out.printf(
                    "%s: %s (%d)%n", product.getCategory().getName(), product.getName(), product.getPrice()
            );
        }*/

        // Подстановка информации из переменных в JPQL запрос может быть реализована через параметры.
        // Параметры бывают двух видов:
        // 1) Порядковые - в запросе определяется параметр при помощи знака вопроса и порядкового номера начиная с
        // единицы.
        // 2) Именованные - в запросе определяется параметр при помощи знака двоеточия и указания идентификатора при
        // условии, что он соответствует правилам составления идентификатора.

        /*int minPrice = 50_000;
        int maxPrice = 250_000;
        TypedQuery<Product> productTypedQuery = manager.createQuery(
                // "select p from Product p where p.price between ?1 and ?2", Product.class
                "select p from Product p where p.price between :min and :max", Product.class
        );
        // productTypedQuery.setParameter(1, minPrice);
        // productTypedQuery.setParameter(2, maxPrice);
        productTypedQuery.setParameter("min", minPrice);
        productTypedQuery.setParameter("max", maxPrice);
        List<Product> products = productTypedQuery.getResultList();
        for (Product product : products) {
            System.out.printf(
                    "%s: %s (%d)%n", product.getCategory().getName(), product.getName(), product.getPrice()
            );
        }*/

        /*int minPrice = 50_000;
        TypedQuery<Long> productCountTypedQuery = manager.createQuery(
                "select count(p.id) from Product p where p.price > ?1", Long.class
        );
        productCountTypedQuery.setParameter(1, minPrice);
        long productCount = productCountTypedQuery.getSingleResult();
        System.out.println(productCount);*/
    }
}
