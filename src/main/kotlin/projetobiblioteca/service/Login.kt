package projetobiblioteca.service

import projetobiblioteca.dao.Conexao
import projetobiblioteca.ui.Menu

class Login {

    val biblioteca = Menu()

    fun autenticar(email: String, senha: String): Boolean {

        val conn = Conexao.conectar()

        val sql = """
            SELECT nome 
            FROM funcionarios
            WHERE email = ? AND senha_hash = ?
        """

        val stmt = conn.prepareStatement(sql)
        stmt.setString(1, email)
        stmt.setString(2, senha)

        val rs = stmt.executeQuery()

        if (rs.next()) {
            val nome = rs.getString("nome")
            println("Bem-vindo/a $nome")
            conn.close()
            return true
        }

        conn.close()
        println("Email ou senha inválidos")
        return false
    }


}