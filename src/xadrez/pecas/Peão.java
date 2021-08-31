package xadrez.pecas;

import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.Color;
import xadrez.PartidaXadrez;
import xadrez.PecaXadrez;

public class Peão extends PecaXadrez {

	private PartidaXadrez partidaXadrez;

	public Peão(Tabuleiro tabuleiro, Color color, PartidaXadrez partidaXadrez) {
		super(tabuleiro, color);
		this.partidaXadrez = partidaXadrez;
	}

	@Override
	public boolean[][] possiveisMove() {
		boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];

		Posicao p = new Posicao(0, 0);

		if (getCor() == Color.WHITE) {
			p.setValues(posicao.getLinha() - 1, posicao.getColuna());
			if (getTabuleiro().posicaoExists(p) && !getTabuleiro().thereIsAPeca(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.setValues(posicao.getLinha() - 2, posicao.getColuna());
			Posicao p2 = new Posicao(posicao.getLinha() - 1, posicao.getColuna());
			if (getTabuleiro().posicaoExists(p) && !getTabuleiro().thereIsAPeca(p) && getTabuleiro().posicaoExists(p2)
					&& !getTabuleiro().thereIsAPeca(p2) && getMoveCount() == 0) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.setValues(posicao.getLinha() - 1, posicao.getColuna() - 1);
			if (getTabuleiro().posicaoExists(p) && isThereOponentePeca(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.setValues(posicao.getLinha() - 1, posicao.getColuna() + 1);
			if (getTabuleiro().posicaoExists(p) && isThereOponentePeca(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			// #specialmove en passant white
			if (posicao.getLinha() == 3) {
				Posicao esquerda = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
				if (getTabuleiro().posicaoExists(esquerda) && isThereOponentePeca(esquerda)
						&& getTabuleiro().peca(esquerda) == partidaXadrez.getEnPassantVulneravel()) {
					mat[esquerda.getLinha() - 1][esquerda.getColuna()] = true;
				}
				Posicao direita = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
				if (getTabuleiro().posicaoExists(direita) && isThereOponentePeca(direita)
						&& getTabuleiro().peca(direita) == partidaXadrez.getEnPassantVulneravel()) {
					mat[direita.getLinha() - 1][direita.getColuna()] = true;
				}
			}

		} else {
			p.setValues(posicao.getLinha() + 1, posicao.getColuna());
			if (getTabuleiro().posicaoExists(p) && !getTabuleiro().thereIsAPeca(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.setValues(posicao.getLinha() + 2, posicao.getColuna());
			Posicao p2 = new Posicao(posicao.getLinha() + 1, posicao.getColuna());
			if (getTabuleiro().posicaoExists(p) && !getTabuleiro().thereIsAPeca(p) && getTabuleiro().posicaoExists(p2)
					&& !getTabuleiro().thereIsAPeca(p2) && getMoveCount() == 0) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.setValues(posicao.getLinha() + 1, posicao.getColuna() - 1);
			if (getTabuleiro().posicaoExists(p) && isThereOponentePeca(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.setValues(posicao.getLinha() + 1, posicao.getColuna() + 1);
			if (getTabuleiro().posicaoExists(p) && isThereOponentePeca(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}

				// #specialmove en passant black
				if (posicao.getLinha() == 4) {
					Posicao esquerda = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
					if (getTabuleiro().posicaoExists(esquerda) && isThereOponentePeca(esquerda)
							&& getTabuleiro().peca(esquerda) == partidaXadrez.getEnPassantVulneravel()) {
						mat[esquerda.getLinha() + 1][esquerda.getColuna()] = true;
					}

					Posicao direita = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
					if (getTabuleiro().posicaoExists(direita) && isThereOponentePeca(direita)
							&& getTabuleiro().peca(direita) == partidaXadrez.getEnPassantVulneravel()) {
						mat[direita.getLinha() + 1][direita.getColuna()] = true;
				}
			}
		}
		return mat;
	}

	@Override
	public String toString() {
		return "P";
	}
}