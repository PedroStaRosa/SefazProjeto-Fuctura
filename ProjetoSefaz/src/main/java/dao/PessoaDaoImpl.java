package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import entidade.Pessoa;

public class PessoaDaoImpl implements PessoaDao {

	// EntityManagar, faz a conexão com o banco.

	private EntityManager ent;
	private EntityTransaction tx;
	// metodo criado e foi passado o construtor.

	public PessoaDaoImpl(EntityManager ent) {
		this.ent = ent;
	}

	public boolean inserir(Pessoa pessoa) {

		EntityTransaction tx = ent.getTransaction();
		tx.begin();

		ent.merge(pessoa);
		tx.commit();

		return true;

	}

	public void alterar(Pessoa pessoa) {
		tx = ent.getTransaction();
		tx.begin();

		ent.merge(pessoa);
		tx.commit();

	}

	public void remover(Pessoa pessoa) {
		tx = ent.getTransaction();
		tx.begin();

		ent.remove(pessoa);
		tx.commit();

	}

	public Pessoa pesquisar(String cpf) {

		Pessoa pessoa = ent.find(Pessoa.class, cpf);

		return pessoa;
	}

	public List<Pessoa> listarTodos() {

		Query query = this.ent.createQuery("from Pessoa u");

		List<Pessoa> pessoas = query.getResultList();

		return pessoas;

	}

}
