@echo off
echo ================================
echo  Bank Account System Launcher
echo ================================

REM Создаем папку для скомпилированных классов если её нет
if not exist bin (
    echo Creating bin directory...
    mkdir bin
)

REM Компилируем все Java файлы включая utils
echo Compiling Java files...
javac -d bin -cp src src\*.java src\model\*.java src\service\*.java src\controller\*.java src\view\*.java src\exception\*.java src\utils\*.java

REM Проверяем успешность компиляции
if %errorlevel% neq 0 (
    echo.
    echo COMPILATION FAILED!
    echo Please check the errors above.
    pause
    exit /b 1
)

echo.
echo Compilation successful!

REM Запускаем программу
echo Starting Bank Account System...
echo.
java -cp bin Main

REM Пауза чтобы увидеть результат если программа завершилась
echo.
pause