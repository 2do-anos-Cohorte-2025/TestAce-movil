# Simulador de Exámenes Interactivos / Proyecto Android Test Ace
Nuestra app tiene como objetivo ayudar a estudiantes a practicar y evaluar sus conocimientos mediante exámenes interactivos de distintas materias y niveles educativos.
Se le permite a los usuarios resolver evaluaciones en un entorno dinámico, obtener resultados inmediatos y recibir retroalimentación sobre su desempeño. Además, cada usuario puede ver, editar y eliminar los exámenes que creó. Este proyecto surge como respuesta a la necesidad de contar con herramientas accesibles que permitan una práctica activa y efectiva, reduciendo la incertidumbre y el estrés al momento de rendir evaluaciones reales.

## Público objetivo
Estudiantes y personas en proceso de aprendizaje autodidacta

## Estado del proyecto
La app móvil está conectada a un backend propio (API REST) y a una base de datos MySQL: el registro, el inicio de sesión, el catálogo, los intentos de examen, el perfil y el panel de administración trabajan con datos reales. Para ejecutarla necesitás tener el backend levantado (ver Cómo ejecutar el proyecto).

## Tecnologías utilizadas: 
### App móvil (Front): Java, Android SDK, XML (layouts), Material Design 3, ViewBinding, Gradle
### Back: 	Django y Django REST Framework, autenticación con JWT. Repositorio: simulador-examenes
### BD: MySQL

## Funcionalidades

- Pantalla Splash (redirige al Home o al Login según haya o no una sesión guardada)
- Navegación por la app (menú lateral)
- Pantalla para registrarse
- Pantalla para iniciar sesión
- Pantalla de catálogo de exámenes / poder buscar y filtrar exámenes (por categoría, nivel y creador)
- Vista previa para exámenes
- Pantalla de intento de examen
- Pantalla de resultado de examen
- Pantalla de perfil (ver, editar y eliminar la cuenta). Si el usuario es profesor, se muestra un gorrito junto a su nombre
- Pantalla donde ver tus exámenes realizados recientes (pestaña del perfil)
- Pantalla de exámenes creados por el usuario (ver, editar y eliminar los propios)
- Pantalla para modificar un examen (datos, preguntas y opciones)
- Panel de administración (gestión de usuarios y exámenes), visible solo para administradores
- Pantalla de contacto

## Roles / Permisos
#### Estudiante -	Registrarse, rendir exámenes, ver y editar su perfil, ver sus exámenes recientes y gestionar sus exámenes creados
#### Profesor -	Lo mismo que el estudiante; en su perfil se lo identifica con un gorrito
#### Administrador	- Todo lo anterior y acceso al panel de administración

##
### **[Documentación](https://github.com/2do-anos-Cohorte-2025/TestAce-movil/wiki)**
