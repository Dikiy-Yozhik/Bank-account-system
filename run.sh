#!/bin/bash

echo "========================================"
echo "    Bank Account System Launcher"
echo "========================================"

# Создаем папку для скомпилированных классов если её нет
if [ ! -d "bin" ]; then
    echo "Creating bin directory..."
    mkdir bin
fi

# Компилируем все Java файлы
echo "Compiling Java files..."
javac -d bin -cp src src/Main.java

# Проверяем успешность компиляции
if [ $? -ne 0 ]; then
    echo ""
    echo "COMPILATION FAILED!"
    echo "Please check the errors above."
    exit 1
fi

echo ""
echo "Compilation successful!"

# Запускаем программу
echo "Starting Bank Account System..."
echo ""
java -cp bin Main

# Пауза чтобы увидеть результат если программа завершилась
echo ""
read -p "Press Enter to continue..."