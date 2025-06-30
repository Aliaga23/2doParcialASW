# Parcial 2 – Arquitectura de Software 📱

*Aplicación Android en **Kotlin** elaborada para el **Segundo Parcial de la materia ASW** (Arquitectura de Software). Gestiona estaciones de servicio, tipos de combustible y su stock, arquitectura tres capas , con dos patrones de diseño , chain of reponsability y observer*

---

## Tabla de contenidos

1. [Características](#características)
2. [Stack y librerías](#stack-y-librerías)
3. [Estructura del proyecto](#estructura-del-proyecto)
4. [Primeros pasos](#primeros-pasos)
5. [Arquitectura](#arquitectura)


---

## Características

* **Bottom Navigation** con cinco módulos:

  1. **Tipo** de combustible
  2. **Estación** (puntos de venta)
  3. **Bomba** (surtidores registrados)
  4. **Stock** (inventario en tiempo real)
  5. **Fila** (cola de vehículos atendidos)
* **ViewBinding** activado para acceso tipado a vistas.
* **Google Maps** integrado: localiza estaciones sobre el mapa. ([raw.githubusercontent.com](https://raw.githubusercontent.com/Aliaga23/2doParcialASW/main/app/build.gradle.kts))
* **Persistencia local in‑memory** (Room no requerido para el parcial).
* **LiveData + ViewModel** para datos reactivos.
* **Material 3** (com.google.android.material).

---

## Stack y librerías

| Área         | Dependencia                                 |
| ------------ | ------------------------------------------- |
| Lenguaje     | Kotlin 1.9                                  |
| UI           | AndroidX AppCompat, Material Components     |
| Navegación   | `androidx.navigation` Fragment + UI         |
| Arquitectura | `androidx.lifecycle` ViewModel & LiveData   |
| Mapas        | `com.google.android.gms:play‑services‑maps` |
| Layouts      | ConstraintLayout 2                          |
| Tests        | JUnit 4, Espresso                           |

> Todas declaradas en **app/build.gradle.kts**. ([raw.githubusercontent.com](https://raw.githubusercontent.com/Aliaga23/2doParcialASW/main/app/build.gradle.kts))

---

## Estructura del proyecto

```text
2doParcialASW/
├── app/
│   ├── src/
│   │   ├── main/java/com/example/parcial2/
│   │   │   ├── ui/             # Activities & Fragments
│   │   │   ├── viewmodel/      # ViewModels
│   │   │   ├── model/          # Entidades (Tipo, Estacion, Bomba…)
│   │   │   └── MainActivity.kt # BottomNavigation + NavHost
│   │   └── res/                # Layouts, drawables, menus, values
│   └── build.gradle.kts
├── build.gradle.kts   # Config raíz Gradle 8 + Kotlin DSL
├── settings.gradle.kts
└── README.md          # (este documento)
```

---

## Primeros pasos

### 1. Requisitos

* **Android Studio Flamingo** o superior.
* **JDK 11** (se instala con el IDE).

### 2. Clona y abre

```bash
git clone https://github.com/Aliaga23/2doParcialASW.git
cd 2doParcialASW
```

Abre la carpeta raíz en Android Studio; Gradle debería sincronizar automáticamente.

### 3. Ejecuta

Selecciona un emulador o dispositivo físico (**minSdk 24**, **targetSdk 35**) y pulsa ▶️ **Run**.


---

## Arquitectura

```
Activity (MainActivity) ↔ Navigation Component ↔ Fragments (Tipo, Estación, …)
      ↕                                         ↕
ViewModel (LiveData)                      Repositories (mock / in‑memory)
```


