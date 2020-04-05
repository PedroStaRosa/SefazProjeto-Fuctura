package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import dao.PessoaDao;
import dao.PessoaDaoImpl;
import entidade.Livro;
import entidade.Pessoa;
import util.JpaUtil;

@ManagedBean(name = "LoginBean")
@RequestScoped
public class LoginBean {

	// USUARIO PADRAO PARA INICIO DA APLICAÇÃO
	final private String userAdm = "11111111111";
	final private String passAdm = "admin";

	// RECEBE VALORES DE ACESSO DA LOGIN.XHTML
	private String userTXT, passTXT;
	private PessoaDao pessoaDao;
	private String mensagem;

	private static final String PESQUISAR = "carregarPessoas.xhtml";

	public LoginBean() {
		this.pessoaDao = new PessoaDaoImpl(JpaUtil.getEntityManager());

	}

	// METHODS
	public void entrar() {

		try {
			if (this.userTXT.equals(this.userAdm) && this.passTXT.equals(this.passAdm)) {
				FacesContext.getCurrentInstance().getExternalContext().redirect(PESQUISAR);
			} else {
				Pessoa pessoaPesquisa = this.pessoaDao.pesquisar(this.userTXT);
				if (pessoaPesquisa != null) {
					if (pessoaPesquisa.getSenha().equals(this.passTXT)) {
						FacesContext.getCurrentInstance().getExternalContext().redirect(PESQUISAR);
					} else {
						// MENSAGEM SO SERÁ EXIBIDA SE ENCONTRAR O LOGIN,
						// ESTÁ INFORMANDO "USUARIO INVALIDO"
						// PARA DESPISTAR HACKERS === RETIRAR ( ) VERSÃO FINAL.
						this.mensagem = "Usuario ou Senha inválida. ( SENHA INVALIDA )";
					}
				} else {
					this.mensagem = "Usuario ou Senha inválida ( USUARIO INEXISTENTE )";
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// GETTERS AND SETTERS, CONSTRUCT
	public String getUserTXT() {
		return userTXT;
	}

	public void setUserTXT(String userTXT) {
		this.userTXT = userTXT;
	}

	public String getPassTXT() {
		return passTXT;
	}

	public void setPassTXT(String passTXT) {
		this.passTXT = passTXT;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

}
