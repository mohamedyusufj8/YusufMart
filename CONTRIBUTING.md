# Contributing to YusufMart

This guide describes the exact steps required to clone, build, and run **YusufMart** locally.

---

## 1. Prerequisites
- **Java Development Kit (JDK)**: Version 17 or higher. Verify with:
  ```bash
  java -version
  ```
- **Apache Maven**: Version 3.8+ (or use the provided `run-app.bat`). Verify with:
  ```bash
  mvn -version
  ```
- **Git**: Installed and configured.

---

## 2. Clone the Repository
```bash
git clone https://github.com/mohamedyusufj8/YusufMart.git
cd YusufMart
```

---

## 3. Build the Application
Compile the code, run the unit test suite, and generate the WAR artifact:
```bash
mvn clean package
```

---

## 4. Run Locally

### Option A: One-Click Runner (Windows)
Double-click `run-app.bat` or run in terminal:
```cmd
.\run-app.bat
```

### Option B: Maven Command
```bash
mvn compile exec:java
```

Once running, access the web marketplace at:
- **Application URL**: `http://localhost:8080/yusufmart`
- **Health Check API**: `http://localhost:8080/yusufmart/api/v1/health`

---

## 5. Seed Test Credentials
The database auto-seeds these accounts on first startup:
* **Admin**: `admin@yusufmart.com` / `Admin@123`
* **Seller**: `seller@yusufmart.com` / `Seller@123`
* **Buyer**: `buyer@yusufmart.com` / `Buyer@123`

---

## 6. Coding & Branching Guidelines
- Branches: `feature/<feature-name>` branched from `main`.
- Commit format: `feat:`, `fix:`, `test:`, `docs:`.
- Ensure all queries use `PreparedStatement` and passwords are hashed via `jBCrypt`.
