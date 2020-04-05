package dao;

import entidade.Livro;

public interface LivroDao {

	public void remover(Livro livro);
	public void removerID(long idLivro);
	
}
