package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import dao.LivroDao;
import dao.LivroDaoImpl;
import dao.PessoaDao;
import dao.PessoaDaoImpl;
import entidade.Livro;
import entidade.Pessoa;
import util.JpaUtil;

@ManagedBean(name = "PessoaBean")
@SessionScoped
public class PessoaBean {

	private Pessoa pessoa;
	private Livro livro;
	private List<Pessoa> listaPessoas;
	private String cpfSelecionado;
	private long idLivroSelecionado;

	private PessoaDao pessoaDao;
	private LivroDao livroDao;
	private String mensagem;
	private int inserir_Editar; // VARIAVEL QUE IRÁ IDENTIFICAR A AÇÃO PARA REUTILIZAR O METODO SALVAR.
	private Pessoa pessoaExiste; // SERÁ PREENCHIDO NO INSERIR CASO cpf SEJA ENCONTRADO, VALIDA CPF EXISTENTE

	private static final String INSERIR = "inserirPessoa.xhtml";
	private static final String PESQUISAR = "carregarPessoas.xhtml";
	private static final String LIVROSLIDOS = "livrosLidos.xhtml";
	private static final String EDITAR = "editarPessoa.xhtml";

	// CONSTRUCTOR
	public PessoaBean() {
		this.pessoa = new Pessoa();
		this.pessoa.setLivros(new ArrayList<Livro>());
		this.livro = new Livro();
		this.listaPessoas = new ArrayList<Pessoa>();
		this.pessoaDao = new PessoaDaoImpl(JpaUtil.getEntityManager());
		this.livroDao = new LivroDaoImpl(JpaUtil.getEntityManager());
		this.listaPessoas = this.pessoaDao.listarTodos();
		if (this.listaPessoas.isEmpty()) {
			this.mensagem = "Lista vazia - Nenhum Usuario Cadastrado";
		}
	}

	// CRUD
	public void pesquisarTodos() {

		addMessage("Lista Carregada com Sucesso", "Sucesso");
		// limpar();
		this.listaPessoas = this.pessoaDao.listarTodos();
	}

	public void pesquisarPessoa() {
		/**
		 * 
		 * AQUI A GAMBIARRA REINOU E FUNCIONOU!!!
		 * 
		 */
		Pessoa pessoaPesquisar = this.pessoaDao.pesquisar(cpfSelecionado);
		List<Pessoa> pessoaSelecionada = new ArrayList<Pessoa>();
		pessoaSelecionada.add(pessoaPesquisar);
		this.listaPessoas = pessoaSelecionada;
		if (pessoaPesquisar == null) {
			addMessageError("Nenhum usuario encontrado", "Não encontrado");
		} else {
			addMessage("Buscar realizada com sucesso", "Sucesso!!");
		}
	}

