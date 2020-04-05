package dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import entidade.Livro;

public class LivroDaoImpl implements LivroDao {
	private EntityManager ent;
	private EntityTransaction tx;

	public LivroDaoImpl(EntityManager ent) {
		this.ent = ent;
	}

	@Override
	public void remover(Livro livro) {

		tx = ent.getTransaction();
		tx.begin();

		ent.remove(livro);
		tx.commit();

	}

	@Override
	public void removerID(long idLivro) {

		Livro livro = null;

		livro = ent.find(Livro.class, idLivro);
		System.out.println(livro.toString() + "reultanbdo do DAO");
		tx = ent.getTransaction();
		tx.begin();

		ent.remove(livro);
		tx.commit();

	}

}
