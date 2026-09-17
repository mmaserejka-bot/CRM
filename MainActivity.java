package ru.advokat.crm;

import android.Manifest;
import android.app.*;
import android.content.*;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainActivity extends Activity {
    public static final String CHANNEL_ID = "advokat_crm_reminders";
    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        createChannel();
        if (Build.VERSION.SDK_INT >= 33 &&
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 100);
        }
        WebView w = new WebView(this);
        WebSettings s = w.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setAllowFileAccess(true);
        w.addJavascriptInterface(new AndroidBridge(this), "AndroidCRM");
        w.loadUrl("file:///android_asset/index.html");
        setContentView(w);
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel c = new NotificationChannel(
                CHANNEL_ID, "Напоминания CRM", NotificationManager.IMPORTANCE_HIGH);
            c.setDescription("Судебные заседания и процессуальные сроки");
            getSystemService(NotificationManager.class).createNotificationChannel(c);
        }
    }

    public static class AndroidBridge {
        private final Activity a;
        AndroidBridge(Activity a) { this.a = a; }

        @JavascriptInterface
        public void scheduleNotification(String id, String when, String title,
                                          String type, String caseNo) {
            try {
                Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                    .parse(when.replace("T"," "));
                if (d == null || d.getTime() <= System.currentTimeMillis()) return;

                Intent i = new Intent(a, NotificationReceiver.class);
                i.putExtra("id", id);
                i.putExtra("title", title);
                i.putExtra("type", type);
                i.putExtra("caseNo", caseNo);

                int code = Math.abs(id.hashCode());
                PendingIntent pi = PendingIntent.getBroadcast(
                    a, code, i,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                AlarmManager am = (AlarmManager)a.getSystemService(ALARM_SERVICE);
                if (Build.VERSION.SDK_INT >= 31 && !am.canScheduleExactAlarms()) {
                    am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, d.getTime(), pi);
                } else if (Build.VERSION.SDK_INT >= 23) {
                    am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, d.getTime(), pi);
                } else {
                    am.setExact(AlarmManager.RTC_WAKEUP, d.getTime(), pi);
                }
            } catch (Exception ignored) {}
        }

        @JavascriptInterface
        public void cancelNotification(String id) {
            int code = Math.abs(id.hashCode());
            Intent i = new Intent(a, NotificationReceiver.class);
            PendingIntent pi = PendingIntent.getBroadcast(
                a, code, i,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            ((AlarmManager)a.getSystemService(ALARM_SERVICE)).cancel(pi);
            pi.cancel();
        }
    }
}
