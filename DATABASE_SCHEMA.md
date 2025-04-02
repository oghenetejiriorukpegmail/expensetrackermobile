# ExpenseTrackerMobile - Database Schema

This document outlines the Room database schema for the ExpenseTrackerMobile Android application, including entities, relationships, DAOs, type converters, and migration strategies.

## 1. Database Overview

The application uses Room Persistence Library to manage a SQLite database with the following entities:
- Expenses
- Categories
- Trips
- Budgets

The database follows a relational model with foreign key relationships between entities.

## 2. Entities

### Expense Entity

Represents an individual expense record.

```kotlin
@Entity(
    tableName = "expenses",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.SET_DEFAULT
        ),
        ForeignKey(
            entity = TripEntity::class,
            parentColumns = ["id"],
            childColumns = ["trip_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(value = ["category_id"]),
        Index(value = ["trip_id"]),
        Index(value = ["date"])
    ]
)
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    @ColumnInfo(name = "amount")
    val amount: BigDecimal,
    
    @ColumnInfo(name = "date")
    val date: Date,
    
    @ColumnInfo(name = "category_id", defaultValue = "1") // Default to "Uncategorized"
    val categoryId: Long,
    
    @ColumnInfo(name = "trip_id")
    val tripId: Long?, // Nullable - not all expenses are associated with trips
    
    @ColumnInfo(name = "vendor")
    val vendor: String,
    
    @ColumnInfo(name = "location")
    val location: String,
    
    @ColumnInfo(name = "notes")
    val notes: String?,
    
    @ColumnInfo(name = "receipt_image_path")
    val receiptImagePath: String?,
    
    @ColumnInfo(name = "created_at")
    val createdAt: Date,
    
    @ColumnInfo(name = "updated_at")
    val updatedAt: Date
)
```

### Category Entity

Represents an expense category.

```kotlin
@Entity(
    tableName = "categories",
    indices = [Index(value = ["name"], unique = true)]
)
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    @ColumnInfo(name = "name")
    val name: String,
    
    @ColumnInfo(name = "color")
    val color: Int, // Color resource as integer
    
    @ColumnInfo(name = "icon")
    val icon: String, // Icon resource name
    
    @ColumnInfo(name = "is_default")
    val isDefault: Boolean = false,
    
    @ColumnInfo(name = "created_at")
    val createdAt: Date,
    
    @ColumnInfo(name = "updated_at")
    val updatedAt: Date
) {
    companion object {
        // Default "Uncategorized" category
        val DEFAULT = CategoryEntity(
            id = 1,
            name = "Uncategorized",
            color = 0xFF9E9E9E.toInt(), // Gray
            icon = "category",
            isDefault = true,
            createdAt = Date(),
            updatedAt = Date()
        )
    }
}
```

### Trip Entity

Represents a trip or event that groups related expenses.

```kotlin
@Entity(
    tableName = "trips",
    indices = [Index(value = ["name"], unique = true)]
)
data class TripEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    @ColumnInfo(name = "name")
    val name: String,
    
    @ColumnInfo(name = "description")
    val description: String?,
    
    @ColumnInfo(name = "start_date")
    val startDate: Date?,
    
    @ColumnInfo(name = "end_date")
    val endDate: Date?,
    
    @ColumnInfo(name = "created_at")
    val createdAt: Date,
    
    @ColumnInfo(name = "updated_at")
    val updatedAt: Date
)
```

### Budget Entity

Represents a budget for a specific time period, either overall or for a specific category.

```kotlin
@Entity(
    tableName = "budgets",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["category_id", "month", "year"], unique = true),
        Index(value = ["category_id"])
    ]
)
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    @ColumnInfo(name = "amount")
    val amount: BigDecimal,
    
    @ColumnInfo(name = "month")
    val month: Int, // 1-12 for January-December
    
    @ColumnInfo(name = "year")
    val year: Int,
    
    @ColumnInfo(name = "category_id")
    val categoryId: Long?, // Null means overall budget
    
    @ColumnInfo(name = "created_at")
    val createdAt: Date,
    
    @ColumnInfo(name = "updated_at")
    val updatedAt: Date
)
```

