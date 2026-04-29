package be.bstorm.introjaxrs.utils;

import at.favre.lib.crypto.bcrypt.BCrypt;
import be.bstorm.introjaxrs.daos.ProductDao;
import be.bstorm.introjaxrs.daos.RoleDao;
import be.bstorm.introjaxrs.daos.UserDao;
import be.bstorm.introjaxrs.pojos.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.Startup;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class DataInitializer {

    @Inject
    private ProductDao productDao;

    @Inject
    private RoleDao roleDao;

    @Inject
    private UserDao userDao;

    public void init(@Observes Startup event) {

        if(userDao.count() == 0) {

            Role userRole = new Role("USER");
            Role adminRole = new Role("ADMIN");

            roleDao.saveAll(List.of(userRole, adminRole));

            String password = BCrypt.withDefaults().hashToString(12, "Test1234=".toCharArray());

            User user = new User("user@test.be","user",password);
            user.addRole(userRole);
            User admin = new User("admin@test.be","admin",password);
            admin.addRole(adminRole);

            userDao.saveAll(List.of(user,admin));

        }

        if(productDao.count() == 0) {

            List<Product> products = new ArrayList<>();

            // Catégories
            Category electronics = new Category(null, "Électronique");
            Category clothing    = new Category(null, "Vêtements");
            Category sports      = new Category(null, "Sport");
            Category food        = new Category(null, "Alimentation");

            // Produits Électronique
            products.add(new Product(UUID.randomUUID(), "iPhone 15 Pro",       "Apple",     129999, "Smartphone haut de gamme Apple",                    null, electronics, new Stock(null, 45, 10, 50)));
            products.add(new Product(UUID.randomUUID(), "Galaxy S24 Ultra",    "Samsung",   119999, "Smartphone flagship Samsung",                       null, electronics, new Stock(null, 32, 10, 50)));
            products.add(new Product(UUID.randomUUID(), "MacBook Air M3",      "Apple",     149999, "Laptop ultrafin avec puce M3",                      null, electronics, new Stock(null, 18, 5, 50)));
            products.add(new Product(UUID.randomUUID(), "Sony WH-1000XM5",     "Sony",       34999, "Casque Bluetooth à réduction de bruit",             null, electronics, new Stock(null, 67, 15, 50)));
            products.add(new Product(UUID.randomUUID(), "iPad Pro 12.9",       "Apple",     109900, "Tablette professionnelle avec écran Liquid Retina", null, electronics, new Stock(null, 28, 8, 50)));

            // Produits Vêtements
            products.add(new Product(UUID.randomUUID(), "T-shirt Classic",     "Zara",        2999, "T-shirt coton bio, col rond",                       null, clothing, new Stock(null, 150, 30, 50)));
            products.add(new Product(UUID.randomUUID(), "Jean Slim Fit",       "Levi's",      6999, "Jean stretch taille slim",                          null, clothing, new Stock(null, 95, 20, 50)));
            products.add(new Product(UUID.randomUUID(), "Veste en cuir",       "Hugo Boss",  18999, "Veste en cuir véritable, coupe droite",             null, clothing, new Stock(null, 22, 5, 50)));
            products.add(new Product(UUID.randomUUID(), "Sneakers Runner",     "Nike",        8999, "Chaussures de running légères",                     null, clothing, new Stock(null, 120, 25, 50)));

            // Produits Sport
            products.add(new Product(UUID.randomUUID(), "Vélo de route X500",  "Trek",       89999, "Vélo carbone pour route et compétition",            null, sports, new Stock(null, 12, 3, 50)));
            products.add(new Product(UUID.randomUUID(), "Tapis de yoga",       "Decathlon",   1999, "Tapis antidérapant 6mm",                            null, sports, new Stock(null, 200, 40, 50)));
            products.add(new Product(UUID.randomUUID(), "Haltères 10kg",       "Domyos",      3499, "Paire d'haltères en fonte",                         null, sports, new Stock(null, 85, 15, 50)));
            products.add(new Product(UUID.randomUUID(), "Raquette de tennis",  "Wilson",      8999, "Raquette légère pour joueurs intermédiaires",       null, sports, new Stock(null, 40, 8, 50)));

            // Produits Alimentation
            products.add(new Product(UUID.randomUUID(), "Whey Protein 1kg",    "MyProtein",   3499, "Protéine de lactosérum saveur chocolat",            null, food, new Stock(null, 110, 25, 50)));
            products.add(new Product(UUID.randomUUID(), "Barres énergétiques", "Clif Bar",    1299, "Pack de 12 barres aux céréales et fruits",          null, food, new Stock(null, 180, 50, 50)));

            // Utilise la méthode SaveAll du repository avec batch processing
            productDao.saveAll(products);
        }
    }
}
