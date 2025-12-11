package com.example.inventory.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\'\u0018\u0000 \u00072\u00020\u0001:\u0001\u0007B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0006H&\u00a8\u0006\b"}, d2 = {"Lcom/example/inventory/data/AppDatabase;", "Landroidx/room/RoomDatabase;", "()V", "borrowHistoryDao", "Lcom/example/inventory/data/BorrowHistoryDao;", "itemDao", "Lcom/example/inventory/data/ItemDao;", "Companion", "app_debug"})
@androidx.room.Database(entities = {com.example.inventory.data.ItemEntity.class, com.example.inventory.data.BorrowRecordEntity.class}, version = 2, exportSchema = false)
@androidx.room.TypeConverters(value = {com.example.inventory.data.Converters.class})
public abstract class AppDatabase extends androidx.room.RoomDatabase {
    @org.jetbrains.annotations.NotNull()
    public static final com.example.inventory.data.AppDatabase.Companion Companion = null;
    
    public AppDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.example.inventory.data.ItemDao itemDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.example.inventory.data.BorrowHistoryDao borrowHistoryDao();
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0003\u001a\u00020\u0004\u00a8\u0006\u0005"}, d2 = {"Lcom/example/inventory/data/AppDatabase$Companion;", "", "()V", "createPrepopulateCallback", "Landroidx/room/RoomDatabase$Callback;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.room.RoomDatabase.Callback createPrepopulateCallback() {
            return null;
        }
    }
}