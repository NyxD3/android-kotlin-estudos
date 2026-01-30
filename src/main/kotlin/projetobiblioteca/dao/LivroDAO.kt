package projetobiblioteca.dao

import projetobiblioteca.model.Livros

class LivroDAO {

    // ================= LISTAR =================
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
                rs.getInt("ano_publicacao"),
                rs.getString("isbn"),
                rs.getString("estilo"),
                emprestado
            )

            lista.add(livro)
        }

        con.close()
        return lista
    }


    // ================= CADASTRAR =================
    fun cadastrarLivro() {

        print("Título: ")
        val titulo = readLine()!!

        print("Autor: ")
        val autor = readLine()!!

        print("Ano: ")
        val ano = readLine()!!

        print("ISBN: ")
        val isbn = readLine()!!.toInt()

        print("Gênero: ")
        val genero = readLine()!!

        val con = Conexao.conectar()

        val stmt = con.prepareStatement("""
            INSERT INTO livros(titulo,autor,ano_publicacao,isbn,estilo)
            VALUES (?,?,?,?,?)
        """)

        stmt.setString(1,titulo)
        stmt.setString(2,autor)
        stmt.setString(3,ano)
        stmt.setInt(4,isbn)
        stmt.setString(5,genero)

        stmt.executeUpdate()

        println("Livro cadastrado!")

        con.close()
    }

    // ================= BUSCAR POR GÊNERO =================
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

    // ================= TOTAL =================
    fun totalLivros(): Int {

        val con = Conexao.conectar()
        val rs = con.prepareStatement("SELECT COUNT(*) FROM livros").executeQuery()

        rs.next()
        val total = rs.getInt(1)

        con.close()
        return total
    }

    // ================= TOTAL POR GÊNERO =================
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

    // ================= MENU =================
    fun menuLivro() {

        var op:Int
        do {

            println("""
==== LIVROS ====
1 Listar
2 Cadastrar
3 Buscar gênero
0 Voltar
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
            }

        }while(op!=0)
    }
}
