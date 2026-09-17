# Адвокат CRM — готовый Android Studio проект

Это проект именно для существующей CRM.

## Включено
- зелёно-золотой рисованный интерфейс;
- карточка дела;
- история, документы, сроки, напоминания;
- календарь;
- задачи и клиенты;
- локальные Android-уведомления через AlarmManager;
- разрешение POST_NOTIFICATIONS для Android 13+.

## Как получить APK
Откройте корневую папку проекта в Android Studio.
После синхронизации Gradle:
**Build → Build APK(s)**.

Debug APK появится в:
`app/build/outputs/apk/debug/app-debug.apk`

## Примечание
Проект содержит Gradle-конфигурацию и исходники. Gradle Wrapper не включён,
поэтому Android Studio должна иметь доступ к установленному Gradle/Android SDK.
