package com.example.inventory;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\f\u001a\u00020\rH\u0016R\u001e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004@BX\u0086.\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u001e\u0010\t\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\b@BX\u0086.\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\u000e"}, d2 = {"Lcom/example/inventory/InventoryApplication;", "Landroid/app/Application;", "()V", "<set-?>", "Lcom/example/inventory/data/AppDatabase;", "database", "getDatabase", "()Lcom/example/inventory/data/AppDatabase;", "Lcom/example/inventory/data/ItemRepository;", "repository", "getRepository", "()Lcom/example/inventory/data/ItemRepository;", "onCreate", "", "app_debug"})
public final class InventoryApplication extends android.app.Application {
    private com.example.inventory.data.AppDatabase database;
    private com.example.inventory.data.ItemRepository repository;
    
    public InventoryApplication() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.example.inventory.data.AppDatabase getDatabase() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.example.inventory.data.ItemRepository getRepository() {
        return null;
    }
    
    @java.lang.Override()
    public void onCreate() {
    }
}