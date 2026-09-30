param(
    [string]$Entrada = "casos/programa.mini"
)

$ErrorActionPreference = "Stop"
Push-Location $PSScriptRoot

try {
    New-Item -ItemType Directory -Force .\etapa1_java\out | Out-Null
    New-Item -ItemType Directory -Force .\salida | Out-Null

    # Quitar resultados anteriores antes de empezar
    Remove-Item .\salida\programa.ir, .\salida\resultado.txt, .\salida\firma.txt `
        -ErrorAction SilentlyContinue

    # Compilar y ejecutar Java
    $fuentes = (Get-ChildItem .\etapa1_java\src -Recurse -Filter *.java).FullName
    javac -d .\etapa1_java\out $fuentes
    if ($LASTEXITCODE -ne 0) { throw "Fallo la compilacion de Java." }

    java -cp .\etapa1_java\out minilang.Main $Entrada .\salida\programa.ir
    if ($LASTEXITCODE -ne 0) { throw "Java rechazo el programa. Pipeline detenido." }

    # Ejecutar Python
    python .\etapa2_python\ejecutor.py
    if ($LASTEXITCODE -ne 0) { throw "Fallo Python. Pipeline detenido." }

    # Ejecutar MIPS en MARS
    java -jar .\tools\Mars4_5.jar nc sm ae1 se2 .\etapa3_mips\firma.asm
    if ($LASTEXITCODE -ne 0 -or -not (Test-Path .\salida\firma.txt)) {
        throw "Fallo MIPS. Pipeline detenido."
    }

    Write-Host "Pipeline completo. Archivos generados en salida\"
}
finally {
    Pop-Location
}