# Copilot Instructions

<!-- Use this file to provide workspace-specific custom instructions to Copilot. For more details, visit https://code.visualstudio.com/docs/copilot/copilot-customization#_use-a-githubcopilotinstructionsmd-file -->

## Project Overview
This is a Spring Boot Java 11 application for employee management with branch assignment functionality.

## Architecture Guidelines
- Use Spring Boot 2.x with Java 11
- Follow layered architecture: Controller -> Service -> Repository
- Use JPA/Hibernate for database operations
- Implement proper validation using Bean Validation
- Use standard REST API conventions
- Follow Java naming conventions and best practices

## Code Style
- Use proper Java naming conventions
- Add appropriate JavaDoc comments for public methods
- Handle exceptions properly with custom exception classes
- Use DTOs for API request/response objects
- Implement proper logging with SLF4J

## Database
- Use H2 in-memory database for development
- Define proper JPA entities with relationships
- Use proper foreign key constraints
- Create meaningful database indexes
