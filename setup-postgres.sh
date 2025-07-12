#!/bin/bash

# PostgreSQL Database Setup Script
# Make sure PostgreSQL is installed and running on your system

echo "Setting up PostgreSQL database for Employee Management System..."

# Check if PostgreSQL is running
if ! pg_isready -h localhost -p 5432 > /dev/null 2>&1; then
    echo "Error: PostgreSQL is not running on localhost:5432"
    echo "Please start PostgreSQL service first:"
    echo "  macOS (with Homebrew): brew services start postgresql@17"
    echo "  Linux: sudo systemctl start postgresql"
    echo "  Docker: docker run --name postgres-17 -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres:17"
    exit 1
fi

# Create database if it doesn't exist
echo "Creating database 'employeedb' if it doesn't exist..."
psql -h localhost -p 5432 -U postgres -c "CREATE DATABASE employeedb;" 2>/dev/null || echo "Database 'employeedb' may already exist"

# Grant privileges
echo "Granting privileges..."
psql -h localhost -p 5432 -U postgres -c "GRANT ALL PRIVILEGES ON DATABASE employeedb TO postgres;"

echo "Database setup completed!"
echo ""
echo "Connection details:"
echo "  Host: localhost"
echo "  Port: 5432"
echo "  Database: employeedb"
echo "  Username: postgres"
echo "  Password: postgres"
echo ""
echo "You can now start the Spring Boot application."
