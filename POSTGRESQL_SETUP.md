# PostgreSQL 17 Installation Guide

## macOS Installation

### Option 1: Using Homebrew (Recommended)
```bash
# Install PostgreSQL 17
brew install postgresql@17

# Start PostgreSQL service
brew services start postgresql@17

# Add PostgreSQL to PATH (add to ~/.zshrc or ~/.bash_profile)
echo 'export PATH="/opt/homebrew/opt/postgresql@17/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc
```

### Option 2: Using PostgreSQL.app
1. Download from https://postgresapp.com/
2. Install and start the app
3. Default connection: localhost:5432, user: your_username

## Alternative: Using Docker (All platforms)
```bash
# Run PostgreSQL 17 in Docker
docker run --name postgres-17 \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_DB=employeedb \
  -p 5432:5432 \
  -d postgres:17

# Check if running
docker ps | grep postgres-17
```

## Linux Installation (Ubuntu/Debian)
```bash
# Install PostgreSQL 17
sudo apt update
sudo apt install postgresql-17 postgresql-client-17

# Start PostgreSQL service
sudo systemctl start postgresql
sudo systemctl enable postgresql

# Set password for postgres user
sudo -u postgres psql -c "ALTER USER postgres PASSWORD 'postgres';"
```

## Post-Installation Setup
```bash
# Create database
createdb -h localhost -p 5432 -U postgres employeedb

# Or using psql
psql -h localhost -p 5432 -U postgres -c "CREATE DATABASE employeedb;"
```

## Verification
```bash
# Test connection
psql -h localhost -p 5432 -U postgres -d employeedb -c "SELECT version();"
```

## Configuration for Spring Boot
The application.properties has been configured with:
- Host: localhost
- Port: 5432
- Database: employeedb
- Username: postgres
- Password: postgres
