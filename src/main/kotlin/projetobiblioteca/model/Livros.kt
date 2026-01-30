package projetobiblioteca.model
import projetobiblioteca.model.Livros
import projetobiblioteca.dao.Conexao


data class Livros(
    val idLivro: Int,
    val titulo: String,
    val autor: String,
    val anoPublicacao: Int,
    val isbn: String,
    val genero: String,
    val emprestado: Boolean
)