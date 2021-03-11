package guru.springframework.spring5webfluxrest.bootstrap;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repositories.CategoryRepository;
import guru.springframework.spring5webfluxrest.repositories.VendorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by jt on 12/23/17.
 */
@Component
public class Bootstrap implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;

    public Bootstrap(CategoryRepository categoryRepository, VendorRepository vendorRepository) {
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        categoryRepository.count().subscribe(aLong -> {
            if (aLong == 0) {
                //load data
                System.out.println("#### LOADING DATA ON BOOTSTRAP #####");

                categoryRepository.save(Category.builder()
                        .description("Fruits").build()).subscribe();

                categoryRepository.save(Category.builder()
                        .description("Nuts").build()).subscribe();

                categoryRepository.save(Category.builder()
                        .description("Breads").build()).subscribe();

                categoryRepository.save(Category.builder()
                        .description("Meats").build()).subscribe();

                categoryRepository.save(Category.builder()
                        .description("Eggs").build()).subscribe();

                categoryRepository.count().subscribe(aLong1 -> {
                    System.out.println("Loaded Categories: " + aLong);
                });

                vendorRepository.save(Vendor.builder()
                        .firstName("Joe")
                        .lastName("Buck").build()).subscribe();

                vendorRepository.save(Vendor.builder()
                        .firstName("Micheal")
                        .lastName("Weston").build()).subscribe();

                vendorRepository.save(Vendor.builder()
                        .firstName("Jessie")
                        .lastName("Waters").build()).subscribe();

                vendorRepository.save(Vendor.builder()
                        .firstName("Bill")
                        .lastName("Nershi").build()).subscribe();

                vendorRepository.save(Vendor.builder()
                        .firstName("Jimmy")
                        .lastName("Buffett").build()).subscribe();

                vendorRepository.count().subscribe(aLong1 -> {
                    System.out.println("Loaded Vendors: " + aLong);
                });
            }
        });
    }
}
