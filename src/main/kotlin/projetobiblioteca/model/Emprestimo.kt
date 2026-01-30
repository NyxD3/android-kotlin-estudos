package projetobiblioteca.model
import projetobiblioteca.model.Emprestimo
import projetobiblioteca.dao.Conexao

import java.time.LocalDate

data class Emprestimo(
    val id: Int,
    val idLivro: Int,
    val idUsuario: Int,
    val dataEmprestimo: LocalDate,
    val dataDevolucao: LocalDate?
)