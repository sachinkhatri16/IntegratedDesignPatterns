package com.foodordering.behavioral;

/**
 * Factory for creating Observer instances
 */
public class NotificationObserverFactory {
    
    public static OrderObserver createEmailObserver(String email) {
        return new EmailNotificationObserver(email);
    }
    
    public static OrderObserver createSMSObserver(String phoneNumber) {
        return new SMSNotificationObserver(phoneNumber);
    }
    
    public static OrderObserver createPushObserver(String deviceId) {
        return new PushNotificationObserver(deviceId);
    }
}
