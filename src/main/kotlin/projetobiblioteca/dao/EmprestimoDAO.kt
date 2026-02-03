package projetobiblioteca.dao

import projetobiblioteca.model.Emprestimo
import java.time.LocalDate

class EmprestimoDAO {

    // ========================================================

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
                rs.getDate("data_devolucao")?.toLocalDate()
            )

            lista.add(emp)
        }

        con.close()
        return lista
    }

    // ========================================================

    fun totalEmprestados(): Int {

        val con = Conexao.conectar()

        val rs = con.prepareStatement("""
            SELECT COUNT(*) FROM emprestimo WHERE data_devolucao IS NULL
        """).executeQuery()

        rs.next()
        val total = rs.getInt(1)

        con.close()
        return total
    }

    // ========================================================

    fun novoEmprestimo() {

        val con = Conexao.conectar()

        println("\n=== LIVROS DISPONÍVEIS ===")


        val livros = LivroDAO().listarLivros()

        for(l in livros){
            if(!l.emprestado){
                println("ID:${l.idLivro} - ${l.titulo}")
            }
        }

        println("0 - Cancelar")

        print("\nID do livro: ")
        val livro = readLine()?.toIntOrNull()

        if(livro == null || livro == 0){
            println("Operação cancelada.")
            con.close()
            return
        }

        println("\n=== USUÁRIOS ===")

        val usuarios = UsuarioDAO().listarUsuario()

        for(u in usuarios){
            println("ID:${u.idUsuario} - ${u.nome}")
        }

        println("0 - Cancelar")

        print("\nID do usuário: ")
        val usuario = readLine()?.toIntOrNull()

        if(usuario == null || usuario == 0){
            println("Operação cancelada.")
            con.close()
            return
        }

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

        println("Empréstimo registrado com sucesso!")

        con.close()
    }

    // ========================================================

    fun devolverLivro() {

        val con = Conexao.conectar()

        println("\n=== EMPRÉSTIMOS ATIVOS ===")

        val lista = listarEmprestimosAtivos()

        for(e in lista){
            println("ID:${e.id} Livro:${e.idLivro} Usuário:${e.idUsuario}")
        }

        println("0 - Cancelar")

        print("\nID empréstimo: ")
        val id = readLine()?.toIntOrNull()

        if(id == null || id == 0){
            println("Cancelado.")
            con.close()
            return
        }

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

    // ========================================================

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
            op = readLine()?.toIntOrNull() ?: -1

            when(op){

                1 -> {
                    val lista = listarEmprestimosAtivos()

                    println("\nTotal emprestados: ${totalEmprestados()}")

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
                else -> println("Opção inválida")
            }

        }while(op!=0)
    }
}
