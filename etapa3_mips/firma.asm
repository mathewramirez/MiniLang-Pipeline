
# Etapa 3 del MiniLang Pipeline: firma de verificación.
#
# Entrada: salida/resultado.txt, generado por Python.
#   Línea 1: número derivado del resultado final.
#   Línea 2: cantidad de operaciones ejecutadas.
#   Las líneas posteriores contienen la traza y no se usan para la firma.
#
# Cálculo: firma = (resultado XOR operaciones) + 17.
# Salida: salida/firma.txt, con la firma en formato decimal.
#
# Registros principales:
#   $s0: descriptor del archivo abierto.
#   $s1: firma calculada.
#   $s2: inicio del texto que se escribirá.
#   $s3: longitud de ese texto.
#   $t0-$t9: valores temporales para lectura y conversiones.

.data
archivo_entrada: .asciiz "salida/resultado.txt"
archivo_salida:  .asciiz "salida/firma.txt"

buffer_entrada: .space 128
buffer_salida:  .space 16
valores:        .word 0, 0

mensaje_firma:  .asciiz "Firma: "
salto_linea:    .asciiz "\n"
error_entrada:  .asciiz "ERROR: no se pudo leer resultado.txt\n"
error_formato:  .asciiz "ERROR: resultado.txt no inicia con dos numeros\n"
error_salida:   .asciiz "ERROR: no se pudo escribir firma.txt\n"

.text
.globl main

main:
    # 1. Abrir resultado.txt
    li   $v0, 13
    la   $a0, archivo_entrada
    li   $a1, 0                  # Lectura
    li   $a2, 0
    syscall
    bltz $v0, fallo_entrada
    move $s0, $v0                # Descriptor del archivo

    # 2. Leer el comienzo del archivo
    li   $v0, 14
    move $a0, $s0
    la   $a1, buffer_entrada
    li   $a2, 127
    syscall
    blez $v0, cerrar_y_fallar_entrada

    # Terminar el texto leído con un byte cero
    la   $t0, buffer_entrada
    addu $t0, $t0, $v0
    sb   $zero, 0($t0)

    # Cerrar resultado.txt
    li   $v0, 16
    move $a0, $s0
    syscall

    # 3. Convertir las dos primeras líneas a enteros
    la   $t0, buffer_entrada
    la   $t1, valores
    li   $t2, 2


# El parser lee exactamente dos enteros, uno por línea.
# Acepta un signo negativo opcional y finales de línea
# tanto de Windows (\r\n) como de otros sistemas (\n).
siguiente_numero:
    li   $t3, 0                  # Numero en construccion
    li   $t6, 1                  # Signo
    li   $t8, 0                  # Cantidad de dígitos

    lb   $t4, 0($t0)
    li   $t7, 45                 # ASCII de '-'
    bne  $t4, $t7, leer_digito
    li   $t6, -1
    addiu $t0, $t0, 1

leer_digito:
    lb   $t4, 0($t0)
    beqz $t4, fallo_formato

    li   $t7, 10                 # '\n'
    beq  $t4, $t7, guardar_numero
    li   $t7, 13                 # '\r'
    beq  $t4, $t7, guardar_numero

    # Comprobar que sea un dígito entre '0' y '9'
    li   $t7, 48
    slt  $t9, $t4, $t7
    bnez $t9, fallo_formato
    li   $t7, 57
    slt  $t9, $t7, $t4
    bnez $t9, fallo_formato

    # número = número * 10 + dígito
    addiu $t4, $t4, -48
    li   $t7, 10
    mul  $t3, $t3, $t7
    addu $t3, $t3, $t4

    addiu $t8, $t8, 1
    addiu $t0, $t0, 1
    j    leer_digito

guardar_numero:
    beqz $t8, fallo_formato

    mul  $t3, $t3, $t6
    sw   $t3, 0($t1)             # Guardar en memoria

    addiu $t1, $t1, 4
    addiu $t2, $t2, -1

