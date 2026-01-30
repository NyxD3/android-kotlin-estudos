package projetobiblioteca.dao

import projetobiblioteca.dao.Conexao
import projetobiblioteca.model.Usuario


class UsuarioDAO {


    fun listarUsuario(): List<Usuario> {

        val lista = mutableListOf<Usuario>()
        val con = Conexao.conectar()

        val rs = con.prepareStatement("SELECT * FROM usuarios").executeQuery()

        while (rs.next()) {

            val usuario = Usuario(
                rs.getInt("id_usuario"),
                rs.getString("nome"),
                rs.getString("telefone"),
                rs.getString("endereco")
            )

            lista.add(usuario)
        }

        con.close()
        return lista
    }
//-------------------------------------------------------------


    fun cadastrarUsuario() {

        println("Nome:")
        val nome = readLine()!!

        println("Telefone:")
        val telefone = readLine()!!

        println("Endereço:")
        val endereco = readLine()!!

        val conn = Conexao.conectar()

        val sql = """
        INSERT INTO usuarios (nome, telefone, endereco)
        VALUES (?, ?, ?)
    """

        val stmt = conn.prepareStatement(sql)


        stmt.setString(1, nome)
        stmt.setString(2, telefone)
        stmt.setString(3, endereco)

        stmt.executeUpdate()

        println("Usuario cadastrado!")

        conn.close()

    }
//-------------------------------------------------

    fun apagarUsuario() {

        listarUsuario()

        val con = Conexao.conectar()

        print("ID: ")
        val id = readLine()!!.toInt()

        val check = con.prepareStatement("SELECT * FROM usuarios WHERE id_usuario=?")
        check.setInt(1, id)

        if (!check.executeQuery().next()) {
            println("Usuário não encontrado.")
            con.close()
            return
        }

        val stmt = con.prepareStatement("DELETE FROM usuarios WHERE id_usuario=?")
        stmt.setInt(1, id)

        stmt.executeUpdate()

        println("Removido!")

    }
    //---------------------------------------------------------

    fun editarUsuario() {

        listarUsuario()

        val con = Conexao.conectar()

        print("ID: ")
        val id = readLine()!!.toInt()

        print("Novo nome: ")
        val nome = readLine()!!

        print("Novo telefone: ")
        val tel = readLine()!!

        print("Novo endereço: ")
        val end = readLine()!!

        val stmt = con.prepareStatement(
            """
        UPDATE usuarios
        SET nome=?, telefone=?, endereco=?
        WHERE id_usuario=?
    """
        )

        stmt.setString(1, nome)
        stmt.setString(2, tel)
        stmt.setString(3, end)
        stmt.setInt(4, id)

        stmt.executeUpdate()

        println("Usuário atualizado!")

        con.close()
    }

//------------------------------------------------------------

    fun menuUsuario() {

        var op: Int

        do {
            println("""
====== MENU USUÁRIO ======
1 - Listar usuário
2 - Cadastrar usuário
3 - Apagar usuário
4 - Editar usuário
0 - Voltar
""")

            print("Escolha: ")
            op = readLine()!!.toInt()

            when (op) {

                1 -> {
                    val usuarios = listarUsuario()

                    for (usuario in usuarios) {
                        println("""
ID: ${usuario.idUsuario}
Nome: ${usuario.nome}
Telefone: ${usuario.telefone}
Endereço: ${usuario.endereco}
------------------
""")
                    }
                }

                2 -> cadastrarUsuario()

                3 -> apagarUsuario()

                4 -> editarUsuario()

                0 -> println("Voltando...")

                else -> println("Opção inválida")
            }

        } while (op != 0)
    }

}