## 3. Data Access Objects (DAOs)

### ExpenseDao

```kotlin
@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>
    
    @Query("SELECT * FROM expenses WHERE id = :id")
    suspend fun getExpenseById(id: Long): ExpenseEntity?
    
    @Query("SELECT * FROM expenses WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getExpensesByDateRange(startDate: Date, endDate: Date): Flow<List<ExpenseEntity>>
    
    @Query("SELECT * FROM expenses WHERE category_id = :categoryId ORDER BY date DESC")
    fun getExpensesByCategory(categoryId: Long): Flow<List<ExpenseEntity>>
    
    @Query("SELECT * FROM expenses WHERE trip_id = :tripId ORDER BY date DESC")
    fun getExpensesByTrip(tripId: Long): Flow<List<ExpenseEntity>>
    
    @Query("SELECT SUM(amount) FROM expenses WHERE date BETWEEN :startDate AND :endDate")
    suspend fun getTotalExpenseAmount(startDate: Date, endDate: Date): BigDecimal?
    
    @Query("SELECT SUM(amount) FROM expenses WHERE category_id = :categoryId AND date BETWEEN :startDate AND :endDate")
    suspend fun getTotalExpenseAmountByCategory(categoryId: Long, startDate: Date, endDate: Date): BigDecimal?
    
    @Query("SELECT SUM(amount) FROM expenses WHERE trip_id = :tripId")
    suspend fun getTotalExpenseAmountByTrip(tripId: Long): BigDecimal?
    
    @Insert
    suspend fun insertExpense(expense: ExpenseEntity): Long
    
    @Update
    suspend fun updateExpense(expense: ExpenseEntity)
    
    @Delete
    suspend fun deleteExpense(expense: ExpenseEntity)
    
    @Query("DELETE FROM expenses WHERE id = :id")
    suspend fun deleteExpenseById(id: Long)
    
    @Transaction
    @Query("SELECT * FROM expenses WHERE id = :expenseId")
    suspend fun getExpenseWithCategory(expenseId: Long): ExpenseWithCategory?
    
    @Transaction
    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpensesWithCategory(): Flow<List<ExpenseWithCategory>>
}
```

### CategoryDao

```kotlin
@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories ORDER BY name ASC")
    fun getAllCategories(): Flow<List<CategoryEntity>>
    
    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getCategoryById(id: Long): CategoryEntity?
    
    @Query("SELECT * FROM categories WHERE is_default = 1 LIMIT 1")
    suspend fun getDefaultCategory(): CategoryEntity?
    
    @Insert
    suspend fun insertCategory(category: CategoryEntity): Long
    
    @Insert
    suspend fun insertCategories(categories: List<CategoryEntity>): List<Long>
    
    @Update
    suspend fun updateCategory(category: CategoryEntity)
    
    @Delete
    suspend fun deleteCategory(category: CategoryEntity)
    
    @Query("DELETE FROM categories WHERE id = :id AND is_default = 0")
    suspend fun deleteCategoryById(id: Long): Int
    
    @Transaction
    @Query("SELECT * FROM categories WHERE id = :categoryId")
    suspend fun getCategoryWithExpenses(categoryId: Long): CategoryWithExpenses?
}
```

### TripDao

```kotlin
@Dao
interface TripDao {
    @Query("SELECT * FROM trips ORDER BY start_date DESC, name ASC")
    fun getAllTrips(): Flow<List<TripEntity>>
    
    @Query("SELECT * FROM trips WHERE id = :id")
    suspend fun getTripById(id: Long): TripEntity?
    
    @Insert
    suspend fun insertTrip(trip: TripEntity): Long
    
    @Update
    suspend fun updateTrip(trip: TripEntity)
    
    @Delete
    suspend fun deleteTrip(trip: TripEntity)
    
    @Query("DELETE FROM trips WHERE id = :id")
    suspend fun deleteTripById(id: Long)
    
    @Transaction
    @Query("SELECT * FROM trips WHERE id = :tripId")
    suspend fun getTripWithExpenses(tripId: Long): TripWithExpenses?
}
```