# Saltar los caracteres de fin de línea antes de leer
# el segundo número. Después del segundo, pasar al cálculo.
saltar_fin_linea:
    lb   $t4, 0($t0)
    li   $t7, 10
    beq  $t4, $t7, avanzar
    li   $t7, 13
    beq  $t4, $t7, avanzar

    bgtz $t2, siguiente_numero
    j    calcular_firma

avanzar:
    addiu $t0, $t0, 1
    j    saltar_fin_linea

# 4. Recorrer ambos números y aplicar XOR
calcular_firma:
    la   $t0, valores           # Dirección del primer número
    li   $t1, 2                 # Cantidad de números por procesar
    move $s1, $zero             # Acumulador del XOR; empieza en 0

ciclo_firma:
    lw   $t2, 0($t0)            # Cargar el número actual
    xor  $s1, $s1, $t2          # Incorporarlo a la firma mediante XOR
    addiu $t0, $t0, 4           # Avanzar a la siguiente palabra (.word)
    addiu $t1, $t1, -1          # Queda un número menos por procesar
    bgtz $t1, ciclo_firma       # Repetir mientras queden numeros

    addiu $s1, $s1, 17          # Completar la fórmula sumando 17

    # Mostrar la firma en la terminal
    li   $v0, 4
    la   $a0, mensaje_firma
    syscall

    li   $v0, 1
    move $a0, $s1
    syscall

    li   $v0, 4
    la   $a0, salto_linea
    syscall

    # 5. Convertir la firma a texto para escribirla en firma.txt
    la   $t0, buffer_salida
    addiu $t0, $t0, 15           # ultima posicion del buffer
    li   $t1, 10                 # salto de línea
    sb   $t1, 0($t0)
    addiu $s3, $t0, 1            # Final del texto

    addiu $t0, $t0, -1
    move $t2, $s1

    slt  $t6, $t2, $zero         # verificar si la firma es negativa
    beqz $t6, convertir_digito
    subu $t2, $zero, $t2

# La conversión se hace desde el final del buffer hacia atrás:
# cada división entre 10 obtiene el siguiente dígito decimal.
# Al terminar, $s2 y $s3 señalan el texto que escribirá syscall 15.
convertir_digito:
    li   $t7, 10
    divu $t2, $t7
    mflo $t2                  # Cociente
    mfhi $t4                  # Dígito
    addiu $t4, $t4, 48        # Convertir a ASCII
    sb   $t4, 0($t0)

    addiu $t0, $t0, -1
    bnez $t2, convertir_digito

    beqz $t6, texto_listo
    li   $t4, 45
    sb   $t4, 0($t0)
    addiu $t0, $t0, -1

texto_listo:
    addiu $s2, $t0, 1        # Inicio del texto
    subu $s3, $s3, $s2       # Longitud del texto

    # 6. Abrir firma.txt para escritura
    li   $v0, 13
    la   $a0, archivo_salida
    li   $a1, 1             # Escritura; crea o reemplaza
    li   $a2, 0
    syscall
    bltz $v0, fallo_salida
    move $s0, $v0

    # Escribir la firma
    li   $v0, 15
    move $a0, $s0
    move $a1, $s2
    move $a2, $s3
    syscall
    bltz $v0, cerrar_y_fallar_salida
    bne  $v0, $s3, cerrar_y_fallar_salida

    # Cerrar firma.txt
    li   $v0, 16
    move $a0, $s0
    syscall

    li   $v0, 10
    syscall

cerrar_y_fallar_entrada:
    li   $v0, 16
    move $a0, $s0
    syscall

fallo_entrada:
    la   $a0, error_entrada
    j    mostrar_error

fallo_formato:
    la   $a0, error_formato
    j    mostrar_error

cerrar_y_fallar_salida:
    li   $v0, 16
    move $a0, $s0
    syscall

fallo_salida:
    la   $a0, error_salida

mostrar_error:
    li   $v0, 4
    syscall

    li   $v0, 17
    li   $a0, 1
    syscall