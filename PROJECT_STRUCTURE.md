# ExpenseTrackerMobile - Project Structure

This document outlines the directory structure and key files for the ExpenseTrackerMobile Android application.

## Root Project Structure

```
expensetrackermobile/
├── app/                        # Main application module
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/expensetracker/
│   │   │   │   ├── data/       # Data layer
│   │   │   │   ├── di/         # Dependency injection
│   │   │   │   ├── domain/     # Domain layer
│   │   │   │   ├── ui/         # UI layer
│   │   │   │   ├── util/       # Utilities
│   │   │   │   └── ExpenseTrackerApplication.kt
│   │   │   ├── res/            # Resources
│   │   │   └── AndroidManifest.xml
│   │   ├── test/               # Unit tests
│   │   └── androidTest/        # Instrumentation tests
│   ├── build.gradle.kts        # App module build script
│   └── proguard-rules.pro      # ProGuard rules
├── build.gradle.kts            # Project build script
├── gradle/                     # Gradle wrapper and configuration
├── settings.gradle.kts         # Gradle settings
├── gradlew                     # Gradle wrapper script (Unix)
├── gradlew.bat                 # Gradle wrapper script (Windows)
└── README.md                   # Project documentation
```

## Detailed Structure

### Data Layer (`app/src/main/java/com/expensetracker/data/`)

```
data/
├── database/                   # Room database
│   ├── AppDatabase.kt          # Database configuration
│   ├── Converters.kt           # Type converters
│   ├── dao/                    # Data Access Objects
│   │   ├── ExpenseDao.kt
│   │   ├── CategoryDao.kt
│   │   ├── TripDao.kt
│   │   └── BudgetDao.kt
│   └── entity/                 # Database entities
│       ├── ExpenseEntity.kt
│       ├── CategoryEntity.kt
│       ├── TripEntity.kt
│       └── BudgetEntity.kt
├── repository/                 # Repository implementations
│   ├── ExpenseRepositoryImpl.kt
│   ├── CategoryRepositoryImpl.kt
│   ├── TripRepositoryImpl.kt
│   └── BudgetRepositoryImpl.kt
├── datasource/                 # Data sources
│   ├── local/                  # Local data sources
│   │   ├── ExpenseLocalDataSource.kt
│   │   ├── CategoryLocalDataSource.kt
│   │   ├── TripLocalDataSource.kt
│   │   └── BudgetLocalDataSource.kt
│   └── file/                   # File operations
│       ├── ImageStorage.kt
│       └── ExportService.kt
└── mapper/                     # Data mappers
    ├── ExpenseMapper.kt
    ├── CategoryMapper.kt
    ├── TripMapper.kt
    └── BudgetMapper.kt
```

### Domain Layer (`app/src/main/java/com/expensetracker/domain/`)

```
domain/
├── model/                      # Domain models
│   ├── Expense.kt
│   ├── Category.kt
│   ├── Trip.kt
│   └── Budget.kt
├── repository/                 # Repository interfaces
│   ├── ExpenseRepository.kt
│   ├── CategoryRepository.kt
│   ├── TripRepository.kt
│   └── BudgetRepository.kt
└── usecase/                    # Use cases
    ├── expense/
    │   ├── GetExpensesUseCase.kt
    │   ├── AddExpenseUseCase.kt
    │   ├── UpdateExpenseUseCase.kt
    │   ├── DeleteExpenseUseCase.kt
    │   └── ProcessReceiptUseCase.kt
    ├── category/
    │   ├── GetCategoriesUseCase.kt
    │   ├── AddCategoryUseCase.kt
    │   ├── UpdateCategoryUseCase.kt
    │   └── DeleteCategoryUseCase.kt
    ├── trip/
    │   ├── GetTripsUseCase.kt
    │   ├── AddTripUseCase.kt
    │   ├── UpdateTripUseCase.kt
    │   └── DeleteTripUseCase.kt
    ├── budget/
    │   ├── GetBudgetsUseCase.kt
    │   ├── AddBudgetUseCase.kt
    │   ├── UpdateBudgetUseCase.kt
    │   └── DeleteBudgetUseCase.kt
    └── analytics/
        ├── GetExpenseSummaryUseCase.kt
        ├── GetCategoryBreakdownUseCase.kt
        ├── GetSpendingTrendsUseCase.kt
        └── GetBudgetComparisonUseCase.kt
```

