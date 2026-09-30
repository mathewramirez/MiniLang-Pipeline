from pathlib import Path
from functools import reduce

def cumple_condicion(numero, comparador, limite):
    """Evalúa la condición de una instrucción FILTER."""
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
    """Aplica a un número la operación indicada por MAP."""
    if operador == "+":
        return numero + cantidad
    elif operador == "-":
        return numero - cantidad
    elif operador == "*":
        return numero * cantidad

    raise ValueError(f"Operador desconocido: {operador}")


# Java genera este archivo IR después de validar el programa MiniLang.
ruta_ir = Path("salida/programa.ir")
lineas = ruta_ir.read_text(encoding="utf-8").splitlines()

# 'datos' guarda la lista actual. 'resultado' guarda un valor escalar
# cuando la última transformación ejecutada fue REDUCE.
datos = []
resultado = None
traza = []
operaciones = 0

# Este ciclo interpreta las instrucciones del IR en el orden recibido.
# Las transformaciones FILTER y MAP se realizan con funciones de estilo funcional.
for linea in lineas:
    partes = linea.split("|")

    if partes[0] == "DATA":
        datos = [int(numero) for numero in partes[1].split(",")]
        print("Datos iniciales:", datos)

    elif partes[0] == "FILTER":
        comparador = partes[1]
        limite = int(partes[2])

        # filter() conserva los elementos que cumplen la condición.
        datos = list(filter(
            lambda numero: cumple_condicion(numero, comparador, limite),
            datos
        ))

        # La salida más reciente vuelve a ser una lista.
        resultado = None
        print("Despues de FILTER:", datos)
        traza.append(f"FILTER {comparador} {limite} => {datos}")
        operaciones += 1

    elif partes[0] == "MAP":
        operador = partes[1]
        cantidad = int(partes[2])

        # map() transforma cada elemento de la lista.
        datos = list(map(
            lambda numero: aplicar_operacion(numero, operador, cantidad),
            datos
        ))

        resultado = None
        print("Despues de MAP:", datos)
        traza.append(f"MAP {operador} {cantidad} => {datos}")
        operaciones += 1

    elif partes[0] == "REDUCE":
        tipo = partes[1]

        # REDUCE convierte la lista en un único número.
        if tipo == "SUM":
            # El valor inicial 0 permite sumar incluso una lista vacía.
            resultado = reduce(
                lambda acumulado, numero: acumulado + numero,
                datos,
                0
            )

        elif tipo == "MAX":
            if not datos:
                raise ValueError("No se puede calcular MAX de una lista vacia")
            resultado = reduce(max, datos)

        elif tipo == "MIN":
            if not datos:
                raise ValueError("No se puede calcular MIN de una lista vacia")
            resultado = reduce(min, datos)

        # Si viene otra operación después de REDUCE, podrá procesar el número obtenido como una lista de un elemento.
        datos = [resultado]
        print("Resultado de REDUCE:", resultado)
        traza.append(f"REDUCE {tipo} => {resultado}")
        operaciones += 1

    elif partes[0] == "PRINT":
        # Si la última operación fue REDUCE, el resultado final es un número.
        # En otro caso, se conserva la lista resultante de FILTER o MAP.
        resultado_final = resultado if resultado is not None else datos

        # MIPS necesita un número: se usa el resultado de REDUCE o,
        # si el resultado final es una lista, la suma de sus elementos.
        numero_firma = resultado if resultado is not None else sum(datos)

        print("Resultado final:", resultado_final)
        print("Operaciones ejecutadas:", operaciones)
        print("Traza:")
        for paso in traza:
            print(paso)

        ruta_resultado = Path("salida/resultado.txt")
        contenido = [
            str(numero_firma),
            str(operaciones),
            *traza,
            f"RESULT={resultado_final}",
            f"OPS={operaciones}"
        ]

        ruta_resultado.write_text(
            "\n".join(contenido) + "\n",
            encoding="utf-8"
        )