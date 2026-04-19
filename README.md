# Proyecto base para el Backend. Curso Modelos de Programación

## Enlaces de interés
- [Jenkins](http://200.69.103.29:8085/jenkins/)
- [SonarQube](http://200.69.103.29:8084/sonar/)

## Revisión de trazabilidad (HU, mockups y cobertura funcional)

- En esta rama **no existen** archivos PDF en una carpeta `archives`, ni documentos de historias de usuario en `.md`, ni imágenes de mockups (`.png`, `.jpg`, `.jpeg`) para validar trazabilidad diseño ↔ implementación.
- Cobertura backend disponible en el repositorio: entidades, servicios, controladores y pruebas para adopciones, mascotas, refugios, notificaciones, mensajería e historia clínica.
- Módulo de **veterinarios**: sí está cubierto en backend (entidad, repositorio, servicio, controlador y pruebas de servicio/entidad).
  - Endpoints en código: `GET /veterinarians`, `GET /veterinarians/{vetId}`, `POST /veterinarians`, `PUT /veterinarians/{vetId}`, `DELETE /veterinarians/{vetId}`.
  - En colección Postman actual hay operaciones de lectura/creación; si se requiere cobertura funcional completa de API para validación manual, conviene agregar también `PUT` y `DELETE`.
- Estado de validación local en este entorno: no fue posible ejecutar pruebas Maven porque el proyecto requiere Java 21 y el entorno actual tiene Java 17.
