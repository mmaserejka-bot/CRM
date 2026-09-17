package ru.advokat.crm;

import android.app.*;
import android.content.*;
import android.os.Build;

public class NotificationReceiver extends BroadcastReceiver {
    @Override public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("title");
        String type = intent.getStringExtra("type");
        String no = intent.getStringExtra("caseNo");
        String id = intent.getStringExtra("id");

        if (title == null) title = "Напоминание";
        if (no == null) no = "";

        String prefix = "event".equals(type) ? "⚖️ " :
                        "deadline".equals(type) ? "⏰ " : "🔔 ";
        String text = prefix + title + (no.isEmpty() ? "" : " · Дело № " + no);

        NotificationManager nm =
            (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel ch = new NotificationChannel(
                MainActivity.CHANNEL_ID, "Напоминания CRM",
                NotificationManager.IMPORTANCE_HIGH);
            nm.createNotificationChannel(ch);
        }

        Intent open = new Intent(context, MainActivity.class);
        open.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pi = PendingIntent.getActivity(
            context, 0, open,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Notification.Builder b = Build.VERSION.SDK_INT >= 26
            ? new Notification.Builder(context, MainActivity.CHANNEL_ID)
            : new Notification.Builder(context);

        b.setSmallIcon(R.drawable.ic_stat_legal)
         .setContentTitle("Адвокат CRM")
         .setContentText(text)
         .setStyle(new Notification.BigTextStyle().bigText(text))
         .setAutoCancel(true)
         .setContentIntent(pi)
         .setPriority(Notification.PRIORITY_HIGH);

        nm.notify(Math.abs((id == null ? title : id).hashCode()), b.build());
    }
}
