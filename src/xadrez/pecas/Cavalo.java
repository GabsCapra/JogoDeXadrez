package xadrez.pecas;

import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.Color;
import xadrez.PecaXadrez;

public class Cavalo extends PecaXadrez {

	public Cavalo(Tabuleiro tabuleiro, Color color) {
		super(tabuleiro, color);
	}

	@Override
	public String toString() {
		return "C";
	}

	private boolean canMove(Posicao posicao) {
		PecaXadrez p = (PecaXadrez) getTabuleiro().peca(posicao);
		return p == null || p.getCor() != getCor();
	}

	@Override
	public boolean[][] possiveisMove() {
		boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];

		Posicao p = new Posicao(0, 0);

		p.setValues(posicao.getLinha() - 1, posicao.getColuna() - 2);
		if (getTabuleiro().posicaoExists(p) && canMove(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		p.setValues(posicao.getLinha() - 2, posicao.getColuna() - 1);
		if (getTabuleiro().posicaoExists(p) && canMove(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		p.setValues(posicao.getLinha() - 2, posicao.getColuna() + 1);
		if (getTabuleiro().posicaoExists(p) && canMove(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		p.setValues(posicao.getLinha() - 1, posicao.getColuna() + 2);
		if (getTabuleiro().posicaoExists(p) && canMove(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		p.setValues(posicao.getLinha() + 1, posicao.getColuna() + 2);
		if (getTabuleiro().posicaoExists(p) && canMove(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		p.setValues(posicao.getLinha() + 2, posicao.getColuna() + 1);
		if (getTabuleiro().posicaoExists(p) && canMove(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		p.setValues(posicao.getLinha() + 2, posicao.getColuna() - 1);
		if (getTabuleiro().posicaoExists(p) && canMove(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		p.setValues(posicao.getLinha() + 1, posicao.getColuna() - 2);
		if (getTabuleiro().posicaoExists(p) && canMove(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		return mat;
	}
}