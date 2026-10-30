# DeltaVisuals 0.7.0 — Fabric, Minecraft 1.21.11, Java 21

Клиентский визуальный мод. Только рендер и интерфейс: без боевых функций, автоматизации и преимуществ в игре.
**Частицы Minecraft не используются.** Каждый эффект — цельная геометрия (ленты, кольца, сферы, трубки, осколки),
которую рисуют собственные `RenderPipeline` / `RenderLayer`.


## Что нового в 0.7.0

* **Custom Sky** (Visuals) — сгенерированный купол неба вокруг камеры (`render/SkyRenderer.java`, `render/SkyPresets.java`): плавные
  градиенты по вершинам, анимированный шум для облаков (потоки «веером» от зенита и медленный вихрь), туманность, мерцающие звёзды с ореолом,
  свечение солнца/луны, ленты северного сияния, чёрная дыра с аккреционным диском. Без текстур и без частиц.
  Пресеты (чипы в меню): Default Sky (ванильное), Space, Stormy Sunset, Pink Sunset, Clear Day, Aurora, Black Hole, Sunset Sky, Night Sky,
  Fantasy Sky, Custom Sky, Gradient Sky, Custom Color Sky. Первые шесть сделаны по вашим референсам (Rapture, Stormy Sunset, Clarico, сияние, чёрная дыра).
  Настройки: Intensity, Brightness, Opacity, Gradient Power, Hue Shift, Glow, Animation Speed; цвета Top / Horizon / Bottom / Glow
  (кнопка *Load Preset Colors* копирует палитру пресета, *Use My Colors On Preset* применяет её); Cloud Amount / Contrast / Scale и цвета облаков;
  Stars, Star Density, Sun Direction / Height, Black Hole Size; Quality; Dome Distance (0 = авто). Всё сохраняется в профиле.
  Модуль **Sky Preset Switch** — отдельный хоткей, который включает небо и переключает пресет по кругу (кнопки Next / Previous есть и в меню).
  **Как это работает в 1.21.11:** купол рисуется событием мира до прозрачных слоёв, на радиусе дальше тумана, с проверкой глубины и без записи глубины:
  рельеф, сущности и вода всегда перед небом. Ванильное небо перекрывается непрозрачным куполом, а ванильные облака скрываются обычной
  видеонастройкой (возвращаются при выключении неба и при выходе из игры). Туман самой местности остаётся ванильным (без миксинов его цвет не подменить),
  поэтому у края дальности прорисовки возможна полоса цвета ванильного тумана.
* **Hit Sounds** (категория **Combat Visuals**) — звук при удачном ударе: Minecraft Bell (ванильный колокол), Hit Sound 1–4 (синтезируются кодом,
  файлов нет), Custom Hit Sound (`config/delta_visuals/sounds/custom.wav`, PCM WAV до 5 с; кнопки *Create Sounds Folder* и *Reload Custom Sound*).
  Звук играет только если после удара здоровье цели действительно упало (промах, блок щитом, кулдаун — тишина). Настройки: Volume, Pitch,
  Pitch Variation, Delay Between Sounds, Players Only, кнопка **Play Sound**; выбор звука сразу проигрывает его. Включение — переключатель модуля и хоткей.
* **Item Position** (Visuals) — положение предмета в руках от первого лица: Position X/Y/Z (±2), Rotation X/Y/Z (±180°), Item Scale (0.1–2.5),
  отдельно для правой и левой руки и для типов предметов: Sword, Axe, Pickaxe, Shovel, Hoe, Food, Shield, Other Items (All Items — запасной вариант).
  23 готовых пресета положения (Classic, Low/High/Side/Diagonal/Reverse Sword, Small/Big Sword, Compact, PvP Classic/Low/High/Side/Diagonal,
  Tilted/Flat Blade, Close Guard, Forward Pose, Overhead, Samurai, Showcase, Low Guard, Dagger Stab) + Custom. Кнопки Preview (закрывает меню,
  чтобы увидеть результат), Reset Position, Reset All. Меняет только отрисовку: дальность, урон и взмах не затрагиваются.
  Реализовано миксином `HeldItemRendererMixin` (конфиг `delta_visuals.mixins.json` помечен `required: false`: если сигнатура метода в 1.21.11 окажется другой,
  мод запустится, а функция просто не сработает).
* Меню: новая категория **Combat Visuals**, заголовки групп внутри модуля, списки выбора в виде «чипов» для настроек с большим числом вариантов.

Этап Б (Floating Objects) пока отложен.

## Что нового в 0.6.0

* **Hit Effects** (новый модуль, категория Visuals) — при ударе по цели из неё разлетаются цельные низкополигональные модели:
  Stars (объёмные 5-конечные звёзды), Skulls (черепа со светящимися глазами, смотрят на игрока), Hearts (пухлые 3D-сердца),
  Crystal Shards (осколки, летят остриём вперёд), Lightning (зигзаги-молнии, каждые 2 тика меняют форму), Shockwave (кольца ударной волны).
  Они вращаются, уменьшаются и гаснут за 0,3–2 с (по умолчанию 0,8 с). При убийстве эффект крупнее: больше фигур, вспышка и дополнительные кольца.
  Настройки: Effect, Count, Size, Speed, Duration, Color Mode (Auto / Static / Gradient / Rainbow / Fire / Neon), цвета, Glow, Through Walls, Players Only, Bigger On Kill.
  Использует тот же `AttackEntityCallback`, что и Target ESP; убийство определяется по здоровью цели, поэтому работает и без Target ESP.
