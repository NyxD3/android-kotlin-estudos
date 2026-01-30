package projetobiblioteca.dao

import java.sql.Connection
import java.sql.DriverManager

object Conexao {

    private const val URL = "jdbc:postgresql://localhost:5432/biblioteca"
    private const val USER = "postgres"
    private const val PASSWORD = "123"

    fun conectar(): Connection {
        return DriverManager.getConnection(URL, USER, PASSWORD)
    }
}
