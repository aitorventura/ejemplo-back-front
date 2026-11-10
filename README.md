# Ejemplo back + front: API REST con Spring Boot y app Android con Jetpack Compose

Dos proyectos que funcionan juntos:

| Carpeta | Qué es | Se abre con |
|---|---|---|
| `backend/` | API REST con Spring Boot + PostgreSQL. Una sola tabla (`videojuego`) con su CRUD. | VS Code / IntelliJ |
| `android-app/` | App Android (Kotlin + Jetpack Compose) que consume esa API con Retrofit. | Android Studio |

## Manuales

| Manual | Contenido |
|---|---|
| [Manual 1 — Cómo arrancar el backend y la app Android](Manual_1_Arranque_backend_y_app.docx) | Requisitos, orden de arranque, comprobaciones, móvil físico, cómo parar y reiniciar los datos, problemas al arrancar. |
| [Manual 2 — Cómo se llama al backend desde la app Android](Manual_2_Llamar_al_backend_desde_Android.docx) | Retrofit paso a paso: dependencias, permisos, modelo, interfaz, cliente, ViewModel, pantalla, recorrido de una petición, cómo añadir un endpoint nuevo y cómo depurar. |

## Resumen rápido

```bash
cd backend
docker compose up -d
./mvnw spring-boot:run
```

Después, abre `android-app/` en Android Studio y ejecuta la app en un emulador.

## Endpoints del backend

| Método | Ruta | Respuesta |
|---|---|---|
| GET | `/api/v1/videojuegos` | 200 + lista |
| GET | `/api/v1/videojuegos/{id}` | 200 + videojuego, o 404 |
| POST | `/api/v1/videojuegos` | 201 + videojuego creado |
| PUT | `/api/v1/videojuegos/{id}` | 200 + videojuego actualizado, o 404 |
| DELETE | `/api/v1/videojuegos/{id}` | 204 sin cuerpo, o 404 |

Documentación interactiva (Swagger): <http://localhost:8080/documentacion>
