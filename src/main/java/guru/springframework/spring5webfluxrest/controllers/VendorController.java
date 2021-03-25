package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repositories.VendorRepository;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class VendorController {

    private final VendorRepository vendorRepository;

    public VendorController(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @GetMapping("/api/v1/vendors")
    public Flux<Vendor> getVendors() {
        return vendorRepository.findAll();
    }

    @GetMapping("/api/v1/vendors/{id}")
    public Mono<Vendor> getVendorById(@PathVariable String id) {
        return vendorRepository.findById(id);
    }

    @PostMapping("/api/v1/vendors")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> create(@RequestBody Publisher<Vendor> vendorStream) {
        return vendorRepository.saveAll(vendorStream).then();
    }

    @PutMapping("/api/v1/vendors/{id}")
    public Mono<Vendor> update(@PathVariable String id, @RequestBody Vendor vendor) {
        vendor.setId(id);
        return vendorRepository.save(vendor);
    }

    @PatchMapping("/api/v1/vendors/{id}")
    public Mono<Vendor> patch(@PathVariable String id, @RequestBody Vendor vendor) {

        return vendorRepository.findById(id).flatMap(foundVendor -> {
            boolean flag = false;
            if (foundVendor.getFirstName() != null
                    && !foundVendor.getFirstName().equals(vendor.getFirstName())) {
                foundVendor.setFirstName(vendor.getFirstName());
                flag = true;
            }

            if (foundVendor.getLastName() != null
                    && !foundVendor.getLastName().equals(vendor.getLastName())) {
                foundVendor.setLastName(vendor.getLastName());
                flag = true;
            }
            return flag ? vendorRepository.save(foundVendor) : Mono.just(foundVendor);
        });
    }
}
