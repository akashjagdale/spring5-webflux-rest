package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.repositories.CategoryRepository;
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

public class CategoryControllerTest {

    WebTestClient webTestClient;
    CategoryRepository categoryRepository;
    CategoryController categoryController;

    @Before
    public void setUp() {
        categoryRepository = Mockito.mock(CategoryRepository.class);
        categoryController = new CategoryController(categoryRepository);
        webTestClient = WebTestClient.bindToController(categoryController).build();
    }

    @Test
    public void list() {
        BDDMockito.given(categoryRepository.findAll())
                .willReturn(Flux.just(Category.builder().description("Cat1").build(),
                        Category.builder().description("Cat2").build()));

        webTestClient.get()
                .uri("/api/v1/categories/")
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(2);
    }

    @Test
    public void getById() {
        BDDMockito.given(categoryRepository.findById("someId"))
                .willReturn(Mono.just(Category.builder().description("Cat1").build()));

        webTestClient.get()
                .uri("/api/v1/categories/someId")
                .exchange()
                .expectBody(Category.class);
    }

    @Test
    public void testCreateCategory() {
        BDDMockito.given(categoryRepository.saveAll(ArgumentMatchers.any(Publisher.class)))
                .willReturn(Flux.just(Category.builder().build()));

        Mono<Category> categoryToSave = Mono.just(Category.builder().description("Some Desc").build());

        webTestClient.post()
                .uri("/api/v1/categories")
                .body(categoryToSave, Category.class)
                .exchange()
                .expectStatus()
                .isCreated();

    }

    @Test
    public void testUpdateCategory() {

        BDDMockito.given(categoryRepository.save(ArgumentMatchers.any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));

        Mono<Category> categoryToUpdate = Mono.just(Category.builder().description("Some Desc").build());

        webTestClient.put()
                .uri("/api/v1/categories/someId")
                .body(categoryToUpdate, Category.class)
                .exchange()
                .expectStatus()
                .isOk();
    }
}