# Productos Service — Correcciones SonarQube (U10 Post-Contenido 2)

![CI](https://github.com/marud21/marquez-post2-u10/actions/workflows/ci.yml/badge.svg)

## Descripción

Continuación del Post-Contenido 1: Quality Gate personalizado configurado, correcciones aplicadas a los hallazgos de SonarQube, segundo análisis ejecutado y pipeline de GitHub Actions integrado.

## Prerrequisitos

- JDK 21+, Maven 3.9+
- Docker Desktop con SonarQube (`docker run -d --name sonarqube -p 9000:9000 -e SONAR_ES_BOOTSTRAP_CHECKS_DISABLE=true sonarqube:community`)

## Ejecutar análisis

```bash
mvn clean verify sonar:sonar -Dsonar.token=TU_TOKEN
```

---

## Paso 1 — Quality Gate "Estándar Universidad"

Quality Gate personalizado creado en SonarQube con 4 condiciones:

| Condición | Umbral |
|-----------|--------|
| Bugs | > 0 → FAIL |
| Coverage | < 60% → FAIL |
| Code Smells | > 5 → FAIL |
| Duplicated Lines (%) | > 5% → FAIL |

---

## Comparativa antes / después

| Métrica | Post-Contenido 1 | Post-Contenido 2 | Cambio |
|---------|-----------------|-----------------|--------|
| Bugs | 0 | 0 | — |
| Vulnerabilidades | 0 | 0 | — |
| Code Smells | 3 | 1 | -2 ✅ |
| Cobertura | 5.3% | 60.5% | +55.2% ✅ |
| Complejidad ciclomática | 27 | 30 | +3 (nuevos métodos) |
| Tests totales | 1 | 22 | +21 ✅ |
| Quality Gate "Estándar Universidad" | — | 6/7 condiciones OK | ✅ |

---

## Correcciones aplicadas

### Fix 1: Bug — orElse(null) → orElseThrow()
**Archivo:** `ProductoService.java`

```java
// ANTES
public Producto buscar(Long id) {
    return repo.findById(id).orElse(null);
}
// DESPUÉS
public Producto buscar(Long id) {
    return productoRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Producto no encontrado: " + id));
}
```

### Fix 2: Code Smell — @Autowired en campo → inyección por constructor
**Archivo:** `ProductoService.java`

```java
// ANTES
@Autowired
private ProductoRepository repo;

// DESPUÉS
private final ProductoRepository productoRepository;
public ProductoService(ProductoRepository productoRepository) {
    this.productoRepository = productoRepository;
}
```

### Fix 3: Code Smell — n.equals("") → isBlank()
**Archivo:** `ProductoService.java`

```java
// ANTES
if (n == null || n.equals("")) { ... }
// DESPUÉS
if (nombre == null || nombre.isBlank()) { ... }
```

### Fix 4: Code Smell — Extraer método validarDatos()
**Archivo:** `ProductoService.java`

Complejidad ciclomática del método principal reducida extrayendo toda la validación a `validarDatos(String, Double, Integer)`.

---

## Capturas del dashboard

### Antes — Post-Contenido 1


### Después — Post-Contenido 2

