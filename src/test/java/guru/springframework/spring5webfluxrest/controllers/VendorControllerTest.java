package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repositories.CategoryRepository;
import guru.springframework.spring5webfluxrest.repositories.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class VendorControllerTest {

    WebTestClient webTestClient;
    VendorRepository vendorRepository;
    VendorController vendorController;

    @Before
    public void setUp() {
        vendorRepository = Mockito.mock(VendorRepository.class);
        vendorController = new VendorController(vendorRepository);
        webTestClient = WebTestClient.bindToController(vendorController).build();
    }

    @Test
    public void getVendors() {
        BDDMockito.given(vendorRepository.findAll())
                .willReturn(Flux.just(Vendor.builder().firstName("Akash").build(),
                        Vendor.builder().firstName("Akash1").build()));

        webTestClient.get()
                .uri("/api/v1/vendors/")
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(2);
    }

    @Test
    public void getVendorById() {
        BDDMockito.given(vendorRepository.findById("someId"))
                .willReturn(Mono.just(Vendor.builder().firstName("Akash").build()));

        webTestClient.get()
                .uri("/api/v1/vendors/someId")
                .exchange()
                .expectBody(Vendor.class);
    }

    @Test
    public void testCreateVendor() {
        BDDMockito.given(vendorRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Vendor.builder().build()));

        Mono<Vendor> vendorToSave = Mono.just(Vendor.builder().firstName("Akash").build());

        webTestClient.post()
                .uri("/api/v1/vendors/")
                .body(vendorToSave, Vendor.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    public void testUpdateVendor() {
        BDDMockito.given(vendorRepository.save(ArgumentMatchers.any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> vendorToUpdate = Mono.just(Vendor.builder().firstName("Akash").build());

        webTestClient.put()
                .uri("/api/v1/vendors/someId")
                .body(vendorToUpdate, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void testPatchVendorWithChange() {

        given(vendorRepository.findById(any(String.class)))
                .willReturn(Mono.just(Vendor.builder()
                        .firstName("Some First Name")
                        .lastName("Some Last Name").build()));

        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> vendorToUpdate = Mono.just(Vendor.builder()
                .firstName("First Name")
                .lastName("Last Name").build());

        webTestClient.patch()
                .uri("/api/v1/vendors/someId")
                .body(vendorToUpdate, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(vendorRepository).save(any());
    }

    @Test
    public void testPatchVendorWithNoChanges() {

        given(vendorRepository.findById(any(String.class)))
                .willReturn(Mono.just(Vendor.builder()
                        .firstName("First Name")
                        .lastName("Last Name")
                        .build()));

        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> vendorToUpdate = Mono.just(Vendor.builder()
                .firstName("First Name")
                .lastName("Last Name").build());

        webTestClient.patch()
                .uri("/api/v1/vendors/someId")
                .body(vendorToUpdate, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(vendorRepository, never()).save(any());
    }
}