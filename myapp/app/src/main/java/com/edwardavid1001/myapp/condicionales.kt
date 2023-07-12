package com.edwardavid1001.myapp

class condicionales {
}

fun main() {

    println("Ingrese un numero")
    var number: Int= readLine()!!.toInt()

    if(number in 1..20){
        println("El número esta en el rango de 1 a 20")
    }else{
        println("El número esta fuera de rango 1 a 20")
    }

    var result:Int=(1..50).random()
    println("el numero es $result")

    when(result){
        0 -> println("no hay resultados")
        1,2,3,4,5-> println("hay menos de 5 resultados")
        in 6..50-> println("hay entre 5 y 50 resultados")
        else -> println("esos son bastantes resultados")
    }
}

