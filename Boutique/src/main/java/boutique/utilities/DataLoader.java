package boutique.utilities;

import boutique.entities.Category;
import boutique.entities.ERole;
import boutique.entities.Product;
import boutique.entities.Role;
import boutique.models.RegisterRequest;
import boutique.repositories.CategoryRepository;
import boutique.repositories.ProductRepository;
import boutique.repositories.RoleRepository;
import boutique.repositories.UserRepository;
import boutique.services.UserService;
import org.hibernate.boot.archive.scan.spi.ClassDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        loadRoleData();
        loadUserData();
        loadCategoryData();
        loadProductData();
    }

    private void loadUserData() {
        if (this.userRepository.count() == 0) {
            RegisterRequest requestUser = new RegisterRequest(
                    "testuser@email.bg", "User", "Test", "123456", "123456");
            RegisterRequest requestAdmin = new RegisterRequest(
                    "admin@email.bg", "Admin", "Test", "123456", "123456");

            Set<String> roles = new HashSet<>();
            roles.add(ERole.ROLE_ADMIN.toString());
            requestAdmin.setRole(roles);

            this.userService.registerUser(requestUser);
            this.userService.registerUser(requestAdmin);
        }
    }

    private void loadRoleData() {
        if (this.roleRepository.count() == 0) {
            Role user = new Role(ERole.ROLE_USER);
            Role admin = new Role(ERole.ROLE_ADMIN);
            this.roleRepository.saveAndFlush(user);
            this.roleRepository.saveAndFlush(admin);
        }
    }

    private void loadCategoryData() {
        if (this.categoryRepository.count() == 0) {
            Category coats = new Category("Coats");
            Category dresses = new Category("Dresses and skirts");
            Category footwear = new Category("Footwear");
            Category jackets = new Category("Jackets");
            Category sports = new Category("Sportswear");
            Category tops = new Category("Tops");
            Category trousers = new Category("Trousers and shorts");
            Category underwear = new Category("Underwear");
            Category swimwear = new Category("Swimwear");
            List<Category> categories = Arrays.asList(coats, dresses, footwear, jackets, sports, tops, trousers,underwear, swimwear);

            this.categoryRepository.saveAllAndFlush(categories);
        }
    }

    private void loadProductData() {
        if (this.productRepository.count() == 0) {
            Product shirt1 = new Product("Alessa Luxury Logo T-Shirt",
                    "Fabric: jersey (cotton, elastane)\n" +
                            "Treatment instructions: up to 30 degrees and 800 rpm\n" +
                            "Yeast is 169 cm high and poses with size S\n" +
                            "Product code: 04654",
                    "https://bit.ly/3xQItz7",
                    69.0, this.categoryRepository.findByName("Tops"));

            Product shirt2 = new Product("All I Want Is A Yacht Top",
                    "Fabric: jersey (polyester, elastane)\n" +
                            "Treatment instructions: up to 30 degrees and 800 rpm\n" +
                            "Mimi is 168 cm tall and poses with size XS\n" +
                            "Product code: 04646",
                    "https://cdn.alessa.bg/files/medium/IMG_8590-1.jpg",
                    84.0, this.categoryRepository.findByName("Tops"));

            Product shoes1 = new Product("Pale Pink Sneakers",
                    "Material: natural varnish\n" +
                            "Product code: 33156",
                    "https://cdn.alessa.bg/files/medium/IMG_3937-2.jpg",
                    169.0, this.categoryRepository.findByName("Footwear"));

            Product shoes2 = new Product("Never Average Heeled Sandals",
                    "Material: natural varnish\n" +
                            "Product code: 3250",
                    "https://cdn.alessa.bg/files/medium/IMG_9410-2.jpg",
                    169.0, this.categoryRepository.findByName("Footwear"));

            Product dress1 = new Product("High Class Tutu Dress",
                    "Fabric: lace, tulle, lining (polyester)\n" +
                            "Treatment instructions: hand wash, without dryer\n" +
                            "Mimi is 168 cm tall and poses size XS\n" +
                            "Product code: 05351",
                    "https://cdn.alessa.bg/files/medium/IMG_7806-1.jpg",
                    219.0, this.categoryRepository.findByName("Dresses and skirts"));

            Product dress2 = new Product("I Need A Mansion Bodycon Dress",
                    "Fabric: astringent jersey (polyester, elastane); lining\n" +
                            "Treatment instructions: up to 30 degrees and 800 rpm\n" +
                            "Mimi is 168 cm tall and poses with size XS\n" +
                            "Product code: 05342",
                    "https://cdn.alessa.bg/files/medium/IMG_7968-1.jpg",
                    97.0, this.categoryRepository.findByName("Dresses and skirts"));

            Product swimsuit1 = new Product("On My Radar Swimsuit Top Ruffles",
                    "Fabric: microfiber (polyester, elastane), cups with lifting pads\n" +
                            "Treatment instructions: up to 30 degrees and 800 rpm\n" +
                            "Mimi is 168 cm tall and poses with size XS\n" +
                            "Product code: 31195",
                    "https://cdn.alessa.bg/files/medium/IMG_2216-1.jpg",
                    59.0, this.categoryRepository.findByName("Swimwear"));

            Product swimsuit2 = new Product("On My Radar Swimsuit Bikini With Low Waist",
                    "Fabric: microfiber (polyester, elastane)\n" +
                            "Treatment instructions: up to 30 degrees and 800 rpm\n" +
                            "Mimi is 168 cm tall and poses with size XS\n" +
                            "Product code: 31173",
                    "https://cdn.alessa.bg/files/medium/IMG_2315-1x.jpg",
                    37.0, this.categoryRepository.findByName("Swimwear"));

            Product coat1 = new Product("I'm Into You Warm Coat Of Eco Hair",
                    "Fabric: eco hair; quilted lining with 120 grams of wadding (polyester)\n" +
                            "Treatment instructions: dry cleaning\n" +
                            "Mimi is 168 cm tall and poses size XS\n" +
                            "Product code: 06118",
                    "https://cdn.alessa.bg/files/medium/IMG_5101-1.jpg",
                    197.0, this.categoryRepository.findByName("Coats"));

            Product coat2 = new Product("I Need Too Much Coat",
                    "Fabric: eco cashmere (wool, polyester), lining\n" +
                            "Treatment instructions: dry cleaning\n" +
                            "Yeast is 169 cm high and poses with size S\n" +
                            "Product code: 06119",
                    "https://cdn.alessa.bg/files/medium/IMG_7264-1.jpg",
                    279.0, this.categoryRepository.findByName("Coats"));

            List<Product> products = Arrays.asList(shirt1, shirt2, shoes1, shoes2, dress1, dress2,
                    swimsuit1, swimsuit2, coat1, coat2);

            this.productRepository.saveAllAndFlush(products);
        }
    }
}