	public void salvar() {
		try {
			/**
			 * SE O CPF NAO EXISTIR NA BASE OU A REQUISIÇÃO VINHER DO EDITAR
			 */
			if (pessoaExiste == null || inserir_Editar == 2) {
				if (this.pessoaDao.inserir(this.pessoa)) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Sucesso !!!"));
					abrirPesquisarPessoa();

				} else {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Erro ao inserir !!!"));
				}
			} else {
				this.mensagem = "CPF ja cadastrado no sistema.";
			}
		} catch (IOException e) {
			// TRATAR EXCEPTION!
			e.printStackTrace();
		}

	}

	public void inserir() throws IOException {
		inserir_Editar = 1;
		this.limparMensagem();
		abrirInserirPessoa();
	}

	public void editar() throws IOException {
		inserir_Editar = 2;
		this.limparMensagem();
		Pessoa pessoaEditar = this.pessoaDao.pesquisar(cpfSelecionado);
		System.out.println("cpf:" + cpfSelecionado);
		this.pessoa = pessoaEditar;
		System.out.println("senha " + this.pessoa.getSenha());
		abrirEditarPessoa();
	}

	public void remover() {
		Pessoa pessoaRemocao = this.pessoaDao.pesquisar(cpfSelecionado);
		this.pessoaDao.remover(pessoaRemocao);
		this.listaPessoas = this.pessoaDao.listarTodos();
		addMessage("* " + pessoaRemocao.getNome() + " deletado com sucesso", "Lista recarregada");
	}

	public void removerLivro() throws IOException {

		this.livroDao.removerID(idLivroSelecionado);

		System.out.println("id do livro " + idLivroSelecionado);

		System.out.println("lista livros nova pessoa " + this.pessoa.getLivros().toString());
		// Criar metodo para REFRESH NA LISTA.
	}

	public void novaPessoa() {
		// VALIDAR CPF EXISTENTE PARA CADASTRO DE NOVA PESSOA
		pessoaExiste = this.pessoaDao.pesquisar(this.pessoa.getCpf());
		salvar();
	}

	public void adicionarLivros() {
		System.out.println("aquii2!!");
		Livro novoLivro = new Livro();

		novoLivro.setAno_lancamento(this.livro.getAno_lancamento());
		novoLivro.setNome(this.livro.getNome());
		novoLivro.setAutor(this.livro.getAutor());
		novoLivro.setPessoa(this.pessoa);

		this.pessoa.getLivros().add(novoLivro);

		this.livro = new Livro();
		System.out.println("aquii!!");
	}

	public void listaLivrosLidos() throws IOException {
		Pessoa pessoaLivros = this.pessoaDao.pesquisar(cpfSelecionado);
		System.out.println(cpfSelecionado);
		this.pessoa = pessoaLivros;
		System.out.println(pessoaLivros.getLivros().size());
		abrirLivrosLidos();
	}

	public void limpar() {
		this.listaPessoas = new ArrayList<Pessoa>();
		this.pessoa = new Pessoa();
		this.pessoa.setLivros(new ArrayList<Livro>());
		this.livro = new Livro();
		this.mensagem = "";
		this.cpfSelecionado = "";
	}

	public void limparPesquisa() {
		pesquisarTodos();
		this.cpfSelecionado = "";
	}

	public void limparMensagem() {
		this.mensagem = "";
	}

	// ADICIONA MENSAGENS
	public void addMessage(String titulo, String detalhes) {

		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, titulo, detalhes);
		FacesContext.getCurrentInstance().addMessage(null, message);

	}

	public void addMessageError(String titulo, String detalhes) {

		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, titulo, detalhes);
		FacesContext.getCurrentInstance().addMessage(null, message);

	}

	// DIRECIONAR PAGINAS
	public void abrirPesquisarPessoa() throws IOException {
		this.limpar();
		FacesContext.getCurrentInstance().getExternalContext().redirect(PESQUISAR);
	}

	public void abrirInserirPessoa() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect(INSERIR);
	}

	public void abrirEditarPessoa() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect(EDITAR);
	}

	public void abrirLivrosLidos() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect(LIVROSLIDOS);
	}

	// GETTERS AND SETTERS
	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public Livro getLivro() {
		return livro;
	}

	public void setLivro(Livro livro) {
		this.livro = livro;
	}

	public List<Pessoa> getListaPessoas() {
		return listaPessoas;
	}

	public void setListaPessoas(List<Pessoa> listaPessoas) {
		this.listaPessoas = listaPessoas;
	}

	public String getCpfSelecionado() {
		return cpfSelecionado;
	}

	public void setCpfSelecionado(String cpfSelecionado) {
		this.cpfSelecionado = cpfSelecionado;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public int getInserir_Editar() {
		return inserir_Editar;
	}

	public void setInserir_Editar(int inserir_Editar) {
		this.inserir_Editar = inserir_Editar;
	}

	public long getIdLivroSelecionado() {
		return idLivroSelecionado;
	}

	public void setIdLivroSelecionado(long idLivroSelecionado) {
		this.idLivroSelecionado = idLivroSelecionado;
	}

}
