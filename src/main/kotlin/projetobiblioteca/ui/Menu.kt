package projetobiblioteca.ui
import projetobiblioteca.dao.Conexao
import projetobiblioteca.dao.EmprestimoDAO
import projetobiblioteca.dao.FuncionarioDAO
import projetobiblioteca.dao.LivroDAO
import projetobiblioteca.dao.UsuarioDAO


class Menu {



    fun menuSistema() {
        var opcao: Int

        do {
            println("""
====== BIBLIOTECA ======
1 - Livros
2 - Empréstimos
3 - Funcionários
4 - Usuarios
0 - Sair
""")

            opcao = readLine()!!.toInt()

            val livroDAO = LivroDAO()
            val emprestimoDAO = EmprestimoDAO()
            val funcionarioDAO = FuncionarioDAO()
            val usuarioDAO = UsuarioDAO()

            when(opcao) {
                1 -> livroDAO.menuLivro()
                2 -> emprestimoDAO.menuEmprestimo()
                3 -> funcionarioDAO.menuFuncionario()
                4 -> usuarioDAO.menuUsuario()
            }





        } while (opcao != 0)
    }
}