package com.example.productos.service;

import com.example.productos.model.Producto;
import com.example.productos.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;  // 1) Simularás el repositorio

    @InjectMocks
    private ProductoService productoService;         // 2) Inyectarás el mock en el servicio

    private Producto producto1;
    private Producto producto2;

    @BeforeEach
    void setUp() {
        // 3) Inicializarás datos de prueba
        producto1 = new Producto("1", "Laptop", 2500.0);
        producto2 = new Producto("2", "Teléfono", 800.0);
    }

    // 4) PRUEBA 1: listarProductos()
    @Test
    void listarproductos_DeberiaRetornarListaDeProductos() {
        // Simulamos la respuesta del repositorio
        when(productoRepository.findAll()).thenReturn(Flux.just(producto1, producto2));

        // Llamamos al método del servicio
        Flux<Producto> resultado = productoService.listarproductos();

        // Verificamos el contenido del Flux
        // blockLast() consume el Flux y retorna el último elemento emitido
        Producto ultimoEmitido = resultado.blockLast();

        // Asegurarnos de que se devuelvan ambos productos
        assertNotNull(ultimoEmitido);
        assertEquals("Teléfono", ultimoEmitido.getNombre());

        // Confirmamos que se llamó al repositorio
        verify(productoRepository, times(1)).findAll();
    }

    // 5) PRUEBA 2: obtenerProductoPorId()
    @Test
    void buscarPorId_DeberiaRetornarProductoSiExiste() {
        // Simulamos que el repositorio encuentra producto1 al buscar por ID "1"
        when(productoRepository.findById("1")).thenReturn(Mono.just(producto1));

        // Llamamos al servicio
        Mono<Producto> resultado = productoService.buscarPorId("1");
        Producto obtenido = resultado.block();  // Consumimos el Mono en pruebas

        // Validamos que el producto obtenido sea igual al simulado
        assertNotNull(obtenido);
        assertEquals("Laptop", obtenido.getNombre());

        // Verificamos que se llamó al repositorio con el ID "1"
        verify(productoRepository, times(1)).findById("1");
    }

    // 6) PRUEBA 3: crearProducto()
    @Test
    void guardar_DeberiaRetornarProductoGuardado() {
        // Simulamos que el repositorio guarda correctamente el producto
        when(productoRepository.save(producto1)).thenReturn(Mono.just(producto1));

        // Llamamos al servicio para guardar
        Mono<Producto> resultado = productoService.guardar(producto1);
        Producto guardado = resultado.block();

        // Validamos que devuelva el producto con los mismos datos
        assertNotNull(guardado);
        assertEquals("Laptop", guardado.getNombre());
        assertEquals(2500.0, guardado.getPrecio());

        // Verificamos que el repositorio llamara a save(producto1)
        verify(productoRepository, times(1)).save(producto1);
    }
}