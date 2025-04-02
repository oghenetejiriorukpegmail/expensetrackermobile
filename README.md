# ExpenseTrackerMobile

A native Android expense tracker application built with Kotlin and Jetpack Compose. This application enables users to efficiently log, categorize, and analyze their expenses with a modern, intuitive, and visually appealing user experience.

## Features

- **Expense Management**: Add, edit, and delete expenses with details like amount, date, category, vendor, location, and notes
- **Receipt Capture**: Take photos of receipts or upload from gallery with OCR data extraction
- **Trip Management**: Group expenses by trips or events
- **Categories**: Organize expenses with customizable categories
- **Dashboard**: View expense summaries and visualizations
- **Analytics**: Analyze spending patterns with interactive charts
- **Budgeting**: Set and track monthly budgets (overall and per category)
- **Data Export**: Export expenses to spreadsheet formats
- **Material Design 3**: Modern UI with support for light/dark themes and dynamic colors

## Project Structure

The project follows a clean architecture approach with MVVM pattern:

- **app**: Main application module
  - **data**: Data layer (database, repositories, data sources)
  - **domain**: Domain layer (models, use cases, repository interfaces)
  - **ui**: UI layer (screens, viewmodels, components)
  - **di**: Dependency injection
  - **util**: Utilities and extensions

## Technology Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM with Clean Architecture
- **Database**: Room Persistence Library
- **Dependency Injection**: Hilt
- **Asynchronous Programming**: Kotlin Coroutines and Flow
- **Image Processing**: CameraX, ML Kit for OCR
- **Charts**: MPAndroidChart or Compose-compatible alternative

## Setup Instructions

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or newer
- JDK 17 or newer
- Android SDK 34 (min SDK 24)

### Getting Started

1. Clone the repository:
   ```
   git clone https://github.com/yourusername/expensetrackermobile.git
   ```

2. Open the project in Android Studio:
   - Launch Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned repository and click "Open"

3. Build the project:
   - Wait for Gradle sync to complete
   - Select "Build > Make Project" or press Ctrl+F9 (Cmd+F9 on Mac)

4. Run the application:
   - Connect an Android device or use an emulator
   - Select "Run > Run 'app'" or press Shift+F10 (Ctrl+R on Mac)

### Configuration

- **API Keys**: If using external OCR services, add your API keys to `local.properties`:
  ```
  ocr.api.key=your_api_key_here
  ```

- **Database Encryption**: To enable database encryption, modify the `DatabaseModule.kt` file and uncomment the encryption setup.

## Project Documentation

Detailed documentation is available in the following files:

- [Development Plan](DEVELOPMENT_PLAN.md): Overall project plan and implementation strategy
- [Project Structure](PROJECT_STRUCTURE.md): Detailed directory structure and file organization
- [UI/UX Design](UI_UX_DESIGN.md): User interface design principles, screen layouts, and user flows
- [Database Schema](DATABASE_SCHEMA.md): Room database structure, entities, and relationships

## Architecture

The application follows the MVVM (Model-View-ViewModel) architectural pattern with a clean architecture approach:

### Layers

1. **Presentation Layer**
   - Compose UI components (Screens, Composables)
   - ViewModels (UI state management, business logic)
   - UI State models

2. **Domain Layer**
   - Use Cases (business logic)
   - Domain Models (pure Kotlin data classes)
   - Repository Interfaces

3. **Data Layer**
   - Repositories (implementation)
   - Data Sources (local database, file storage)
   - Data Models (entities, DTOs)

### Data Flow

1. UI interacts with ViewModel
2. ViewModel executes Use Cases
3. Use Cases interact with Repositories
4. Repositories access Data Sources
5. Data flows back up through the layers, transforming as needed

## Development Guidelines

### Code Style

- Follow Kotlin coding conventions
- Use meaningful names for variables, functions, and classes
- Write descriptive comments for complex logic
- Keep functions small and focused on a single responsibility

### Compose UI

- Use Material 3 components
- Create reusable composable functions
- Use state hoisting pattern
- Implement preview functions for UI components

### Testing

- Write unit tests for ViewModels and Use Cases
- Write integration tests for Repositories
- Implement UI tests with Compose testing APIs

### Git Workflow

- Create feature branches from `develop`
- Use descriptive commit messages
- Submit pull requests for code review
- Merge to `develop` after approval
- Periodically merge `develop` to `main` for releases

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- This project is adapted from a web-based expense tracker application
- Icons provided by Material Design Icons
- Special thanks to all contributors