### BudgetDao

```kotlin
@Dao
interface BudgetDao {
    @Query("SELECT * FROM budgets ORDER BY year DESC, month DESC")
    fun getAllBudgets(): Flow<List<BudgetEntity>>
    
    @Query("SELECT * FROM budgets WHERE month = :month AND year = :year")
    fun getBudgetsByMonthYear(month: Int, year: Int): Flow<List<BudgetEntity>>
    
    @Query("SELECT * FROM budgets WHERE category_id = :categoryId AND month = :month AND year = :year")
    suspend fun getBudgetByCategoryAndMonthYear(categoryId: Long, month: Int, year: Int): BudgetEntity?
    
    @Query("SELECT * FROM budgets WHERE category_id IS NULL AND month = :month AND year = :year")
    suspend fun getOverallBudgetByMonthYear(month: Int, year: Int): BudgetEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budget: BudgetEntity): Long
    
    @Update
    suspend fun updateBudget(budget: BudgetEntity)
    
    @Delete
    suspend fun deleteBudget(budget: BudgetEntity)
    
    @Query("DELETE FROM budgets WHERE id = :id")
    suspend fun deleteBudgetById(id: Long)
}
```

## 4. Relationships and Embedded Objects

### Expense with Category

```kotlin
data class ExpenseWithCategory(
    @Embedded val expense: ExpenseEntity,
    @Relation(
        parentColumn = "category_id",
        entityColumn = "id"
    )
    val category: CategoryEntity
)
```

### Category with Expenses

```kotlin
data class CategoryWithExpenses(
    @Embedded val category: CategoryEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "category_id"
    )
    val expenses: List<ExpenseEntity>
)
```

### Trip with Expenses

```kotlin
data class TripWithExpenses(
    @Embedded val trip: TripEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "trip_id"
    )
    val expenses: List<ExpenseEntity>
)
```

## 5. Type Converters

```kotlin
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromBigDecimal(value: BigDecimal?): String? {
        return value?.toString()
    }

    @TypeConverter
    fun toBigDecimal(value: String?): BigDecimal? {
        return value?.let { BigDecimal(it) }
    }
}
```

## 6. Database Configuration

```kotlin
@Database(
    entities = [
        ExpenseEntity::class,
        CategoryEntity::class,
        TripEntity::class,
        BudgetEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
    abstract fun categoryDao(): CategoryDao
    abstract fun tripDao(): TripDao
    abstract fun budgetDao(): BudgetDao

    companion object {
        private const val DATABASE_NAME = "expense_tracker_db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Populate default data on first creation
                        CoroutineScope(Dispatchers.IO).launch {
                            val database = getInstance(context)
                            // Insert default category
                            database.categoryDao().insertCategory(CategoryEntity.DEFAULT)
                            
                            // Insert other default categories
                            val defaultCategories = listOf(
                                CategoryEntity(
                                    name = "Food & Dining",
                                    color = 0xFF4CAF50.toInt(), // Green
                                    icon = "restaurant",
                                    createdAt = Date(),
                                    updatedAt = Date()
                                ),
                                CategoryEntity(
                                    name = "Transportation",
                                    color = 0xFF2196F3.toInt(), // Blue
                                    icon = "directions_car",
                                    createdAt = Date(),
                                    updatedAt = Date()
                                ),
                                CategoryEntity(
                                    name = "Shopping",
                                    color = 0xFFF44336.toInt(), // Red
                                    icon = "shopping_bag",
                                    createdAt = Date(),
                                    updatedAt = Date()
                                ),
                                CategoryEntity(
                                    name = "Entertainment",
                                    color = 0xFF9C27B0.toInt(), // Purple
                                    icon = "movie",
                                    createdAt = Date(),
                                    updatedAt = Date()
                                ),
                                CategoryEntity(
                                    name = "Bills & Utilities",
                                    color = 0xFFFF9800.toInt(), // Orange
                                    icon = "receipt",
                                    createdAt = Date(),
                                    updatedAt = Date()
                                )
                            )
                            database.categoryDao().insertCategories(defaultCategories)
                        }
                    }
                })
                .fallbackToDestructiveMigration() // For development only
                .build()
                
                INSTANCE = instance
                instance
            }
        }
    }
}
```

