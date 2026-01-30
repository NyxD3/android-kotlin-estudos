package projetobiblioteca.model
import projetobiblioteca.dao.Conexao
import projetobiblioteca.model.Funcionario

data class Funcionario(
    val idFuncionario: Int,
    val nome: String,
    val email: String,
    val senhaHash: String
)

