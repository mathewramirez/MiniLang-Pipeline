from pathlib import Path
from functools import reduce

def cumple_condicion(numero, comparador, limite):
    if comparador == ">":
        return numero > limite
    elif comparador == "<":
        return numero < limite
    elif comparador == ">=":
        return numero >= limite
    elif comparador == "<=":
        return numero <= limite
    elif comparador == "==":
        return numero == limite

    raise ValueError(f"Comparador desconocido: {comparador}")

def aplicar_operacion(numero, operador, cantidad):
    if operador == "+":
        return numero + cantidad
    elif operador == "-":
        return numero - cantidad
    elif operador == "*":
        return numero * cantidad

    raise ValueError(f"Operador desconocido: {operador}")


ruta_ir = Path("salida/programa.ir")
lineas = ruta_ir.read_text(encoding="utf-8").splitlines()
datos = []
resultado = None
traza = []
operaciones = 0

for linea in lineas:
    partes = linea.split("|")

    if partes[0] == "DATA":
        datos = [int(numero) for numero in partes[1].split(",")]
        print("Datos iniciales:", datos)

    elif partes[0] == "FILTER":
        comparador = partes[1]
        limite = int(partes[2])

        datos = list(filter(
            lambda numero: cumple_condicion(numero, comparador, limite),
            datos
        ))

        print("Despues de FILTER:", datos)
        traza.append(f"FILTER {comparador} {limite} => {datos}")
        operaciones += 1

    elif partes[0] == "MAP":
        operador = partes[1]
        cantidad = int(partes[2])

        datos = list(map(
            lambda numero: aplicar_operacion(numero, operador, cantidad),
            datos
        ))

        print("Despues de MAP:", datos)
        traza.append(f"MAP {operador} {cantidad} => {datos}")
        operaciones += 1
    
    elif partes[0] == "REDUCE":
        tipo = partes[1]

        if tipo == "SUM":
            resultado = reduce(lambda acumulado, numero: acumulado + numero, datos, 0)

        elif tipo == "MAX":
            if not datos:
                raise ValueError("No se puede calcular MAX de una lista vacia")
            resultado = reduce(max, datos)

        elif tipo == "MIN":
            if not datos:
                raise ValueError("No se puede calcular MIN de una lista vacia")
            resultado = reduce(min, datos)

        print("Resultado de REDUCE:", resultado)
        traza.append(f"REDUCE {tipo} => {resultado}")
        operaciones += 1
        
    elif partes[0] == "PRINT":
        print("Resultado final:", resultado)
        print("Operaciones ejecutadas:", operaciones)
        print("Traza:")
        for paso in traza:
            print(paso)
            
        ruta_resultado = Path("salida/resultado.txt")
        contenido = [
            str(resultado),
            str(operaciones),
            *traza,
            f"RESULT={resultado}",
            f"OPS={operaciones}"
        ]

        ruta_resultado.write_text(
            "\n".join(contenido) + "\n",
            encoding="utf-8"
        )