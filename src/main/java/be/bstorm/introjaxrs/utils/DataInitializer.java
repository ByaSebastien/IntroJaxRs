package be.bstorm.introjaxrs.utils;

import be.bstorm.introjaxrs.daos.ProductDao;
import be.bstorm.introjaxrs.pojos.Category;
import be.bstorm.introjaxrs.pojos.Product;
import be.bstorm.introjaxrs.pojos.Stock;
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

    public void init(@Observes Startup event) {
        List<Product> products = new ArrayList<>();

        // Catégories
        Category electronics = new Category(null, "Électronique");
        Category clothing    = new Category(null, "Vêtements");
        Category sports      = new Category(null, "Sport");
        Category food        = new Category(null, "Alimentation");

        // Produits Électronique
        products.add(new Product(UUID.randomUUID(), "iPhone 15 Pro",       "Apple",     129999, "Smartphone haut de gamme Apple",                    null, electronics, new Stock(null, 45, 10)));
        products.add(new Product(UUID.randomUUID(), "Galaxy S24 Ultra",    "Samsung",   119999, "Smartphone flagship Samsung",                       null, electronics, new Stock(null, 32, 10)));
        products.add(new Product(UUID.randomUUID(), "MacBook Air M3",      "Apple",     149999, "Laptop ultrafin avec puce M3",                      null, electronics, new Stock(null, 18, 5)));
        products.add(new Product(UUID.randomUUID(), "Sony WH-1000XM5",     "Sony",       34999, "Casque Bluetooth à réduction de bruit",             null, electronics, new Stock(null, 67, 15)));
        products.add(new Product(UUID.randomUUID(), "iPad Pro 12.9",       "Apple",     109900, "Tablette professionnelle avec écran Liquid Retina", null, electronics, new Stock(null, 28, 8)));

        // Produits Vêtements
        products.add(new Product(UUID.randomUUID(), "T-shirt Classic",     "Zara",        2999, "T-shirt coton bio, col rond",                       null, clothing, new Stock(null, 150, 30)));
        products.add(new Product(UUID.randomUUID(), "Jean Slim Fit",       "Levi's",      6999, "Jean stretch taille slim",                          null, clothing, new Stock(null, 95, 20)));
        products.add(new Product(UUID.randomUUID(), "Veste en cuir",       "Hugo Boss",  18999, "Veste en cuir véritable, coupe droite",             null, clothing, new Stock(null, 22, 5)));
        products.add(new Product(UUID.randomUUID(), "Sneakers Runner",     "Nike",        8999, "Chaussures de running légères",                     null, clothing, new Stock(null, 120, 25)));

        // Produits Sport
        products.add(new Product(UUID.randomUUID(), "Vélo de route X500",  "Trek",       89999, "Vélo carbone pour route et compétition",            null, sports, new Stock(null, 12, 3)));
        products.add(new Product(UUID.randomUUID(), "Tapis de yoga",       "Decathlon",   1999, "Tapis antidérapant 6mm",                            null, sports, new Stock(null, 200, 40)));
        products.add(new Product(UUID.randomUUID(), "Haltères 10kg",       "Domyos",      3499, "Paire d'haltères en fonte",                         null, sports, new Stock(null, 85, 15)));
        products.add(new Product(UUID.randomUUID(), "Raquette de tennis",  "Wilson",      8999, "Raquette légère pour joueurs intermédiaires",       null, sports, new Stock(null, 40, 8)));

        // Produits Alimentation
        products.add(new Product(UUID.randomUUID(), "Whey Protein 1kg",    "MyProtein",   3499, "Protéine de lactosérum saveur chocolat",            null, food, new Stock(null, 110, 25)));
        products.add(new Product(UUID.randomUUID(), "Barres énergétiques", "Clif Bar",    1299, "Pack de 12 barres aux céréales et fruits",          null, food, new Stock(null, 180, 50)));

        // Utilise la méthode SaveAll du repository avec batch processing
        productDao.saveAll(products);
    }
}