### UI Layer (`app/src/main/java/com/expensetracker/ui/`)

```
ui/
├── MainActivity.kt             # Main activity
├── theme/                      # Theme configuration
│   ├── Color.kt
│   ├── Theme.kt
│   ├── Type.kt
│   └── Shape.kt
├── navigation/                 # Navigation configuration
│   ├── NavGraph.kt
│   └── Screen.kt
├── common/                     # Common UI components
│   ├── components/             # Reusable composables
│   │   ├── ExpenseItem.kt
│   │   ├── CategoryChip.kt
│   │   ├── TripCard.kt
│   │   ├── BudgetProgressBar.kt
│   │   ├── DatePicker.kt
│   │   ├── AmountInput.kt
│   │   ├── ReceiptThumbnail.kt
│   │   └── Charts.kt
│   └── util/                   # UI utilities
│       ├── ComposeExtensions.kt
│       └── UiText.kt
├── dashboard/                  # Dashboard screen
│   ├── DashboardScreen.kt
│   ├── DashboardViewModel.kt
│   └── components/
│       ├── SummaryCard.kt
│       ├── RecentExpensesList.kt
│       └── DashboardCharts.kt
├── expenses/                   # Expenses screen
│   ├── list/
│   │   ├── ExpensesScreen.kt
│   │   ├── ExpensesViewModel.kt
│   │   └── components/
│   │       ├── ExpenseFilter.kt
│   │       └── ExpenseList.kt
│   └── detail/
│       ├── AddEditExpenseScreen.kt
│       ├── AddEditExpenseViewModel.kt
│       └── components/
│           ├── ExpenseForm.kt
│           ├── CategorySelector.kt
│           ├── TripSelector.kt
│           └── ReceiptCapture.kt
├── categories/                 # Categories screen
│   ├── list/
│   │   ├── CategoriesScreen.kt
│   │   ├── CategoriesViewModel.kt
│   │   └── components/
│   │       └── CategoryList.kt
│   └── detail/
│       ├── AddEditCategoryScreen.kt
│       ├── AddEditCategoryViewModel.kt
│       └── components/
│           ├── CategoryForm.kt
│           └── ColorPicker.kt
├── trips/                      # Trips screen
│   ├── list/
│   │   ├── TripsScreen.kt
│   │   ├── TripsViewModel.kt
│   │   └── components/
│   │       └── TripList.kt
│   └── detail/
│       ├── TripDetailScreen.kt
│       ├── TripDetailViewModel.kt
│       ├── AddEditTripScreen.kt
│       ├── AddEditTripViewModel.kt
│       └── components/
│           ├── TripForm.kt
│           └── TripExpensesList.kt
├── budgets/                    # Budgets screen
│   ├── BudgetsScreen.kt
│   ├── BudgetsViewModel.kt
│   └── components/
│       ├── BudgetList.kt
│       ├── AddEditBudgetDialog.kt
│       └── BudgetChart.kt
├── analytics/                  # Analytics screen
│   ├── AnalyticsScreen.kt
│   ├── AnalyticsViewModel.kt
│   └── components/
│       ├── CategoryBreakdownChart.kt
│       ├── SpendingTrendChart.kt
│       └── AnalyticsFilters.kt
└── settings/                   # Settings screen
    ├── SettingsScreen.kt
    ├── SettingsViewModel.kt
    └── components/
        ├── ThemeSelector.kt
        ├── ExportOptions.kt
        └── BackupOptions.kt
```

### Dependency Injection (`app/src/main/java/com/expensetracker/di/`)

