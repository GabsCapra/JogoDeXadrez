package tabuleiro;

public class Tabuleiro {
 
	private int linhas;
	private int colunas;
	private Peca[][]pecas;
	
	public Tabuleiro(int linhas, int colunas) {
		if (linhas < 1 || colunas < 1) {
			throw new TabuleiroException("Erro ao criar tabuleiro: é necessário ao menos 1 linha e 1 coluna.");
		}
		this.linhas = linhas;
		this.colunas = colunas;
		pecas = new Peca [linhas][colunas];
	}

	public int getLinhas() {
		return linhas;
	}

	public int getColunas() {
		return colunas;
	}

	public Peca peca(int linha,  int coluna) {
		if(!posicaoExists(linha, coluna)) {
			throw new TabuleiroException("Posição não existe no tabuleiro");
		}
			return pecas[linha][coluna];
	}
	public Peca peca(Posicao posicao) {
		if(!posicaoExists(posicao)) {
			throw new TabuleiroException("Posição não existe no tabuleiro");
		}
		return pecas[posicao.getLinha()][posicao.getColuna()];
	}
	
	public void localPeca(Peca peca, Posicao posicao) {
		if (thereIsAPeca(posicao)) {
			throw new TabuleiroException("Já existe uma peça nesta posição " + posicao); 
		}
		pecas[posicao.getLinha()][posicao.getColuna()] = peca;
		peca.posicao = posicao;
	}
	
	public Peca removePeca(Posicao posicao) {
		if (!posicaoExists(posicao)) {
			throw new TabuleiroException("Posicação não existe no tabuleiro");
		}
		if (peca(posicao) == null) {
			return null;
		}
		Peca aux = peca(posicao);
		aux.posicao = null;
		pecas[posicao.getLinha()][posicao.getColuna()] = null;
		return aux;
	}
	
	private boolean posicaoExists(int linha, int coluna) {
		return linha >=0 && linha < linhas && coluna >=0 && coluna < colunas;
	}
	
	public boolean posicaoExists(Posicao posicao) { 
		return posicaoExists(posicao.getLinha(), posicao.getColuna());
	}
	
	public boolean thereIsAPeca(Posicao posicao) {
		if(!posicaoExists(posicao)) {
			throw new TabuleiroException("Posição não existe no tabuleiro");
		}
		return peca(posicao) != null;
	}
}
