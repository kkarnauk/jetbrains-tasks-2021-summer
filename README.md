## Window Management

### Общая информация

Задание решалось на `Linux Mint 20.1 Ulyssa`. Думаю, что проблем на `Ubuntu` версии 16 и выше быть не должно.

### Сборка и запуск

В данном задании используются `Xlib` и `OpenGL`, поэтому необходимо поставить следующие библиотеки:
```bash
sudo apt install libx11-dev        # Xlib
sudo apt install libgl1-mesa-dev   # OpenGl
sudo apt install libglu1-mesa-dev  # GLU
```

Для компиляции необходим `CMake` версии хотя бы 3.11.

Чтобы скомпилировать проект, необходимо зайти в терминале в нужную директорию и выполнить следующие команды:
```bash
mkdir build
cd build
cmake ..
make
```

Для запуска достаточно:
```bash
./window_management
```
