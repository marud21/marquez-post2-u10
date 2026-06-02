package com.universidad.productosservice.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductoTest {

    @Test
    void getEstado_stockNulo_retornaDesconocido() {
        Producto p = new Producto();
        assertThat(p.getEstado()).isEqualTo("DESCONOCIDO");
    }

    @Test
    void getEstado_stockCero_retornaAgotado() {
        Producto p = new Producto();
        p.setStock(0);
        assertThat(p.getEstado()).isEqualTo("AGOTADO");
    }

    @Test
    void getEstado_stockBajo_retornaBajo() {
        Producto p = new Producto();
        p.setStock(3);
        assertThat(p.getEstado()).isEqualTo("BAJO");
    }

    @Test
    void getEstado_stockNormal_retornaNormal() {
        Producto p = new Producto();
        p.setStock(10);
        assertThat(p.getEstado()).isEqualTo("NORMAL");
    }

    @Test
    void getEstado_stockAlto_retornaAlto() {
        Producto p = new Producto();
        p.setStock(30);
        assertThat(p.getEstado()).isEqualTo("ALTO");
    }

    @Test
    void getEstado_stockMuyAlto_retornaMuyAlto() {
        Producto p = new Producto();
        p.setStock(75);
        assertThat(p.getEstado()).isEqualTo("MUY_ALTO");
    }

    @Test
    void getEstado_stockExcedente_retornaExcedente() {
        Producto p = new Producto();
        p.setStock(150);
        assertThat(p.getEstado()).isEqualTo("EXCEDENTE");
    }
}
