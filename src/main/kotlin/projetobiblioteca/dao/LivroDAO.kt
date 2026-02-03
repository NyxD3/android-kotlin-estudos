package projetobiblioteca.dao

import projetobiblioteca.model.Livros

class LivroDAO {

    // ========================================================
    fun listarLivros(): List<Livros> {

        val lista = mutableListOf<Livros>()
        val con = Conexao.conectar()

        val rs = con.prepareStatement("""
        SELECT l.*, e.id_emprestimo
        FROM livros l
        LEFT JOIN emprestimo e
        ON l.id_livro = e.id_livro
        AND e.data_devolucao IS NULL
    """).executeQuery()

        while (rs.next()) {

            val emprestado = rs.getObject("id_emprestimo") != null

            val livro = Livros(
                rs.getInt("id_livro"),
                rs.getString("titulo"),
                rs.getString("autor"),
                rs.getString("ano_publicacao").toInt(),
                rs.getString("isbn"),
                rs.getString("estilo"),
                emprestado
            )

            lista.add(livro)
        }

        con.close()
        return lista
    }


    // ========================================================
    fun cadastrarLivro() {

        print("Título: ")
        val titulo = readLine()!!

        print("Autor: ")
        val autor = readLine()!!

        print("Ano: ")
        val ano = readLine()!!.toInt()

        print("ISBN: ")
        val isbn = readLine()!!

        print("Gênero: ")
        val genero = readLine()!!

        val con = Conexao.conectar()

        val stmt = con.prepareStatement("""
            INSERT INTO livros(titulo,autor,ano_publicacao,isbn,estilo)
            VALUES (?,?,?,?,?)
        """)

        stmt.setString(1,titulo)
        stmt.setString(2,autor)
        stmt.setInt(3,ano)
        stmt.setString(4,isbn)
        stmt.setString(5,genero)

        stmt.executeUpdate()

        println("Livro cadastrado!")

        con.close()
    }

    //========================================================
    fun buscarPorGenero() {

        print("Digite gênero: ")
        val genero = readLine()!!

        val con = Conexao.conectar()

        val rs = con.prepareStatement("""
            SELECT * FROM livros WHERE LOWER(estilo)=LOWER(?)
        """).apply {
            setString(1,genero)
        }.executeQuery()

        while(rs.next()) {
            println("${rs.getString("titulo")} - ${rs.getString("autor")}")
        }

        con.close()
    }

    //========================================================
    fun totalLivros(): Int {

        val con = Conexao.conectar()
        val rs = con.prepareStatement("SELECT COUNT(*) FROM livros").executeQuery()

        rs.next()
        val total = rs.getInt(1)

        con.close()
        return total
    }

    // ========================================================
    fun totalPorGenero() {

        val con = Conexao.conectar()

        val rs = con.prepareStatement("""
            SELECT estilo, COUNT(*) FROM livros GROUP BY estilo
        """).executeQuery()

        while(rs.next()) {
            println("${rs.getString(1)} : ${rs.getInt(2)}")
        }

        con.close()
    }
    //========================================================

    fun apagarLivro(){

        val livros = listarLivros()

        for(l in livros){
            println("ID:${l.idLivro} - ${l.titulo}")
        }

        val con = Conexao.conectar()

        print("ID do livro: ")
        val id = readLine()?.toIntOrNull()

        if(id == null){
            println("ID inválido")
            con.close()
            return
        }

        // verifica empréstimo ativo
        val check = con.prepareStatement("""
        SELECT 1 FROM emprestimo
        WHERE id_livro=? AND data_devolucao IS NULL
    """)

        check.setInt(1,id)

        if(check.executeQuery().next()){
            println("Livro está emprestado.")
            con.close()
            return
        }

        val stmt = con.prepareStatement("DELETE FROM livros WHERE id_livro=?")
        stmt.setInt(1,id)

        if(stmt.executeUpdate()>0)
            println("Livro removido!")
        else
            println("Livro não encontrado.")

        con.close()
    }


    //========================================================
    fun menuLivro() {

        var op:Int
        do {

            println("""
==== LIVROS ====
1 - Listar
2 - Cadastrar
3 - Buscar gênero
4 - Apagar Livro
0 - Voltar
""")

            op = readLine()!!.toInt()

            when(op){
                1 -> {
                    val livros = listarLivros()

                    for(l in livros){

                        val status = if(l.emprestado) "Emprestado" else "Disponível"

                        println("""
ID:${l.idLivro}
Título:${l.titulo}
Autor:${l.autor}
Ano:${l.anoPublicacao}
ISBN:${l.isbn}
Gênero:${l.genero}
Status: $status
--------------
""")
                    }

                    println("Total livros: ${totalLivros()}")
                    totalPorGenero()
                }

                2 -> cadastrarLivro()
                3 -> buscarPorGenero()
                4 -> apagarLivro()
            }

        }while(op!=0)
    }
}
