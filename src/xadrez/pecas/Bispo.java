package xadrez.pecas;

import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.Color;
import xadrez.PecaXadrez;

public class Bispo extends PecaXadrez {

	public Bispo(Tabuleiro tabuleiro, Color color) {
		super(tabuleiro, color);
	}

	@Override
	public String toString() {
		return "B";
	}

	@Override
	public boolean[][] possiveisMove() {
		boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];

		Posicao p = new Posicao(0, 0);

		// nw
		p.setValues(posicao.getLinha() - 1, posicao.getColuna() - 1);
		while (getTabuleiro().posicaoExists(p) && !getTabuleiro().thereIsAPeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setValues(p.getLinha() - 1, p.getColuna() - 1);
		}
		if (getTabuleiro().posicaoExists(p) && isThereOponentePeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		// ne
		p.setValues(posicao.getLinha() - 1, posicao.getColuna() + 1);
		while (getTabuleiro().posicaoExists(p) && !getTabuleiro().thereIsAPeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setValues(p.getLinha() - 1, p.getColuna() + 1);
		}
		if (getTabuleiro().posicaoExists(p) && isThereOponentePeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		// se
		p.setValues(posicao.getLinha() + 1, posicao.getColuna() + 1);
		while (getTabuleiro().posicaoExists(p) && !getTabuleiro().thereIsAPeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setValues(p.getLinha() + 1, p.getColuna() + 1);
		}
		if (getTabuleiro().posicaoExists(p) && isThereOponentePeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		// sw
		p.setValues(posicao.getLinha() + 1, posicao.getColuna() - 1);
		while (getTabuleiro().posicaoExists(p) && !getTabuleiro().thereIsAPeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setValues(p.getLinha() + 1, p.getColuna() - 1);
		}
		if (getTabuleiro().posicaoExists(p) && isThereOponentePeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		return mat;
	}
}
