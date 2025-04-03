package com.example.productos.controller;

import com.example.productos.model.Producto;
import com.example.productos.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test") // Opcional, si usas perfiles para test
class ProductoIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ProductoRepository productoRepository;

    // Si quieres reutilizar el WebTestClient con credenciales en todas las pruebas,
    // puedes crear una variable authWebTestClient y mutarla en @BeforeEach
    private WebTestClient authWebTestClient;

    @BeforeEach
    void setUp() {
        // Limpia la base de datos antes de cada prueba.
        productoRepository.deleteAll().block();

        // Creamos un WebTestClient con autenticación básica para todas las pruebas
        authWebTestClient = webTestClient
                .mutate()
                .defaultHeaders(headers -> headers.setBasicAuth("usuario", "clave123"))
                .build();
    }

    @Test
    void crearProducto_DeberiaRetornarProductoCreado() {
        // 1) Construye un producto de ejemplo
        Producto producto = new Producto(null, "Laptop Gamer", 2800.0);

        // 2) Llama al endpoint POST /api/productos con autenticación
        authWebTestClient.post()
                .uri("/api/productos")
                .body(Mono.just(producto), Producto.class)
                .exchange()
                // 3) Verifica el código de estado HTTP
                .expectStatus().isOk()
                // 4) Verifica el cuerpo de la respuesta, esperando un objeto Producto
                .expectBody(Producto.class)
                .consumeWith(response -> {
                    Producto creado = response.getResponseBody();
                    assert creado != null;
                    assert creado.getId() != null;  // Debe haberse generado un ID
                    assert "Laptop Gamer".equals(creado.getNombre());
                    assert 2800.0 == creado.getPrecio();
                });
    }

    @Test
    void obtenerProductoPorId_DeberiaRetornarProducto() {
        // 1) Primero crea y guarda un producto en la BD real
        Producto productoGuardado = productoRepository.save(
            new Producto(null, "Teclado Mecanico", 120.0)
        ).block();

        // 2) Llama al endpoint GET /api/productos/{id} con autenticación
        authWebTestClient.get()
                .uri("/api/productos/{id}", productoGuardado.getId())
                .exchange()
                // 3) Verifica el estado HTTP
                .expectStatus().isOk()
                // 4) Verifica el cuerpo de la respuesta como un objeto Producto
                .expectBody(Producto.class)
                .consumeWith(response -> {
                    Producto p = response.getResponseBody();
                    assert p != null;
                    assert p.getId().equals(productoGuardado.getId());
                    assert "Teclado Mecanico".equals(p.getNombre());
                    assert 120.0 == p.getPrecio();
                });
    }

    @Test
    void eliminarProducto_DeberiaRetornarOk() {
        // 1) Guarda un producto que luego será eliminado
        Producto productoGuardado = productoRepository.save(
            new Producto(null, "Mouse", 30.0)
        ).block();

        // 2) Llama al endpoint DELETE /api/productos/{id} con autenticación
        authWebTestClient.delete()
                .uri("/api/productos/{id}", productoGuardado.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody().isEmpty();

        // 3) Verifica que el producto ya no exista en la BD
        Mono<Producto> posibleProducto = productoRepository.findById(productoGuardado.getId());
        assert posibleProducto.block() == null;
    }
}
