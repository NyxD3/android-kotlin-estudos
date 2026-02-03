package org.example.projetobiblioteca

import projetobiblioteca.dao.Conexao
import projetobiblioteca.service.Login
import projetobiblioteca.ui.Menu



fun main() {

    val biblioteca = Menu()


    val login = Login()

    println("INICIANDO")

    try {
        val conn = Conexao.conectar()
        println("CONECTOU")
        conn.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }


    print("Email: ")
    val email = readLine()?.trim().orEmpty()


    print("Senha: ")
    val senha = readLine()?.trim().orEmpty()



    if (login.autenticar(email, senha)) {
        biblioteca.menuSistema()
    } else {
        println("Acesso negado")
    }



}



