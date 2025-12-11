package com.example.inventory.workers;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ.\u0010\t\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\u000eJ\u0010\u0010\u000f\u001a\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\bH\u0002\u00a8\u0006\u0010"}, d2 = {"Lcom/example/inventory/workers/ReminderScheduler;", "", "()V", "cancelRemindersForItem", "", "context", "Landroid/content/Context;", "itemId", "", "scheduleRemindersForItem", "itemName", "", "userName", "borrowTime", "", "tagForItem", "app_debug"})
public final class ReminderScheduler {
    @org.jetbrains.annotations.NotNull()
    public static final com.example.inventory.workers.ReminderScheduler INSTANCE = null;
    
    private ReminderScheduler() {
        super();
    }
    
    public final void scheduleRemindersForItem(@org.jetbrains.annotations.NotNull()
    android.content.Context context, int itemId, @org.jetbrains.annotations.NotNull()
    java.lang.String itemName, @org.jetbrains.annotations.NotNull()
    java.lang.String userName, long borrowTime) {
    }
    
    public final void cancelRemindersForItem(@org.jetbrains.annotations.NotNull()
    android.content.Context context, int itemId) {
    }
    
    private final java.lang.String tagForItem(int itemId) {
        return null;
    }
}