```
di/
├── AppModule.kt                # Application-level dependencies
├── DatabaseModule.kt           # Database-related dependencies
├── RepositoryModule.kt         # Repository dependencies
├── UseCaseModule.kt            # Use case dependencies
└── ViewModelModule.kt          # ViewModel dependencies
```

### Utilities (`app/src/main/java/com/expensetracker/util/`)

```
util/
├── Constants.kt                # Application constants
├── DateUtils.kt                # Date handling utilities
├── CurrencyUtils.kt            # Currency formatting utilities
├── FileUtils.kt                # File handling utilities
├── PermissionUtils.kt          # Permission handling utilities
└── extensions/                 # Kotlin extensions
    ├── ContextExtensions.kt
    ├── FlowExtensions.kt
    └── StringExtensions.kt
```

### Resources (`app/src/main/res/`)

```
res/
├── drawable/                   # Drawable resources
├── drawable-night/             # Night mode drawable resources
├── layout/                     # XML layouts (minimal for Compose)
├── values/                     # Value resources
│   ├── colors.xml              # Color definitions
│   ├── strings.xml             # String resources
│   ├── themes.xml              # Light theme
│   └── dimens.xml              # Dimension values
├── values-night/               # Night mode values
│   └── themes.xml              # Dark theme
├── xml/                        # XML configuration files
│   ├── backup_rules.xml        # Backup rules
│   └── file_paths.xml          # File provider paths
└── font/                       # Custom fonts
```

## Key Files Description

### Application Configuration

- **ExpenseTrackerApplication.kt**: Application class with Hilt initialization
- **AndroidManifest.xml**: App manifest with permissions and components
- **build.gradle.kts**: Dependency and build configuration

### Database Configuration

- **AppDatabase.kt**: Room database setup with entities and migrations
- **Converters.kt**: Type converters for complex data types (Date, BigDecimal)

### Core Domain Models

- **Expense.kt**: Represents an expense with amount, date, category, etc.
- **Category.kt**: Represents an expense category with name, color, icon
- **Trip.kt**: Represents a trip with name, dates, description
- **Budget.kt**: Represents a budget with amount, period, category

### Main UI Screens

- **MainActivity.kt**: Single activity hosting Compose navigation
- **NavGraph.kt**: Navigation graph defining app screens and routes
- **DashboardScreen.kt**: Main dashboard with summary and charts
- **ExpensesScreen.kt**: List of expenses with filtering and sorting
- **AddEditExpenseScreen.kt**: Form for adding/editing expenses
- **CategoriesScreen.kt**: Category management screen
- **TripsScreen.kt**: Trip management screen
- **BudgetsScreen.kt**: Budget management screen
- **AnalyticsScreen.kt**: Detailed analytics and charts
- **SettingsScreen.kt**: App settings and preferences

### ViewModels

- **DashboardViewModel.kt**: Manages dashboard data and state
- **ExpensesViewModel.kt**: Manages expense list data and filtering
- **AddEditExpenseViewModel.kt**: Manages expense form state and submission
- **CategoriesViewModel.kt**: Manages category data and operations
- **TripsViewModel.kt**: Manages trip data and operations
- **BudgetsViewModel.kt**: Manages budget data and operations
- **AnalyticsViewModel.kt**: Manages analytics data and calculations
- **SettingsViewModel.kt**: Manages app settings and preferences

### Repositories

- **ExpenseRepository.kt**: Interface for expense data operations
- **CategoryRepository.kt**: Interface for category data operations
- **TripRepository.kt**: Interface for trip data operations
- **BudgetRepository.kt**: Interface for budget data operations

### Use Cases

- **AddExpenseUseCase.kt**: Business logic for adding expenses
- **ProcessReceiptUseCase.kt**: Logic for OCR processing of receipts
- **GetExpenseSummaryUseCase.kt**: Logic for generating expense summaries
- **GetCategoryBreakdownUseCase.kt**: Logic for category distribution analysis