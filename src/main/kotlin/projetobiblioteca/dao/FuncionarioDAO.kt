package projetobiblioteca.dao

import projetobiblioteca.model.Funcionario

class FuncionarioDAO {

    //================ LISTAR ==================

    fun listarFuncionarios(): List<Funcionario> {

        val lista = mutableListOf<Funcionario>()
        val con = Conexao.conectar()

        val rs = con.prepareStatement("SELECT * FROM funcionarios").executeQuery()

        while (rs.next()) {

            val funcionario = Funcionario(
                rs.getInt("id_funcionario"),
                rs.getString("nome"),
                rs.getString("email"),
                rs.getString("senha_hash")
            )

            lista.add(funcionario)
        }

        con.close()
        return lista
    }

    //================ EDITAR ==================

    fun editarFuncionario() {

        val funcionarios = listarFuncionarios()

        for (f in funcionarios) {
            println("""
ID:${f.idFuncionario}
Nome:${f.nome}
Email:${f.email}
------------------
""")
        }

        val con = Conexao.conectar()

        print("ID: ")
        val id = readLine()!!.toInt()

        print("Novo nome: ")
        val nome = readLine()!!

        print("Novo email: ")
        val email = readLine()!!

        print("Nova senha: ")
        val senha = readLine()!!

        val stmt = con.prepareStatement("""
            UPDATE funcionarios
            SET nome=?, email=?, senha_hash=?
            WHERE id_funcionario=?
        """)

        stmt.setString(1,nome)
        stmt.setString(2,email)
        stmt.setString(3,senha)
        stmt.setInt(4,id)

        if(stmt.executeUpdate()>0)
            println("Funcionário atualizado!")
        else
            println("Funcionário não encontrado.")

        con.close()
    }

    //================ APAGAR ==================

    fun apagarFuncionario() {

        val funcionarios = listarFuncionarios()

        for (f in funcionarios) {
            println("""
ID:${f.idFuncionario}
Nome:${f.nome}
------------------
""")
        }

        val con = Conexao.conectar()

        print("ID: ")
        val id = readLine()!!.toInt()

        val stmt = con.prepareStatement("DELETE FROM funcionarios WHERE id_funcionario=?")
        stmt.setInt(1,id)

        if(stmt.executeUpdate()>0)
            println("Removido!")
        else
            println("Funcionário não encontrado.")

        con.close()
    }

    //================ MENU ==================

    fun menuFuncionario() {

        var op:Int

        do {

            println("""
====== FUNCIONÁRIOS ======
1 - Listar
2 - Editar
3 - Apagar
0 - Voltar
""")

            print("Escolha: ")
            op = readLine()!!.toInt()

            when(op){
                1 -> {
                    val lista = listarFuncionarios()
                    for (f in lista) {
                        println("""
ID:${f.idFuncionario}
Nome:${f.nome}
Email:${f.email}
------------------
""")
                    }
                }
                2 -> editarFuncionario()
                3 -> apagarFuncionario()
                0 -> println("Voltando...")
                else -> println("Inválido")
            }

        } while(op!=0)
    }
}