## 7. Migration Strategy

For the initial version, we're using `fallbackToDestructiveMigration()` during development. For production, we'll implement proper migration strategies:

```kotlin
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Example migration: Add a new column to expenses table
        database.execSQL("ALTER TABLE expenses ADD COLUMN is_recurring INTEGER NOT NULL DEFAULT 0")
    }
}

// In the database builder:
.addMigrations(MIGRATION_1_2)
```

## 8. Database Module (Hilt)

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideExpenseDao(database: AppDatabase): ExpenseDao {
        return database.expenseDao()
    }

    @Provides
    fun provideCategoryDao(database: AppDatabase): CategoryDao {
        return database.categoryDao()
    }

    @Provides
    fun provideTripDao(database: AppDatabase): TripDao {
        return database.tripDao()
    }

    @Provides
    fun provideBudgetDao(database: AppDatabase): BudgetDao {
        return database.budgetDao()
    }
}
```

## 9. Database Queries and Operations

### Common Expense Queries

1. **Get all expenses for the current month:**
   ```kotlin
   fun getCurrentMonthExpenses(): Flow<List<ExpenseEntity>> {
       val calendar = Calendar.getInstance()
       val year = calendar.get(Calendar.YEAR)
       val month = calendar.get(Calendar.MONTH)
       
       calendar.set(year, month, 1, 0, 0, 0)
       val startDate = calendar.time
       
       calendar.add(Calendar.MONTH, 1)
       calendar.add(Calendar.MILLISECOND, -1)
       val endDate = calendar.time
       
       return expenseDao.getExpensesByDateRange(startDate, endDate)
   }
   ```

2. **Get expenses by category with total:**
   ```kotlin
   @Transaction
   suspend fun getExpensesSummaryByCategory(startDate: Date, endDate: Date): Map<CategoryEntity, BigDecimal> {
       val categories = categoryDao.getAllCategories().first()
       val result = mutableMapOf<CategoryEntity, BigDecimal>()
       
       categories.forEach { category ->
           val total = expenseDao.getTotalExpenseAmountByCategory(category.id, startDate, endDate) ?: BigDecimal.ZERO
           result[category] = total
       }
       
       return result
   }
   ```

3. **Get monthly spending trend:**
   ```kotlin
   suspend fun getMonthlySpendingTrend(months: Int): List<MonthlySpending> {
       val result = mutableListOf<MonthlySpending>()
       val calendar = Calendar.getInstance()
       
       // Start from current month and go back 'months' months
       for (i in 0 until months) {
           val year = calendar.get(Calendar.YEAR)
           val month = calendar.get(Calendar.MONTH)
           
           // Set to first day of month
           calendar.set(year, month, 1, 0, 0, 0)
           val startDate = calendar.time
           
           // Set to last day of month
           calendar.add(Calendar.MONTH, 1)
           calendar.add(Calendar.MILLISECOND, -1)
           val endDate = calendar.time
           
           val total = expenseDao.getTotalExpenseAmount(startDate, endDate) ?: BigDecimal.ZERO
           
           result.add(MonthlySpending(month, year, total))
           
           // Go back one month
           calendar.set(year, month, 1)
           calendar.add(Calendar.MONTH, -1)
       }
       
       return result.reversed() // Return in chronological order
   }
   ```

### Budget Operations

1. **Check if budget is exceeded:**
   ```kotlin
   suspend fun isBudgetExceeded(categoryId: Long?, month: Int, year: Int): Boolean {
       val budget = if (categoryId == null) {
           budgetDao.getOverallBudgetByMonthYear(month, year)
       } else {
           budgetDao.getBudgetByCategoryAndMonthYear(categoryId, month, year)
       } ?: return false // No budget means not exceeded
       
       val calendar = Calendar.getInstance()
       calendar.set(year, month - 1, 1, 0, 0, 0)
       val startDate = calendar.time
       
       calendar.add(Calendar.MONTH, 1)
       calendar.add(Calendar.MILLISECOND, -1)
       val endDate = calendar.time
       
       val spent = if (categoryId == null) {
           expenseDao.getTotalExpenseAmount(startDate, endDate) ?: BigDecimal.ZERO
       } else {
           expenseDao.getTotalExpenseAmountByCategory(categoryId, startDate, endDate) ?: BigDecimal.ZERO
       }
       
       return spent > budget.amount
   }
   ```

2. **Get budget utilization percentage:**
   ```kotlin
   suspend fun getBudgetUtilizationPercentage(categoryId: Long?, month: Int, year: Int): Float {
       val budget = if (categoryId == null) {
           budgetDao.getOverallBudgetByMonthYear(month, year)
       } else {
           budgetDao.getBudgetByCategoryAndMonthYear(categoryId, month, year)
       } ?: return 0f // No budget means 0% utilization
       
       val calendar = Calendar.getInstance()
       calendar.set(year, month - 1, 1, 0, 0, 0)
       val startDate = calendar.time
       
       calendar.add(Calendar.MONTH, 1)
       calendar.add(Calendar.MILLISECOND, -1)
       val endDate = calendar.time
       
       val spent = if (categoryId == null) {
           expenseDao.getTotalExpenseAmount(startDate, endDate) ?: BigDecimal.ZERO
       } else {
           expenseDao.getTotalExpenseAmountByCategory(categoryId, startDate, endDate) ?: BigDecimal.ZERO
       }
       
       if (budget.amount.compareTo(BigDecimal.ZERO) == 0) return 100f
       
       return (spent.toFloat() / budget.amount.toFloat()) * 100f
   }
   ```

## 10. Data Models

### MonthlySpending

```kotlin
data class MonthlySpending(
    val month: Int, // 0-11 for January-December
    val year: Int,
    val amount: BigDecimal
) {
    fun getMonthName(): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, month)
        return SimpleDateFormat("MMMM", Locale.getDefault()).format(calendar.time)
    }
}
```

### BudgetWithSpending

```kotlin
data class BudgetWithSpending(
    val budget: BudgetEntity,
    val spent: BigDecimal,
    val remaining: BigDecimal,
    val utilizationPercentage: Float,
    val category: CategoryEntity?
)
```

## 11. Security Considerations

1. **Encryption**: For sensitive financial data, consider using SQLCipher for database encryption:

```kotlin
Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
    .openHelperFactory(SupportFactory(SQLiteDatabase.getBytes("encryption-passphrase".toCharArray())))
    .build()
```

2. **Secure Key Storage**: Store encryption keys in Android Keystore:

```kotlin
private fun getEncryptionKey(): ByteArray {
    val keyStore = KeyStore.getInstance("AndroidKeyStore")
    keyStore.load(null)
    
    if (!keyStore.containsAlias(KEY_ALIAS)) {
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore"
        )
        keyGenerator.init(
            KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .build()
        )
        keyGenerator.generateKey()
    }
    
    val key = keyStore.getKey(KEY_ALIAS, null) as SecretKey
    return key.encoded
}
```

3. **Input Validation**: Validate all inputs before storing in the database to prevent SQL injection (Room handles most of this automatically).

4. **Backup Considerations**: When implementing backup functionality, ensure sensitive data is properly encrypted during the backup process.