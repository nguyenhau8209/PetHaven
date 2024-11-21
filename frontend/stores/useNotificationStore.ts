import { defineStore } from 'pinia';

export const useNotificationStore = defineStore('notification', {
    state: () => ({
        notifications: [] as Array<{ message: string; type: string }>,
    }),
    actions: {
        addNotification(message: string, type: string ) {
            this.notifications.push({ message, type });
        },
        removeNotification(index: number) {
            this.notifications.splice(index, 1);
        },
        removeAllNotifications() {
            if (this.notifications.length > 0) {
               for (let i = this.notifications.length - 1; i >= 0; i--) {
                   this.removeNotification(i);
               }
            }
        }
    },
});
