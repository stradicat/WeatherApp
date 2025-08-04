# App del Clima con API

## Nivel: Intermedio-Avanzado

### Conceptos:

API REST, `ViewBinding`, _Corrutinas_, JSON parsing

### Objetivo

Desarrollar una aplicación que consulte el clima actual y pronóstico usando una API REST, con
interfaz moderna y manejo asíncrono.

### Funcionalidades Requeridas

- Consultar clima actual por ciudad
- Mostrar pronóstico de 5 días
- Geolocalización automática
- Búsqueda de ciudades
- Guardar ciudades favoritas
- Modo offline con datos cached

### Estructura de Archivos

```
app/src/main/java/com/tuapp/climaapp/
├── MainActivity.kt
├── PronosticoActivity.kt
├── model/
│ ├── ClimaResponse.kt
│ ├── PronosticoResponse.kt
│ └── Ciudad.kt
├── api/
│ ├── ClimaApiService.kt
│ └── RetrofitClient.kt
├── repository/
│ └── ClimaRepository.kt
├── adapter/
│ └── PronosticoAdapter.kt
└── utils/
├── LocationHelper.kt
└── Constants.kt

app/src/main/res/layout/
├── activity_main.xml
├── activity_pronostico.xml
├── item_dia_pronostico.xml
└── dialog_buscar_ciudad.xml
```

### Retos Adicionales

- [ ] Implementar caché con Room Database
- [ ] Widgets de escritorio
- [ ] Notificaciones de alertas meteorológicas
- [ ] Gráficos de temperatura
- [ ] Múltiples ubicaciones

## Consideraciones personales

- Compatibilidad mínima: API 23 (Marshmallow, Android 6.0)
