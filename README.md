# Matrix Neo Simulation

**Escuela Colombiana de Ingeniería Julio Garavito**  
Arquitecturas y Reutilización de Software (ARSW)

---

## Descripción

Matrix Neo Simulation es una aplicación de escritorio desarrollada en Java que simula el universo de la película *The Matrix*. Neo, representado como un agente autónomo, intenta escapar hacia un teléfono mientras es perseguido por Agentes en una cuadrícula bidimensional.

El proyecto tiene como objetivo demostrar el uso práctico de **concurrencia en Java** mediante hilos (`Thread`), sincronización (`synchronized`, `wait`, `notifyAll`, `volatile`, `join`). 

---

## Tabla de Contenidos

- [Características](#características)
- [Arquitectura](#arquitectura)
- [Concurrencia](#concurrencia)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Requisitos](#requisitos)
- [Ejecución](#ejecución)
- [Configuración de Dificultad](#configuración-de-dificultad)

---

## Características

- Simulación automática — sin intervención del usuario durante la ejecución
- Tres niveles de dificultad: Easy, Medium y Hard
- Tres tamaños de tablero: 12×12, 30×30 y 64×64
- Múltiples simulaciones consecutivas con resumen final
- Animación en tiempo real del movimiento de entidades
- Pausa y reanudación de la simulación en cualquier momento
- Menú
- Cada entidad corre en su propio hilo independiente

---

## Arquitectura

El proyecto sigue una arquitectura en capas claramente separadas:

```
matrix.simulation
├── model/           — estado del dominio (Matrix, Cell)
├── entities/        — entidades concurrentes (Neo, Agent)
├── patterns/
│   ├── strategy/    — algoritmos de movimiento intercambiables
│   ├── factory/     — construcción de entidades
│   └── observer/    — notificación de eventos entre capas
├── ui/              — interfaz gráfica Swing
├── GameState.java   — control global de pausa entre hilos
├── DifficultyConfig.java — configuración por nivel de dificultad
├── Simulation.java  — orquestación del ciclo de simulación
└── Main.java        — punto de entrada
```

---

## Concurrencia

Cada entidad activa en el tablero corre en su propio hilo Java (`Thread`). El estado compartido entre hilos es la instancia de `Matrix`, protegida con `synchronized`.

### Hilos

```java
public class Neo extends Thread { ... }
public class Agent extends Thread { ... }
```

Cada hilo ejecuta un ciclo `while` que duerme según su velocidad, revisa si debe pausarse y ejecuta un paso de movimiento.

### Sincronización de la Matrix

```java
public synchronized Cell getCell(int row, int col) { ... }
public synchronized void setCell(int row, int col, Cell value) { ... }
```

Cualquier lectura o escritura sobre la cuadrícula es atómica, previniendo condiciones de carrera cuando Neo y múltiples Agentes intentan moverse simultáneamente.

### Control de Pausa — `wait` y `notifyAll`

`GameState` implementa el patrón de bloque con guarda del slide 20 del material de clase:

```java
// Los hilos llaman esto antes de cada movimiento
public static void waitIfPaused() throws InterruptedException {
    synchronized (lock) {
        while (paused) {
            lock.wait();       // libera el lock y espera
        }
    }
}

// La UI llama esto al reanudar
public static void resume() {
    synchronized (lock) {
        paused = false;
        lock.notifyAll();      // despierta todos los hilos en espera
    }
}
```

### `volatile`

Los flags de estado compartidos entre hilos se declaran `volatile` para garantizar visibilidad inmediata entre núcleos sin necesidad de `synchronized`:

```java
public volatile boolean alive   = true;   // Neo sigue vivo
public volatile boolean escaped = false;  // Neo llegó al teléfono
public volatile boolean active  = true;   // Agent sigue activo
```

### `join`

`Simulation` espera a que Neo termine antes de evaluar el resultado y mostrar el diálogo:

```java
neo.start();
for (Agent agent : agents) agent.start();

neo.join(); // bloquea hasta que Neo termine (escape o captura)

// aquí ya se sabe el resultado
```

---

## Estructura del Proyecto

```
src/
└── main/
    └── java/
        └── matrix/simulation/
            ├── Main.java
            ├── Simulation.java
            ├── GameState.java
            ├── DifficultyConfig.java
            ├── entities/
            │   ├── Neo.java
            │   └── Agent.java
            ├── model/
            │   ├── Matrix.java
            │   └── Cell.java
            ├── patterns/
            │   ├── factory/
            │   │   └── EntityFactory.java
            │   ├── observer/
            │   │   ├── SimulationObserver.java
            │   │   └── SimulationEvent.java
            │   └── strategy/
            │       ├── MovementStrategy.java
            │       ├── NeoSmartMovement.java
            │       ├── NeoRandomMovement.java
            │       ├── AgentSmartMovement.java
            │       └── AgentRandomMovement.java
            └── ui/
                ├── MenuFrame.java
                ├── SimulationFrame.java
                └── MatrixPanel.java
```

---

## Requisitos

- Java 17 o superior (se usa `switch` expressions y records)
- Maven 3.8+

---

## Ejecución

```bash
git clone https://github.com/usuario/Matrix_ARSW.git
cd Matrix_ARSW

mvn compile

mvn exec:java -Dexec.mainClass="matrix.simulation.Main"
```

O desde el IDE ejecutar directamente `Main.java`.

---

## Configuración de Dificultad

La clase `DifficultyConfig` centraliza todos los parámetros por nivel y tamaño de tablero:

| Dificultad | Agentes | Velocidad Neo | Velocidad Agentes | Estrategia |
|---|---|---|---|---|
| Easy | Pocos | Rápido (300ms) | Lentos (600ms) | Random |
| Medium | Medios | Normal (400ms) | Normal (450ms) | Smart |
| Hard | Muchos | Lento (500ms) | Rápidos (300ms) | Smart |

Los números de agentes, paredes y teléfonos escalan automáticamente según el tamaño del tablero seleccionado (12×12, 30×30, 64×64).

---

## Símbolos del tablero

| Símbolo | Color | Entidad |
|---|---|---|
| N | 🔵 Azul | Neo |
| A | 🔴 Rojo | Agente |
| T | 🟢 Verde | Teléfono |
| # | 🟠 Naranja | Muro |
| · | ⬛ Oscuro | Celda vacía |

---

## Autores

Desarrollado como proyecto del curso (ARSW)
Escuela Colombiana de Ingeniería Julio Garavito