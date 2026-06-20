# VanguardLedger-Core

[![Java](https://img.shields.io/badge/Java-21-blue.svg)](https://openjdk.org/projects/jdk/21/)
[![License: Apache 2.0](https://img.shields.io/badge/License-Apache_2.0-red.svg)](https://opensource.org/licenses/Apache-2.0)
[![Status](https://img.shields.io/badge/Status-Active_Development-brightgreen.svg)]()

VanguardLedger-Core is a high-concurrency, lock-free financial transaction engine engineered in **Java 21**. It is designed to handle high-frequency resource transfers by bypassing traditional JVM thread-blocking mechanisms in favor of hardware-level atomic operations.

---

## 🧠 Architectural Overview
Traditional financial systems rely on `synchronized` blocks or `ReentrantLocks`, which introduce significant latency due to OS-level thread scheduling and context switching. VanguardLedger-Core achieves ultra-low latency by utilizing **Compare-And-Swap (CAS)** atomic primitives to ensure thread safety without the cost of mutual exclusion.

### Technical Pillars
- **Non-Blocking Synchronization:** Implements lock-free algorithms using `java.util.concurrent.atomic` primitives to minimize contention during high-frequency balance updates.
- **Durable Write-Ahead Log (WAL):** Ensures transactional integrity by flushing data to an append-only sequential log on disk before state finalization, guaranteeing crash recovery.
- **High-Throughput Testing:** Stress-tested with 10,000+ concurrent virtual threads to validate thread-safety and zero race conditions under load.

---

## 📊 Performance Benchmark
The engine was benchmarked using Java Virtual Threads against a concurrent transaction volume to ensure stability and deterministic behavior.

| Metric | Benchmark Result |
| :--- | :--- |
| **Transaction Throughput** | 10,000 Ops / batch |
| **Execution Latency** | < 400ms |
| **Race Conditions** | Zero |
| **System State Finality** | 100% Deterministic |

---

## 🗺️ Project Roadmap
This project is currently in **Phase 1 (MVP)**. The architecture is designed for future extensibility and production-hardening.

| Status | Milestone | Focus |
| :--- | :--- | :--- |
| ✅ | **Phase 1: Concurrency Engine** | Lock-free CAS logic & Atomic operations. |
| ✅ | **Phase 2: WAL Durability** | Sequential append-only log for data persistence. |
| 🚧 | **Phase 3: Telemetry Integration** | OpenTelemetry agents for real-time observability. |
| 🔜 | **Phase 4: Distributed Sharding** | Horizontal scaling via data partitioning. |

---

## 💡 Future Extensibility
VanguardLedger-Core is built to be the foundational "Atomic Layer" for a larger distributed financial system. Planned enhancements include:
- **JMX Monitoring:** Exposing internal thread contention metrics to JConsole.
- **GRPC Integration:** Transforming the local engine into a distributed microservice.
- **Zero-Copy Serialization:** Leveraging `MappedByteBuffer` to further reduce latency during log flushes.

---

## 🏗️ Getting Started

### Prerequisites
- Java 21 SDK
- Maven 3.8+

### Execution
```bash
# Build the project
mvn clean compile

# Run stress test
mvn exec:java -Dexec.mainClass="com.vanguard.core.Main"
