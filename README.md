# ДеМузыка

Свой Android-аналог Яндекс.Музыки + Кинопоиска в одном приложении.
Внутри две вкладки — **ДеМузыка** (плеер, моя волна, лайки, концерты, книги)
и **ДеПоиск** (фильмы, сериалы, «моё», поиск). Отдельный APK под **Android TV**
(Leanback). Тёмная тема, оранжевые акценты, градиент «волна».

Это **скелет** — UI, навигация, темы, заготовки данных. Реальные источники
(музыкальный CDN, каталог фильмов, Lordfilm-парсер, что угодно) подключаются
через простые интерфейсы — см. раздел [Куда подключать данные](#куда-подключать-данные).

---

## Что в проекте

```
demuzyka/
├── settings.gradle.kts       — два модуля: :app (телефон) и :tv (Android TV)
├── build.gradle.kts          — версии AGP / Kotlin / Compose plugin
├── gradle.properties         — флаги JVM/Gradle (heap, caching, parallel)
├── gradle/wrapper/           — обёртка Gradle 8.9
├── gradlew, gradlew.bat      — запускалки обёртки
├── app/                      — телефонный модуль
│   ├── build.gradle.kts
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/demuzyka/app/
│       │   ├── DeMuzykaApp.kt              — Application + AppContainer
│       │   ├── ui/MainActivity.kt          — точка входа
│       │   ├── ui/DeMuzykaRoot.kt          — Scaffold + табы + NavHost
│       │   ├── ui/tabs/AppTab.kt           — переключатель «ДеМузыка / ДеПоиск»
│       │   ├── ui/nav/                     — нижние табы
│       │   ├── ui/theme/                   — тёмная тема, цвета, шрифты
│       │   ├── ui/music/                   — экраны вкладки «ДеМузыка»
│       │   ├── ui/poisk/                   — экраны вкладки «ДеПоиск»
│       │   ├── ui/player/MiniPlayerHost.kt — мини-плеер над нижним меню
│       │   └── data/                       — провайдеры контента (заглушки)
│       │       ├── AppContainer.kt         — простой DI: тут меняем заглушки на реальные
│       │       ├── music/MusicProvider.kt  — интерфейс музыкального каталога
│       │       ├── music/StubMusicProvider — демо-данные для превью / эмулятора
│       │       ├── music/WaveProvider.kt   — интерфейс рекомендательной «волны»
│       │       └── poisk/FilmProvider.kt   — интерфейс каталога фильмов
│       └── res/                            — иконки, темы, строки, мипмапы
└── tv/                       — Android TV модуль (Leanback)
    ├── build.gradle.kts
    └── src/main/
        ├── AndroidManifest.xml             — uses-feature leanback, LEANBACK_LAUNCHER
        ├── java/com/demuzyka/tv/
        │   ├── ui/MainTvActivity.kt
        │   ├── ui/DeMuzykaTvApp.kt
        │   ├── ui/home/MusicHomeTvScreen.kt
        │   ├── ui/home/PoiskHomeTvScreen.kt
        │   └── data/SampleData.kt          — пока локальные модели; см. ниже
        └── res/                            — TV-баннер, тема Leanback
```

---

## Требования

* **JDK 17+** (тестировал на OpenJDK 21).
* **Android SDK** с установленными платформами `android-34` или `android-35`.
* **Android Build-Tools** `34.0.0` или `35.0.0`.
* На сборочной машине (Linux/Mac/Windows) ничего больше не нужно — Gradle
  Wrapper сам скачает Gradle 8.9 при первом запуске.

Создайте файл `local.properties` в корне (он в `.gitignore`, в репо его нет):

```properties
sdk.dir=/абсолютный/путь/к/android-sdk
```

Например, на этом сервере: `sdk.dir=/home/denis/android-sdk`.

---

## Быстрый старт (Debug APK, без подписи)

```bash
git clone https://github.com/denkalonelys/demuzyka.git
cd demuzyka

# Создайте local.properties (см. выше).
echo "sdk.dir=$ANDROID_HOME" > local.properties

# Сборка только телефонного APK:
./gradlew :app:assembleDebug

# Сборка только TV-APK:
./gradlew :tv:assembleDebug

# Сборка всего, что есть:
./gradlew assembleDebug
```

Готовые APK:

```
app/build/outputs/apk/debug/app-debug.apk     # телефон
tv/build/outputs/apk/debug/tv-debug.apk       # Android TV
```

Установка на устройство по USB / Wi-Fi (включите отладку по USB):

```bash
# Телефон
adb install -r app/build/outputs/apk/debug/app-debug.apk

# Android TV (через сеть)
adb connect 192.168.1.50:5555
adb -s 192.168.1.50:5555 install -r tv/build/outputs/apk/debug/tv-debug.apk
adb disconnect 192.168.1.50:5555
```

---

## Release APK

> По умолчанию **release-сборка подписывается debug-ключом** — это сделано
> намеренно, чтобы `./gradlew assembleRelease` сразу выдавал ставимый APK
> без ручного создания keystore. **Такой APK НЕ годится для Google Play
> и публичной раздачи** — он подписан общим debug-сертификатом и не
> уникален для вашего проекта.
>
> Для распространения (Play Market, RuStore, прямые ссылки) собирайте
> с настоящим keystore — см. шаги ниже.

### 1) Один раз — сгенерируйте keystore

```bash
keytool -genkeypair -v \
    -keystore demuzyka-release.jks \
    -alias demuzyka \
    -keyalg RSA -keysize 4096 -validity 36500 \
    -storepass "ПРИДУМАЙ_ПАРОЛЬ_ХРАНИЛИЩА" \
    -keypass   "ПРИДУМАЙ_ПАРОЛЬ_КЛЮЧА" \
    -dname "CN=DeMuzyka, OU=Mobile, O=Personal, L=Yekaterinburg, ST=RU, C=RU"
```

Файл `demuzyka-release.jks` **никогда** не коммитьте в git.

### 2) Положите рядом `keystore.properties` (тоже в .gitignore):

```properties
storeFile=/абсолютный/путь/к/demuzyka-release.jks
storePassword=ПРИДУМАЙ_ПАРОЛЬ_ХРАНИЛИЩА
keyAlias=demuzyka
keyPassword=ПРИДУМАЙ_ПАРОЛЬ_КЛЮЧА
```

### 3) Раскомментируйте подпись в `app/build.gradle.kts` и `tv/build.gradle.kts`

В каждом `build.gradle.kts` модуля добавьте перед `android { … }`:

```kotlin
import java.util.Properties
val keystoreProps = Properties().apply {
    val f = rootProject.file("keystore.properties")
    if (f.exists()) f.inputStream().use { load(it) }
}
```

И внутри `android { … }`:

```kotlin
signingConfigs {
    create("release") {
        storeFile = file(keystoreProps["storeFile"] as String)
        storePassword = keystoreProps["storePassword"] as String
        keyAlias = keystoreProps["keyAlias"] as String
        keyPassword = keystoreProps["keyPassword"] as String
    }
}
buildTypes {
    release {
        signingConfig = signingConfigs.getByName("release")
        // … остальной release-блок
    }
}
```

(Я намеренно не положил это в шаблон сразу: без `keystore.properties`
Gradle бы падал с ошибкой при первом `./gradlew build`.)

### 4) Соберите

```bash
./gradlew :app:assembleRelease
./gradlew :tv:assembleRelease
```

```
app/build/outputs/apk/release/app-release.apk
tv/build/outputs/apk/release/tv-release.apk
```

---

## Установка на Android TV без USB-кабеля

Большинство ТВ-приставок не имеют физического USB-порта, доступного для adb.
Включите «**Отладку по сети**» / «**ADB по сети**» в настройках разработчика
и подключитесь по Wi-Fi:

```bash
# 1. Узнайте IP вашего ТВ
# Настройки → Сеть → Информация → IP-адрес  (например, 192.168.1.50)

# 2. На компьютере / сервере
adb connect 192.168.1.50:5555
adb -s 192.168.1.50:5555 install -r tv/build/outputs/apk/release/tv-release.apk

# Проверьте
adb -s 192.168.1.50:5555 shell pm list packages | grep demuzyka
```

После установки приложение появится в строке Android TV-лаунчера благодаря
`LEANBACK_LAUNCHER` категории в манифесте.

---

## Куда подключать данные

Главная точка интеграции — `app/src/main/java/com/demuzyka/app/data/AppContainer.kt`.
Это **простой DI-граф** (~30 строк) без внешних библиотек. Меняете эти три
свойства — и весь UI берёт данные из вашего источника:

```kotlin
class DefaultAppContainer(private val context: Context) : AppContainer {
    override val musicProvider: MusicProvider = StubMusicProvider()          // ← сюда
    override val waveProvider:  WaveProvider  = StubWaveProvider(musicProvider) // ← сюда
    override val filmProvider:  FilmProvider  = StubFilmProvider()           // ← сюда
}
```

### 1. Музыкальный каталог — `MusicProvider`

Интерфейс лежит в `app/src/main/java/com/demuzyka/app/data/music/MusicProvider.kt`.

Что нужно реализовать:

| метод                  | смысл                                                       |
| ---------------------- | ----------------------------------------------------------- |
| `homeRows()`           | Главный экран: подборки, моя волна, плейлисты, лайки.       |
| `concerts()`           | Вкладка «Концерты» — карточки с датой/местом/возрастом/ценой. |
| `books()`              | Вкладка «Книги и подкасты».                                  |
| `likes`                | Стрим «Мне нравится». Сохраняется локально.                  |
| `toggleLike(trackId)`  | Лайкнуть / снять лайк (UI вызывает оптимистично).           |
| `nowPlaying`           | Что сейчас играет — обновляется плеером.                    |

**Куда подключать настоящий CDN** — реализуйте интерфейс
(`YourCdnMusicProvider : MusicProvider`) и поставьте его в `DefaultAppContainer`.

`StubMusicProvider` оставьте — он используется в `@Preview` Compose'а и
отлично работает на эмуляторе без сети.

### 2. Рекомендательная «волна» — `WaveProvider`

Интерфейс: `app/src/main/java/com/demuzyka/app/data/music/WaveProvider.kt`.

Простой контракт:

```kotlin
interface WaveProvider {
    val queue: Flow<List<Track>>       // что заиграет дальше
    val mood:  Flow<String?>           // выбранное настроение (или null)
    suspend fun setMood(moodId: String?)
    suspend fun skip()
    suspend fun like(trackId: String)
    suspend fun dislike(trackId: String)
}
```

Стандартный путь — закатать в `WaveProvider` ваш ML-сервис, который по
истории/лайкам/жанрам возвращает следующий трек. UI подписывается на `queue`.

### 3. Каталог фильмов — `FilmProvider`

Интерфейс: `app/src/main/java/com/demuzyka/app/data/poisk/FilmProvider.kt`.

| метод                      | смысл                                                  |
| -------------------------- | ------------------------------------------------------ |
| `featured()`               | Большой баннер с тэглайном + кнопкой («Купить билеты»). |
| `homeRows()`               | Горизонтальные подборки.                                |
| `bookmarks()`              | «Буду смотреть» (закладки пользователя).               |
| `search(query)`            | Поиск (`debounce` делайте на стороне ViewModel/Flow).  |
| `toggleBookmark(filmId)`   | Закладка вкл/выкл.                                     |
| `resolveStream(filmId)`    | Получить стримируемый URL под Play. Тяжёлая операция.  |

**Готовый пример — TMDB:**
В репо лежит `TmdbFilmProvider` — рабочая реализация поверх
[The Movie Database](https://www.themoviedb.org/) API. Бесплатный ключ
получаете на https://www.themoviedb.org/settings/api , после чего в
`AppContainer.kt` меняете одну строку:

```kotlin
override val filmProvider: FilmProvider =
    TmdbFilmProvider(apiKey = "ВАШ_TMDB_КЛЮЧ")
```

После этого «ДеПоиск» сразу заполнится реальными постерами и рейтингами
для российского региона (`language = ru-RU`, `region = RU`) — «Сейчас в
кино», «Топ-250», «Сериалы», поиск.

**Альтернативы:** `KinopoiskUnofficialFilmProvider` (структурно почти
идентичный, ставите `https://kinopoiskapiunofficial.tech/api/v2.2/films`),
свой каталог через REST, всё что угодно — главное реализовать
`FilmProvider`.

**Lordfilm / пиратские источники в репо НЕ интегрированы намеренно** —
встраивание пиратского контента ведёт к удалению из Google Play,
требованиям правообладателей и риску 146 УК РФ. Если действительно
нужен такой источник — пишите парсер сами, имплементируете
`FilmProvider`, ставите в `AppContainer`. Ответственность за каталог
остаётся на вашей стороне.

### 4. Реальный плеер

В app/build.gradle.kts уже есть Media3 ExoPlayer (`androidx.media3:media3-exoplayer`).
Подключите его в новом сервисе `PlaybackService` и обновляйте
`musicProvider.nowPlaying` при смене трека — мини-плеер сразу подхватит.

### 5. Android TV

`tv/` пока имеет свой минимальный `data/SampleData.kt`, потому что
прямая зависимость TV-модуля от приложения телефона невозможна
(`com.android.application` нельзя резолвить из другого `application`).
Когда вы подключите реальные провайдеры, **извлеките data-слой в
отдельный модуль-библиотеку** `:data`:

```
demuzyka/
├── data/                    ← новый Android-library модуль
│   └── src/main/java/com/demuzyka/data/…
├── app/  (зависит от :data)
└── tv/   (зависит от :data)
```

И `app/build.gradle.kts` + `tv/build.gradle.kts` оба добавляют:

```kotlin
dependencies {
    implementation(project(":data"))
}
```

После этого TV и телефон будут пить одни и те же данные / провайдеры.

---

## Дизайн / визуальные эффекты

Кода намеренно мало, но он повторяет ключевые визуальные приёмы:

| элемент Я.Музыки / КП                                     | где в проекте                                       |
| --------------------------------------------------------- | --------------------------------------------------- |
| Тёмный фон, оранжевый primary, жёлтый secondary           | `ui/theme/Color.kt`, `Theme.kt`                     |
| Большой градиент «Моя волна»                              | `ui/music/MusicHomeScreen.kt` → `WaveHero`          |
| Чипы настроений («Любимое ×», «Бодрое», «Фокус»)          | `WaveHero` + `MoodCard`                             |
| Карточки треков с кнопкой загрузки                        | `ui/music/MusicCollectionScreen.kt`                 |
| Постер с рейтингом (зелёный 7+, серый <7)                 | `ui/poisk/parts/FilmTile.kt`                        |
| Бейдж «новых серий» (⚡10)                                 | `FilmTile.kt` → `NewEpisodesChip`                   |
| Большой featured-баннер с tagline + «Купить билеты»       | `ui/poisk/PoiskHomeScreen.kt` → `FeaturedHero`      |
| Скруглённый поиск с фильтр-иконкой                        | `ui/poisk/PoiskSearchScreen.kt` → `SearchBar`       |
| Карточка «Концерты» с датой / возрастом / ценой           | `ui/music/MusicConcertsScreen.kt` → `ConcertHero`   |
| Мини-плеер над bottom-bar                                 | `ui/player/MiniPlayerHost.kt`                       |
| Переключатель приложений «ДеМузыка / ДеПоиск»             | `ui/tabs/AppTab.kt`                                 |

---

## Частые проблемы

**1. `SDK location not found`**

Создайте `local.properties` с `sdk.dir=…` (см. выше).

**2. `JAVA_HOME is not set to a valid JDK`**

Поставьте JDK 17 или 21 (`apt install openjdk-21-jdk` / `brew install openjdk@21`).
В большинстве систем `JAVA_HOME` определяется автоматически.

**3. Gradle падает на ARM / на старой машине с OOM**

Уменьшите heap в `gradle.properties`: `org.gradle.jvmargs=-Xmx1500m`
(и отключите `org.gradle.parallel`).

**4. На Android TV приложение не появилось на главном экране**

Проверьте в манифесте:

```xml
<category android:name="android.intent.category.LEANBACK_LAUNCHER" />
```

и `<uses-feature android:name="android.software.leanback" android:required="true"/>`.

**5. Compose preview не рендерится**

Открывайте `*Screen.kt` в Android Studio, для preview включите Run Build As Group.
Preview использует `StubMusicProvider` / `StubFilmProvider` — они работают
без сети.

---

## Лицензия

Шаблон личный. Никаких чужих ресурсов / API ключей / постеров в коде нет
— только заглушки. Если будете публиковать в Play Market: подключите свой
каталог, прикрутите политику конфиденциальности, замените иконки на
не-шаблонные.
