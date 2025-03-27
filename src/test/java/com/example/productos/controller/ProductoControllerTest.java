package com.example.productos.controller;

import com.example.productos.model.Producto;
import com.example.productos.service.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoControllerTest {

    @Mock
    private ProductoService service;

    @InjectMocks
    private ProductoController controller;

    private Producto producto1;
    private Producto producto2;

    @BeforeEach
    void setUp() {
        producto1 = new Producto("1", "Laptop", 1500.0);
        producto2 = new Producto("2", "Celular", 800.0);
    }

    // PRUEBA 1: Verificar que listarproductos() devuelve una lista de productos
    @Test
    void listarproductos_DeberiaRetornarListaDeProductos() {
        when(service.listarproductos()).thenReturn(Flux.just(producto1, producto2));

        Flux<Producto> resultado = controller.listarproductos();

        StepVerifier.create(resultado)
            .expectNext(producto1)
            .expectNext(producto2)
            .verifyComplete();
        
        verify(service, times(1)).listarproductos();
    }

    // PRUEBA 2: Verificar que buscarPorId() devuelve un producto si existe
    @Test
    void buscarPorId_DeberiaRetornarProductoSiExiste() {
        when(service.buscarPorId("1")).thenReturn(Mono.just(producto1));

        Mono<ResponseEntity<Producto>> resultado = controller.buscarPorId("1");

        StepVerifier.create(resultado)
            .assertNext(response -> {
                assertNotNull(response.getBody());
                assertEquals("Laptop", response.getBody().getNombre());
                assertEquals(1500.0, response.getBody().getPrecio());
            })
            .verifyComplete();
        
        verify(service, times(1)).buscarPorId("1");
    }

    // PRUEBA 3: Verificar que buscarPorId() devuelve 404 si el producto no existe
    @Test
    void buscarPorId_DeberiaRetornarNotFoundSiNoExiste() {
        when(service.buscarPorId("3")).thenReturn(Mono.empty());

        Mono<ResponseEntity<Producto>> resultado = controller.buscarPorId("3");

        StepVerifier.create(resultado)
            .expectNext(ResponseEntity.notFound().build())
            .verifyComplete();
        
        verify(service, times(1)).buscarPorId("3");
    }

    // PRUEBA 4: Verificar que crear() guarda un producto
    @Test
    void crear_DeberiaGuardarProducto() {
        when(service.guardar(producto1)).thenReturn(Mono.just(producto1));

        Mono<Producto> resultado = controller.crear(producto1);

        StepVerifier.create(resultado)
            .expectNext(producto1)
            .verifyComplete();
        
        verify(service, times(1)).guardar(producto1);
    }

    // PRUEBA 5: Verificar que actualizar() modifica un producto existente
    @Test
    void actualizar_DeberiaModificarProductoSiExiste() {
        Producto productoActualizado = new Producto("1", "Laptop Gamer", 2000.0);
        when(service.actualizar(eq("1"), any(Producto.class))).thenReturn(Mono.just(productoActualizado));

        Mono<ResponseEntity<Producto>> resultado = controller.actualizar("1", productoActualizado);

        StepVerifier.create(resultado)
            .assertNext(response -> {
                assertNotNull(response.getBody());
                assertEquals("Laptop Gamer", response.getBody().getNombre());
                assertEquals(2000.0, response.getBody().getPrecio());
            })
            .verifyComplete();
        
        verify(service, times(1)).actualizar(eq("1"), any(Producto.class));
    }

    // PRUEBA 6: Verificar que eliminar() elimina un producto
    @Test
    void eliminar_DeberiaEliminarProducto() {
        when(service.eliminar("1")).thenReturn(Mono.empty());

        Mono<Void> resultado = controller.eliminar("1");

        StepVerifier.create(resultado)
            .verifyComplete();
        
        verify(service, times(1)).eliminar("1");
    }
}