* **Trails / Blink тоньше и короче**: ширина по умолчанию 0.3 (Blink 0.4) вместо 0.6 (0.8), настройка **Trail Width** 0.02–0.5;
  длина по умолчанию 12 тиков (Blink 8) вместо 25 (12), настройка **Trail Length** 3–100. Сужение к хвосту и гашение альфы сохранены.
  Настройки переименованы (`Trail Width`, `Trail Length`), поэтому старые сохранённые значения не перекрывают новые умолчания.
* Общий слой для 3D-моделей: `render/Xf.java` (трансформация, освещение граней, отсечение обратных граней) и `render/Models.java`
  (звезда, сердце, кристалл, череп, мягкое свечение, блик-«искра»). Его же используют следующие этапы (летающие объекты).

Дальше по плану: этап Б — Floating Objects (парящие звёзды, лепестки сакуры, кристаллы, бабочки, сердечки, снежинки),
этап В — Custom Sky (шесть небесных куполов с анимированным шумом).

## Статус

| Этап | Состояние |
|---|---|
| 1. Ядро, меню, TargetESP, Trails/Blink, KillEffect, BlockOverlay, share-ключ | написан, **ещё не собирался** |
| 2. Impact, Hit Bubble, Footsteps, JFK, GlowFeet, Hats/Halo, BackSword, Damage badges | не начат |
| 3. HUD, прицел, NameTags, Tab, чат, Zoom и т. д. | не начат |
| 4. Скин-слои, анимации, косметика, питомцы, бабочки, мир, дождь, кристалл | не начат |

Этап 1 написан без доступа к сети и к исходникам Minecraft, поэтому ни разу не компилировался против 1.21.11.
Старые вызовы Minecraft (создание `RenderLayer`, событие мира, позиция камеры) собраны в трёх файлах:
`render/Pipelines.java`, `render/WorldHook.java`, `util/Compat.java`. Если сборка упадёт, ошибка почти наверняка там.
Пришлите вывод ошибок из вкладки Actions, и я исправлю их, прежде чем идти на этап 2.

Из версии 0.4.0 удалены: Pearl/Totem/Elytra Swap, Item Status, Reach Display, Aura, Radius Circle, старые
частичные Target Effects и Trails.

## Что есть в этапе 1

* **Target ESP** — 8 режимов: Arcs, Petals, Rings, Mechanical, Lightning, Saturn, Ghosts, Helix. Цель — тот, кого вы бьёте или на кого смотрите;
  пропадает через 3 с без контакта, плавное появление/исчезновение (альфа + масштаб). Цвет: Static / Gradient / Rainbow / Health / Fire / Neon, свечение, «сквозь стены».
* **Trails** и **Blink** — лента из последних N позиций (по умолчанию 25 тиков), сглаживание Catmull-Rom, сужение и затухание к хвосту,
  обрыв при телепорте. Режимы: Vertical, Horizontal, Spiral, Wave. Опция «Other Players». Blink — плотнее и ярче, с эффектом размытия.
* **Kill Effect** — Decay (осколки), Singularity (сфера + ударная волна), Abyss (щупальца-трубки). 1–2 с, плавное затухание.
* **Block Overlay** — заливка и контур по `VoxelShape`, пульсация, градиент/радуга.
* **Меню** — Right Shift (меняется кнопкой Keybind в Click GUI). У каждой функции свой хоткей и переключатель.
* **Конфиг** — JSON-профили в `.minecraft/config/delta_visuals/profiles/`. В категории Configurations: *Copy Share Key*, *Load Key From Clipboard*
  (сжатый JSON → gzip → Base64, всё локально) и *Reload Config From Disk* (без перезапуска игры).

Ленты строятся из quad'ов (две соседних вершины на точку), визуально это тот же triangle strip.

## Как залить на GitHub и собрать

1. Создайте на github.com новый пустой репозиторий (без README/.gitignore), например `delta-visuals`.
2. В папке проекта:
   ```bash
   git init
   git add .
   git update-index --chmod=+x gradlew
   git commit -m "DeltaVisuals 0.6.0"
   git branch -M main
   git remote add origin https://github.com/<ваш-логин>/delta-visuals.git
   git push -u origin main
   ```
   (Если вы взяли архив с уже готовым `.git`, достаточно `git remote add ...` и `git push`.)
3. Сборка стартует сама при каждом push и pull request. Вручную: вкладка **Actions → build → Run workflow**.
4. Если в репозитории нет `gradle/wrapper/gradle-wrapper.jar`, workflow создаёт его командой `gradle wrapper`.
   Чтобы wrapper лежал в репозитории, один раз выполните локально `gradle wrapper --gradle-version 9.4.1`
   и закоммитьте `gradle/wrapper/gradle-wrapper.jar`.

## Где взять готовый .jar

* **Actions:** откройте последний зелёный запуск **build** → раздел **Artifacts** → `delta-visuals-jar`.
  В артефакте только основной jar (без `-sources` и `-dev`).
* **Releases:** вкладка **Releases** справа на странице репозитория (создаётся тегом, см. ниже).

## Как выпустить новую версию

```bash
git tag v1.0.0
git push origin v1.0.0
```
Workflow **release** соберёт проект с версией `1.0.0`, создаст GitHub Release и приложит jar.

## Установка мода

1. Fabric Loader ≥ 0.18 для Minecraft 1.21.11 и Fabric API.
2. Положите `delta-visuals-<версия>.jar` в `.minecraft/mods/`.
3. В игре нажмите **Right Shift** — откроется меню.

## Если Gradle не находит версию

Актуальные `loader_version`, `loom_version`, `yarn_mappings`, `fabric_api_version` берутся с https://fabricmc.net/develop/ и правятся в `gradle.properties`.
