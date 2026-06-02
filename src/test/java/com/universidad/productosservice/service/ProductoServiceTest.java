package com.universidad.productosservice.service;

import com.universidad.productosservice.domain.Producto;
import com.universidad.productosservice.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService service;

    private Producto productoGuardado;

    @BeforeEach
    void setUp() {
        productoGuardado = new Producto();
        productoGuardado.setId(1L);
        productoGuardado.setNombre("Laptop");
        productoGuardado.setPrecio(1500.0);
        productoGuardado.setStock(10);
    }

    @Test
    void procesarProducto_guardaYRetornaProducto() {
        when(productoRepository.save(any())).thenReturn(productoGuardado);

        Producto resultado = service.procesarProducto("Laptop", 1500.0, 10);

        assertThat(resultado.getNombre()).isEqualTo("Laptop");
        assertThat(resultado.getPrecio()).isEqualTo(1500.0);
        verify(productoRepository).save(any());
    }

    @Test
    void procesarProducto_nombreBlanco_lanzaExcepcion() {
        assertThatThrownBy(() -> service.procesarProducto("  ", 100.0, 5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nombre");
    }

    @Test
    void procesarProducto_nombreNulo_lanzaExcepcion() {
        assertThatThrownBy(() -> service.procesarProducto(null, 100.0, 5))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void procesarProducto_precioNulo_lanzaExcepcion() {
        assertThatThrownBy(() -> service.procesarProducto("Mesa", null, 5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("precio");
    }

    @Test
    void procesarProducto_precioCero_lanzaExcepcion() {
        assertThatThrownBy(() -> service.procesarProducto("Mesa", 0.0, 5))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void procesarProducto_precioExcesivo_lanzaExcepcion() {
        assertThatThrownBy(() -> service.procesarProducto("Mesa", 1_000_000.0, 5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("máximo");
    }

    @Test
    void procesarProducto_stockNegativo_lanzaExcepcion() {
        assertThatThrownBy(() -> service.procesarProducto("Mesa", 100.0, -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("stock");
    }

    @Test
    void listar_retornaListaDeProductos() {
        when(productoRepository.findAll()).thenReturn(List.of(productoGuardado));

        List<Producto> lista = service.listar();

        assertThat(lista).hasSize(1);
        assertThat(lista.get(0).getNombre()).isEqualTo("Laptop");
    }

    @Test
    void buscar_idExistente_retornaProducto() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoGuardado));

        Producto resultado = service.buscar(1L);

        assertThat(resultado.getId()).isEqualTo(1L);
    }

    @Test
    void buscar_idInexistente_lanzaNoSuchElementException() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscar(99L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("99");
    }
}
