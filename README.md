# Android FSM Demo

This repository contains a **public demo** of an Android **Field Service Management (FSM) / CMMS** application.

It is a **simplified and sanitized version** of a larger private project and is intended to **demonstrate architecture, code structure, and core functionality**, rather than serve as a production-ready system.

---

## 🚧 Demo Disclaimer

⚠️ **This is a demo application**

- Not fully functional
- Backend, authentication, and synchronization are **mocked, partial, or non-functional**
- Includes **only selected core features**
- Uses **demo / local data only**
- No sensitive or real customer data is included

The goal of this project is **technical demonstration**, not feature completeness.

---

## 🎯 Purpose of This Repository

This demo is designed for:
- **Recruiters** reviewing Android & Kotlin skills
- **Technical interviews**
- **Investors / stakeholders** interested in architecture and scalability
- Demonstrating **real-world application structure and design decisions**

---

## ✨ Implemented Features

- Basic **CRUD operations** (Create, Read, Update, Delete)
- Core entities:
  - Customers
  - Equipments
  - Work Orders
- **Filtering** and simple list interactions
- Basic **notifications / reminders**
- Offline-first approach using **Room database**
- Clean UI implemented with **XML layouts**

---

## 🧱 Architecture Overview

The project follows **MVVM + Clean Architecture** principles.

### Package Structure

```text
data/
 ├─ local/              → Room entities, DAOs, database
 ├─ repositoryImpl/     → Data access implementations
 └─ remote/             → Sync & authentication placeholders (demo only)

core/
 └─ utils/              → PDF utilities, UI messages, time & date helpers

domain/
 └─ useCase/            → Business logic & notification use cases

presentation/
 ├─ ui/                 → Feature screens (Customer, Work Order, Equipment)
 ├─ adapter/            → RecyclerView, dropdown, and pager adapters
 ├─ common/             → Dialogs, modals, shared UI components
 └─ viewmodel/          → ViewModels acting as the bridge between UI and data layers 
```

##Architectural Highlights

Clear separation of concerns

Repository pattern

ViewModel-driven UI

Scalable structure suitable for real backend & synchronization logic

## 🛠 Technologies Used

Kotlin

Android SDK

XML UI

Room Database

MVVM + Clean Architecture

RecyclerView

LiveData / StateFlow (where applicable)

How to Run

Clone the repository:

git clone https://github.com/georeaper/android-fsm-demo.git


Open the project in Android Studio

Build and run on an emulator or physical device

Demo data is available locally — no login required

ℹ️ PDF generation configuration (used in demo features) may require additional setup.
For details, feel free to contact me at: giorgoskouvarakis@gmail.com
