# Облачная сборка APK — Advokat CRM

1. Создайте **private repository** на GitHub.
2. Загрузите в него всё содержимое этой папки, включая `.github/workflows/build-apk.yml`.
3. Откройте **Actions → Build Advokat CRM APK → Run workflow**.
4. После окончания сборки откройте запуск workflow и скачайте artifact **AdvokatCRM-debug-apk**.
5. Внутри будет `app-debug.apk`.

Android Studio для облачной сборки не требуется: GitHub Actions устанавливает JDK 17, Gradle 8.7 и Android SDK 35 на своей виртуальной машине.

Рекомендуется использовать private repository и не загружать в GitHub клиентские данные.
