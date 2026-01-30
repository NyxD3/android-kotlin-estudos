package projetobiblioteca.model
import projetobiblioteca.model.Usuario
import projetobiblioteca.dao.Conexao



data class Usuario(
    val idUsuario: Int,
    val nome: String,
    val telefone: String,
    val endereco: String
)