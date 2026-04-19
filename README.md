# Proyecto base para el Backend. Curso Modelos de Programación

## Enlaces de interés
- [Jenkins](http://200.69.103.29:8085/jenkins/)
- [SonarQube](http://200.69.103.29:8084/sonar/)

## Revisión de entregables solicitados

### Estado de archivos PDF y Markdown
- **No existe** una carpeta `Archives` en este repositorio backend.
- **No hay archivos `.pdf`** versionados en el repositorio.
- Solo existe **un archivo `.md`** (`README.md`) y **no contiene imágenes** asociadas.
- Por lo anterior, la revisión de mockups/documentación visual queda **pendiente de insumos**.

### Formato de revisión cruzada (Persona 1 revisa a Persona 2)
- `Primer Nombre Primer Apellido a Primer Nombre Primer Apellido`

### Validación funcional sobre veterinarios (backend)
Sí hay cobertura del dominio de veterinarios en backend:
- Controlador: `src/main/java/co/edu/udistrital/mdp/pets/controllers/VeterinarianController.java`
- Servicio: `src/main/java/co/edu/udistrital/mdp/pets/services/VeterinarianService.java`
- Repositorio: `src/main/java/co/edu/udistrital/mdp/pets/repositories/VeterinarianRepository.java`
- Entidad relacionada en procedimientos: `src/main/java/co/edu/udistrital/mdp/pets/entities/ProcedureEntity.java` (campo `veterinarian`)
