package com.universidad.productosservice.controller;

import com.universidad.productosservice.domain.Producto;
import com.universidad.productosservice.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductoRepository repo;

    @BeforeEach
    void setUp() {
        repo.deleteAll();
    }

    @Test
    void crear_retornaProductoCreado() throws Exception {
        mockMvc.perform(post("/api/productos")
                        .param("nombre", "Silla")
                        .param("precio", "200.0")
                        .param("stock", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Silla"));
    }

    @Test
    void listar_retornaLista() throws Exception {
        Producto p = new Producto();
        p.setNombre("Mesa");
        p.setPrecio(100.0);
        p.setStock(3);
        repo.save(p);

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Mesa"));
    }

    @Test
    void buscar_idExistente_retorna200() throws Exception {
        Producto p = new Producto();
        p.setNombre("Monitor");
        p.setPrecio(500.0);
        p.setStock(2);
        Producto guardado = repo.save(p);

        mockMvc.perform(get("/api/productos/" + guardado.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Monitor"));
    }

    @Test
    void buscar_idInexistente_retorna404() throws Exception {
        mockMvc.perform(get("/api/productos/99999"))
                .andExpect(status().isNotFound());
    }
}
