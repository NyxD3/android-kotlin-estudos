package projetobiblioteca.dao

import projetobiblioteca.model.Emprestimo

class EmprestimoDAO {

    //================ LISTAR ==================

    fun listarEmprestimosAtivos(): List<Emprestimo> {

        val lista = mutableListOf<Emprestimo>()
        val con = Conexao.conectar()

        val rs = con.prepareStatement("""
            SELECT * FROM emprestimo
            WHERE data_devolucao IS NULL
        """).executeQuery()




        while (rs.next()) {

            val emp = Emprestimo(
                rs.getInt("id_emprestimo"),
                rs.getInt("id_livro"),
                rs.getInt("id_usuario"),
                rs.getDate("data_emprestimo").toLocalDate(),
                rs.getDate("data_devolucao").toLocalDate()
            )

            lista.add(emp)
        }

        con.close()
        return lista
    }

    //================ TOTAL ==================

    fun totalEmprestados(): Int {

        val con = Conexao.conectar()

        val rs = con.prepareStatement("""
            SELECT COUNT(*) AS total FROM emprestimo WHERE data_devolucao IS NULL
        """).executeQuery()

        rs.next()
        val total = rs.getInt("total")

        con.close()
        return total
    }

    //================ NOVO ==================

    fun novoEmprestimo() {

        val con = Conexao.conectar()

        print("ID livro: ")
        val livro = readLine()!!.toInt()

        print("ID usuário: ")
        val usuario = readLine()!!.toInt()

        val check = con.prepareStatement("""
            SELECT * FROM emprestimo
            WHERE id_livro=? AND data_devolucao IS NULL
        """)

        check.setInt(1,livro)

        if(check.executeQuery().next()){
            println("Livro já emprestado.")
            con.close()
            return
        }

        val stmt = con.prepareStatement("""
            INSERT INTO emprestimo(id_livro,id_usuario,data_emprestimo)
            VALUES(?,?,CURRENT_DATE)
        """)

        stmt.setInt(1,livro)
        stmt.setInt(2,usuario)

        stmt.executeUpdate()

        println("Empréstimo registrado!")

        con.close()
    }

    //================ DEVOLVER ==================

    fun devolverLivro() {

        val con = Conexao.conectar()

        print("ID empréstimo: ")
        val id = readLine()!!.toInt()

        val stmt = con.prepareStatement("""
            UPDATE emprestimo
            SET data_devolucao=CURRENT_DATE
            WHERE id_emprestimo=?
        """)

        stmt.setInt(1,id)

        if(stmt.executeUpdate()>0)
            println("Livro devolvido!")
        else
            println("Empréstimo não encontrado.")

        con.close()
    }

    //================ MENU ==================

    fun menuEmprestimo() {

        var op:Int

        do {

            println("""
====== EMPRÉSTIMOS ======
1 - Listar ativos
2 - Novo empréstimo
3 - Devolver livro
0 - Voltar
""")

            print("Escolha: ")
            op = readLine()!!.toInt()



            when(op){

                1 -> {
                    val lista = listarEmprestimosAtivos()

                    println("Total emprestados: ${totalEmprestados()}")

                    for(e in lista){
                        println("""
ID:${e.id}
Livro:${e.idLivro}
Usuário:${e.idUsuario}
Data:${e.dataEmprestimo}
------------------
""")
                    }
                }

                2 -> novoEmprestimo()
                3 -> devolverLivro()
                0 -> println("Voltando...")
                else -> println("Inválido")
            }

        }while(op!=0)
    }
}
